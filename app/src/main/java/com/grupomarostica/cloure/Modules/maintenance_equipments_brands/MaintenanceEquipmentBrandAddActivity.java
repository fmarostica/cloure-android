package com.grupomarostica.cloure.Modules.maintenance_equipments_brands;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.grupomarostica.cloure.R;

import org.json.JSONException;
import org.json.JSONObject;

public class MaintenanceEquipmentBrandAddActivity extends AppCompatActivity {
    ImageButton btnSave;
    TextInputEditText txtNombre;
    MaintenanceEquipmentBrand brand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_equipment_brand_add);

        txtNombre = findViewById(R.id.activity_equipment_brand_add_txt_name);

        btnSave = findViewById(R.id.activity_equipment_brand_add_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });
    }

    private void Save(){
        if(brand==null) brand = new MaintenanceEquipmentBrand();
        brand.Name = txtNombre.getText().toString();
        JSONObject result = MaintenanceEquipmentBrands.Save(brand);
        try {
            String error = result.getString("Error");
            if(error.equals("")){
                finish();
            } else{
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
