package com.grupomarostica.cloure.Modules.products_services;

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
import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;
import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.Core.GlobalCommand;
import com.grupomarostica.cloure.Core.ModuleInfo;
import com.grupomarostica.cloure.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class products_services_fragment extends Fragment {
    private ModuleInfo moduleInfo;
    private ProductsServicesArrayAdapter adapter;
    private RelativeLayout loaderLayout;
    private ListView listView;

    private ArrayList<ProductService> registers;
    private ConstraintLayout emptyLayout;
    private Handler handler = new Handler();

    @Override
    public void onResume() {
        super.onResume();
        load_data();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_panel_scroll,container, false);

        moduleInfo = CloureManager.getModuleInfo();

        listView = rootView.findViewById(R.id.fragment_panel_scroll_items);
        loaderLayout = rootView.findViewById(R.id.loader);
        emptyLayout = rootView.findViewById(R.id.fragment_scroll_no_results);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductService product_clicked = adapter.getItem(position);
                Intent intent = new Intent(getActivity(), ProductServiceAdd.class);
                intent.putExtra("Id", product_clicked.Id);
                startActivity(intent);
            }
        });

        registerForContextMenu(listView);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        for(int i=0;i<moduleInfo.globalCommands.size(); i++){
            GlobalCommand gc = moduleInfo.globalCommands.get(i);
            if(gc.Name.equals("add")) menu.add(Menu.NONE, gc.Id, Menu.NONE, gc.Title);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case 1:
                //Agregar
                Intent intent = new Intent(getContext(), ProductServiceAdd.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        ProductService productService = (ProductService)listView.getItemAtPosition(info.position);
        for (int i=0;i<productService.AvailableCommands.size();i++){
            AvailableCommand availableCommand = productService.AvailableCommands.get(i);
            menu.add(Menu.NONE, availableCommand.Id, Menu.NONE, availableCommand.Title);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        ProductService register = adapter.getItem(index);
        switch(item.getItemId()) {
            case 1:
                edit(register.Id);
                break;
            case 2:
                delete(register.Id);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void edit(final int Id){
        Intent intent = new Intent(getContext(), ProductServiceAdd.class);
        startActivity(intent);
    }

    private void delete(final int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle("Borrar");
        builder.setMessage("¿Esta seguro que desea borrar el registro?");
        builder.setPositiveButton("Si",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<CloureParam> params = new ArrayList<>();
                        params.add(new CloureParam("module", "products_services"));
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
            registers = ProductsServices.getList();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try{
                        adapter = new ProductsServicesArrayAdapter(getContext(), registers);
                        if(registers.size()>0){
                            listView.setAdapter(adapter);
                            listView.setVisibility(View.VISIBLE);
                            emptyLayout.setVisibility(View.INVISIBLE);
                        } else {
                            listView.setVisibility(View.INVISIBLE);
                            emptyLayout.setVisibility(View.VISIBLE);
                        }
                        loaderLayout.setVisibility(View.INVISIBLE);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}