package com.grupomarostica.cloure.Modules.places;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.grupomarostica.cloure.R;

import java.util.ArrayList;

public class PlacesAdapter extends ArrayAdapter<Place> {
    private final Context context;
    private final ArrayList<Place> values;

    public PlacesAdapter(Context context, ArrayList<Place> values) {
        super(context, R.layout.row_single_adapter, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Place record_tmp = values.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_single_adapter, parent, false);

        TextView lblGeneral = rowView.findViewById(R.id.single_adapter_label);
        lblGeneral.setText(record_tmp.Name);

        return rowView;
    }
}
