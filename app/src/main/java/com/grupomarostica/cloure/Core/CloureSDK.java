package com.grupomarostica.cloure.Core;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CloureSDK {
    private String Response = "";
    private Context context;

    public String execute(List<CloureParam> params) {
        StringBuilder query = new StringBuilder();

        if (Core.appToken != null) {
            if (Core.appToken.length() == 32)
                params.add(new CloureParam("app_token", Core.appToken));
        }
        if (Core.userToken != null) {
            if (Core.userToken.length() == 32)
                params.add(new CloureParam("user_token", Core.userToken));
        }
        params.add(new CloureParam("referer", "android"));
        params.add(new CloureParam("referer_version", Core.versionName));
        params.add(new CloureParam("language", Locale.getDefault().getDisplayLanguage()));

        try{
            for (CloureParam pair : params) {
                query.append("&");
                query.append(URLEncoder.encode(pair.name, "UTF-8"));
                query.append("=");
                query.append(URLEncoder.encode(pair.value.toString(), "UTF-8"));
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        String queryString = query.toString();

        try {
            String url_str = "https://cloure.com/api/v1/index.php";
            Response = new Executor().execute(url_str, queryString).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Response;
    }

    public ModuleInfo get_module_info(String module_name){
        ModuleInfo moduleInfo = new ModuleInfo();

        ArrayList<CloureParam> params = new ArrayList<>();
        params.add(new CloureParam("module", module_name));
        params.add(new CloureParam("topic", "get_module_info"));

        CloureSDK cloureSDK = new CloureSDK();
        try{
            String res = cloureSDK.execute(params);
            JSONObject api_response = new JSONObject(res);
            String error = api_response.getString("Error");
            if(error.equals("")){
                JSONArray registers = api_response.getJSONArray("Response");
                if(registers!=null){
                    for (int j = 0; j<registers.length(); j++){
                        JSONObject jobj = registers.getJSONObject(j);
                        int cmd_id = jobj.getInt("Id");
                        String cmd_name = jobj.getString("Name");
                        final String cmd_title = jobj.getString("Title");
                        GlobalCommand command = new GlobalCommand(cmd_id, cmd_name, cmd_title);
                    }
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return moduleInfo;
    }

    private static class Executor extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            String result = "";

            try {
                URL url = new URL(args[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(false);
                //urlConnection.setContentType("");

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                writer.write(args[1]);
                writer.flush();
                writer.close();
                os.close();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                result = inputStreamToString(in);
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        private String inputStreamToString(InputStream is) {
            String rLine;
            StringBuilder answer = new StringBuilder();
            String response = "";

            InputStreamReader isr = new InputStreamReader(is);

            BufferedReader rd = new BufferedReader(isr);

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
                response = answer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }
    }
}
