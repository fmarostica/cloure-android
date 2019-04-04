package com.grupomarostica.cloure.Modules.messages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.grupomarostica.cloure.R;

import java.util.ArrayList;

public class messages_adapter extends ArrayAdapter<Message> {
    private final Context context;
    private final ArrayList<Message> values;

    public messages_adapter(Context context, ArrayList<Message> values) {
        super(context, R.layout.messages_adapter, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message record_tmp = values.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.messages_adapter, parent, false);

        /*
        TextView lblFecha = rowView.findViewById(R.id.messages_adapter_txt_date);
        TextView lblAsunto = rowView.findViewById(R.id.messages_adapter_txt_subject);
        TextView lblUsuario = rowView.findViewById(R.id.messages_adapter_txt_user);

        lblFecha.setText(record_tmp.DateStr);
        lblAsunto.setText(record_tmp.Subject);
        if(record_tmp.UserStr=="null") record_tmp.UserStr = "";
        lblUsuario.setText(record_tmp.UserStr);
        */

        return rowView;
    }
}
