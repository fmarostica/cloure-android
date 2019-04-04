package com.grupomarostica.cloure.Modules.users;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupomarostica.cloure.R;

import java.util.ArrayList;

public class UsersSimplifiedAdapter extends ArrayAdapter<User> {
    private final Context context;
    private ArrayList<User> values;

    public UsersSimplifiedAdapter(Context context, ArrayList<User> values) {
        super(context, R.layout.user_adapter_v3, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user_tmp = values.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.user_adapter_v3, parent, false);

        TextView lbl_nombre = rowView.findViewById(R.id.users_adapter_v3_name);
        TextView lbl_telefono = rowView.findViewById(R.id.users_adapter_v3_phone);

        lbl_nombre.setText(user_tmp.apellido+", "+user_tmp.nombre);

        if(!user_tmp.telefono.equals("")){
            lbl_telefono.setText(Html.fromHtml("<a href=\"tel:"+user_tmp.telefono+"\">"+user_tmp.telefono+"</a>"));
            lbl_telefono.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            lbl_telefono.setText("");
        }

        return rowView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User register = values.get(position);

        LayoutInflater mInflater = LayoutInflater.from(context);;
        final View view = mInflater.inflate(R.layout.adapter_single_row, parent, false);

        TextView lbl_nombre = view.findViewById(R.id.single_adapter_label);
        lbl_nombre.setText(register.apellido + ", "+register.nombre);

        return view;
    }
}
