package com.grupomarostica.cloure.Modules.users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.grupomarostica.cloure.Modules.finances.finances;
import com.grupomarostica.cloure.R;

import java.util.ArrayList;

public class UserFinanceAdapter extends ArrayAdapter<finances> {
    private final Context context;
    private final ArrayList<finances> values;

    public UserFinanceAdapter(Context context, ArrayList<finances> values) {
        super(context, R.layout.user_finance_adapter, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        finances record_tmp = values.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.user_finance_adapter, parent, false);

        TextView lbl_details = rowView.findViewById(R.id.user_finance_adapter_tv_description);
        TextView lbl_import = rowView.findViewById(R.id.user_finance_adapter_tv_amount);
        TextView lbl_fecha = rowView.findViewById(R.id.user_finance_adapter_tv_date);

        lbl_details.setText(record_tmp.Detalles);
        lbl_import.setText("$ "+record_tmp.Importe.toString());
        lbl_fecha.setText(record_tmp.FechaStr);
        if(record_tmp.Usuario=="null") record_tmp.Usuario = "";

        //lbl_usuario.setText(record_tmp.Usuario);

        if(record_tmp.TipoMovimientoId.equals("debito_usuario")) lbl_import.setTextColor(context.getResources().getColor(R.color.red));
        if(record_tmp.TipoMovimientoId.equals("credito_usuario")) lbl_import.setTextColor(context.getResources().getColor(R.color.green));

        return rowView;
    }
}
