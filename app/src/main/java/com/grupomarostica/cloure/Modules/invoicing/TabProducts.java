package com.grupomarostica.cloure.Modules.invoicing;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.grupomarostica.cloure.Modules.products_services.ProductsServicesPickerActivity;
import com.grupomarostica.cloure.Modules.receipts.CartItem;
import com.grupomarostica.cloure.Modules.receipts.CartItemEditableAdapter;
import com.grupomarostica.cloure.R;

import java.util.ArrayList;
import java.util.Locale;

public class TabProducts extends Fragment {
    public Button btnSelectProducts;
    public ArrayList<CartItem> items = new ArrayList<>();
    public CartItemEditableAdapter adapter;
    public RecyclerView lstProductos;
    private double total = 0;

    invoicing_fragment parentFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_invoicing_tab_products, container, false);

        btnSelectProducts = rootView.findViewById(R.id.fragment_invoicing_tabProducts_btnSelectProduct);
        lstProductos = rootView.findViewById(R.id.fragment_invoicing_tabProducts_lstProducts);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        lstProductos.setLayoutManager(mLayoutManager);
        adapter = new CartItemEditableAdapter(getActivity(), items);
        lstProductos.setAdapter(adapter);

        btnSelectProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectProductsIntent = new Intent(getContext(), ProductsServicesPickerActivity.class);
                startActivityForResult(selectProductsIntent, 1);
            }
        });

        adapter.setOnDataChangedListener(new CartItemEditableAdapter.CartItemChangedListener() {
            @Override
            public void OnDataChanged() {
                calcularTotal();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentFragment = (invoicing_fragment) getParentFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(requestCode==1){
                try{
                    Bundle bundle = data.getExtras();
                    ArrayList<CartItem> cartItems = (ArrayList<CartItem>)bundle.getSerializable("selected_items");
                    for (int i=0; i<cartItems.size(); i++){
                        CartItem cartItem = cartItems.get(i);
                        items.add(cartItem);
                        adapter.notifyDataSetChanged();
                    }

                    calcularTotal();

                    //Toast.makeText(getActivity(), Integer.toString(cartItems.size()), Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void calcularTotal(){
        try {
            total = 0;
            for (int i=0; i<items.size(); i++){
                CartItem cartItem = items.get(i);
                total+=cartItem.Total;
            }

            parentFragment.setTxtTotal(total);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
