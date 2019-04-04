package com.grupomarostica.cloure.Modules.company_branches_receipts;

import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CompanyBranchesReceipts {
    public static ArrayList<CompanyBranchReceipt> getList(){
        ArrayList<CompanyBranchReceipt> items = new ArrayList<>();

        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "company_branches_receipts"));
            params.add(new CloureParam("topic", "listar"));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);
            JSONObject object = new JSONObject(res);
            String error = object.getString("Error");
            if(error.equals("")){
                JSONObject response = object.getJSONObject("Response");
                JSONArray registros = response.getJSONArray("Registros");
                for (int i=0; i<registros.length(); i++){
                    JSONObject registro = registros.getJSONObject(i);
                    CompanyBranchReceipt register_tmp = new CompanyBranchReceipt();
                    register_tmp.Id = registro.getInt("Id");
                    register_tmp.Name = registro.getString("Nombre");

                    //Get list of available commands
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

                    items.add(register_tmp);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return items;
    }
}
