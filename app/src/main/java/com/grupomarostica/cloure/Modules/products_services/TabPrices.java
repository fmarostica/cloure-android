package com.grupomarostica.cloure.Modules.products_services;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grupomarostica.cloure.R;

import java.util.Locale;

public class TabPrices extends android.support.v4.app.Fragment {
    public TextInputEditText txtIVA;
    public TextInputEditText txtCostoPrecio;
    public TextInputEditText txtCostoImporte;
    public TextInputEditText txtVentaPrecio;
    public TextInputEditText txtVentaImporte;

    private boolean CambiandoCostoPrecio = false;
    private boolean CambiandoCostoImporte = false;
    private boolean CambiandoVentaPrecio = false;
    private boolean CambiandoVentaImporte = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_products_services_tab_prices, container, false);

        txtIVA = rootView.findViewById(R.id.products_tab_prices_txt_iva);
        txtCostoPrecio = rootView.findViewById(R.id.products_tab_prices_txt_costo_precio);
        txtCostoImporte = rootView.findViewById(R.id.products_tab_prices_txt_costo_importe);
        txtVentaPrecio = rootView.findViewById(R.id.products_tab_prices_txt_venta_precio);
        txtVentaImporte = rootView.findViewById(R.id.products_tab_prices_txt_venta_importe);

        txtCostoPrecio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                CambiandoCostoPrecio=true;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CalcularCostoImporte();
                CambiandoCostoPrecio=false;
            }
        });

        txtCostoImporte.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                CambiandoCostoImporte = true;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CalcularCostoPrecio();
                CambiandoCostoImporte=false;
            }
        });

        //PRECIOS DE VENTA

        txtVentaPrecio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                CambiandoVentaPrecio=true;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CalcularVentaImporte();
                CambiandoVentaPrecio=false;
            }
        });

        txtVentaImporte.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                CambiandoVentaImporte = true;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CalcularVentaPrecio();
                CambiandoVentaImporte=false;
            }
        });

        return rootView;
    }

    private void CalcularCostoImporte(){
        if(!CambiandoCostoImporte){
            Double IVA = 0.00;
            Double Precio = 0.00;
            Double Importe = 0.00;

            try{
                if(!txtIVA.getText().toString().equals("")) IVA = Double.parseDouble(txtIVA.getText().toString());
                if(!txtCostoPrecio.getText().toString().equals("")) Precio = Double.parseDouble(txtCostoPrecio.getText().toString());

                if(Precio>0){
                    if(IVA>0){
                        Importe = Precio + ((Precio * IVA)/100);
                    } else {
                        Importe = Precio;
                    }
                } else {
                    Importe = 0.00;
                }
            } catch (Exception e){
                Importe=0.00;
            }
            txtCostoImporte.setText(String.format(Locale.US, "%.2f", Importe));
        }
    }

    private void CalcularCostoPrecio(){
        if(!CambiandoCostoPrecio){
            Double IVA = 0.00;
            Double Precio = 0.00;
            Double Importe = 0.00;

            try{
                if(!txtIVA.getText().toString().equals("")) IVA = Double.parseDouble(txtIVA.getText().toString());
                if(!txtCostoImporte.getText().toString().equals("")) Importe = Double.parseDouble(txtCostoImporte.getText().toString());

                if(Importe>0){
                    if(IVA>0){
                        Precio = Importe / ((100+IVA)/100);
                    } else {
                        Precio = Importe;
                    }
                } else {
                    Precio = 0.00;
                }
            } catch (Exception e){
                Precio=0.00;
            }
            txtCostoPrecio.setText(String.format(Locale.US, "%.2f", Precio));
        }
    }

    // PRECIOS DE VENTA

    private void CalcularVentaImporte(){
        if(!CambiandoVentaImporte){
            Double IVA = 0.00;
            Double Precio = 0.00;
            Double Importe = 0.00;

            try{
                if(!txtIVA.getText().toString().equals("")) IVA = Double.parseDouble(txtIVA.getText().toString());
                if(!txtVentaPrecio.getText().toString().equals("")) Precio = Double.parseDouble(txtVentaPrecio.getText().toString());

                if(Precio>0){
                    if(IVA>0){
                        Importe = Precio + ((Precio * IVA)/100);
                    } else {
                        Importe = Precio;
                    }
                } else {
                    Importe = 0.00;
                }
            } catch (Exception e){
                Importe=0.00;
            }
            txtVentaImporte.setText(String.format(Locale.US, "%.2f", Importe));
        }
    }

    private void CalcularVentaPrecio(){
        if(!CambiandoVentaPrecio){
            Double IVA = 0.00;
            Double Precio = 0.00;
            Double Importe = 0.00;

            try{
                if(!txtIVA.getText().toString().equals("")) IVA = Double.parseDouble(txtIVA.getText().toString());
                if(!txtVentaImporte.getText().toString().equals("")) Importe = Double.parseDouble(txtVentaImporte.getText().toString());

                if(Importe>0){
                    if(IVA>0){
                        Precio = Importe / ((100+IVA)/100);
                    } else {
                        Precio = Importe;
                    }
                } else {
                    Precio = 0.00;
                }
            } catch (Exception e){
                Precio=0.00;
            }
            txtVentaPrecio.setText(String.format(Locale.US, "%.2f", Precio));
        }
    }
}
