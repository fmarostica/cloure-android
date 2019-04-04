package com.grupomarostica.cloure.Modules.bands_artists;

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

public class BandsArtistsAddActivity extends AppCompatActivity {
    private ImageButton btnSave;
    private BandArtist bandArtist;
    private boolean new_record=true;

    private TextInputEditText txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bands_artists_add);
        if(bandArtist==null) bandArtist = new BandArtist();

        Bundle extras = getIntent().getExtras();
        if(extras!=null) bandArtist.Id = extras.getInt("id");

        txtName = findViewById(R.id.bands_artistst_add_txt_name);

        btnSave = findViewById(R.id.bands_artistst_add_btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar();
            }
        });

        if(bandArtist.Id>0){
            try {
                ArrayList<CloureParam> params = new ArrayList<>();
                params.add(new CloureParam("module", "bands_artists"));
                params.add(new CloureParam("topic", "obtener"));
                params.add(new CloureParam("id", Integer.toString(bandArtist.Id)));
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

    private void guardar(){
        ArrayList<CloureParam> params = new ArrayList<>();
        params.add(new CloureParam("module", "bands_artists"));
        params.add(new CloureParam("topic", "guardar"));
        params.add(new CloureParam("id", Integer.toString(bandArtist.Id)));
        params.add(new CloureParam("nombre", txtName.getText().toString()));

        CloureSDK cloureSDK = new CloureSDK();
        try{
            String res = cloureSDK.execute(params);
            JSONObject response = new JSONObject(res);
            String error = response.getString("Error");
            if(error.equals("")){
                //JSONObject response = object.getJSONObject("Response");
                bandArtist.Id = response.getInt("Response");

                InputMethodManager imm = (InputMethodManager) BandsArtistsAddActivity.this.getSystemService(BandsArtistsAddActivity.this.INPUT_METHOD_SERVICE);
                View nview = BandsArtistsAddActivity.this.getCurrentFocus();
                imm.hideSoftInputFromWindow(nview.getWindowToken(), 0);
                if (nview == null) {
                    nview = new View(BandsArtistsAddActivity.this);
                }
                Intent intent = new Intent();
                intent.putExtra("band_artist_id", bandArtist.Id);
                intent.putExtra("band_artist_name", txtName.getText().toString());
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
