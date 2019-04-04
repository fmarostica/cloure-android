package com.grupomarostica.cloure.Modules.products_services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.grupomarostica.cloure.R;

import java.util.ArrayList;

public class ProductStockAdapter extends ArrayAdapter<StockItem> {
    private final Context context;
    private final ArrayList<StockItem> values;

    public ProductStockAdapter(Context context, ArrayList<StockItem> values) {
        super(context, R.layout.adapter_product_stock, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StockItem registro_tmp = values.get(position);

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_product_stock, parent, false);
        }


        //TextView lblSucursal = rowView.findViewById(R.id.adapter_product_stock_txt_branch);
        //EditText txtCantidad = rowView.findViewById(R.id.adapter_product_stock_txt_stock);

        return convertView;
    }

    class ViewHolder {

    }
}
