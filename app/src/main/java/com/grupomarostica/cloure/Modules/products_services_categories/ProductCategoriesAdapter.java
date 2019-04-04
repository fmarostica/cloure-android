package com.grupomarostica.cloure.Modules.products_services_categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.grupomarostica.cloure.R;

import java.util.ArrayList;

public class ProductCategoriesAdapter extends ArrayAdapter<ProductCategory> {
    private final Context context;
    private final ArrayList<ProductCategory> values;

    public ProductCategoriesAdapter(Context context, ArrayList<ProductCategory> values){
        super(context, R.layout.row_single_adapter, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProductCategory registro_tmp = values.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_single_adapter, parent, false);

        TextView tv = rowView.findViewById(R.id.single_adapter_label);
        tv.setText(registro_tmp.Name);

        return rowView;
    }
}
