package com.grupomarostica.cloure.Modules.company_branches;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.grupomarostica.cloure.R;

import java.util.ArrayList;

public class CompanyBranchesAdapter extends ArrayAdapter<CompanyBranch> {
    private final Context context;
    private final ArrayList<CompanyBranch> values;

    public CompanyBranchesAdapter(Context context, ArrayList<CompanyBranch> values) {
        super(context, R.layout.adapter_finances, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CompanyBranch record_tmp = values.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_company_branches, parent, false);
        TextView lblBranchName = rowView.findViewById(R.id.company_branches_adapter_txt_branch_name);
        lblBranchName.setText(record_tmp.Name);

        return rowView;
    }
}