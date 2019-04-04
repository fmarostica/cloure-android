package com.grupomarostica.cloure.Modules.support;

import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Support {
    public static ArrayList<SupportType> getSupportTypes(){
        ArrayList<SupportType> items = new ArrayList<>();

        try{
            ArrayList<CloureParam> params;
            params = new ArrayList<>();
            params.add(new CloureParam("module", "support"));
            params.add(new CloureParam("topic", "get_support_types"));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);
            JSONObject object = new JSONObject(res);
            String error = object.getString("Error");
            if(error.equals("")){
                JSONObject response = object.getJSONObject("Response");
                JSONArray registros = response.getJSONArray("Registros");
                for (int i=0; i<registros.length(); i++){
                    JSONObject registro = registros.getJSONObject(i);
                    SupportType register_tmp = new SupportType();
                    register_tmp.Id = registro.getString("id");
                    register_tmp.Title = registro.getString("title");
                    items.add(register_tmp);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return items;
    }
}
