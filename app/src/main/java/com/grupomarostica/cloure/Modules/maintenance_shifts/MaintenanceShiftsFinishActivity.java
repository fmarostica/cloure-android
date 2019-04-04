package com.grupomarostica.cloure.Modules.maintenance_shifts;


import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.grupomarostica.cloure.Modules.products_services.ProductService;
import com.grupomarostica.cloure.Modules.products_services.ProductsServices;
import com.grupomarostica.cloure.Modules.products_services.ProductsServicesPickerActivity;
import com.grupomarostica.cloure.Modules.receipts.CartItem;
import com.grupomarostica.cloure.Modules.receipts.CartItemEditableAdapter;
import com.grupomarostica.cloure.Modules.receipts.CartItemsAdapter;
import com.grupomarostica.cloure.R;

import java.util.ArrayList;
import java.util.List;

public class MaintenanceShiftsFinishActivity extends AppCompatActivity {

    private MaintenanceShift maintenanceShift;
    private RecyclerView lstMaterials;
    private ImageButton btnSave;
    private ImageButton btnAddMaterial;

    private ArrayList<CartItem> items = new ArrayList<>();
    private CartItemEditableAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_shifts_finish);

        btnSave = findViewById(R.id.maintenance_shifts_finish_btnSave);
        lstMaterials = findViewById(R.id.maintenance_shifts_finish_materials_lst);
        btnAddMaterial = findViewById(R.id.maintenance_shifts_finish_btnAgregarMaterial);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        lstMaterials.setLayoutManager(mLayoutManager);
        adapter = new CartItemEditableAdapter(getApplicationContext(), items);
        lstMaterials.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            int id = extras.getInt("id");
            //Toast.makeText(getBaseContext(), "ID: "+Integer.toString(id), Toast.LENGTH_SHORT).show();
            maintenanceShift = MaintenanceShifts.get(id);
        }

        btnAddMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MaintenanceShiftsFinishActivity.this, ProductsServicesPickerActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    private void save(){
        Toast.makeText(getApplicationContext(), Integer.toString(maintenanceShift.Id), Toast.LENGTH_SHORT).show();
        MaintenanceShifts.Finish(maintenanceShift);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && data!=null){
            try{
                Bundle bundle = data.getExtras();
                ArrayList<CartItem> cartItems = (ArrayList<CartItem>)bundle.getSerializable("selected_items");
                for (int i=0; i<cartItems.size(); i++){
                    adapter.items.add(cartItems.get(i));
                    adapter.notifyDataSetChanged();
                }

                Toast.makeText(getApplicationContext(), Integer.toString(cartItems.size()), Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
