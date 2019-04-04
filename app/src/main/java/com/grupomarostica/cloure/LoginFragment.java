package com.grupomarostica.cloure;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.Core.Core;

import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class LoginFragment extends Fragment {
    public TextInputEditText txtUser;
    public TextInputEditText txtPass;
    public CheckBox mCheckBox;
    private TextView tvVersion;
    private ProgressBar progressBar;
    private Handler handler = new Handler();
    private IInAppBillingService mService;
    private JSONObject Locales;

    Button mEmailSignInButton;
    Button btnRegister;

    TextInputLayout txtUserPrompt;
    TextInputLayout txtPassPrompt;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login,container, false);

        try {
            Core.lang = Locale.getDefault().getDisplayLanguage();

            txtUser = rootView.findViewById(R.id.fragment_login_txt_email);
            txtPass = rootView.findViewById(R.id.fragment_login_txt_pass);
            mCheckBox = rootView.findViewById(R.id.ch_remember_me);
            progressBar = rootView.findViewById(R.id.fragment_login_progress);
            tvVersion = rootView.findViewById(R.id.fragment_login_tv_version);
            txtUserPrompt = rootView.findViewById(R.id.fragment_login_til_user_mail);
            txtPassPrompt = rootView.findViewById(R.id.fragment_login_til_password);

            mEmailSignInButton = rootView.findViewById(R.id.fragment_login_btn_login);
            btnRegister = rootView.findViewById(R.id.fragment_login_btn_register);

            mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), RegistrationActivity.class);
                    startActivity(intent);
                }
            });

            loadLocales();

            PackageInfo pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            String version = pInfo.versionName;
            Core.versionName = version;
            tvVersion.setText("1."+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void loadLocales(){
        try {
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("topic", "get_locales"));
            params.add(new CloureParam("module_name", "users"));
            params.add(new CloureParam("lang", Core.lang));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);

            Locales = new JSONObject(res);
            txtUserPrompt.setHint(Locales.getString("user_login_field"));
            txtPassPrompt.setHint(Locales.getString("password"));
            btnRegister.setText(Locales.getString("register_button"));
            mEmailSignInButton.setText(Locales.getString("login_button"));
            mCheckBox.setText(Locales.getString("keep_connected"));

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void attemptLogin() {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new BGLoginTask(txtUser.getText().toString(), txtPass.getText().toString())).start();
    }

    private class BGLoginTask implements Runnable{
        String user;
        String pass;
        String message="";
        boolean error = false;

        BGLoginTask(String user, String pass){
            this.user = user;
            this.pass = pass;
        }

        @Override
        public void run() {
            CloureSDK cloureSDK = new CloureSDK();
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("topic", "login"));
            params.add(new CloureParam("user", user));
            params.add(new CloureParam("pass", pass));
            String api_response_str = cloureSDK.execute(params);
            String user_token = "";

            try{
                if(!api_response_str.equals("")){
                    JSONObject object = new JSONObject(api_response_str);
                    String error = object.getString("Error");
                    if(error.equals("")){
                        JSONObject response = object.getJSONObject("Response");
                        message = response.getString("message");

                        Core.appToken = response.getString("app_token");
                        Core.userToken = response.getString("user_token");
                        Core.modulesGroupsArr = response.getJSONArray("modules_groups");
                        Core.accountType = response.getString("account_type");
                        Core.finishedByBackButton = false;

                        Intent myIntent = new Intent(getContext(), MainActivity.class);
                        startActivity(myIntent);
                    } else {
                        message = error;
                    }

                    if(mCheckBox.isChecked()){
                        Core.savedData = true;
                        JSONObject data = new JSONObject();
                        data.put("user", user);
                        data.put("pass", pass);
                        String data_str = data.toString();
                        FileOutputStream outputStream = getContext().openFileOutput("cloure_data", Context.MODE_PRIVATE);
                        outputStream.write(data_str.getBytes());
                        outputStream.close();
                    }
                }
            } catch (Exception ex){
                error = true;
                ex.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                    if(error) Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
