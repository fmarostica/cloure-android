package com.grupomarostica.cloure.Modules.credit_cards;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.grupomarostica.cloure.R;

import org.json.JSONObject;

public class CreditCardAddActivity extends AppCompatActivity {
    TextInputEditText txtName;
    ImageButton btnSave;
    CreditCard creditCard = new CreditCard();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_add);

        txtName = findViewById(R.id.activity_add_credit_card_txtName);
        btnSave = findViewById(R.id.activity_add_credit_card_btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            creditCard.Id = extras.getInt("id");
            get_data(creditCard.Id);
        }
    }

    private void get_data(int Id){
        creditCard = CreditCards.get(Id);
        txtName.setText(creditCard.Name);
    }

    private void Save(){
        creditCard.Name = txtName.getText().toString();
        JSONObject result = CreditCards.Save(creditCard);

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
