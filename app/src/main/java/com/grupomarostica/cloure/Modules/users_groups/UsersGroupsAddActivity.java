package com.grupomarostica.cloure.Modules.users_groups;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.ClourePrivilege;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.Core.Core;
import com.grupomarostica.cloure.Core.ModulePrivilege;
import com.grupomarostica.cloure.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UsersGroupsAddActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private UsersGroupsPagerAdapter sectionsPagerAdapter;
    private UsersGroupsTabGeneral tabGeneral;
    private UsersGroupsTabPrivileges tabPrivileges;
    private ImageButton btnSave;
    private UserGroup userGroup = new UserGroup();
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_groups_add);

        tabLayout = findViewById(R.id.activityUsersGroupAddTabLayout);
        viewPager = findViewById(R.id.activityUsersGroupAddViewPager);
        btnSave = findViewById(R.id.activityUsersGroupAddBtnSave);

        tabGeneral = new UsersGroupsTabGeneral();
        tabPrivileges = new UsersGroupsTabPrivileges();

        this.sectionsPagerAdapter = new UsersGroupsPagerAdapter(getSupportFragmentManager());
        this.sectionsPagerAdapter.AddFragment(tabGeneral);
        this.sectionsPagerAdapter.AddFragment(tabPrivileges);

        viewPager.setAdapter(this.sectionsPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    public void save(){
        try{
            TextInputEditText txtNombre = findViewById(R.id.users_groups_add_txt_name);
            ArrayList<ModulePrivilege> modulePrivileges = tabPrivileges.modulePrivileges;

            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "users_groups"));
            params.add(new CloureParam("topic", "guardar"));
            params.add(new CloureParam("nombre", txtNombre.getText().toString()));

            //SAVE PRIVILEGES
            JSONArray modulesPrivilegesArr = new JSONArray();
            for (int i=0; i<modulePrivileges.size(); i++){
                ModulePrivilege modulePrivilege = modulePrivileges.get(i);
                for (int j=0; j<modulePrivilege.ClourePrivileges.size(); j++){
                    ClourePrivilege privilege = modulePrivilege.ClourePrivileges.get(j);
                    JSONObject privilegeObject = new JSONObject();
                    privilegeObject.put("module_id", modulePrivilege.ModuleId);
                    privilegeObject.put("option", privilege.Id);
                    privilegeObject.put("value", privilege.Value);
                    modulesPrivilegesArr.put(privilegeObject);
                }
            }
            //END SAVE PRIVILEGES

            params.add(new CloureParam("privilegios", modulesPrivilegesArr.toString()));

            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);
            JSONObject object = new JSONObject(res);
            String error = object.getString("Error");
            if(error.equals("")){
                JSONObject response = object.getJSONObject("Response");
                userGroup.Id = response.getString("Id");

                InputMethodManager imm = (InputMethodManager) UsersGroupsAddActivity.this.getSystemService(UsersGroupsAddActivity.this.INPUT_METHOD_SERVICE);
                View nview = UsersGroupsAddActivity.this.getCurrentFocus();
                imm.hideSoftInputFromWindow(nview.getWindowToken(), 0);
                if (nview == null) {
                    nview = new View(UsersGroupsAddActivity.this);
                }

                finish();
            } else {
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public class UsersGroupsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();

        public UsersGroupsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void AddFragment(Fragment fragment){
            fragmentList.add(fragment);
        }
    }
}
