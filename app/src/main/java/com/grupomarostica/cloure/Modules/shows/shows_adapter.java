package com.grupomarostica.cloure.Modules.shows;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grupomarostica.cloure.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class shows_adapter extends ArrayAdapter<Show> {
    private final Context context;
    private final ArrayList<Show> values;

    public shows_adapter(Context context, ArrayList<Show> values) {
        super(context, R.layout.shows_events_adapter, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Show registro_tmp = values.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.shows_events_adapter, parent, false);

        TextView lblDate = rowView.findViewById(R.id.shows_events_adapter_txt_date);
        TextView lblBandArtist = rowView.findViewById(R.id.shows_events_adapter_txt_band_artists);
        TextView lblPlace = rowView.findViewById(R.id.shows_events_adapter_txt_place);

        LinearLayout usersContainer = rowView.findViewById(R.id.shows_events_photographers);

        if(!registro_tmp.Photographers.equals("null")){
            TextView tv = new TextView(context);
            tv.setText(registro_tmp.Photographers);
            usersContainer.addView(tv);
        }

        /*
        for (int i=0; i<registro_tmp.Photographers.size(); i++){
            TextView tv = new TextView(context);
            tv.setText(registro_tmp.Photographers.get(i));
            usersContainer.addView(tv);
        }
        */

        lblDate.setText(registro_tmp.DateStr);
        lblBandArtist.setText(registro_tmp.BandArtist);
        lblPlace.setText(registro_tmp.Place);

        return rowView;
    }


}
