package com.grupomarostica.cloure.Modules.products_services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupomarostica.cloure.R;

import java.util.ArrayList;

public class ProductsServicesArrayAdapter extends ArrayAdapter<ProductService> {
    private final Context context;
    private final ArrayList<ProductService> values;

    public ProductsServicesArrayAdapter(Context context, ArrayList<ProductService> values) {
        super(context, R.layout.adapter_product_service, values);
        this.context = context;
        this.values = values;
    }

    private static class ViewHolder{
        private TextView lblTitle;
        private TextView lblCategory;
        private TextView lblPrice;
        private ImageView imgPhoto;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(convertView==null){
            ProductService registro_tmp = values.get(position);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.adapter_product_service, parent, false);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.lblTitle = view.findViewById(R.id.adapter_prodcut_service_tv_title);
            viewHolder.lblCategory = view.findViewById(R.id.adapter_prodcut_service_tv_category);
            viewHolder.lblPrice = view.findViewById(R.id.adapter_prodcut_service_tv_price);
            viewHolder.imgPhoto = view.findViewById(R.id.adapter_prodcut_service_imgPhoto);

            if(registro_tmp.ImageBitmap!=null) viewHolder.imgPhoto.setImageBitmap(registro_tmp.ImageBitmap);

            viewHolder.lblTitle.setText(registro_tmp.Title);
            viewHolder.lblCategory.setText(registro_tmp.CategoriaN1);
            viewHolder.lblPrice.setText("$ "+registro_tmp.Importe.toString());
        } else {
            view = convertView;
        }

        return view;
    }
}