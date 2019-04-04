package com.grupomarostica.cloure.Core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.grupomarostica.cloure.R;

import java.util.ArrayList;

public class ModuleFilterItemAdapter extends ArrayAdapter<ModuleFilterItem> {
    private final Context context;
    private final ArrayList<ModuleFilterItem> values;

    public ModuleFilterItemAdapter(Context context, ArrayList<ModuleFilterItem> values) {
        super(context, R.layout.finances_adapter, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ModuleFilterItem register = values.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_single_row, parent, false);

        TextView lbl_nombre = rowView.findViewById(R.id.single_adapter_label);
        lbl_nombre.setText(register.Title);

        return rowView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ModuleFilterItem register = values.get(position);

        LayoutInflater mInflater = LayoutInflater.from(context);;
        final View view = mInflater.inflate(R.layout.adapter_single_row, parent, false);

        TextView lbl_nombre = view.findViewById(R.id.single_adapter_label);
        lbl_nombre.setText(register.Title);
        return view;
    }
}
