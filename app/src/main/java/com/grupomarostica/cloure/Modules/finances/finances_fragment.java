package com.grupomarostica.cloure.Modules.finances;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;
import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.Core.GlobalCommand;
import com.grupomarostica.cloure.Core.ModuleInfo;
import com.grupomarostica.cloure.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class finances_fragment extends Fragment {
    private ModuleInfo moduleInfo;
    private ListView listView;
    private FinancesAdapter adapter;
    private ArrayList<FinanceMovement> registers;
    private RelativeLayout loaderLayout;
    private Handler handler = new Handler();
    private ConstraintLayout emptyLayout;
    private Boolean ModuleInitializing = true;

    private TextView tvIngresosPrompt;
    private TextView tvGastosPrompt;
    private TextView tvSaldoPrompt;

    private TextView tvIngresos;
    private TextView tvGastos;
    private TextView tvSaldo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_finances,container, false);

        moduleInfo = CloureManager.getModuleInfo();

        listView = rootView.findViewById(R.id.fragment_finances_items);
        loaderLayout = rootView.findViewById(R.id.fragment_finances_loader);
        emptyLayout = rootView.findViewById(R.id.fragment_finances_no_results);

        tvIngresosPrompt = rootView.findViewById(R.id.fragment_finances_tv_title_ingresos);
        tvGastosPrompt = rootView.findViewById(R.id.fragment_finances_tv_title_gastos);
        tvSaldoPrompt = rootView.findViewById(R.id.fragment_finances_tv_title_saldo);

        tvIngresos = rootView.findViewById(R.id.fragment_finances_tv_ingresos);
        tvGastos = rootView.findViewById(R.id.fragment_finances_tv_gastos);
        tvSaldo = rootView.findViewById(R.id.fragment_finances_tv_saldo);

        setLocales();

        setHasOptionsMenu(true);
        return rootView;
    }

    private void setLocales(){
        try{
            tvIngresosPrompt.setText(moduleInfo.locales.getString("incoming_prompt"));
            tvGastosPrompt.setText(moduleInfo.locales.getString("outcoming_prompt"));
            tvSaldoPrompt.setText(moduleInfo.locales.getString("balance_prompt"));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        load_data();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        for(int i=0;i<moduleInfo.globalCommands.size(); i++){
            GlobalCommand gc = moduleInfo.globalCommands.get(i);
            if(gc.Name.equals("add")) menu.add(Menu.NONE, gc.Id, Menu.NONE, gc.Title);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case 1:
                //Agregar
                Intent intent = new Intent(getContext(), FinancesAddActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void load_data(){
        BackgroundLoad backgroundLoad = new BackgroundLoad();
        new Thread(backgroundLoad).start();
    }

    private class BackgroundLoad implements Runnable{
        String res = "";
        String error = "";
        Double totalIngresos = 0.00;
        Double totalGastos = 0.00;
        Double saldo = 0.00;

        ArrayList<FinanceMovement> registers = new ArrayList<>();

        @Override
        public void run() {
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "finances"));
            params.add(new CloureParam("topic", "listar"));

            try{
                CloureSDK cloureSDK = new CloureSDK();
                res = cloureSDK.execute(params);
                JSONObject api_response = new JSONObject(res);
                error = api_response.getString("Error");
                if(error.equals("")){
                    JSONObject response = api_response.getJSONObject("Response");
                    JSONArray registros = response.getJSONArray("Registros");
                    for (int i=0; i<registros.length(); i++){
                        JSONObject registro = registros.getJSONObject(i);
                        FinanceMovement record_tmp = new FinanceMovement();
                        record_tmp.FechaStr = registro.getString("FechaStr");
                        record_tmp.Detalles = registro.getString("Detalles");
                        record_tmp.Importe = registro.getDouble("Importe");
                        record_tmp.Usuario = registro.getString("Usuario");
                        record_tmp.TipoMovimientoId = registro.getString("TipoMovimiento");

                        registers.add(record_tmp);
                    }

                    totalIngresos = response.getDouble("TotalIngresos");
                    if(!response.get("TotalEgresos").equals(null) && !response.get("TotalEgresos").equals("null")){
                        totalGastos = response.getDouble("TotalEgresos");
                    }

                    saldo = response.getDouble("Saldo");

                    adapter = new FinancesAdapter(getContext(), registers);
                }

            } catch (Exception ex){
                ex.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(registers.size()>0){
                        if(adapter!=null) listView.setAdapter(adapter);
                        listView.setVisibility(View.VISIBLE);
                        emptyLayout.setVisibility(View.INVISIBLE);

                        tvIngresos.setText(String.format(Locale.US, "%.2f", totalIngresos));
                        tvGastos.setText(String.format(Locale.US, "%.2f", totalGastos));
                        tvSaldo.setText(String.format(Locale.US,"%.2f", saldo));
                        if(saldo>0) tvSaldo.setTextColor(getResources().getColor(R.color.green));
                        if(saldo<0) tvSaldo.setTextColor(getResources().getColor(R.color.red));
                    } else {
                        listView.setVisibility(View.INVISIBLE);
                        emptyLayout.setVisibility(View.VISIBLE);
                    }
                    loaderLayout.setVisibility(View.INVISIBLE);

                    if(!error.equals("")) Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    ModuleInitializing=false;
                }
            });
        }
    }
}
