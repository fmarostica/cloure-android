package com.grupomarostica.cloure.Modules.products_services;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.Core;

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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ProductsLoader extends AsyncTask<ArrayList<CloureParam>, Void, ArrayList<ProductService>> {
    private ArrayList<CloureParam> params;
    private ArrayList<ProductService> registers_tmp;

    public ProductsLoader(ArrayList<CloureParam> params){
        this.params = params;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected ArrayList<ProductService> doInBackground(ArrayList... params) {
        ProductService result = null;
        String response = "";
        String url_str = "https://cloure.com/api/v1/";
        try {
            URL url = new URL(url_str);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            //urlConnection.setContentType("");

            if(Core.appToken.length()==32) this.params.add(new CloureParam("app_token",Core.appToken));
            if(Core.userToken.length()==32) this.params.add(new CloureParam("user_token",Core.userToken));

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(this.params));
            writer.flush();
            writer.close();
            os.close();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            response = inputStreamToString(in);

            JSONObject object = new JSONObject(response);
            String error = object.getString("Error");
            if(error.equals("")){
                JSONObject response_object = object.getJSONObject("Response");
                JSONArray registros = response_object.getJSONArray("Registros");
                registers_tmp = new ArrayList<>();
                for (int i=0; i<registros.length(); i++){
                    JSONObject registro = registros.getJSONObject(i);
                    ProductService register_tmp = new ProductService();
                    register_tmp.Id = registro.getInt("Id");
                    register_tmp.Title = registro.getString("Titulo");
                    register_tmp.ImagePath = registro.getString("ImagenPath");
                    register_tmp.CategoriaN1 = registro.getString("CategoriaN1");
                    register_tmp.Importe = registro.getDouble("Importe");

                    URL url_image = new URL(register_tmp.ImagePath);
                    register_tmp.ImageBitmap = BitmapFactory.decodeStream(url_image.openConnection().getInputStream());

                    registers_tmp.add(register_tmp);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return registers_tmp;
    }

    @Override
    protected void onPostExecute(ArrayList<ProductService> s) {
        super.onPostExecute(s);
    }

    private String getQuery(List<CloureParam> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (CloureParam pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.name, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.value.toString(), "UTF-8"));
        }

        return result.toString();
    }

    private String inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();

        InputStreamReader isr = new InputStreamReader(is);

        BufferedReader rd = new BufferedReader(isr);

        try {
            while ((rLine = rd.readLine()) != null) {
                answer.append(rLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return answer.toString();
    }
}