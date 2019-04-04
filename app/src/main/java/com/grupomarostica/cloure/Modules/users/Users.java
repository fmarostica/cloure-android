package com.grupomarostica.cloure.Modules.users;

import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.AvailableCommand;
import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class Users {
    public static ArrayList<User> get_list(String filtro){
        ArrayList<User> users = new ArrayList<>();

        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "users"));
            params.add(new CloureParam("topic", "get_list"));
            params.add(new CloureParam("filtro", filtro));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);
            JSONObject object = new JSONObject(res);
            String error = object.getString("Error");
            if(error.equals("")){
                JSONObject response = object.getJSONObject("Response");
                JSONArray registros = response.getJSONArray("Registros");
                users = new ArrayList<>();
                for (int i=0; i<registros.length(); i++){
                    JSONObject registro = registros.getJSONObject(i);
                    User register_tmp = new User();
                    register_tmp.id = registro.getInt("id");
                    register_tmp.nombre = registro.getString("nombre");
                    register_tmp.apellido = registro.getString("apellido");
                    register_tmp.telefono = registro.getString("telefono");
                    register_tmp.email = registro.getString("mail");
                    register_tmp.saldo = registro.getDouble("saldo");
                    register_tmp.direccion = registro.getString("direccion");
                    register_tmp.ImageURL = registro.getString("imagen");

                    URL url_image = new URL(register_tmp.ImageURL);
                    register_tmp.Image = BitmapFactory.decodeStream(url_image.openConnection().getInputStream());

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

                    users.add(register_tmp);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return users;
    }

    public static User get(String userToken){
        User user = new User();
        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "users"));
            params.add(new CloureParam("topic", "get_by_token"));
            params.add(new CloureParam("usr_token", userToken));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);
            JSONObject api_result = new JSONObject(res);
            String error = api_result.getString("Error");
            if(error.equals("")){
                JSONObject user_obj = api_result.getJSONObject("Response");

                user.id = user_obj.getInt("id");
                user.nombre = user_obj.getString("nombre");
                user.apellido = user_obj.getString("apellido");
                user.telefono = user_obj.getString("telefono");
                user.email = user_obj.getString("mail");
                user.saldo = user_obj.getDouble("saldo");
                user.ImageURL = user_obj.getString("imagen");
            } else {
                throw new Exception(error);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return user;
    }

    public static User getById(int Id){
        User user = new User();
        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "users"));
            params.add(new CloureParam("topic", "get_by_id"));
            params.add(new CloureParam("id", Id));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);
            JSONObject api_result = new JSONObject(res);
            String error = api_result.getString("Error");
            if(error.equals("")){
                JSONObject user_obj = api_result.getJSONObject("Response");

                user.id = Id;
                user.nombre = user_obj.getString("nombre");
                user.apellido = user_obj.getString("apellido");
                user.telefono = user_obj.getString("telefono");
                user.direccion = user_obj.getString("direccion");
                user.email = user_obj.getString("mail");
                user.grupo_id = user_obj.getString("grupo_id");
                //user.saldo = user_obj.getDouble("saldo");
                user.ImageURL = user_obj.getString("imagen");

                if(!user.ImageURL.equals("")){
                    URL url_image = new URL(user.ImageURL);
                    user.Image = BitmapFactory.decodeStream(url_image.openConnection().getInputStream());
                }

            } else {
                throw new Exception(error);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return user;
    }
}
