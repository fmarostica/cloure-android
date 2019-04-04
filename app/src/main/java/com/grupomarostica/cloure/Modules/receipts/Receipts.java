package com.grupomarostica.cloure.Modules.receipts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.grupomarostica.cloure.Core.AvailableCommand;
import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Receipts {
    public static ArrayList<Receipt> get_list(String filtro){
        ArrayList<Receipt> items = new ArrayList<>();
        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "receipts"));
            params.add(new CloureParam("topic", "listar"));
            params.add(new CloureParam("filtro", filtro));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);
            JSONObject object = new JSONObject(res);
            String error = object.getString("Error");
            if(error.equals("")){
                JSONObject response = object.getJSONObject("Response");
                JSONArray registros = response.getJSONArray("Registros");
                for (int i=0; i<registros.length(); i++){
                    JSONObject registro = registros.getJSONObject(i);
                    Receipt register_tmp = new Receipt();
                    register_tmp.Id = registro.getInt("Id");
                    register_tmp.Description = registro.getString("Detalles");

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

    public static Receipt get(int Id){
        Receipt receipt = new Receipt();
        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "receipts"));
            params.add(new CloureParam("topic", "obtener"));
            params.add(new CloureParam("id", Id));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);
            JSONObject object = new JSONObject(res);
            String error = object.getString("Error");
            if(error.equals("")){
                JSONObject response = object.getJSONObject("Response");
                receipt.Id = response.getInt("Id");
                receipt.CustomerName = response.getString("Usuario");

                JSONArray items_arr = response.getJSONArray("Items");
                for (int i=0; i<items_arr.length(); i++){
                    JSONObject itemObj = items_arr.getJSONObject(i);
                    int cant = itemObj.getInt("Cantidad");
                    String desc = itemObj.getString("Detalles");

                    CartItem item = new CartItem(cant, desc, 0,0);

                    receipt.Items.add(item);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return receipt;
    }

    public static JSONObject Save(Receipt receipt){
        JSONObject response = new JSONObject();

        try {

            //CREATE ITEMS IN JSON ARRAY FORMAT
            String ItemsArr = "[";
            for (int i = 0; i < receipt.Items.size(); i++)
            {
                CartItem cartItem = receipt.Items.get(i);
                ItemsArr += "{";
                ItemsArr += "\"id\": \"" + cartItem.ProductoId + "\",";
                ItemsArr += "\"cantidad\": \"" + cartItem.Cantidad + "\",";
                ItemsArr += "\"detalle\": \"" + cartItem.Descripcion + "\",";
                ItemsArr += "\"precio\": \"" + cartItem.Precio + "\",";
                ItemsArr += "\"iva\": \"" + cartItem.Iva + "\",";
                ItemsArr += "\"importe\": \"" + cartItem.Total + "\"";
                ItemsArr += "}";
                if (i < receipt.Items.size() - 1) ItemsArr += ",";
            }
            ItemsArr += "]";
            //END ITEMS ARRAY CREATION

            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "receipts"));
            params.add(new CloureParam("topic", "guardar"));
            params.add(new CloureParam("tipo_comprobante_id", receipt.ReceiptType));
            params.add(new CloureParam("cliente_id", receipt.CustomerId));
            params.add(new CloureParam("items", ItemsArr));
            params.add(new CloureParam("entrega", ""));
            params.add(new CloureParam("forma_de_pago_id", ""));
            params.add(new CloureParam("forma_de_pago_entidad_id", ""));
            params.add(new CloureParam("forma_de_pago", ""));
            params.add(new CloureParam("forma_de_pago_data", ""));
            params.add(new CloureParam("forma_de_pago_cobro", ""));
            params.add(new CloureParam("sucursal_id", "1"));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);
            response = new JSONObject(res);
        } catch (Exception e){
            e.printStackTrace();
        }

        return response;
    }

    public static File ExportPDF(int receiptId){
        File response = null;

        try {
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "receipts"));
            params.add(new CloureParam("topic", "export_pdf"));
            params.add(new CloureParam("comprobante_id", receiptId));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);
            JSONObject apiResponse = new JSONObject(res);
            String error = apiResponse.getString("Error");
            if(error.equals("")){
                JSONObject responseObject = apiResponse.getJSONObject("Response");
                String downloadURI = responseObject.getString("url");
                String fileName = responseObject.getString("file_name");
                URL url = new URL(downloadURI);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(false);
                c.connect();

                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                File file = new File(dir, fileName);
                if(file.createNewFile()){
                    FileOutputStream fos = new FileOutputStream(file);
                    InputStream is = c.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len1 = 0;
                    while ((len1 = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len1);
                    }
                    fos.close();
                    is.close();

                    response = file;
                }
            } else {
                throw new Exception(error);
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return response;
    }
}
