package com.grupomarostica.cloure.Modules.maintenance_shifts;

import com.grupomarostica.cloure.Core.AvailableCommand;
import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MaintenanceShifts {

    public static MaintenanceShift get(int id){
        MaintenanceShift maintenanceShift = new MaintenanceShift();
        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "maintenance_shifts"));
            params.add(new CloureParam("topic", "obtener"));
            params.add(new CloureParam("id", Integer.toString(id)));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);
            JSONObject api_result = new JSONObject(res);
            String error = api_result.getString("Error");
            if(error.equals("")){
                JSONObject jsonObject = api_result.getJSONObject("Response");

                maintenanceShift.Id = jsonObject.getInt("Id");
                maintenanceShift.CustomerId = jsonObject.getInt("UserId");
                maintenanceShift.FechaStr = jsonObject.getString("FechaStr");
                maintenanceShift.HoraStr = jsonObject.getString("HoraStr");
                maintenanceShift.EquipmentTypeId = jsonObject.getInt("EquipmentTypeId");
                maintenanceShift.IssueDescription = jsonObject.getString("IssueDescription");
                maintenanceShift.Customer = jsonObject.getString("Customer");
                maintenanceShift.CustomerAddress = jsonObject.getString("Address");
                maintenanceShift.CustomerPhone = jsonObject.getString("CustomerPhone");
                maintenanceShift.StatusId = jsonObject.getInt("StatusId");
                maintenanceShift.Status = jsonObject.getString("Status");
            } else {
                throw new Exception(error);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return maintenanceShift;
    }

    public static ArrayList<MaintenanceShift> GetList(String EstadoId){
        ArrayList<MaintenanceShift> registers = new ArrayList<>();
        ArrayList<CloureParam> params = new ArrayList<>();
        params.add(new CloureParam("module", "maintenance_shifts"));
        params.add(new CloureParam("topic", "listar"));
        params.add(new CloureParam("estado_id", EstadoId));

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
                        MaintenanceShift register_tmp = new MaintenanceShift();
                        register_tmp.Id = registro.getInt("Id");
                        register_tmp.FechaCompletaStr = registro.getString("FechaCompletaStr");
                        register_tmp.FechaStr = registro.getString("FechaStr");
                        register_tmp.HoraStr = registro.getString("HoraStr");
                        register_tmp.IssueDescription = registro.getString("IssueDescription");
                        register_tmp.Customer = registro.getString("Customer");
                        register_tmp.CustomerAddress = registro.getString("Address");
                        register_tmp.CustomerPhone = registro.getString("CustomerPhone");
                        register_tmp.StatusId = registro.getInt("StatusId");
                        register_tmp.Status = registro.getString("Status");

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

                        registers.add(register_tmp);
                    }
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return registers;
    }

    public static JSONObject Save(MaintenanceShift maintenanceShift){
        JSONObject result = null;

        ArrayList<CloureParam> params = new ArrayList<>();
        params.add(new CloureParam("module", "maintenance_shifts"));
        params.add(new CloureParam("topic", "guardar"));
        params.add(new CloureParam("id", maintenanceShift.Id));

        CloureSDK cloureSDK = new CloureSDK();
        try{
            String res = cloureSDK.execute(params);
            result = new JSONObject(res);
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return result;
    }

    public static JSONObject Finish(MaintenanceShift maintenanceShift){
        JSONObject result = null;

        ArrayList<CloureParam> params = new ArrayList<>();
        params.add(new CloureParam("module", "maintenance_shifts"));
        params.add(new CloureParam("topic", "finalizar"));
        params.add(new CloureParam("id", maintenanceShift.Id));

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
