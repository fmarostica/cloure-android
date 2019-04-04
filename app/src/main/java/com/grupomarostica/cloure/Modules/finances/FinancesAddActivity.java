package com.grupomarostica.cloure.Modules.finances;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.Core.ModuleInfo;
import com.grupomarostica.cloure.Modules.payments_methods.PaymentMethod;
import com.grupomarostica.cloure.Modules.payments_methods.PaymentsMethods;
import com.grupomarostica.cloure.Modules.payments_methods.PaymentsMethodsArrayAdapter;
import com.grupomarostica.cloure.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class FinancesAddActivity extends AppCompatActivity {
    private ModuleInfo moduleInfo;
    private ImageButton btnSave;
    private Spinner txtOperation;
    private Spinner txtPaymentMethod;
    private TextInputEditText txtDescription;
    private TextInputEditText txtAmount;

    private ArrayList<PaymentMethod> paymentMethods;
    private ArrayList<Operation> operations;

    private PaymentsMethodsArrayAdapter paymentsMethodsArrayAdapter;
    private FinancesOperationsAdapter financesOperationsAdapter;

    private int payment_method_id = 0;
    private String movement_type_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finances_add);

        moduleInfo = CloureManager.getModuleInfo();

        txtPaymentMethod = findViewById(R.id.finances_add_txt_pm);
        txtOperation = findViewById(R.id.finances_add_txt_operation);
        txtDescription = findViewById(R.id.finances_add_txt_description);
        txtAmount= findViewById(R.id.finances_add_txt_amount);

        txtOperation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                movement_type_id = ((Operation)parent.getItemAtPosition(position)).Id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtPaymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                payment_method_id = ((PaymentMethod)parent.getItemAtPosition(position)).Id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSave = findViewById(R.id.finances_add_done);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        load_operations();
        load_payments_methods("");
    }

    private void load_operations(){
        operations = new Operations().get_list();
        financesOperationsAdapter = new FinancesOperationsAdapter(getBaseContext(), operations);
        txtOperation.setAdapter(financesOperationsAdapter);
    }
    private void load_payments_methods(String filtro){
        paymentMethods = new PaymentsMethods().get_list(filtro);
        paymentsMethodsArrayAdapter = new PaymentsMethodsArrayAdapter(getBaseContext(), paymentMethods);
        txtPaymentMethod.setAdapter(paymentsMethodsArrayAdapter);
    }

    private void save(){
        Calendar c = Calendar.getInstance();
        String date_str = String.format("%04d", c.get(Calendar.YEAR)) + "-"+
                String.format("%02d", c.get(Calendar.MONTH)+1)+"-"+
                String.format("%02d", c.get(Calendar.DAY_OF_MONTH))+" "+
                String.format("%02d", c.get(Calendar.HOUR_OF_DAY))+":"+
                String.format("%02d", c.get(Calendar.MINUTE))+":"+
                String.format("%02d", c.get(Calendar.SECOND));

        //Toast.makeText(getApplicationContext(), movement_type_id, Toast.LENGTH_SHORT).show();

        ArrayList<CloureParam> params = new ArrayList<>();
        params.add(new CloureParam("module", "finances"));
        params.add(new CloureParam("topic", "guardar"));
        params.add(new CloureParam("fecha", date_str));
        params.add(new CloureParam("operacion", ""));
        params.add(new CloureParam("descripcion", txtDescription.getText().toString()));
        params.add(new CloureParam("importe", txtAmount.getText().toString()));
        params.add(new CloureParam("forma_de_pago", Integer.toString(payment_method_id)));
        params.add(new CloureParam("forma_de_pago_entidad_id", "0"));
        params.add(new CloureParam("tipo_movimiento", movement_type_id));

        CloureSDK cloureSDK = new CloureSDK();
        try{
            String res = cloureSDK.execute(params);
            JSONObject api_response = new JSONObject(res);
            String error = api_response.getString("Error");

            if(error.equals("")){
                JSONObject response = api_response.getJSONObject("Response");
                String response_msg = response.getString("message");

                InputMethodManager imm = (InputMethodManager) FinancesAddActivity.this.getSystemService(FinancesAddActivity.this.INPUT_METHOD_SERVICE);
                View nview = FinancesAddActivity.this.getCurrentFocus();
                imm.hideSoftInputFromWindow(nview.getWindowToken(), 0);
                if (nview == null) {
                    nview = new View(FinancesAddActivity.this);
                }

                Toast.makeText(getApplicationContext(), response_msg, Toast.LENGTH_SHORT).show();

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
