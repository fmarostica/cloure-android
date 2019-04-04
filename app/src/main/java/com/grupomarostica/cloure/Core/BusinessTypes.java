package com.grupomarostica.cloure.Core;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BusinessTypes {
    public static ArrayList<BusinessType> get_list(){
        ArrayList<BusinessType> arrayList = new ArrayList<>();

        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("topic", "get_business_types"));

            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);
            JSONObject object = new JSONObject(res);
            String error = object.getString("Error");
            if(error.equals("")){
                JSONObject response = object.getJSONObject("Response");
                JSONArray registros = response.getJSONArray("Registros");
                arrayList = new ArrayList<>();
                for (int i=0; i<registros.length(); i++){
                    JSONObject registro = registros.getJSONObject(i);
                    BusinessType register_tmp = new BusinessType();
                    register_tmp.Id = registro.getString("id");
                    register_tmp.Title = registro.getString("title");

                    arrayList.add(register_tmp);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return arrayList;
    }
}
