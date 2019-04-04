package com.grupomarostica.cloure.Modules.countries;

import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Countries {
    public ArrayList<Country> get_list(){
        ArrayList<Country> registers = new ArrayList<>();

        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "countries"));
            params.add(new CloureParam("topic", "get_list"));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);
            JSONObject object = new JSONObject(res);
            String error = object.getString("Error");
            if(error.equals("")){
                JSONObject response = object.getJSONObject("Response");
                JSONArray registros = response.getJSONArray("Registros");
                for (int i=0; i<registros.length(); i++){
                    JSONObject registro = registros.getJSONObject(i);
                    Country register_tmp = new Country();
                    register_tmp.Id = registro.getInt("Id");
                    register_tmp.Name = registro.getString("Nombre");
                    registers.add(register_tmp);
                    //if(selected.equals(register_tmp.Id)) this.selected = register_tmp;
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return registers;
    }

    public static ArrayList<Country> getCloureList(){
        ArrayList<Country> registers = new ArrayList<>();

        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "countries"));
            params.add(new CloureParam("topic", "get_cloure_list"));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);
            JSONObject object = new JSONObject(res);
            String error = object.getString("Error");
            if(error.equals("")){
                JSONObject response = object.getJSONObject("Response");
                JSONArray registros = response.getJSONArray("Registros");
                for (int i=0; i<registros.length(); i++){
                    JSONObject registro = registros.getJSONObject(i);
                    Country register_tmp = new Country();
                    register_tmp.Id = registro.getInt("Id");
                    register_tmp.Name = registro.getString("Nombre");
                    registers.add(register_tmp);
                    //if(selected.equals(register_tmp.Id)) this.selected = register_tmp;
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return registers;
    }
}
