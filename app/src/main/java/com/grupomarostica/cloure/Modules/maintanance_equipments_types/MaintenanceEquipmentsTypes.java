package com.grupomarostica.cloure.Modules.maintanance_equipments_types;

import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MaintenanceEquipmentsTypes {


    public ArrayList<MaintenanceEquipmentType> get_list(){
        ArrayList<MaintenanceEquipmentType> registers = new ArrayList<>();

        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "maintanance_equipments_types"));
            params.add(new CloureParam("topic", "listar"));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);
            JSONObject object = new JSONObject(res);
            String error = object.getString("Error");
            if(error.equals("")){
                JSONObject response = object.getJSONObject("Response");
                JSONArray registros = response.getJSONArray("Registros");
                registers = new ArrayList<>();
                for (int i=0; i<registros.length(); i++){
                    JSONObject registro = registros.getJSONObject(i);
                    MaintenanceEquipmentType register_tmp = new MaintenanceEquipmentType();
                    register_tmp.id = registro.getInt("Id");
                    register_tmp.name = registro.getString("Name");
                    registers.add(register_tmp);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return registers;
    }
}
