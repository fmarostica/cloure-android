package com.grupomarostica.cloure.Modules.support;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class support_fragment extends Fragment {
    EditText txtMessage;
    Spinner txtSupportType;
    Button btnEnviar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_support,container, false);

        txtSupportType = rootView.findViewById(R.id.fragment_support_txtSupportType);
        txtMessage = rootView.findViewById(R.id.fragment_support_txtMessage);
        btnEnviar = rootView.findViewById(R.id.fragment_support_btnEnviar);

        loadSupportTypes();

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        setHasOptionsMenu(true);
        return rootView;
    }

    public void save(){
        try{
            JSONObject jsonObject = new JSONObject();

            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "support"));
            params.add(new CloureParam("topic", "send"));
            params.add(new CloureParam("message_type", ((SupportType)txtSupportType.getSelectedItem()).Id));
            params.add(new CloureParam("message", txtMessage.getText().toString()));

            CloureSDK cloureSDK = new CloureSDK();

            String res = cloureSDK.execute(params);
            JSONObject object = new JSONObject(res);
            String error = object.getString("Error");
            if(error.equals("")){
                //JSONObject response = object.getJSONObject("Response");
                Toast.makeText(getActivity(), "El mensaje ha sido enviado", Toast.LENGTH_SHORT).show();
                txtMessage.setText("");
            } else {
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void loadSupportTypes(){
        try {
            ArrayList<SupportType> items = Support.getSupportTypes();
            SupportTypeArrayAdapter arrayAdapter = new SupportTypeArrayAdapter(getActivity(), items);
            txtSupportType.setAdapter(arrayAdapter);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
