package com.grupomarostica.cloure.Modules.users_groups;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.grupomarostica.cloure.R;

public class UsersGroupsTabGeneral extends Fragment {
    public TextInputEditText txtNombre;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_users_groups_tab_general, container, false);

        txtNombre = rootView.findViewById(R.id.users_groups_add_txt_name);

        return rootView;
    }
}
