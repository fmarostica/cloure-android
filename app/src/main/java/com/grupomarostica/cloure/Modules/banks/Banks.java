package com.grupomarostica.cloure.Modules.banks;

import com.grupomarostica.cloure.Core.AvailableCommand;
import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Banks {
    public static ArrayList<Bank> get_list(String filtro){
        ArrayList<Bank> items = new ArrayList<>();
        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "banks"));
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
                    JSONObject registro = registros.getJSONObject(i);
                    Bank register_tmp = new Bank();
                    register_tmp.Id = registro.getInt("Id");
                    register_tmp.Name = registro.getString("Nombre");

                    //Get list of available commands
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

                    items.add(register_tmp);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return items;
    }

    public static Bank get(int Id){
        Bank item = new Bank();
        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "banks"));
            params.add(new CloureParam("topic", "obtener"));
            params.add(new CloureParam("id", Id));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);
            JSONObject api_result = new JSONObject(res);
            String error = api_result.getString("Error");
            if(error.equals("")){
                JSONObject item_obj = api_result.getJSONObject("Response");

                item.Id = Id;
                item.Name = item_obj.getString("Nombre");
                item.OnlineBankingURL = item_obj.getString("OnlineBanking");
                item.Web = item_obj.getString("Web");
            } else {
                throw new Exception(error);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return item;
    }

    public static JSONObject Save(Bank item){
        JSONObject result = null;

        ArrayList<CloureParam> params = new ArrayList<>();
        params.add(new CloureParam("module", "banks"));
        params.add(new CloureParam("topic", "guardar"));
        params.add(new CloureParam("id", item.Id));
        params.add(new CloureParam("nombre", item.Name));
        params.add(new CloureParam("online_banking", item.OnlineBankingURL));
        params.add(new CloureParam("web", item.Web));

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
