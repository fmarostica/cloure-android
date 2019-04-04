package com.grupomarostica.cloure.Modules.products_services_categories;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupomarostica.cloure.R;

import java.util.ArrayList;

public class ProductsCategoriesAdapter extends ArrayAdapter<ProductCategory> {
    private final Context context;
    private final ArrayList<ProductCategory> values;

    ProductsCategoriesAdapter(Context context, ArrayList<ProductCategory> values) {
        super(context, R.layout.adapter_product_category, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ProductCategory registro_tmp = values.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_product_category, parent, false);

        TextView lbl_nombre = rowView.findViewById(R.id.adapter_product_categery_tv_title);
        ImageView imageView = rowView.findViewById(R.id.adapter_product_categery_img);
        //if(registro_tmp.ImageBitmap!=null) imageView.setImageBitmap(registro_tmp.ImageBitmap);

        lbl_nombre.setText(registro_tmp.Name);
        return rowView;
    }
}
