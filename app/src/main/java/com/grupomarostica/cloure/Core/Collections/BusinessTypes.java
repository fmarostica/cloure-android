package com.grupomarostica.cloure.Core.Collections;

import android.util.Log;

import com.grupomarostica.cloure.Core.BusinessType;
import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

import static com.grupomarostica.cloure.Core.Core.API_ERROR_TAG;
import static com.grupomarostica.cloure.Core.Core.API_INVOKED;

public class BusinessTypes {
    public static ArrayList<BusinessType> getList(String filter){
        ArrayList<BusinessType> businessTypes = new ArrayList<>();
        Log.i(API_INVOKED, "Business Types getList init");

        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("topic", "get_business_types"));
            params.add(new CloureParam("filter", filter));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);
            JSONObject object = new JSONObject(res);
            String error = object.getString("Error");
            if(error.equals("")){
                JSONObject response = object.getJSONObject("Response");
                JSONArray registros = response.getJSONArray("Registros");
                for (int i=0; i<registros.length(); i++){
                    JSONObject registro = registros.getJSONObject(i);
                    BusinessType register_tmp = new BusinessType();
                    register_tmp.Id = registro.getString("id");
                    register_tmp.Title = registro.getString("title");
                    businessTypes.add(register_tmp);
                }
            } else {
                Log.w(API_ERROR_TAG, error);
                //Exception e = new Exception(error);
                //throw e;
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return businessTypes;
    }
}
