package com.grupomarostica.cloure.Modules.maintenance_warranties;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.AvailableCommand;
import com.grupomarostica.cloure.Core.CloureModule;
import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.Core.GlobalCommand;
import com.grupomarostica.cloure.Core.ModuleInfo;
import com.grupomarostica.cloure.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class maintenance_warranties_fragment extends Fragment {
    private ModuleInfo moduleInfo;
    private CloureSDK cloureSDK = null;
    private ListView listView;
    private MaintenanceWarrantyAdapter adapter;
    private ArrayList<MaintenanceWarranty> registers;
    private Handler handler = new Handler();
    private RelativeLayout loaderLayout;
    private ConstraintLayout emptyLayout;
    private Boolean ModuleInitializing = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_panel_scroll,container, false);
        listView = rootView.findViewById(R.id.fragment_panel_scroll_items);
        loaderLayout = rootView.findViewById(R.id.loader);
        emptyLayout = rootView.findViewById(R.id.fragment_scroll_no_results);

        registerForContextMenu(listView);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(moduleInfo.Title);
        load_data();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!ModuleInitializing)load_data();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        for(int i=0;i<moduleInfo.globalCommands.size(); i++){
            GlobalCommand gc = moduleInfo.globalCommands.get(i);
            menu.add(Menu.NONE, gc.Id, Menu.NONE, gc.Title);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        MaintenanceWarranty mw = (MaintenanceWarranty)listView.getItemAtPosition(info.position);
        for (int i=0;i<mw.AvailableCommands.size();i++){
            AvailableCommand availableCommand = (AvailableCommand) mw.AvailableCommands.get(i);
            menu.add(Menu.NONE, availableCommand.Id, Menu.NONE, availableCommand.Title);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        MaintenanceWarranty mw = adapter.getItem(index);


        return super.onContextItemSelected(item);
    }

    public void load_data(){
        BackgroundLoad backgroundLoad = new BackgroundLoad();
        new Thread(backgroundLoad).start();
    }

    class BackgroundLoad implements Runnable{
        String error = "";
        ArrayList<MaintenanceWarranty> registers = new ArrayList<>();

        @Override
        public void run() {

            try{
                ArrayList<CloureParam> params = new ArrayList<>();
                params.add(new CloureParam("module", "maintenance_warranties"));
                params.add(new CloureParam("topic", "get_list"));
                cloureSDK = new CloureSDK();
                String res = cloureSDK.execute(params);
                JSONObject object = new JSONObject(res);
                error = object.getString("Error");
                if(error.equals("")){
                    JSONObject response = object.getJSONObject("Response");
                    JSONArray registros = response.getJSONArray("Registros");
                    for (int i=0; i<registros.length(); i++){
                        JSONObject registro = registros.getJSONObject(i);
                        final MaintenanceWarranty register_tmp = new MaintenanceWarranty();
                        register_tmp.Id = registro.getInt("id");

                        final JSONArray availableCommands = registro.getJSONArray("AvailableCommands");
                        if(availableCommands!=null){
                            for (int j = 0; j<availableCommands.length(); j++){
                                JSONObject jobj = availableCommands.getJSONObject(j);
                                int cmd_id = jobj.getInt("Id");
                                String cmd_name = jobj.getString("Name");
                                final String cmd_title = jobj.getString("Title");
                                AvailableCommand availableCommand = new AvailableCommand(cmd_id, cmd_name, cmd_title);
                                register_tmp.AvailableCommands.add(availableCommand);
                            }
                        }
                        registers.add(register_tmp);
                    }
                }
            } catch (Exception ex){
                error = "Ocurrio un error al conectar con el servidor";
                ex.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(error.equals("")){
                        adapter = new MaintenanceWarrantyAdapter(getContext(), registers);
                        if(error.length()>0) Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    }

                    if(registers.size()>0){
                        listView.setAdapter(adapter);
                        listView.setVisibility(View.VISIBLE);
                        emptyLayout.setVisibility(View.INVISIBLE);
                    } else {
                        listView.setVisibility(View.INVISIBLE);
                        emptyLayout.setVisibility(View.VISIBLE);
                    }
                    loaderLayout.setVisibility(View.INVISIBLE);
                    ModuleInitializing=false;
                }
            });
        }
    }
}
