package com.grupomarostica.cloure.Modules.products_services_categories;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.AvailableCommand;
import com.grupomarostica.cloure.Core.CloureModule;
import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.Core.GlobalCommand;
import com.grupomarostica.cloure.Core.ModuleInfo;
import com.grupomarostica.cloure.Modules.products_services.ProductServiceAdd;
import com.grupomarostica.cloure.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class products_services_categories_fragment extends Fragment {
    private ModuleInfo moduleInfo;
    private ListView listView;
    private ArrayList<ProductCategory> registers;
    private RelativeLayout loaderLayout;
    private ConstraintLayout emptyLayout;
    private Handler handler = new Handler();
    private CloureSDK cloureSDK;
    private ProductsCategoriesAdapter adapter;
    private boolean ModuleInitializing = true;

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
                ProductCategory productCategory = adapter.getItem(position);
                if(productCategory!=null) edit(productCategory.Id, productCategory.Type);
            }
        });

        registerForContextMenu(listView);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        load_data();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /*
        for(int i=0;i<moduleInfo.globalCommands.size(); i++){
            GlobalCommand gc = moduleInfo.globalCommands.get(i);
            menu.add(Menu.NONE, gc.Id, Menu.NONE, gc.Title);
        }
        */
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case 1:
                //Agregar
                Intent intent = new Intent(getContext(), ProductCategoryN1AddActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        ProductCategory productCategory = (ProductCategory)listView.getItemAtPosition(info.position);
        for (int i=0;i<productCategory.AvailableCommands.size();i++){
            AvailableCommand availableCommand = productCategory.AvailableCommands.get(i);
            menu.add(Menu.NONE, availableCommand.Id, Menu.NONE, availableCommand.Title);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        ProductCategory productCategory = adapter.getItem(index);

        if(productCategory!=null){
            switch(item.getItemId()) {
                case 1:
                    edit(productCategory.Id, productCategory.Type);
                    return true;
                case 2:
                    delete(productCategory.Id, productCategory.Type);
                    return true;
            }
        }

        return super.onContextItemSelected(item);
    }

    public void load_data(){
        BackgroundLoad backgroundLoad = new BackgroundLoad();
        new Thread(backgroundLoad).start();
    }

    private void edit(int category_id, String type){
        if(type.equals("categoria_n1")){
            Intent editIntent = new Intent(getContext(), ProductCategoryN1AddActivity.class);
            editIntent.putExtra("id", category_id);
            editIntent.putExtra("type", type);
            startActivity(editIntent);
        }
    }

    private void delete(final int id, final String type){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle("Borrar");
        builder.setMessage("¿Esta seguro que desea borrar el registro?");
        builder.setPositiveButton("Si",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<CloureParam> params = new ArrayList<>();
                        params.add(new CloureParam("module", "products_services_categories"));
                        params.add(new CloureParam("topic", "borrar"));
                        params.add(new CloureParam("id", Integer.toString(id)));
                        params.add(new CloureParam("tipo", type));

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

    class BackgroundLoad implements Runnable{
        String error="";

        @Override
        public void run() {

            try{
                ArrayList<CloureParam> params = new ArrayList<>();
                params.add(new CloureParam("module", "products_services_categories"));
                params.add(new CloureParam("topic", "listar_categorias_n1"));
                cloureSDK = new CloureSDK();
                String res = cloureSDK.execute(params);
                JSONObject object = new JSONObject(res);
                error = object.getString("Error");
                registers = new ArrayList<>();
                if(error.equals("")){
                    JSONObject response = object.getJSONObject("Response");
                    JSONArray registros = response.getJSONArray("Registros");
                    for (int i=0; i<registros.length(); i++){
                        JSONObject registro = registros.getJSONObject(i);
                        final ProductCategory register_tmp = new ProductCategory();
                        register_tmp.Id = registro.getInt("Id");
                        register_tmp.Name = registro.getString("Nombre");
                        register_tmp.Type = registro.getString("Type");

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
                ex.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(error.equals("")){
                        adapter = new ProductsCategoriesAdapter(getContext(), registers);

                        if(registers.size()>0){
                            listView.setAdapter(adapter);
                            listView.setVisibility(View.VISIBLE);
                            emptyLayout.setVisibility(View.INVISIBLE);
                        } else {
                            listView.setVisibility(View.INVISIBLE);
                            emptyLayout.setVisibility(View.VISIBLE);
                        }
                        loaderLayout.setVisibility(View.INVISIBLE);
                        if(error.length()>0) Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    }
                    ModuleInitializing=false;
                }
            });
        }
    }
}
