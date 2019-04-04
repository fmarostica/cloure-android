package com.grupomarostica.cloure.Core;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.grupomarostica.cloure.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Handler;

public class CloureManager {
    private static AppCompatActivity _activity;
    public static String ActiveModule = "";
    private ModuleInfo moduleInfo;
    private static String host = "192.168.0.10";
    private static int port = 2084;

    public static void Navigate(Fragment fragment){
        FragmentTransaction ft = _activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.activity_main_frame_layout, fragment);
        ft.commit();
    }

    public static ModuleInfo getModuleInfo(){
        ModuleInfo moduleInfo = new ModuleInfo();

        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", ActiveModule));
            params.add(new CloureParam("topic", "get_module_info"));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);

            JSONObject api_response = new JSONObject(res);
            moduleInfo.Name = ActiveModule;
            moduleInfo.Title = api_response.getString("title");
            final JSONArray globalCommands_arr = api_response.getJSONArray("global_commands");
            if(globalCommands_arr!=null){
                for (int j = 0; j<globalCommands_arr.length(); j++){
                    JSONObject jobj = globalCommands_arr.getJSONObject(j);
                    int cmd_id = jobj.getInt("Id");
                    String cmd_name = jobj.getString("Name");
                    final String cmd_title = jobj.getString("Title");
                    GlobalCommand globalCommand = new GlobalCommand(cmd_id, cmd_name, cmd_title);
                    moduleInfo.globalCommands.add(globalCommand);
                }
            }

            moduleInfo.locales = api_response.getJSONObject("locales");

            /*
            final JSONArray locales_arr = api_response.getJSONArray("locales");
            if(locales_arr!=null){
                for (int j = 0; j<locales_arr.length(); j++){
                    JSONObject jobj = locales_arr.getJSONObject(j);
                    String lkey = jobj.getString("Key");
                    String lval = jobj.getString("Value");
                    CloureLocale locale = new CloureLocale(lkey, lval);
                    moduleInfo.locales.add(locale);
                }
            }
            */

            final JSONArray moduleFiltersArr = api_response.getJSONArray("filters");
            if(moduleFiltersArr!=null){
                for (int j = 0; j<moduleFiltersArr.length(); j++){
                    JSONObject jobj = moduleFiltersArr.getJSONObject(j);
                    String cmd_name = jobj.getString("Name");
                    String cmd_title = jobj.getString("Title");
                    String cmd_type = jobj.getString("Type");
                    ModuleFilter moduleFilter = new ModuleFilter(cmd_name, cmd_title, cmd_type);

                    JSONArray itemsArr = jobj.getJSONArray("Items");
                    for (int k=0;k<itemsArr.length();k++){
                        JSONObject itemObj = itemsArr.getJSONObject(k);
                        String itemId = itemObj.getString("Id");
                        String itemTitle = itemObj.getString("Title");

                        ModuleFilterItem moduleFilterItem = new ModuleFilterItem(itemId, itemTitle);
                        moduleFilter.moduleFilterItems.add(moduleFilterItem);
                    }

                    moduleInfo.moduleFilters.add(moduleFilter);
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return moduleInfo;
    }

    public static JSONObject getLocales(){
        JSONObject locales = new JSONObject();

        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module_name", ActiveModule));
            params.add(new CloureParam("topic", "get_locales"));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);

            locales = new JSONObject(res);
        } catch (Exception e){
            e.printStackTrace();
        }

        return locales;
    }

    public static void setTitle(String title){
        _activity.setTitle(title);
    }

    public static void setActivity(AppCompatActivity activity) {
        _activity = activity;
    }

    /*
    public class connectSocket implements Runnable {
        PrintWriter out;

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

    class updateUIThread implements Runnable {
        private String msg;

        public updateUIThread(String str) {
            this.msg = str;
        }

        @Override
        public void run() {
            //text.setText(text.getText().toString()+"Client Says: "+ msg + "\n");
        }
    }
    */

}
