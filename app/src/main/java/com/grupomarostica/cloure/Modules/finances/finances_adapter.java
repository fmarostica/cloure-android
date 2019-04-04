package com.grupomarostica.cloure.Modules.finances;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.grupomarostica.cloure.R;

import java.util.ArrayList;

public class finances_adapter extends ArrayAdapter<finances> {
    private final Context context;
    private final ArrayList<finances> values;

    public finances_adapter(Context context, ArrayList<finances> values) {
        super(context, R.layout.finances_adapter, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        finances record_tmp = values.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.finances_adapter, parent, false);
        TextView lbl_details = rowView.findViewById(R.id.tv_details);
        TextView lbl_import = rowView.findViewById(R.id.tv_import);
        TextView lbl_fecha = rowView.findViewById(R.id.tv_date);
        TextView lbl_usuario = rowView.findViewById(R.id.tv_user);

        lbl_details.setText(record_tmp.Detalles);
        lbl_import.setText("$ "+record_tmp.Importe.toString());
        lbl_fecha.setText(record_tmp.FechaStr);
        if(record_tmp.Usuario=="null") record_tmp.Usuario = "";
        lbl_usuario.setText(record_tmp.Usuario);

        if(record_tmp.TipoMovimientoId.equals("egreso")) lbl_import.setTextColor(context.getResources().getColor(R.color.red));
        if(record_tmp.TipoMovimientoId.equals("ingreso")) lbl_import.setTextColor(context.getResources().getColor(R.color.green));

        return rowView;
    }
}
