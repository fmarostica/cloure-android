package com.grupomarostica.cloure.Modules.shows;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.grupomarostica.cloure.Core.ModuleInfo;
import com.grupomarostica.cloure.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class shows_fragment extends Fragment {
    private ModuleInfo moduleInfo;
    private CloureSDK cloureSDK = null;
    private ListView listView;
    private shows_adapter adapter;
    private ArrayList<Show> registers;
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Show show = adapter.getItem(position);
                edit(show.Id);
            }
        });

        registerForContextMenu(listView);
        setHasOptionsMenu(true);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Shows");

        load_data();
    }

    @Override
    public void onResume() {
        super.onResume();
        load_data();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        Show show = (Show)listView.getItemAtPosition(info.position);
        for (int i=0;i<show.AvailableCommands.size();i++){
            AvailableCommand availableCommand = show.AvailableCommands.get(i);
            menu.add(Menu.NONE, availableCommand.Id, Menu.NONE, availableCommand.Title);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        Show show = adapter.getItem(index);

        switch(item.getItemId()) {
            case 1:
                edit(show.Id);
                return true;
            case 2:
                delete(show.Id);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, 1, Menu.NONE, R.string.add);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case 1:
                //Agregar
                Intent intent = new Intent(getContext(), ShowAddActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void edit(int id){
        Intent intent = new Intent(getContext(), ShowAddActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void delete(final int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(R.string.delete);
        builder.setMessage(R.string.confirm_delete);
        builder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<CloureParam> params = new ArrayList<>();
                        params.add(new CloureParam("module", "shows"));
                        params.add(new CloureParam("topic", "borrar"));
                        params.add(new CloureParam("id", Integer.toString(id)));

                        CloureSDK cloureSDK = new CloureSDK();
                        try{
                            String res = cloureSDK.execute(params);
                            JSONObject object = new JSONObject(res);
                            String error = object.getString("Error");
                            if(error.equals("")){
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
        String res = "";
        String error = "";

        @Override
        public void run() {
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "shows"));
            params.add(new CloureParam("topic", "listar"));

            try{
                cloureSDK = new CloureSDK();
                res = cloureSDK.execute(params);
                JSONObject object = new JSONObject(res);
                error = object.getString("Error");
                if(error.equals("")){
                    JSONObject response = object.getJSONObject("Response");
                    JSONArray registros = response.getJSONArray("Registros");
                    registers = new ArrayList<>();
                    for (int i=0; i<registros.length(); i++){
                        JSONObject registro = registros.getJSONObject(i);
                        Show record_tmp = new Show();
                        record_tmp.Id = registro.getInt("Id");
                        record_tmp.DateStr = registro.getString("FechaStr");
                        record_tmp.BandArtist = registro.getString("Artista");
                        record_tmp.Place = registro.getString("Lugar");

                        record_tmp.Photographers = registro.getString("Fotografos");

                        JSONArray availableCommands = registro.getJSONArray("AvailableCommands");
                        if(availableCommands!=null){
                            for (int j = 0; j<availableCommands.length(); j++){
                                JSONObject jobj = availableCommands.getJSONObject(j);
                                int cmd_id = jobj.getInt("Id");
                                String cmd_name = jobj.getString("Name");
                                String cmd_title = jobj.getString("Title");

                                AvailableCommand availableCommand = new AvailableCommand(cmd_id, cmd_name, cmd_title);
                                record_tmp.AvailableCommands.add(availableCommand);
                            }
                        }
                        registers.add(record_tmp);
                    }
                    adapter = new shows_adapter(getContext(), registers);
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
