package com.grupomarostica.cloure.Modules.users_groups;

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

public class UsersGroupsFilterAdapter extends ArrayAdapter<UserGroup> {
    private Context context;
    private ArrayList<UserGroup> values;

    public UsersGroupsFilterAdapter(Context context, ArrayList<UserGroup> values) {
        super(context, R.layout.adapter_single_row, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_single_row, parent, false);

        UserGroup group = values.get(position);

        TextView lbl_nombre = rowView.findViewById(R.id.single_adapter_label);
        lbl_nombre.setText(group.Name);

        return rowView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UserGroup register = values.get(position);

        LayoutInflater mInflater = LayoutInflater.from(context);;
        final View view = mInflater.inflate(R.layout.adapter_single_row, parent, false);

        TextView lbl_nombre = view.findViewById(R.id.single_adapter_label);
        lbl_nombre.setText(register.Name);

        return view;
    }
}