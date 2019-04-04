package com.grupomarostica.cloure.Modules.payments_methods;

import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PaymentsMethods {
    private ArrayList<PaymentMethod> paymentMethods = new ArrayList<>();

    public ArrayList<PaymentMethod> get_list(String filtro){
        try{
            ArrayList<CloureParam> params;
            params = new ArrayList<>();
            params.add(new CloureParam("module", "payments_methods"));
            params.add(new CloureParam("topic", "get_list"));
            params.add(new CloureParam("filtro", filtro));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);
            JSONObject object = new JSONObject(res);
            String error = object.getString("Error");
            if(error.equals("")){
                JSONObject response = object.getJSONObject("Response");
                JSONArray registros = response.getJSONArray("Registros");
                paymentMethods = new ArrayList<>();
                for (int i=0; i<registros.length(); i++){
                    JSONObject registro = registros.getJSONObject(i);
                    PaymentMethod register_tmp = new PaymentMethod();
                    register_tmp.Id = registro.getInt("Id");
                    register_tmp.Name = registro.getString("Name");
                    paymentMethods.add(register_tmp);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return paymentMethods;
    }
}
