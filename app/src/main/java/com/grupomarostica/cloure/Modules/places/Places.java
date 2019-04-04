package com.grupomarostica.cloure.Modules.places;

import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Places {
    private ArrayList<Place> places = new ArrayList<>();

    public ArrayList<Place> get_list(String filtro){
        places = new ArrayList<>();
        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "places"));
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
                    Place register_tmp = new Place();
                    JSONObject registro = registros.getJSONObject(i);
                    register_tmp.Id = registro.getInt("Id");
                    register_tmp.Name = registro.getString("Nombre");
                    places.add(register_tmp);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return places;
    }
}
