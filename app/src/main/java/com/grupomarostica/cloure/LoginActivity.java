package com.grupomarostica.cloure;

import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.grupomarostica.cloure.Core.Core;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class LoginActivity extends AppCompatActivity {
    LoginFragment loginFragment = new LoginFragment();
    TextInputEditText txtUser;
    TextInputEditText txtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.login_frame_layout, loginFragment);
        ft.commit();

        txtUser = loginFragment.txtUser;
        txtPass = loginFragment.txtPass;
    }

    @Override
    protected void onStart() {
        super.onStart();
        runOnUiThread(new BGLoadData());

    }

    private class BGLoadData implements Runnable{
        @Override
        public void run() {
            try{
                File file = getFileStreamPath("cloure_data");
                if(file!=null && file.exists() && !Core.finishedByBackButton){
                    FileInputStream fis = openFileInput("cloure_data");
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader bufferedReader = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                    String content = sb.toString();
                    JSONObject res_data = new JSONObject(content);
                    final String user = res_data.getString("user");
                    final String pass = res_data.getString("pass");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            txtUser = findViewById(R.id.fragment_login_txt_email);
                            txtPass = findViewById(R.id.fragment_login_txt_pass);
                            txtUser.setText(user);
                            txtPass.setText(pass);
                            loginFragment.mCheckBox.setChecked(true);
                            loginFragment.attemptLogin();
                        }
                    }, 1000);
                }
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
