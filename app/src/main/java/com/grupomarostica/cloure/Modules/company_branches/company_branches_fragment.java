package com.grupomarostica.cloure.Modules.company_branches;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;
import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.Core.Core;
import com.grupomarostica.cloure.Core.GlobalCommand;
import com.grupomarostica.cloure.Core.ModuleInfo;
import com.grupomarostica.cloure.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.billingclient.util.BillingHelper.RESPONSE_BUY_INTENT;

public class company_branches_fragment extends Fragment {
    private ModuleInfo moduleInfo;
    private CloureSDK cloureSDK = null;
    private ListView listView;
    private CompanyBranchesAdapter adapter;
    private ArrayList<CompanyBranch> registers;
    private RelativeLayout loaderLayout;
    private Handler handler = new Handler();
    private ConstraintLayout emptyLayout;
    private IInAppBillingService mService;
    private boolean binded;

    //InApp purchases
    private ArrayList<String> ownedSkus;

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_panel_scroll,container, false);

        moduleInfo = CloureManager.getModuleInfo();

        listView = rootView.findViewById(R.id.fragment_panel_scroll_items);
        loaderLayout = rootView.findViewById(R.id.loader);
        emptyLayout = rootView.findViewById(R.id.fragment_scroll_no_results);

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        load_data();

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        binded = getActivity().bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        for(int i=0;i<moduleInfo.globalCommands.size(); i++){
            GlobalCommand gc = moduleInfo.globalCommands.get(i);
            //Se deshabilita para mandarlo a produccion y despues ver los precios y cobros
            //if(gc.Name.equals("add")) menu.add(Menu.NONE, gc.Id, Menu.NONE, gc.Title);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case 1:
                //Agregar
                onAddBranch();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onAddBranch(){
        if(Core.accountType.equals("free") || Core.accountType.equals("test_free")){
            //PurchaseSub();
            //Check if purchase any cloure premium account from google play
            checkSubs();
            if(ownedSkus.size()==0){
                PurchaseSub();
            } else {
                //Send data to cloure server to update account
                Log.i(Core.API_BILLING_TAG, "Sending data to cloure server");
                Agregar();
            }
        } else {
            Agregar();
        }
    }

    private void Agregar(){
        Intent intent = new Intent(getActivity(), CompanyBranchesAddActivity.class);
        startActivity(intent);
    }

    private void checkSubs(){
        if (!binded){
            Log.i(Core.API_BILLING_TAG, "Not binded");
            return;
        }
        if (mService == null){
            Log.i(Core.API_BILLING_TAG, "mService is null");
            return;
        }

        Bundle ownedItems;
        try {
            ownedItems = mService.getPurchases(3, getActivity().getPackageName(), "subs", null);
            //Toast.makeText(getActivity(), "getPurchases() - success return Bundle", Toast.LENGTH_SHORT).show();
            Log.i(Core.API_BILLING_TAG, "getPurchases() - success return Bundle");
        } catch (RemoteException e) {
            e.printStackTrace();

            //Toast.makeText(getActivity(), "getPurchases - fail!", Toast.LENGTH_SHORT).show();
            Log.w(Core.API_BILLING_TAG, "getPurchases() - fail!");
            return;
        }

        int response = ownedItems.getInt("RESPONSE_CODE");
        //Toast.makeText(getActivity(), "getPurchases() - \"RESPONSE_CODE\" return " + String.valueOf(response), Toast.LENGTH_SHORT).show();
        Log.i(Core.API_BILLING_TAG, "getPurchases() - \"RESPONSE_CODE\" return " + String.valueOf(response));

        if (response != 0) return;

        ownedSkus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
        ArrayList<String> purchaseDataList = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
        ArrayList<String> signatureList = ownedItems.getStringArrayList("INAPP_DATA_SIGNATURE");
        String continuationToken = ownedItems.getString("INAPP_CONTINUATION_TOKEN");

        Log.i(Core.API_BILLING_TAG, "getPurchases() - \"INAPP_PURCHASE_ITEM_LIST\" return " + ownedSkus.toString());
        Log.i(Core.API_BILLING_TAG, "getPurchases() - \"INAPP_PURCHASE_DATA_LIST\" return " + purchaseDataList.toString());
        Log.i(Core.API_BILLING_TAG, "getPurchases() - \"INAPP_DATA_SIGNATURE\" return " + (signatureList != null ? signatureList.toString() : "null"));
        Log.i(Core.API_BILLING_TAG, "getPurchases() - \"INAPP_CONTINUATION_TOKEN\" return " + (continuationToken != null ? continuationToken : "null"));
    }

    public void PurchaseSub(){
        ArrayList<String> skuList = new ArrayList<String> ();
        skuList.add("starter");
        Bundle querySkus = new Bundle();
        querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
        try {
            Bundle skuDetails = mService.getSkuDetails(3, getActivity().getPackageName(), "subs", querySkus);
            int response = skuDetails.getInt("RESPONSE_CODE");
            if (response == 0) {
                ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");

                for (String thisResponse : responseList) {
                    JSONObject object = new JSONObject(thisResponse);
                    String sku = object.getString("productId");
                    String price = object.getString("price");
                    //Toast.makeText(getActivity(), sku, Toast.LENGTH_LONG).show();
                }
                Bundle bundle = mService.getBuyIntent(3, "com.grupomarostica.cloure", "starter", "subs", "Test code");
                PendingIntent pendingIntent = bundle.getParcelable(RESPONSE_BUY_INTENT);
                getActivity().startIntentSenderForResult(pendingIntent.getIntentSender(),
                        1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                        Integer.valueOf(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load_data(){
        BackgroundLoad backgroundLoad = new BackgroundLoad();
        new Thread(backgroundLoad).start();
    }

    class BackgroundLoad implements Runnable{
        String res = "";
        String error = "";

        @Override
        public void run() {
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "company_branches"));
            params.add(new CloureParam("topic", "listar"));

            try{
                cloureSDK = new CloureSDK();
                res = cloureSDK.execute(params);
                JSONObject object = new JSONObject(res);
                error = object.getString("Error");
                if(error.equals("")){
                    JSONObject response = object.getJSONObject("Response");
                    JSONArray registros = response.getJSONArray("Registros");
                    registers = new ArrayList<>();
                    for (int i=0; i<registros.length(); i++){
                        JSONObject registro = registros.getJSONObject(i);
                        CompanyBranch record_tmp = new CompanyBranch();
                        record_tmp.Name = registro.getString("Nombre");

                        registers.add(record_tmp);
                    }
                    adapter = new CompanyBranchesAdapter(getContext(), registers);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(registers.size()>0){
                            if(adapter!=null) listView.setAdapter(adapter);
                            listView.setVisibility(View.VISIBLE);
                            emptyLayout.setVisibility(View.INVISIBLE);
                        } else {
                            listView.setVisibility(View.INVISIBLE);
                            emptyLayout.setVisibility(View.VISIBLE);
                        }
                        loaderLayout.setVisibility(View.INVISIBLE);
                    }
                });
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001){
            if(resultCode != Activity.RESULT_OK){
                Log.i(Core.API_BILLING_TAG, "Response code " + String.valueOf(resultCode));
                return;
            }
            int responseCode = data.getIntExtra("RESPONSE_CODE", 1);
            Log.i(Core.API_BILLING_TAG, "Response code " + String.valueOf(responseCode));
            //Toast.makeText(getContext(), "onActivityResult() - \"RESPONSE_CODE\" return " + String.valueOf(responseCode), Toast.LENGTH_SHORT).show();
        }
    }
}
