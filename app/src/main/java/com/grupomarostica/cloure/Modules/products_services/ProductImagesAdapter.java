package com.grupomarostica.cloure.Modules.products_services;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupomarostica.cloure.Core.ImageItem;
import com.grupomarostica.cloure.R;

import java.util.ArrayList;

public class ProductImagesAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<ImageItem> data = new ArrayList();

    public ProductImagesAdapter(Context context, ArrayList<ImageItem> data) {
        super(context, R.layout.adapter_image, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.adapter_image, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = row.findViewById(R.id.adapter_image_txtTitle);
            holder.image = row.findViewById(R.id.adapter_image_imgPhoto);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        ImageItem item = data.get(position);
        holder.imageTitle.setText(item.title);
        holder.image.setImageBitmap(item.image);
        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }
}
