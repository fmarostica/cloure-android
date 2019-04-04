package com.grupomarostica.cloure.Modules.receipts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.grupomarostica.cloure.R;

public class ReceiptsDetailsActivity extends AppCompatActivity {
    TextView txtCliente;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipts_details);

        txtCliente = findViewById(R.id.activity_receipts_details_txtCliente);
        listView = findViewById(R.id.activity_receipts_details_lstItems);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            int id = extras.getInt("id");
            Receipt receipt = Receipts.get(id);
            txtCliente.setText(receipt.CustomerName);

            CartItemsAdapter adapter = new CartItemsAdapter(getBaseContext(), receipt.Items);
            listView.setAdapter(adapter);
        }
    }
}
