package com.grupomarostica.cloure.Modules.maintenance_warranties;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.grupomarostica.cloure.R;

import java.util.ArrayList;

public class MainenanceWarrantyAdapter extends ArrayAdapter<MaintenanceWarranty> {
    private final Context context;
    private ArrayList<MaintenanceWarranty> values;

    public MainenanceWarrantyAdapter(Context context, ArrayList<MaintenanceWarranty> values) {
        super(context, R.layout.users_adapter, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MaintenanceWarranty user_tmp = values.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.users_adapter, parent, false);


        return rowView;
    }
}
