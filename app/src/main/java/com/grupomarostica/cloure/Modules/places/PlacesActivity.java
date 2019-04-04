package com.grupomarostica.cloure.Modules.places;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class PlacesActivity extends AppCompatActivity {
    Place place=new Place();
    ImageButton btnSave;
    TextInputEditText txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            place.Id = extras.getInt("id");
        }

        btnSave = findViewById(R.id.places_add_btn_save);
        txtName = findViewById(R.id.places_add_txt_name);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        if(place.Id>0){
            try {
                ArrayList<CloureParam> params = new ArrayList<>();
                params.add(new CloureParam("module", "places"));
                params.add(new CloureParam("topic", "obtener"));
                params.add(new CloureParam("id", Integer.toString(place.Id)));
                CloureSDK cloureSDK = new CloureSDK();
                String res = cloureSDK.execute(params);

                JSONObject object = new JSONObject(res);
                String error = object.getString("Error");
                if(error.equals("")){
                    JSONObject response = object.getJSONObject("Response");
                    txtName.setText(response.getString("Nombre"));
                } else {
                    Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private void save(){
        ArrayList<CloureParam> params = new ArrayList<>();
        params.add(new CloureParam("module", "places"));
        params.add(new CloureParam("topic", "guardar"));
        params.add(new CloureParam("id", Integer.toString(place.Id)));
        params.add(new CloureParam("nombre", txtName.getText().toString()));

        CloureSDK cloureSDK = new CloureSDK();
        try{
            String res = cloureSDK.execute(params);
            JSONObject response = new JSONObject(res);
            String error = response.getString("Error");
            if(error.equals("")){
                place.Id = response.getInt("Response");

                InputMethodManager imm = (InputMethodManager) PlacesActivity.this.getSystemService(PlacesActivity.this.INPUT_METHOD_SERVICE);
                View nview = PlacesActivity.this.getCurrentFocus();
                imm.hideSoftInputFromWindow(nview.getWindowToken(), 0);
                if (nview == null) {
                    nview = new View(PlacesActivity.this);
                }
                Intent intent = new Intent();
                intent.putExtra("place_id", place.Id);
                intent.putExtra("place_name", txtName.getText().toString());
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
}
