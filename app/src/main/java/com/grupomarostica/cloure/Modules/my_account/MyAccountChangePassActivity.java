package com.grupomarostica.cloure.Modules.my_account;

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

public class MyAccountChangePassActivity extends AppCompatActivity {

    private ImageButton btnSave;
    private TextInputEditText txtOldPass;
    private TextInputEditText txtNewPass;
    private TextInputEditText txtRepeatPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account_change_pass);

        //ui
        btnSave = findViewById(R.id.activity_change_pass_btn_save);
        txtOldPass = findViewById(R.id.activity_change_pass_txt_old_pass);
        txtNewPass = findViewById(R.id.activity_change_pass_txt_new_pass);
        txtRepeatPass = findViewById(R.id.activity_change_pass_txt_repeat_pass);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    private void save(){
        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "users"));
            params.add(new CloureParam("topic", "cambiar_clave"));
            params.add(new CloureParam("clave_anterior", txtOldPass.getText().toString()));
            params.add(new CloureParam("clave_nueva", txtNewPass.getText().toString()));
            params.add(new CloureParam("repetir_clave", txtRepeatPass.getText().toString()));

            CloureSDK cloureSDK = new CloureSDK();

            String res = cloureSDK.execute(params);
            JSONObject api_response = new JSONObject(res);
            String error = api_response.getString("Error");
            String response = api_response.getString("Response");
            if(error.equals("")){
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                InputMethodManager imm = (InputMethodManager) MyAccountChangePassActivity.this.getSystemService(MyAccountChangePassActivity.this.INPUT_METHOD_SERVICE);
                View nview = MyAccountChangePassActivity.this.getCurrentFocus();
                imm.hideSoftInputFromWindow(nview.getWindowToken(), 0);
                if (nview == null) {
                    nview = new View(MyAccountChangePassActivity.this);
                }
                finish();
            } else {
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
