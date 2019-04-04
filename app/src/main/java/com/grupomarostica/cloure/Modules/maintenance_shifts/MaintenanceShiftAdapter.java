package com.grupomarostica.cloure.Modules.maintenance_shifts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.grupomarostica.cloure.R;

import java.util.ArrayList;

public class MaintenanceShiftAdapter extends ArrayAdapter<MaintenanceShift> {
    private final Context context;
    private final ArrayList<MaintenanceShift> values;

    public MaintenanceShiftAdapter(Context context, ArrayList<MaintenanceShift> values){
        super(context, R.layout.adapter_maintenance_shift, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MaintenanceShift registro_tmp = values.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_maintenance_shift, parent, false);

        TextView lblDate = rowView.findViewById(R.id.maintenance_adapter_txt_date);
        TextView lblIssue = rowView.findViewById(R.id.maintenance_adapter_txt_issue);
        TextView lblCustomer = rowView.findViewById(R.id.maintenance_adapter_txt_customer);
        TextView lblCustomerAddress = rowView.findViewById(R.id.maintenance_adapter_txt_address);
        TextView lblStatus = rowView.findViewById(R.id.maintenance_adapter_txt_estado);

        lblDate.setText(registro_tmp.FechaCompletaStr);
        lblIssue.setText(registro_tmp.IssueDescription);
        lblCustomer.setText(registro_tmp.Customer);
        lblCustomerAddress.setText(registro_tmp.CustomerAddress);
        lblStatus.setText(registro_tmp.Status);
        if(registro_tmp.StatusId==1) lblStatus.setTextColor(context.getResources().getColor(R.color.pending));
        if(registro_tmp.StatusId==2) lblStatus.setTextColor(context.getResources().getColor(R.color.green));

        return rowView;
    }
}
