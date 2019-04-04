package com.grupomarostica.cloure.Modules.users;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.Modules.finances.finances;
import com.grupomarostica.cloure.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UsersAccountSummary extends AppCompatActivity {
    private ListView listView;
    private TextView tvBalance;
    private TextView tvTotalAmount;

    private UserFinanceAdapter adapter;
    private ArrayList<finances> registers;
    private Handler handler = new Handler();

    int user_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_account_summary);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            user_id = extras.getInt("user_id");
        }

        listView = findViewById(R.id.users_finances_lst_details);
        tvBalance = findViewById(R.id.users_finances_tv_balance);
        tvTotalAmount = findViewById(R.id.users_finances_tv_amount);

        //loaderLayout = rootView.findViewById(R.id.loader);
        //emptyLayout = rootView.findViewById(R.id.fragment_scroll_no_results);

        load_data();
    }

    public void load_data(){
        BackgroundLoad backgroundLoad = new BackgroundLoad();
        new Thread(backgroundLoad).start();
    }

    private class BackgroundLoad implements Runnable{
        String res = "";
        String error = "";
        String TotalAmountStr = "";

        @Override
        public void run() {
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "finances"));
            params.add(new CloureParam("topic", "listar"));
            params.add(new CloureParam("orden", "asc"));
            params.add(new CloureParam("usuario_id", Integer.toString(user_id)));

            try{
                CloureSDK cloureSDK = new CloureSDK();
                res = cloureSDK.execute(params);
                JSONObject object = new JSONObject(res);
                error = object.getString("Error");
                if(error.equals("")){
                    JSONObject response = object.getJSONObject("Response");
                    JSONArray registros = response.getJSONArray("Registros");

                    registers = new ArrayList<>();
                    for (int i=0; i<registros.length(); i++){
                        JSONObject registro = registros.getJSONObject(i);
                        finances record_tmp = new finances();
                        record_tmp.FechaStr = registro.getString("FechaStr");
                        record_tmp.Detalles = registro.getString("Detalles");
                        record_tmp.Importe = registro.getDouble("Importe");
                        record_tmp.Usuario = registro.getString("Usuario");
                        record_tmp.TipoMovimientoId = registro.getString("TipoMovimiento");

                        registers.add(record_tmp);
                    }
                    TotalAmountStr =response.getString("SaldoCC");
                    adapter = new UserFinanceAdapter(getBaseContext(), registers);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(registers.size()>0){
                            if(adapter!=null) listView.setAdapter(adapter);
                            listView.setVisibility(View.VISIBLE);
                            tvTotalAmount.setText("$ "+TotalAmountStr);
                            //emptyLayout.setVisibility(View.INVISIBLE);
                        } else {
                            listView.setVisibility(View.INVISIBLE);
                            //emptyLayout.setVisibility(View.VISIBLE);
                        }
                        //loaderLayout.setVisibility(View.INVISIBLE);
                    }
                });
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
