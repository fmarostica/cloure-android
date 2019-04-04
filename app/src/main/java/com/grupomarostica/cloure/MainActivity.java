package com.grupomarostica.cloure;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureMenuGroup;
import com.grupomarostica.cloure.Core.CloureMenuItem;
import com.grupomarostica.cloure.Core.CloureModule;
import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.Core.Core;
import com.grupomarostica.cloure.Core.DownloadImageTask;
import com.grupomarostica.cloure.Core.ExpandableMenuAdapter;
import com.grupomarostica.cloure.Core.GlobalCommand;
import com.grupomarostica.cloure.Core.ModuleInfo;
import com.grupomarostica.cloure.Modules.users.User;
import com.grupomarostica.cloure.Modules.users.Users;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import static com.android.billingclient.util.BillingHelper.RESPONSE_BUY_INTENT;

public class MainActivity extends AppCompatActivity {
    private int touchs = 0; //count for back button
    private CountDownTimer countDownTimer;
    private DrawerLayout drawer;
    private Handler handler = new Handler();
    private CloureModule current_module;

    private TextView txtLoguedUserName;
    private TextView txtLoguedUserEmail;
    private TextView txtAccountType;
    private ImageView imgProfilePhoto;
    private IInAppBillingService mService;
    private boolean binded;

    Button btnCloseSession;
    ExpandableListView expandableListView;
    ArrayList<CloureMenuGroup> headerList = new ArrayList<>();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloureManager.setActivity(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);

        drawer = findViewById(R.id.activity_main_drawer);
        expandableListView = findViewById(R.id.activity_main_expandable_menu);
        btnCloseSession = findViewById(R.id.mainBtnForgetSession);
        imgProfilePhoto = findViewById(R.id.activity_main_img_user);

        if(!Core.savedData) btnCloseSession.setVisibility(View.INVISIBLE);

        btnCloseSession.setText("Close session");
        btnCloseSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseSession();
            }
        });

        load_modules();

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (headerList.get(groupPosition).cloureMenuItems.size()==0) {
                    CloureMenuGroup model = headerList.get(groupPosition);
                    try{
                        //new Thread(new BGLoadModuleInfo(model.Name)).start();

                        init_module(model.Name);
                        //CloureManager.getModuleInfo();
                    } catch (Exception ex){
                        Toast.makeText(getApplicationContext(), "Module "+model.Name+" doesn't exists", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                CloureMenuItem cloureMenuItem = headerList.get(groupPosition).cloureMenuItems.get(childPosition);
                init_module(cloureMenuItem.Nombre);
                return false;
            }
        });

        txtLoguedUserName = findViewById(R.id.activity_main_tv_logued_user);
        txtLoguedUserEmail = findViewById(R.id.activity_main_tv_logued_user_email);
        txtAccountType = findViewById(R.id.activity_main_tv_cloure_account_type);
        //imgLoguedUser = findViewById(R.id.activity_main_img_user);

        new Thread(new UserLoader()).start();
        txtAccountType.setText("Cloure "+Core.accountType);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name,  R.string.app_name){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                touchs=0;
            }
        };
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        AdView adView = findViewById(R.id.activity_main_ad);
        if(Core.accountType.equals("free") || Core.accountType.equals("test_free")){
            if(Core.accountType.equals("free")){
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);
            }
            if(Core.accountType.equals("test_free")){
                AdRequest adRequest = new AdRequest.Builder().addTestDevice("0D1BEF4165EA691A8A216867DDB657BE").build();
                adView.loadAd(adRequest);
            }
        } else {
            adView.setVisibility(View.GONE);
        }

        init_module("my_account");

        //Check user subscription
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        binded = bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        //Toast.makeText(getApplicationContext(), "bindService - return " + String.valueOf(binded), Toast.LENGTH_SHORT).show();
        Log.i(Core.API_BILLING_TAG, "bindService - return " + String.valueOf(binded));

        ConnectSocket connectSocket = new ConnectSocket();
        new Thread(connectSocket).start();

        //broadcastCustomIntent();
    }

    
    public class ConnectSocket implements Runnable {
        PrintWriter out;
        String host = "192.168.0.10";
        int port = 2084;

        @Override
        public void run() {
            try {
                //here you must put your computer's IP address.
                //serverAddr = InetAddress.getByName(SERVERIP);
                Log.i(Core.API_INFORMATION_TAG, "C: Connecting to "+host+"...");
                //create a socket to make the connection with the server

                Socket socket = new Socket(host, port);

                try {
                    //send the message to the server
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    Log.i(Core.API_INFORMATION_TAG, "C: Sent.");
                    Log.i(Core.API_INFORMATION_TAG, "C: Done.");

                    CommunicationThread commThread = new CommunicationThread(socket);
                    new Thread(commThread).start();
                }
                catch (Exception e) {
                    Log.i(Core.API_INFORMATION_TAG, "S: Error", e);
                }
            } catch (Exception e) {
                Log.i(Core.API_INFORMATION_TAG, "C: Error", e);
            }
        }
    }

    class CommunicationThread implements Runnable {

        private Socket clientSocket;

        private BufferedReader input;

        public CommunicationThread(Socket clientSocket) {

            this.clientSocket = clientSocket;

            try {

                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String read = input.readLine();
                    Log.i(Core.API_INFORMATION_TAG, read);
                    //handler.post(new updateUIThread(read));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void broadcastCustomIntent()
    {
        Intent intent = new Intent("MyCustomIntent");
        // add data to the Intent
        intent.putExtra("message", "test");
        intent.setAction("com.grupomarostica.cloure.A_CUSTOM_INTENT");
        sendBroadcast(intent);
    }

    private void CloseSession(){
        try{
            if(getApplicationContext().deleteFile("cloure_data")){
                Toast.makeText(getApplicationContext(), "Session closed", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
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
            ownedItems = mService.getPurchases(3, getPackageName(), "subs", null);

            Toast.makeText(getApplicationContext(), "getPurchases() - success return Bundle", Toast.LENGTH_SHORT).show();
            Log.i(Core.API_BILLING_TAG, "getPurchases() - success return Bundle");
        } catch (RemoteException e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "getPurchases - fail!", Toast.LENGTH_SHORT).show();
            Log.w(Core.API_BILLING_TAG, "getPurchases() - fail!");
            return;
        }

        int response = ownedItems.getInt("RESPONSE_CODE");
        Toast.makeText(getApplicationContext(), "getPurchases() - \"RESPONSE_CODE\" return " + String.valueOf(response), Toast.LENGTH_SHORT).show();
        Log.i(Core.API_BILLING_TAG, "getPurchases() - \"RESPONSE_CODE\" return " + String.valueOf(response));

        if (response != 0) return;

        ArrayList<String> ownedSkus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
        ArrayList<String> purchaseDataList = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
        ArrayList<String> signatureList = ownedItems.getStringArrayList("INAPP_DATA_SIGNATURE");
        String continuationToken = ownedItems.getString("INAPP_CONTINUATION_TOKEN");

        Log.i(Core.API_BILLING_TAG, "getPurchases() - \"INAPP_PURCHASE_ITEM_LIST\" return " + ownedSkus.toString());
        Log.i(Core.API_BILLING_TAG, "getPurchases() - \"INAPP_PURCHASE_DATA_LIST\" return " + purchaseDataList.toString());
        Log.i(Core.API_BILLING_TAG, "getPurchases() - \"INAPP_DATA_SIGNATURE\" return " + (signatureList != null ? signatureList.toString() : "null"));
        Log.i(Core.API_BILLING_TAG, "getPurchases() - \"INAPP_CONTINUATION_TOKEN\" return " + (continuationToken != null ? continuationToken : "null"));
    }

    private void purchase(){
        ArrayList<String> skuList = new ArrayList<String> ();
        skuList.add("starter");
        Bundle querySkus = new Bundle();
        querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
        try {
            Bundle skuDetails = mService.getSkuDetails(3, getPackageName(), "subs", querySkus);
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
                startIntentSenderForResult(pendingIntent.getIntentSender(),
                        1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                        Integer.valueOf(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void load_modules(){
        ModulesBGLoader modulesBGLoader = new ModulesBGLoader();
        new Thread(modulesBGLoader).start();
    }

    class ModulesBGLoader implements Runnable{
        @Override
        public void run() {
            try{
                for (int i = 0; i < Core.modulesGroupsArr.length(); i++) {
                    JSONObject groupItem = Core.modulesGroupsArr.getJSONObject(i);
                    JSONArray menuItems = groupItem.getJSONArray("Items");

                    String group_id = groupItem.getString("Id");
                    String group_title = groupItem.getString("Title");
                    String group_icon = groupItem.getString("Icon");

                    CloureMenuGroup group_model = new CloureMenuGroup(group_id, group_title, group_icon);

                    if(menuItems.length()>1){
                        for (int j=0; j<menuItems.length();j++){
                            JSONObject menuItem = menuItems.getJSONObject(j);

                            String menu_id = menuItem.getString("Id");
                            String menu_title = menuItem.getString("Title");
                            String menu_group_id = menuItem.getString("GroupId");

                            if(menu_group_id.equals(group_id)){
                                CloureMenuItem mnu = new CloureMenuItem(menu_id, menu_title, menu_group_id);
                                group_model.cloureMenuItems.add(mnu);
                            }
                        }
                    } else {
                        JSONObject menuItem = menuItems.getJSONObject(0);
                        String menu_id = menuItem.getString("Id");
                        String menu_title = menuItem.getString("Title");
                        group_model.Name = menu_id;
                        group_model.Title = menu_title;
                    }

                    if(!group_model.Name.equals("linked_accounts")){
                        headerList.add(group_model);
                    }
                }
                //ArrayList<CloureMenuItem> menu_list = new ArrayList<>();
                //menu_list.add(new CloureMenuItem("test", "Test", ""));

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ExpandableMenuAdapter expandableStringAdapter = new ExpandableMenuAdapter(headerList, getApplicationContext());
                        expandableListView.setAdapter(expandableStringAdapter);
                    }
                });

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class UserLoader implements Runnable{
        @Override
        public void run() {
            User user = new Users().get(Core.userToken);
            Core.loguedUser = user;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    txtLoguedUserName.setText(Core.loguedUser.nombre);
                    txtLoguedUserEmail.setText(Core.loguedUser.email);

                    if(!Core.loguedUser.ImageURL.equals("")){
                        try{
                            Bitmap bmp = new DownloadImageTask(Core.loguedUser.ImageURL).execute().get();
                            imgProfilePhoto.setImageBitmap(bmp);
                        } catch (Exception e){
                            e.printStackTrace();
                            imgProfilePhoto.setImageResource(R.drawable.logo250);
                        }
                    } else {
                        imgProfilePhoto.setImageResource(R.drawable.logo250);
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        touchs++;

        if (drawer.isDrawerOpen(GravityCompat.START)){
            Toast.makeText(getApplicationContext(), "Presione volver nuevamente para salir", Toast.LENGTH_LONG).show();
            if(touchs==2){
                Core.finishedByBackButton = true;
                finish();
            }
        }
        else {
            touchs = 0;
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void init_module(String moduleName){
        touchs = 0;
        try{
            Class cls = Class.forName("com.grupomarostica.cloure.Modules."+moduleName+".mod_"+moduleName);
            CloureModule cloureModule = (CloureModule) cls.newInstance();
            cloureModule.onModuleCreated();
            CloureManager.ActiveModule = moduleName;
            drawer.closeDrawer(GravityCompat.START);
        } catch (Exception e){
            e.printStackTrace();
        }
    }




}

