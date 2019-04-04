package com.grupomarostica.cloure.Modules.users;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.Modules.payments_methods.PaymentMethod;
import com.grupomarostica.cloure.Modules.payments_methods.PaymentsMethods;
import com.grupomarostica.cloure.Modules.payments_methods.PaymentsMethodsArrayAdapter;
import com.grupomarostica.cloure.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class UsersAddPaymetActivity extends AppCompatActivity {
    private ArrayList<PaymentMethod> paymentMethods = new ArrayList<>();
    private PaymentsMethodsArrayAdapter paymentsMethodsArrayAdapter;
    private Spinner txtPaymentMethod;
    private ImageButton btnSave;
    private TextInputEditText txtAmount;

    private int userId = 0;
    private int payment_method_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_add_paymet);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            userId = extras.getInt("user_id");
        }

        txtPaymentMethod = findViewById(R.id.users_payment_add_txt_method);
        btnSave = findViewById(R.id.users_payment_add_btn_save);
        txtAmount = findViewById(R.id.users_payment_add_txt_amount);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        load_payments_methods("");
        txtPaymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PaymentMethod pm = (PaymentMethod)parent.getItemAtPosition(position);
                if(pm!=null) payment_method_id = pm.Id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void load_payments_methods(String filtro){
        paymentMethods = new PaymentsMethods().get_list(filtro);
        paymentsMethodsArrayAdapter = new PaymentsMethodsArrayAdapter(getBaseContext(), paymentMethods);
        txtPaymentMethod.setAdapter(paymentsMethodsArrayAdapter);
    }

    private void save(){
        ArrayList<CloureParam> params = new ArrayList<>();
        params.add(new CloureParam("module", "users"));
        params.add(new CloureParam("topic", "agregar_pago"));
        params.add(new CloureParam("usuario_id", Integer.toString(userId)));
        params.add(new CloureParam("importe", txtAmount.getText().toString()));
        params.add(new CloureParam("forma_de_pago_id", Integer.toString(payment_method_id)));
        params.add(new CloureParam("forma_de_pago_entidad_id", "0"));

        CloureSDK cloureSDK = new CloureSDK();
        try{
            String res = cloureSDK.execute(params);
            JSONObject response = new JSONObject(res);
            String error = response.getString("Error");
            if(error.equals("")){
                //JSONObject response = object.getJSONObject("Response");
                //equipment_type_id = response.getInt("Response");

                InputMethodManager imm = (InputMethodManager) UsersAddPaymetActivity.this.getSystemService(UsersAddPaymetActivity.this.INPUT_METHOD_SERVICE);
                View nview = UsersAddPaymetActivity.this.getCurrentFocus();
                imm.hideSoftInputFromWindow(nview.getWindowToken(), 0);
                if (nview == null) {
                    nview = new View(UsersAddPaymetActivity.this);
                }
                Toast.makeText(getApplicationContext(), R.string.successful_operation, Toast.LENGTH_SHORT).show();

                /*
                Intent intent = new Intent();
                intent.putExtra("equipment_type_id", equipment_type_id);
                intent.putExtra("equipment_type_name", txtName.getText().toString());
                setResult(RESULT_OK, intent);
                */
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
