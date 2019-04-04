package com.grupomarostica.cloure.Modules.users;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
//import android.support.v7.widget.CardView;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupomarostica.cloure.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.UsersRecyclerAdapterViewHolder> {
    Context mContext;
    ArrayList<User> Items;
    ArrayList<User> SelectedItems;

    public UsersRecyclerAdapter(Context context, ArrayList<User> items, ArrayList<User> selectedItems){
        this.mContext=context;
        this.Items = items;
        this.SelectedItems = selectedItems;
    }

    public class UsersRecyclerAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView lblNombre;
        public TextView lblGrupo;
        public TextView lblSaldo;
        public ConstraintLayout itemContainer;

        public UsersRecyclerAdapterViewHolder(View itemView) {
            super(itemView);
            lblNombre = itemView.findViewById(R.id.adapter_user_tv_name);
            lblGrupo = itemView.findViewById(R.id.adapter_user_tv_group);
            lblSaldo = itemView.findViewById(R.id.adapter_user_tv_balance);
            itemContainer = itemView.findViewById(R.id.adapter_user_container);

        }
    }


    @Override
    public UsersRecyclerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user, parent, false);
        return new UsersRecyclerAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UsersRecyclerAdapterViewHolder holder, int position) {
        User item = Items.get(position);
        holder.lblNombre.setText(item.apellido + ", " + item.nombre);
        holder.lblGrupo.setText(item.grupo_id);
        holder.lblSaldo.setText(String.format(Locale.US, "%.2f", item.saldo));

        if(SelectedItems.contains(Items.get(position)))
            holder.itemContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.item_state_selected));
        else
            holder.itemContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.item_state_normal));
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }
}
