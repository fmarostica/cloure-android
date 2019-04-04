package com.grupomarostica.cloure.Modules.products_services_types;

import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductsServicesTypes {public ArrayList<ProductServiceType> get_list(String filtro){
    ArrayList<ProductServiceType> productServiceTypes = new ArrayList<>();

    try{
        ArrayList<CloureParam> params = new ArrayList<>();
        params.add(new CloureParam("module", "products_services_types"));
        params.add(new CloureParam("topic", "get_list"));
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
                ProductServiceType register_tmp = new ProductServiceType();
                register_tmp.Id = registro.getInt("Id");
                register_tmp.Title = registro.getString("Title");
                productServiceTypes.add(register_tmp);
            }
        }
    } catch (Exception ex){
        ex.printStackTrace();
    }

    return productServiceTypes;
}
}
