package com.grupomarostica.cloure.Modules.products_services_categories;

import android.content.Context;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;

import org.json.JSONObject;

import java.util.ArrayList;

public class ProductsServicesCategories {
    private Context context;

    ProductsServicesCategories(Context context){
        this.context = context;
    }

    public ProductCategory get_item(int id, String type){
        ProductCategory productCategory = new ProductCategory();

        try {
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "products_services_categories"));
            params.add(new CloureParam("topic", "obtener"));
            params.add(new CloureParam("id", Integer.toString(id)));
            params.add(new CloureParam("tipo", type));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);

            JSONObject api_response = new JSONObject(res);
            String error = api_response.getString("Error");
            if(error.equals("")){
                JSONObject response = api_response.getJSONObject("Response");
                productCategory.Id = response.getInt("Id");
                productCategory.Name = response.getString("Nombre");
            } else {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex){
            productCategory = null;
            ex.printStackTrace();
        }

        return productCategory;
    }
}
