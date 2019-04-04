package com.grupomarostica.cloure.Modules.users;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import com.grupomarostica.cloure.R;

import java.util.ArrayList;

public class UsersFilterAdapter extends ArrayAdapter<User> {
    private Context context;
    private ListFilter listFilter = new ListFilter();
    private ArrayList<User> values;
    private ArrayList<User> dataListAllItems;

    public UsersFilterAdapter(Context context, ArrayList<User> values) {
        super(context, R.layout.row_single_adapter, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user_tmp = values.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_single_adapter, parent, false);

        TextView lbl_nombre = rowView.findViewById(R.id.single_adapter_label);
        lbl_nombre.setText(user_tmp.apellido+", "+user_tmp.nombre);

        return rowView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
        //return super.getFilter();
    }

    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            try{
                if (dataListAllItems == null) {
                    synchronized (lock) {
                        dataListAllItems = new ArrayList<User>(values);
                    }
                }

                if (prefix == null || prefix.length() == 0) {
                    synchronized (lock) {
                        results.values = dataListAllItems;
                        results.count = dataListAllItems.size();
                    }
                } else {
                    final String searchStrLowerCase = prefix.toString().toLowerCase();

                    ArrayList<String> matchValues = new ArrayList<>();

                    for (User dataItem : dataListAllItems) {
                        if (dataItem.nombre.toLowerCase().startsWith(searchStrLowerCase)) {
                            matchValues.add(dataItem.apellido+", "+dataItem.nombre);
                        }
                    }

                    results.values = matchValues;
                    results.count = matchValues.size();
                }
            } catch (Exception ex){
                ex.printStackTrace();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
