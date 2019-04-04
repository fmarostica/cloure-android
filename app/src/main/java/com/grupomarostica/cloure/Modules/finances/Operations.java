package com.grupomarostica.cloure.Modules.finances;

import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Operations {
    private ArrayList<Operation> operations = new ArrayList<>();

    public ArrayList<Operation> get_list(){
        try{
            ArrayList<CloureParam> params;
            params = new ArrayList<>();
            params.add(new CloureParam("module", "finances"));
            params.add(new CloureParam("topic", "get_operations"));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);
            JSONObject object = new JSONObject(res);
            String error = object.getString("Error");
            if(error.equals("")){
                JSONObject response = object.getJSONObject("Response");
                JSONArray registros = response.getJSONArray("Registros");
                operations = new ArrayList<>();
                for (int i=0; i<registros.length(); i++){
                    JSONObject registro = registros.getJSONObject(i);
                    Operation register_tmp = new Operation();
                    register_tmp.Id = registro.getString("Id");
                    register_tmp.Name = registro.getString("Nombre");
                    operations.add(register_tmp);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return operations;
    }
}
