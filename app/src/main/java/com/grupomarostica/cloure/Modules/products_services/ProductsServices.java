package com.grupomarostica.cloure.Modules.products_services;

import android.content.Intent;
import android.graphics.BitmapFactory;

import com.grupomarostica.cloure.Core.AvailableCommand;
import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class ProductsServices {
    public static ArrayList<ProductService> getList(){
        ArrayList<ProductService> items = new ArrayList<>();

        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "products_services"));
            params.add(new CloureParam("topic", "listar"));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);
            JSONObject object = new JSONObject(res);
            String error = object.getString("Error");
            if(error.equals("")){
                JSONObject response = object.getJSONObject("Response");
                JSONArray registros = response.getJSONArray("Registros");
                for (int i=0; i<registros.length(); i++){
                    JSONObject registro = registros.getJSONObject(i);
                    ProductService register_tmp = new ProductService();
                    register_tmp.Id = registro.getInt("Id");
                    register_tmp.Title = registro.getString("Titulo");
                    register_tmp.Importe = registro.getDouble("Importe");

                    register_tmp.ImagePath = registro.getString("ImagenPath");

                    URL url_image = new URL(register_tmp.ImagePath);
                    register_tmp.ImageBitmap = BitmapFactory.decodeStream(url_image.openConnection().getInputStream());

                    JSONArray availableCommands = registro.getJSONArray("AvailableCommands");
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
                    items.add(register_tmp);
                }
            }

        } catch (Exception ex){
            ex.printStackTrace();
        }

        return items;
    }

    public static ProductService get(int id){
        ProductService productService = new ProductService();

        try {
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "products_services"));
            params.add(new CloureParam("topic", "obtener"));
            params.add(new CloureParam("id", Integer.toString(id)));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);

            JSONObject object = new JSONObject(res);
            String error = object.getString("Error");
            if(error.equals("")){
                JSONObject response = object.getJSONObject("Response");
                productService.Id = id;
                productService.Title = response.getString("Titulo");
                productService.Description = response.getString("Descripcion");
                productService.MeasureUnitId = response.getInt("SistemaMedidaId");

                productService.IVA = response.getDouble("Iva");
                productService.PrecioCosto = response.getDouble("CostoPrecio");
                productService.ImporteCosto = response.getDouble("CostoImporte");
                productService.PrecioVenta = response.getDouble("VentaPrecio");
                productService.ImporteVenta = response.getDouble("VentaImporte");
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return productService;
    }
}
