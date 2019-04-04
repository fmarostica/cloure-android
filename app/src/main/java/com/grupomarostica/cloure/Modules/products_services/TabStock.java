package com.grupomarostica.cloure.Modules.products_services;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TabStock extends android.support.v4.app.Fragment {
    private CloureSDK cloureSDK;
    private ArrayList<StockItem> registers;
    private Handler handler = new Handler();
    private ProductStockAdapter adapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_products_services_tab_stock, container, false);

        listView = rootView.findViewById(R.id.products_services_tab_stock_lst_stock);
        listView.setItemsCanFocus(true);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        load_stock();
    }

    public void load_stock(){
        new Thread(new BackgroundLoadStock()).start();
    }

    class BackgroundLoadStock implements Runnable{
        String error;

        @Override
        public void run() {

            try{
                ArrayList<CloureParam> params = new ArrayList<>();
                params.add(new CloureParam("module", "products_services"));
                params.add(new CloureParam("topic", "cargar_stock"));
                params.add(new CloureParam("producto_id", "0"));
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
                        final StockItem register_tmp = new StockItem();
                        //register_tmp.ProductId = registro.getInt("Id");
                        //register_tmp.BranchId = registro.getInt("Titulo");
                        register_tmp.BranchName = registro.getString("Min");
                        register_tmp.MinQuantity = registro.getDouble("Min");
                        register_tmp.Quantity = registro.getDouble("Actual");

                        /*
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
                        */
                        registers.add(register_tmp);
                    }
                } else {
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception ex){
                ex.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if(error.equals("")){
                            adapter = new ProductStockAdapter(getContext(), registers);

                            if(registers.size()>0){
                                listView.setAdapter(adapter);
                                listView.setVisibility(View.VISIBLE);
                                //emptyLayout.setVisibility(View.INVISIBLE);
                            } else {
                                listView.setVisibility(View.INVISIBLE);
                                //emptyLayout.setVisibility(View.VISIBLE);
                            }
                            //loaderLayout.setVisibility(View.INVISIBLE);
                            if(error.length()>0) Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    //ModuleInitializing=false;
                }
            });
        }
    }
}
