package com.grupomarostica.cloure.Modules.maintanance_equipments_types;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class MaintananceEquipmentTypeAddActivity extends AppCompatActivity {
    private ImageButton btnSave;
    private int equipment_type_id=0;
    private String equipment_type_name="";
    private EditText txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintanance_equipment_type_add);

        txtName = findViewById(R.id.activity_equipment_type_add_txt_name);
        btnSave = findViewById(R.id.activity_equipment_type_add_btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<CloureParam> params = new ArrayList<>();
                params.add(new CloureParam("module", "maintanance_equipments_types"));
                params.add(new CloureParam("topic", "guardar"));
                params.add(new CloureParam("id", Integer.toString(equipment_type_id)));
                params.add(new CloureParam("name", txtName.getText().toString()));

                CloureSDK cloureSDK = new CloureSDK();
                try{
                    String res = cloureSDK.execute(params);
                    JSONObject response = new JSONObject(res);
                    String error = response.getString("Error");
                    if(error.equals("")){
                        //JSONObject response = object.getJSONObject("Response");
                        equipment_type_id = response.getInt("Response");

                        InputMethodManager imm = (InputMethodManager) MaintananceEquipmentTypeAddActivity.this.getSystemService(MaintananceEquipmentTypeAddActivity.this.INPUT_METHOD_SERVICE);
                        View nview = MaintananceEquipmentTypeAddActivity.this.getCurrentFocus();
                        imm.hideSoftInputFromWindow(nview.getWindowToken(), 0);
                        if (nview == null) {
                            nview = new View(MaintananceEquipmentTypeAddActivity.this);
                        }
                        Intent intent = new Intent();
                        intent.putExtra("equipment_type_id", equipment_type_id);
                        intent.putExtra("equipment_type_name", txtName.getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(getBaseContext(), error, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex){
                    ex.printStackTrace();
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
