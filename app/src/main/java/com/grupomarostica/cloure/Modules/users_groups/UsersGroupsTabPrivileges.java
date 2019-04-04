package com.grupomarostica.cloure.Modules.users_groups;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.grupomarostica.cloure.Core.ClourePrivilege;
import com.grupomarostica.cloure.Core.Core;
import com.grupomarostica.cloure.Core.ModulePrivilege;
import com.grupomarostica.cloure.R;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class UsersGroupsTabPrivileges extends Fragment {
    private ScrollView scrollView;
    private LinearLayout itemsContainer;
    ArrayList<ModulePrivilege> modulePrivileges = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_users_groups_tab_privileges, container, false);
        scrollView = rootView.findViewById(R.id.users_privileges_list);
        itemsContainer = rootView.findViewById(R.id.users_privileges_list_items);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);
        load_privileges("");
    }
    private void load_privileges(String groupId){
        try{
            modulePrivileges = UsersGroups.getPrivileges(groupId);

            for(int i=0; i<modulePrivileges.size(); i++){
                ModulePrivilege module = modulePrivileges.get(i);

                TextView textView = new TextView(getContext());
                textView.setText(module.ModuleTitle);
                itemsContainer.addView(textView);

                for (int j=0; j<module.ClourePrivileges.size(); j++){
                    final ClourePrivilege privilege = module.ClourePrivileges.get(j);
                    Log.i(Core.API_INFORMATION_TAG, "Privilege type: " + privilege.Type);
                    if(privilege.Type.equals("bool")){
                        Switch privilegeSwitch = new Switch(getContext());
                        privilegeSwitch.setTag(privilege);
                        privilegeSwitch.setText(privilege.Title);
                        privilegeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                                if(checked){
                                    privilege.Value = "true";
                                } else {
                                    privilege.Value = "false";
                                }
                            }
                        });
                        itemsContainer.addView(privilegeSwitch);
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
