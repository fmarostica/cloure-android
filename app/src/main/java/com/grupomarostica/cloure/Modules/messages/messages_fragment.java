package com.grupomarostica.cloure.Modules.messages;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.CloureModule;
import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.Core.ModuleInfo;
import com.grupomarostica.cloure.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class messages_fragment extends Fragment {
    private ModuleInfo moduleInfo;
    private CloureSDK cloureSDK = null;
    private ListView listView;
    private MessagesAdapter adapter;
    private ArrayList<Message> registers;
    private RelativeLayout loaderLayout;
    private Handler handler = new Handler();
    private ConstraintLayout emptyLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_panel_scroll,container, false);

        listView = rootView.findViewById(R.id.fragment_panel_scroll_items);
        loaderLayout = rootView.findViewById(R.id.loader);
        emptyLayout = rootView.findViewById(R.id.fragment_scroll_no_results);

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        load_data();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //menu.add(Menu.NONE, 1, Menu.NONE, R.string.add_expense);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case 1:
                //Agregar
                //Intent intent = new Intent(getContext(), MaintenanceShiftsAddActivity.class);
                //startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void load_data(){
        BackgroundLoad backgroundLoad = new BackgroundLoad();
        new Thread(backgroundLoad).start();
    }

    class BackgroundLoad implements Runnable{
        String res = "";
        String error = "";

        @Override
        public void run() {
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "messages"));
            params.add(new CloureParam("topic", "listar"));

            try{
                cloureSDK = new CloureSDK();
                res = cloureSDK.execute(params);
                JSONObject object = new JSONObject(res);
                error = object.getString("Error");
                registers = new ArrayList<>();

                if(error.equals("")){
                    JSONObject response = object.getJSONObject("Response");
                    JSONArray registros = response.getJSONArray("Registros");
                    for (int i=0; i<registros.length(); i++){
                        JSONObject registro = registros.getJSONObject(i);
                        Message record_tmp = new Message();
                        record_tmp.DateStr = registro.getString("FechaStr");
                        record_tmp.Subject = registro.getString("Asunto");
                        record_tmp.UserStr = registro.getString("Usuario");
                        registers.add(record_tmp);
                    }
                    adapter = new MessagesAdapter(getContext(), registers);
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(registers.size()>0){
                            if(adapter!=null) listView.setAdapter(adapter);
                            listView.setVisibility(View.VISIBLE);
                            emptyLayout.setVisibility(View.INVISIBLE);
                        } else {
                            listView.setVisibility(View.INVISIBLE);
                            emptyLayout.setVisibility(View.VISIBLE);
                        }
                        loaderLayout.setVisibility(View.INVISIBLE);
                        if(error.length()>0) Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
