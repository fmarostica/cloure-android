package com.grupomarostica.cloure.Core.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.grupomarostica.cloure.Core.BusinessType;
import com.grupomarostica.cloure.R;

import java.util.ArrayList;

public class BusinessTypesFilterAdapter extends ArrayAdapter<BusinessType> {
    private Context context;
    private ArrayList<BusinessType> values;

    public BusinessTypesFilterAdapter(Context context, ArrayList<BusinessType> values) {
        super(context, R.layout.row_single_adapter, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BusinessType record_tmp = values.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_single_adapter, parent, false);

        TextView lbl_nombre = rowView.findViewById(R.id.single_adapter_label);
        lbl_nombre.setText(record_tmp.Title);

        return rowView;
    }
}
