package com.grupomarostica.cloure.Modules.maintenance_equipments_brands;

import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MaintenanceEquipmentBrands {
    public static ArrayList<MaintenanceEquipmentBrand> GetList(){
        ArrayList<MaintenanceEquipmentBrand> registers = new ArrayList<>();
        ArrayList<CloureParam> params = new ArrayList<>();
        params.add(new CloureParam("module", "maintenance_equipments_brands"));
        params.add(new CloureParam("topic", "listar"));

        try{
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);
            JSONObject object = new JSONObject(res);
            String error = object.getString("Error");
            if(error.equals("")){
                JSONObject response = object.getJSONObject("Response");
                JSONArray registros = response.getJSONArray("Registros");

                if(registros.length()>0){
                    for (int i=0; i<registros.length(); i++){
                        JSONObject registro = registros.getJSONObject(i);
                        MaintenanceEquipmentBrand register_tmp = new MaintenanceEquipmentBrand();
                        register_tmp.Id = registro.getInt("Id");
                        register_tmp.Name = registro.getString("Nombre");

                        /*
                        JSONArray availableCommands = registro.getJSONArray("AvailableCommands");
                        if(availableCommands!=null){
                            for (int j = 0; j<availableCommands.length(); j++){
                                JSONObject jobj = availableCommands.getJSONObject(j);
                                int cmd_id = jobj.getInt("Id");
                                String cmd_name = jobj.getString("Name");
                                String cmd_title = jobj.getString("Title");
                                AvailableCommand availableCommand = new AvailableCommand(cmd_id, cmd_name, cmd_title);
                                register_tmp.AvailableCommands.add(availableCommand);
                            }
                        }
                        */
                        registers.add(register_tmp);
                    }
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return registers;
    }

    public static JSONObject Save(MaintenanceEquipmentBrand brand){
        JSONObject result = null;

        ArrayList<CloureParam> params = new ArrayList<>();
        params.add(new CloureParam("module", "maintenance_equipments_brands"));
        params.add(new CloureParam("topic", "guardar"));
        params.add(new CloureParam("id", brand.Id));
        params.add(new CloureParam("name", brand.Name));

        CloureSDK cloureSDK = new CloureSDK();
        try{
            String res = cloureSDK.execute(params);
            result = new JSONObject(res);
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return result;
    }
}
