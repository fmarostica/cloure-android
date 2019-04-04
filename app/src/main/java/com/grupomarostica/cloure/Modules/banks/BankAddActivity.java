package com.grupomarostica.cloure.Modules.banks;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.grupomarostica.cloure.R;

import org.json.JSONException;
import org.json.JSONObject;

public class BankAddActivity extends AppCompatActivity {
    TextInputEditText txtName;
    TextInputEditText txtOnlineBanking;
    TextInputEditText txtWeb;
    ImageButton btnSave;
    Bank bank = new Bank();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_add);

        txtName = findViewById(R.id.activity_add_bank_txtName);
        txtOnlineBanking = findViewById(R.id.activity_add_bank_txtOnlineBankingUrl);
        txtWeb = findViewById(R.id.activity_add_bank_txtWeb);
        btnSave = findViewById(R.id.activity_add_bank_btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            bank.Id = extras.getInt("id");
            get_data(bank.Id);
        }
    }

    private void get_data(int Id){
        bank = Banks.get(Id);

        txtName.setText(bank.Name);
        txtOnlineBanking.setText(bank.OnlineBankingURL);
        txtWeb.setText(bank.Web);
    }

    private void Save(){
        bank.Name = txtName.getText().toString();
        bank.OnlineBankingURL = txtOnlineBanking.getText().toString();
        bank.Web = txtWeb.getText().toString();
        JSONObject result = Banks.Save(bank);

        try {
            String error = result.getString("Error");
            if(error.equals("")){
                finish();
            } else{
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
