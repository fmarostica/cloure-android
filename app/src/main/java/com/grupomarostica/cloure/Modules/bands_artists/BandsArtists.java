package com.grupomarostica.cloure.Modules.bands_artists;

import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BandsArtists {
    private ArrayList<BandArtist> bands_artists = new ArrayList<>();

    public ArrayList<BandArtist> get_list(String filtro){
        bands_artists = new ArrayList<>();
        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "bands_artists"));
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
                    BandArtist register_tmp = new BandArtist();
                    JSONObject registro = registros.getJSONObject(i);
                    register_tmp.Id = registro.getInt("Id");
                    register_tmp.Name = registro.getString("Nombre");
                    bands_artists.add(register_tmp);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return bands_artists;
    }
}
