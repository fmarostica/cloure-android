package com.grupomarostica.cloure.Modules.my_account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupomarostica.cloure.Core.CloureModule;
import com.grupomarostica.cloure.Core.Core;
import com.grupomarostica.cloure.Core.DownloadImageTask;
import com.grupomarostica.cloure.Core.ModuleInfo;
import com.grupomarostica.cloure.R;

public class my_account_fragment extends Fragment {
    private ModuleInfo moduleInfo;
    private TextView txtNombre;
    private TextView txtCargo;
    private ImageView imgFoto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_account,container, false);

        try{

            txtNombre = rootView.findViewById(R.id.fragment_account_tv_name);
            txtCargo = rootView.findViewById(R.id.fragment_account_tv_group);
            imgFoto = rootView.findViewById(R.id.fragment_account_img_profile);

            imgFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try{
            Core.loguedUser.Image = new DownloadImageTask(Core.loguedUser.ImageURL).execute().get();
            txtNombre.setText(Core.loguedUser.nombre+" "+Core.loguedUser.apellido);
            txtCargo.setText(Core.loguedUser.grupo_id);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(Menu.NONE, 1, Menu.NONE, "Cambiar clave");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case 1:
                //Guardar
                Intent intent = new Intent(getContext(), MyAccountChangePassActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
