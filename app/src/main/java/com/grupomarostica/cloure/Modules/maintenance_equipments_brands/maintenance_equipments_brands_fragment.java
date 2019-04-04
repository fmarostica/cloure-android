package com.grupomarostica.cloure.Modules.maintenance_equipments_brands;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

public class maintenance_equipments_brands_fragment extends Fragment {
    private ModuleInfo moduleInfo;

    private ListView listView;
    private LinearLayout buttonsBar;
    private Handler handler = new Handler();
    private RelativeLayout loaderLayout;
    private CloureSDK cloureSDK;
    private ArrayList<MaintenanceEquipmentBrand> registers;
    private MaintenanceEquipmentBrandsAdapter adapter;
    private ImageButton btnAdd;
    private ConstraintLayout emptyLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_panel_scroll,container, false);
        loaderLayout = rootView.findViewById(R.id.loader);
        listView = rootView.findViewById(R.id.fragment_panel_scroll_items);
        emptyLayout = rootView.findViewById(R.id.fragment_scroll_no_results);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MaintenanceEquipmentBrand met = (MaintenanceEquipmentBrand)adapter.getItem(position);
                edit(met);
            }
        });

        registerForContextMenu(listView);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getActivity().setTitle(moduleInfo.Title);
        //load_data();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /*
        for(int i=0;i<moduleInfo.globalCommands.size(); i++){
            GlobalCommand gc = moduleInfo.globalCommands.get(i);
            menu.add(Menu.NONE, gc.Id, Menu.NONE, gc.Title);
        }
        */
        menu.add(Menu.NONE, 1, Menu.NONE, "Agregar");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case 1:
                //Agregar
                Intent intent = new Intent(getContext(), MaintenanceEquipmentBrandAddActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        MaintenanceEquipmentBrand mets = (MaintenanceEquipmentBrand)listView.getItemAtPosition(info.position);
        for (int i=0;i<mets.AvailableCommands.size();i++){
            AvailableCommand availableCommand = (AvailableCommand) mets.AvailableCommands.get(i);
            menu.add(Menu.NONE, availableCommand.Id, Menu.NONE, availableCommand.Title);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        MaintenanceEquipmentBrand met = (MaintenanceEquipmentBrand)adapter.getItem(index);

        switch(item.getItemId()) {
            case 1:
                edit(met);
                return true;
            case 2:
                delete(met.Id);
                return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        load_data();
    }

    public void edit(MaintenanceEquipmentBrand met){
        boolean can_edit = false;
        for(int i=0;i<met.AvailableCommands.size(); i++){
            if(met.AvailableCommands.get(i).Name.equals("edit")) can_edit=true;
        }
        if(can_edit){
            Intent intent = new Intent(getActivity(), MaintenanceEquipmentBrandAddActivity.class);
            intent.putExtra("id", met.Id);
            startActivity(intent);
        }
    }

    public void delete(final int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle("Borrar");
        builder.setMessage("¿Está seguro que desea eliminar el registro?");
        builder.setPositiveButton("Si",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<CloureParam> params = new ArrayList<>();
                        params.add(new CloureParam("module", "maintenance_equipments_brands"));
                        params.add(new CloureParam("topic", "borrar"));
                        params.add(new CloureParam("id", Integer.toString(id)));

                        CloureSDK cloureSDK = new CloureSDK();
                        try{
                            String res = cloureSDK.execute(params);
                            JSONObject object = new JSONObject(res);
                            String error = object.getString("Error");
                            if(error.equals("")){
                                //JSONObject response = object.getJSONObject("Response");
                                load_data();
                            } else {
                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void load_data(){
        BackgroundLoad backgroundLoad = new BackgroundLoad();
        new Thread(backgroundLoad).start();
    }

    class BackgroundLoad implements Runnable{
        @Override
        public void run() {

            try{
                ArrayList<CloureParam> params = new ArrayList<>();
                params.add(new CloureParam("module", "maintenance_equipments_brands"));
                params.add(new CloureParam("topic", "listar"));
                cloureSDK = new CloureSDK();
                String res = cloureSDK.execute(params);
                JSONObject object = new JSONObject(res);
                String error = object.getString("Error");
                if(error.equals("")){
                    JSONObject response = object.getJSONObject("Response");
                    JSONArray registros = response.getJSONArray("Registros");
                    registers = new ArrayList<>();
                    for (int i=0; i<registros.length(); i++){
                        JSONObject registro = registros.getJSONObject(i);
                        MaintenanceEquipmentBrand register_tmp = new MaintenanceEquipmentBrand();
                        register_tmp.Id = registro.getInt("Id");
                        register_tmp.Name = registro.getString("Name");

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
                    adapter = new MaintenanceEquipmentBrandsAdapter(getContext(), registers);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(registers.size()>0){
                                listView.setAdapter(adapter);
                                listView.setVisibility(View.VISIBLE);
                                emptyLayout.setVisibility(View.INVISIBLE);
                            } else {
                                listView.setVisibility(View.INVISIBLE);
                                emptyLayout.setVisibility(View.VISIBLE);
                            }
                            loaderLayout.setVisibility(View.INVISIBLE);
                        }
                    });
                } else {
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
