package com.grupomarostica.cloure.Modules.products_services;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grupomarostica.cloure.R;

import java.util.ArrayList;

public class ProductsServicesAdapter extends RecyclerView.Adapter<ProductsServicesAdapter.ProductsServicesViewHolder> {
    Context mContext;
    ArrayList<ProductService> Items;
    ArrayList<ProductService> SelectedItems;

    public ProductsServicesAdapter(Context context, ArrayList<ProductService> items, ArrayList<ProductService> selectedItems){
        this.mContext=context;
        this.Items = items;
        this.SelectedItems = selectedItems;
    }

    @Override
    public ProductsServicesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_product_service, parent, false);
        return new ProductsServicesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductsServicesViewHolder holder, int position) {
        ProductService productService = Items.get(position);
        holder.lblTitle.setText(productService.Title);
        holder.lblCategory.setText(productService.CategoriaN1);
        holder.lblPrice.setText("$ "+productService.Importe.toString());

        if(SelectedItems.contains(Items.get(position)))
            holder.itemContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.item_state_selected));
        else
            holder.itemContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.item_state_normal));
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    public class ProductsServicesViewHolder extends RecyclerView.ViewHolder{
        public TextView lblTitle;
        public TextView lblCategory;
        public TextView lblPrice;
        public ConstraintLayout itemContainer;

        public ProductsServicesViewHolder(View itemView) {
            super(itemView);
            lblTitle = itemView.findViewById(R.id.adapter_prodcut_service_tv_title);
            lblCategory = itemView.findViewById(R.id.adapter_prodcut_service_tv_category);
            lblPrice = itemView.findViewById(R.id.adapter_prodcut_service_tv_price);
            itemContainer = itemView.findViewById(R.id.adapter_product_service_container);
        }
    }
}
