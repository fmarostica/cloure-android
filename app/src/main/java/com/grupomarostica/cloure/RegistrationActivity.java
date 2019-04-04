package com.grupomarostica.cloure;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.BusinessType;
import com.grupomarostica.cloure.Core.BusinessTypes;
import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.Core.Core;
import com.grupomarostica.cloure.Modules.countries.Countries;
import com.grupomarostica.cloure.Modules.countries.CountriesAdapter;
import com.grupomarostica.cloure.Modules.countries.Country;
import com.grupomarostica.cloure.Modules.users.Users;
import com.grupomarostica.cloure.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class RegistrationActivity extends AppCompatActivity {
    Button btnRegistro;
    ProgressBar progressBar;
    TextInputEditText txtName;
    TextInputEditText txtLastName;
    TextInputEditText txtMail;
    TextInputEditText txtPass;
    TextInputEditText txtRepeatPass;
    TextInputEditText txtBusinessName;
    TextInputEditText txtCloureUrl;
    Spinner txtBusinessType;
    Spinner txtCountry;
    TextView txtDisclaimer;

    TextInputLayout txtNamePrompt;
    TextInputLayout txtLastNamePrompt;
    TextInputLayout txtMailPrompt;
    TextInputLayout txtPassPrompt;
    TextInputLayout txtRepeatPassPrompt;
    TextInputLayout txtBusinessNamePrompt;
    TextView txtBusinessTypePrompt;
    TextView txtCountryPrompt;
    TextView txtCloureURLPrompt;
    TextInputLayout txtCloureURLHint;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        txtName = findViewById(R.id.registration_txt_name);
        txtLastName = findViewById(R.id.registration_txt_last_name);
        txtPass = findViewById(R.id.registration_txt_password);
        txtRepeatPass = findViewById(R.id.registration_txt_repeat_password);
        txtMail = findViewById(R.id.registration_txt_email);
        txtBusinessName = findViewById(R.id.registration_txt_company_name);
        txtBusinessType = findViewById(R.id.registration_txt_business_type);
        txtCountry = findViewById(R.id.registration_txt_country);
        btnRegistro = findViewById(R.id.registration_btn_confirm);
        txtCloureUrl = findViewById(R.id.registration_txt_cloure_url);
        progressBar = findViewById(R.id.registration_progress);

        txtNamePrompt = findViewById(R.id.registration_tv_name);
        txtLastNamePrompt = findViewById(R.id.registration_tv_last_name);
        txtMailPrompt = findViewById(R.id.registration_tv_email);
        txtPassPrompt = findViewById(R.id.registration_tv_password);
        txtRepeatPassPrompt = findViewById(R.id.registration_tv_repeat_pass);
        txtBusinessNamePrompt = findViewById(R.id.registration_tv_business_name);
        txtBusinessTypePrompt = findViewById(R.id.tv_chose_business_type_prompt);
        txtCountryPrompt = findViewById(R.id.tv_country_prompt);
        txtCloureURLPrompt = findViewById(R.id.tv_chose_cloure_web_prompt);
        txtCloureURLHint = findViewById(R.id.registration_tv_cloure_url);
        txtDisclaimer = findViewById(R.id.registration_tv_accept_terms);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

        LoadBusinessTypes();
        LoadCloureCountries();
        loadLocales();
    }

    private void loadLocales(){
        try {
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("topic", "get_locales"));
            params.add(new CloureParam("module_name", "users"));
            params.add(new CloureParam("lang", Core.lang));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);

            JSONObject Locales = new JSONObject(res);
            txtNamePrompt.setHint(Locales.getString("name"));
            txtLastNamePrompt.setHint(Locales.getString("last_name"));
            txtMailPrompt.setHint(Locales.getString("email"));
            txtPassPrompt.setHint(Locales.getString("password"));
            txtRepeatPassPrompt.setHint(Locales.getString("repeat_password"));
            txtBusinessNamePrompt.setHint(Locales.getString("business_name"));
            txtBusinessTypePrompt.setText(Locales.getString("business_type"));
            txtCountryPrompt.setText(Locales.getString("country"));
            txtCloureUrl.setText(Locales.getString("cloure_url_example"));
            txtCloureURLHint.setHint(Locales.getString("cloure_url_prompt"));
            btnRegistro.setText(Locales.getString("register_button"));
            txtDisclaimer.setText(Locales.getString("disclaimer"));

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void LoadBusinessTypes(){
        ArrayList<BusinessType> businessTypes = BusinessTypes.get_list();
        BusinessTypesAdapter businessTypesAdapter = new BusinessTypesAdapter(getApplicationContext(), businessTypes);
        txtBusinessType.setAdapter(businessTypesAdapter);
    }

    private void LoadCloureCountries(){
        ArrayList<Country> countries = Countries.getCloureList();
        CountriesAdapter countriesAdapter = new CountriesAdapter(getApplicationContext(), countries);
        txtCountry.setAdapter(countriesAdapter);
    }

    public void attemptRegister() {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new BGLoginTask()).start();
    }

    private class BGLoginTask implements Runnable{
        String message="";

        @Override
        public void run() {
            CloureSDK cloureSDK = new CloureSDK();
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("topic", "register_account"));
            params.add(new CloureParam("name", txtName.getText().toString()));
            params.add(new CloureParam("last_name", txtLastName.getText().toString()));
            params.add(new CloureParam("password", txtPass.getText().toString()));
            params.add(new CloureParam("repeat_password", txtRepeatPass.getText().toString()));
            params.add(new CloureParam("email", txtMail.getText().toString()));
            params.add(new CloureParam("company_name", txtBusinessName.getText().toString()));
            params.add(new CloureParam("country_id", ((Country)txtCountry.getSelectedItem()).Id));
            params.add(new CloureParam("company_type", ((BusinessType)txtBusinessType.getSelectedItem()).Id));
            params.add(new CloureParam("cloure_url", txtCloureUrl.getText().toString()));
            String api_response_str = cloureSDK.execute(params);
            String user_token = "";

            try{
                if(!api_response_str.equals("")){
                    JSONObject object = new JSONObject(api_response_str);
                    String error = object.getString("Error");
                    if(error.equals("")){
                        JSONObject response = object.getJSONObject("Response");

                        Core.appToken = response.getString("app_token");
                        Core.userToken = response.getString("user_token");
                        Core.accountType = response.getString("account_type");
                        Core.modulesGroupsArr = response.getJSONArray("modules_groups");
                        Core.loguedUser = Users.get(Core.userToken);
                        Core.primaryDomain = response.getString("primary_domain");

                        Intent myIntent = new Intent(RegistrationActivity.this, MainActivity.class);
                        startActivity(myIntent);
                    } else {
                        message = error;
                    }
                }
            } catch (Exception ex){
                ex.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
