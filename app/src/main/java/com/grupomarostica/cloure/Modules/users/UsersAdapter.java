package com.grupomarostica.cloure.Modules.users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupomarostica.cloure.R;

import java.util.ArrayList;

public class UsersAdapter extends ArrayAdapter<User> {
    private final Context context;
    private final ArrayList<User> values;
    public  boolean ShowCustomerFields = false;

    public UsersAdapter(Context context, ArrayList<User> values, boolean ShowCustomerFields) {
        super(context, R.layout.adapter_user, values);
        this.context = context;
        this.values = values;
        this.ShowCustomerFields = ShowCustomerFields;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User registro_tmp = values.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_user, parent, false);
        TextView lbl_nombre = rowView.findViewById(R.id.adapter_user_tv_name);
        TextView lbl_group = rowView.findViewById(R.id.adapter_user_tv_group);
        TextView lbl_email = rowView.findViewById(R.id.adapter_user_tv_email);
        TextView lbl_saldo = rowView.findViewById(R.id.adapter_user_tv_balance);
        ImageView imageView = rowView.findViewById(R.id.adapter_user_image);
        if(registro_tmp.Image!=null) imageView.setImageBitmap(registro_tmp.Image);

        lbl_nombre.setText(registro_tmp.nombre);

        if(!ShowCustomerFields){
            lbl_group.setText(registro_tmp.grupo_id);
            lbl_email.setText(registro_tmp.email);
        } else {
            lbl_group.setText(registro_tmp.telefono);
            lbl_email.setText(registro_tmp.direccion);
        }

        lbl_saldo.setText(Double.toString(registro_tmp.saldo));
        //lbl_importe.setText("$ "+registro_tmp.Importe.toString());
        return rowView;
    }
}
