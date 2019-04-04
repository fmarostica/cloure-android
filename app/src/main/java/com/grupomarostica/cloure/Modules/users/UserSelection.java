package com.grupomarostica.cloure.Modules.users;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.RecyclerItemClickListener;
import com.grupomarostica.cloure.R;

import java.util.ArrayList;

public class UserSelection extends AppCompatActivity {
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<User> selectedUsers = new ArrayList<>();
    private UsersRecyclerAdapter adapter;
    private RecyclerView listView;
    private TextInputEditText txtSearch;
    private ImageButton btnAddUser;
    private ImageButton btnSearch;
    private ImageButton btnDone;
    private int SelectionMode = 1;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);

        listView = findViewById(R.id.activity_users_selection_lst_users);
        txtSearch = findViewById(R.id.users_selection_txt_search);
        btnSearch = findViewById(R.id.users_selection_btnSearch);
        btnAddUser = findViewById(R.id.users_selection_btnAddUser);
        btnDone = findViewById(R.id.activity_user_selection_btnDone);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(mLayoutManager);

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserAddActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_data();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(selectedUsers.size()==0){
                        Toast.makeText(getApplicationContext(), "Debes seleccionar al menos un cliente", Toast.LENGTH_SHORT).show();
                    } else {
                        /*
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("selected_items", selectedUsers);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                        */
                        if(selectedUsers.size()==1){
                            User user = selectedUsers.get(0);
                            Intent intent = new Intent();
                            intent.putExtra("user_id", user.id);
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        listView.addOnItemTouchListener(new RecyclerItemClickListener(this, listView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                multi_select(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        load_data();
    }

    public void multi_select(int position) {
        if(SelectionMode==1){
            selectedUsers = new ArrayList<>();
            selectedUsers.add(users.get(position));
        } else {
            if (selectedUsers.contains(users.get(position)))
                selectedUsers.remove(users.get(position));
            else
                selectedUsers.add(users.get(position));
        }


        refreshAdapter();
    }

    public void refreshAdapter()
    {
        adapter.SelectedItems=selectedUsers;
        adapter.Items=users;
        adapter.notifyDataSetChanged();
    }

    private void load_data(){
        BackgroundLoader backgroundLoader = new BackgroundLoader();
        new Thread(backgroundLoader).start();
    }

    private class BackgroundLoader implements Runnable{
        String filter = txtSearch.getText().toString();


        @Override
        public void run() {
            users = Users.get_list(filter);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try{
                        adapter = new UsersRecyclerAdapter(getApplicationContext(), users, selectedUsers);
                        listView.setAdapter(adapter);
                        //_usersAdapter.notifyDataSetChanged();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        }
    }
}
