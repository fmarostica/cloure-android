package com.grupomarostica.cloure.Modules.bands_artists;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.grupomarostica.cloure.Modules.finances.finances;
import com.grupomarostica.cloure.R;

import java.util.ArrayList;

public class BandsArtistsAdapter extends ArrayAdapter<BandArtist> {
    private final Context context;
    private final ArrayList<BandArtist> values;

    public BandsArtistsAdapter(Context context, ArrayList<BandArtist> values) {
        super(context, R.layout.row_single_adapter, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BandArtist record_tmp = values.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_single_adapter, parent, false);

        TextView lblGeneral = rowView.findViewById(R.id.single_adapter_label);
        lblGeneral.setText(record_tmp.Name);

        return rowView;
    }


}
