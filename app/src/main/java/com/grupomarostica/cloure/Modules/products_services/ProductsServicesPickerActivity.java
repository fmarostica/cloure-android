package com.grupomarostica.cloure.Modules.products_services;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.Core.RecyclerItemClickListener;
import com.grupomarostica.cloure.Modules.receipts.CartItem;
import com.grupomarostica.cloure.R;

import java.util.ArrayList;

public class ProductsServicesPickerActivity extends AppCompatActivity {
    Handler handler = new Handler();
    ProductsServicesAdapter adapter;
    ArrayList<ProductService> items = new ArrayList<>();
    ArrayList<ProductService> selectedItems = new ArrayList<>();
    ImageButton btnDone;
    RecyclerView listView;
    RelativeLayout loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_services_picker);

        btnDone = findViewById(R.id.activity_products_selector_btnDone);
        listView = findViewById(R.id.activity_products_selector_lst);
        loader = findViewById(R.id.activity_products_services_picker_loader);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(mLayoutManager);

        listView.addOnItemTouchListener(new RecyclerItemClickListener(this, listView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                multi_select(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    ArrayList<CartItem> cartItems = new ArrayList<>();
                    for (int i=0; i<selectedItems.size(); i++){
                        ProductService productService = selectedItems.get(i);
                        CartItem cartItem = new CartItem(1, productService.Title, productService.Importe,productService.Importe);
                        cartItem.ProductoId = productService.Id;
                        cartItems.add(cartItem);
                    }

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("selected_items", cartItems);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        load_data();
    }

    public void multi_select(int position) {
        if (selectedItems.contains(items.get(position)))
            selectedItems.remove(items.get(position));
        else
            selectedItems.add(items.get(position));

        refreshAdapter();
    }


    public void refreshAdapter()
    {
        adapter.SelectedItems=selectedItems;
        adapter.Items=items;
        adapter.notifyDataSetChanged();
    }

    public void load_data(){
        BackgroundLoad backgroundLoad = new BackgroundLoad();
        new Thread(backgroundLoad).start();
    }

    class BackgroundLoad implements Runnable{
        @Override
        public void run() {
            items = ProductsServices.getList();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    try{
                        adapter = new ProductsServicesAdapter(getApplicationContext(), items, selectedItems);
                        listView.setAdapter(adapter);
                        loader.setVisibility(View.GONE);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
