package com.grupomarostica.cloure.Modules.users_groups;

import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.ClourePrivilege;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.Core.ModulePrivilege;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UsersGroups {
    public ArrayList<UserGroup> get_list(){
        ArrayList<UserGroup> registers = new ArrayList<>();

        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "users_groups"));
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
                    UserGroup register_tmp = new UserGroup();
                    register_tmp.Id = registro.getString("Id");
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

    public static ArrayList<ModulePrivilege> getPrivileges(String groupId){
        ArrayList<ModulePrivilege> registers = new ArrayList<>();

        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("topic", "get_modules_privileges"));
            params.add(new CloureParam("grupo_id", groupId));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);
            JSONObject api_result = new JSONObject(res);
            String error = api_result.getString("Error");

            if(error.equals("")){
                JSONObject api_response = api_result.getJSONObject("Response");
                JSONArray registros = api_response.getJSONArray("Registros");

                for (int i=0; i<registros.length(); i++){
                    JSONObject registro = registros.getJSONObject(i);
                    ModulePrivilege modulePrivilege = new ModulePrivilege();
                    modulePrivilege.ClourePrivileges = new ArrayList<>();
                    modulePrivilege.ModuleTitle = registro.getString("Title");
                    modulePrivilege.ModuleId = registro.getString("Id");

                    JSONArray privileges_tmp = registro.getJSONArray("Privileges");
                    for (int j=0; j<privileges_tmp.length(); j++){
                        JSONObject privilegeObject = privileges_tmp.getJSONObject(j);
                        ClourePrivilege item = new ClourePrivilege(
                                privilegeObject.getString("Id"),
                                privilegeObject.getString("Titulo"),
                                privilegeObject.getString("Type"),
                                privilegeObject.getString("Value"));
                        modulePrivilege.ClourePrivileges.add(item);
                    }
                    registers.add(modulePrivilege);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return registers;
    }
}
