package com.grupomarostica.cloure.Modules.debit_cards;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.grupomarostica.cloure.R;

import org.json.JSONObject;

public class DebitCardAddActivity extends AppCompatActivity {
    TextInputEditText txtName;
    ImageButton btnSave;
    DebitCard debitCard = new DebitCard();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debit_card_add);

        txtName = findViewById(R.id.activity_add_debit_card_txtName);
        btnSave = findViewById(R.id.activity_add_debit_card_btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            debitCard.Id = extras.getInt("id");
            get_data(debitCard.Id);
        }
    }

    private void get_data(int Id){
        debitCard = DebitCards.get(Id);
        txtName.setText(debitCard.Name);
    }

    private void Save(){
        debitCard.Name = txtName.getText().toString();
        JSONObject result = DebitCards.Save(debitCard);

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
