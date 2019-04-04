package com.grupomarostica.cloure.Modules.maintenance_shifts;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.Core.ModuleInfo;
import com.grupomarostica.cloure.Modules.maintanance_equipments_types.MaintananceEquipmentTypeAddActivity;
import com.grupomarostica.cloure.Modules.maintanance_equipments_types.MaintenanceEquipmentType;
import com.grupomarostica.cloure.Modules.maintanance_equipments_types.MaintenanceEquipmentsTypesAdapter;
import com.grupomarostica.cloure.Modules.maintanance_equipments_types.MaintentanceEquipmentsTypes;
import com.grupomarostica.cloure.Modules.users.User;
import com.grupomarostica.cloure.Modules.users.UserAddActivity;
import com.grupomarostica.cloure.Modules.users.Users;
import com.grupomarostica.cloure.Modules.users.UsersAdapter;
import com.grupomarostica.cloure.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MaintenanceShiftsAddActivity extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    //ModuleInfo moduleInfo = CloureManager.getModuleInfo();

    TextInputLayout tvDate;
    TextInputLayout tvTime;
    TextInputLayout tvCustomer;
    TextInputLayout tvDescription;

    EditText txtDate;
    EditText txtTime;
    EditText txtDescription;

    AutoCompleteTextView txtCustomer;

    ImageButton btnAddCustomer;
    ImageButton btnAddEquipmentType;
    ImageButton btnSave;

    Spinner txtEquipmentType;

    TextView txtAddress;
    TextView txtPhone;
    TextView tvEquipmentTypePrompt;

    MaintenanceEquipmentsTypesAdapter maintenanceEquipmentsTypesAdapter;

    MaintenanceShift maintenanceShift = new MaintenanceShift();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_shifts_add);
        JSONObject locales = CloureManager.getLocales();

        tvDate = findViewById(R.id.maintenance_shifts_add_txt_date_container);
        tvTime = findViewById(R.id.maintenance_shifts_add_txt_time_container);
        tvCustomer = findViewById(R.id.maintenance_shift_add_txtCustomerContainer);
        tvDescription = findViewById(R.id.maintenance_shifts_txt_issue_desc_container);

        txtDate = findViewById(R.id.maintenance_shifts_add_txt_date);
        txtTime = findViewById(R.id.maintenance_shifts_add_txt_time);
        txtCustomer = findViewById(R.id.maintenanche_shifts_add_txt_user);

        btnAddCustomer = findViewById(R.id.maintenance_shift_add_btn_add_user);
        btnAddEquipmentType = findViewById(R.id.maintenance_shift_add_btn_add_equip);
        btnSave = findViewById(R.id.activity_maintenance_shift_add_btn_save);

        txtEquipmentType = findViewById(R.id.maintenance_shifts_txt_euipments_types);
        txtDescription = findViewById(R.id.maintenance_shifts_txt_issue_desc);
        txtAddress = findViewById(R.id.maintenance_shift_add_tvAddress);
        txtPhone = findViewById(R.id.mainenance_shift_add_tvPhone);
        tvEquipmentTypePrompt = findViewById(R.id.activity_maintenance_shift_add_lbl_et);

        try {
            tvDate.setHint(locales.getString("date"));
            tvTime.setHint(locales.getString("time"));
            tvCustomer.setHint(locales.getString("customer"));
            tvEquipmentTypePrompt.setText(locales.getString("equipment_type"));
            tvDescription.setHint(locales.getString("observations"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        LoadEquipmentTypes();

        btnAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserAddActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        btnAddEquipmentType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MaintananceEquipmentTypeAddActivity.class);
                startActivity(intent);
            }
        });

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MaintenanceShiftsAddActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(MaintenanceShiftsAddActivity.this, time, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<CloureParam> params = new ArrayList<>();
                params.add(new CloureParam("module", "maintenance_shifts"));
                params.add(new CloureParam("topic", "guardar"));
                params.add(new CloureParam("id", Integer.toString(maintenanceShift.Id)));
                params.add(new CloureParam("date", txtDate.getText().toString()));
                params.add(new CloureParam("time", txtTime.getText().toString()));
                params.add(new CloureParam("user_id", Integer.toString(maintenanceShift.CustomerId)));
                params.add(new CloureParam("equipment_type_id", Integer.toString(maintenanceShift.EquipmentTypeId)));
                params.add(new CloureParam("issue_description", txtDescription.getText().toString()));

                CloureSDK cloureSDK = new CloureSDK();
                try{
                    String res = cloureSDK.execute(params);
                    JSONObject response = new JSONObject(res);
                    String error = response.getString("Error");
                    if(error.equals("")){
                        InputMethodManager imm = (InputMethodManager) MaintenanceShiftsAddActivity.this.getSystemService(MaintenanceShiftsAddActivity.this.INPUT_METHOD_SERVICE);
                        View nview = MaintenanceShiftsAddActivity.this.getCurrentFocus();
                        imm.hideSoftInputFromWindow(nview.getWindowToken(), 0);
                        if (nview == null) {
                            nview = new View(MaintenanceShiftsAddActivity.this);
                        }
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(getBaseContext(), error, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex){
                    ex.printStackTrace();
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        txtCustomer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchCustomer();
                    //handled = true;
                }
                return handled;
            }
        });

        txtCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User item = (User)parent.getItemAtPosition(position);
                getUserInfo(item);
                maintenanceShift.CustomerId = item.id;
                txtDescription.requestFocus();
            }
        });

        txtEquipmentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MaintenanceEquipmentType equipmentType = (MaintenanceEquipmentType)parent.getItemAtPosition(position);
                maintenanceShift.EquipmentTypeId = equipmentType.id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                maintenanceShift.EquipmentTypeId = 0;
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            int id = extras.getInt("id");
            maintenanceShift = MaintenanceShifts.get(id);
            maintenanceShift.Id = id;
            txtDate.setText(maintenanceShift.FechaStr);
            txtTime.setText(maintenanceShift.HoraStr);
            txtCustomer.setText(maintenanceShift.Customer);
            txtAddress.setText(maintenanceShift.CustomerAddress);
            txtPhone.setText(maintenanceShift.CustomerPhone);
            try {
                for (int i=0; i<maintenanceEquipmentsTypesAdapter.getCount(); i++){
                    MaintenanceEquipmentType maintenanceEquipmentType = maintenanceEquipmentsTypesAdapter.getItem(i);
                    if(maintenanceShift.EquipmentTypeId==maintenanceEquipmentType.id){
                        txtEquipmentType.setSelection(i);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            txtDescription.setText(maintenanceShift.IssueDescription);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //User added
        if(requestCode==1){
            if(data!=null){
                int user_id = data.getIntExtra("user_id", 0);
                if(user_id>0){
                    maintenanceShift.CustomerId = user_id;
                    User user = Users.getById(user_id);
                    getUserInfo(user);
                }
            }
        }
    }

    private void searchCustomer(){
        ArrayList<User> users = Users.get_list(txtCustomer.getText().toString());
        if(users.size()>0){
            if(users.size()==1){
                User user = users.get(0);
                getUserInfo(user);
                maintenanceShift.CustomerId = user.id;
                txtDescription.requestFocus();
            } else {
                UsersAdapter adapter = new UsersAdapter(MaintenanceShiftsAddActivity.this, users, true);
                txtCustomer.setAdapter(adapter);
                txtCustomer.showDropDown();
            }
        } else {
            Toast.makeText(getApplicationContext(), "No se encontraron usuarios que coincidan con "+txtCustomer.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getUserInfo(User user){
        txtCustomer.setText(user.apellido+", "+user.nombre);
        txtPhone.setText(user.telefono);
        txtAddress.setText(user.direccion);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void LoadEquipmentTypes(){
        ArrayList<MaintenanceEquipmentType> equipmentTypes = MaintentanceEquipmentsTypes.get_list();
        maintenanceEquipmentsTypesAdapter = new MaintenanceEquipmentsTypesAdapter(getBaseContext(), equipmentTypes);
        txtEquipmentType.setAdapter(maintenanceEquipmentsTypesAdapter);
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
        }
    };

    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);
            updateTime();
        }
    };

    private void updateDate() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        txtDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateTime() {
        String myFormat = "HH:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        txtTime.setText(sdf.format(myCalendar.getTime()));
    }
}
