package com.grupomarostica.cloure.Modules.receipts;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grupomarostica.cloure.R;

import java.util.ArrayList;
import java.util.Locale;

public class CartItemEditableAdapter extends RecyclerView.Adapter<CartItemEditableAdapter.CartItemEditableAdapterViewHolder> {
    private Context context;
    public ArrayList<CartItem> items;
    private CartItemChangedListener listener;

    double cant = 0;
    double price = 0;
    double total = 0;

    public CartItemEditableAdapter(Context context, ArrayList<CartItem> items){
        this.context = context;
        this.items = items;

    }

    @Override
    public CartItemEditableAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_editable_cartitem, parent, false);
        return new CartItemEditableAdapterViewHolder(itemView);
    }

    public void setOnDataChangedListener(CartItemChangedListener listener){
        this.listener = listener;
    }

    public interface CartItemChangedListener{
        public void OnDataChanged();
    }

    @Override
    public void onBindViewHolder(final CartItemEditableAdapterViewHolder holder, int position) {
        final CartItem item = items.get(position);
        holder.txtQuantity.setText(Double.toString(item.Cantidad));
        holder.lblDescription.setText(item.Descripcion);
        holder.txtPrice.setText(Double.toString(item.Precio));
        holder.txtTotal.setText(Double.toString(item.Total));
        holder.txtTotal.setEnabled(false);

        holder.txtQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(holder.txtQuantity.getText().toString().length()>0){
                    cant = Double.parseDouble(holder.txtQuantity.getText().toString());
                } else {
                    cant = 0;
                }
                if(holder.txtPrice.getText().toString().length()>0){
                    price = Double.parseDouble(holder.txtPrice.getText().toString());
                } else {
                    price = 0;
                }

                total = cant*price;
                holder.txtTotal.setText(String.format(Locale.US, "%.2f", total));

                item.Total = total;
                item.Precio = price;
                item.Cantidad = cant;

                listener.OnDataChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CartItemEditableAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextInputEditText txtQuantity;
        public TextInputEditText txtPrice;
        public TextInputEditText txtTotal;
        public TextView lblDescription;

        public CartItemEditableAdapterViewHolder(View itemView) {
            super(itemView);
            txtQuantity = itemView.findViewById(R.id.adapter_editable_cartitem_txtQuantity);
            txtPrice = itemView.findViewById(R.id.adapter_editable_cartitem_txtPrice);
            txtTotal = itemView.findViewById(R.id.adapter_editable_cartitem_txtTotal);
            lblDescription = itemView.findViewById(R.id.adapter_editable_cartitem_tvDescription);
        }
    }
}
