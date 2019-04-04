package com.grupomarostica.cloure.Modules.products_services;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.grupomarostica.cloure.BarcodeScanActivity;
import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.Modules.products_services_categories.ProductCategoryN1AddActivity;
import com.grupomarostica.cloure.Modules.products_services_categories.ProductCategoryN2AddActivity;
import com.grupomarostica.cloure.Modules.products_services_types.ProductServiceType;
import com.grupomarostica.cloure.Modules.products_services_types.ProductsServicesTypes;
import com.grupomarostica.cloure.Modules.products_services_types.ProductsServicesTypesAdapter;
import com.grupomarostica.cloure.Modules.products_services_units.ProductServiceUnit;
import com.grupomarostica.cloure.Modules.products_services_units.ProductsServicesUnits;
import com.grupomarostica.cloure.Modules.products_services_units.ProductsServicesUnitsAdapter;
import com.grupomarostica.cloure.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class TabGeneral extends android.support.v4.app.Fragment {
    public Spinner txtProductType;
    public Spinner txtProductMeasure;
    public TextInputEditText txtTitulo;
    public TextInputEditText txtDescripcion;
    public TextInputEditText txtCategoriaN1;
    public TextInputEditText txtBarCode;
    public ImageButton barCodeScanBtn;
    public ImageButton btnAddCategoryN1;
    public ImageButton btnAddCategoryN2;

    private boolean CameraPermission = false;
    private ArrayAdapter<String> adapter;
    private String[] spinnerArray;
    private CloureSDK cloureSDK;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_products_services_tab_general, container, false);

        txtProductType = rootView.findViewById(R.id.products_tab_general_product_type);
        txtProductMeasure = rootView.findViewById(R.id.products_tab_general_measure_unit);
        txtTitulo = rootView.findViewById(R.id.products_tab_general_txt_title);
        txtDescripcion = rootView.findViewById(R.id.products_tab_general_txt_description);
        txtCategoriaN1 = rootView.findViewById(R.id.products_tab_general_txt_cat_n1);
        txtCategoriaN1 = rootView.findViewById(R.id.products_tab_general_txt_cat_n2);
        txtBarCode = rootView.findViewById(R.id.products_services_add_txt_barcode);
        barCodeScanBtn = rootView.findViewById(R.id.products_tab_general_btn_barcode);
        btnAddCategoryN1 = rootView.findViewById(R.id.products_tab_general_add_cat1);
        btnAddCategoryN2 = rootView.findViewById(R.id.products_tab_general_add_cat2);

        barCodeScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                } else {
                    CameraPermission = true;
                }

                if(CameraPermission){
                    Intent intent = new Intent(getContext(), BarcodeScanActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    Toast.makeText(getContext(), "Debes permitir el acceso a la cámara para poder escanear códigos de barras", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnAddCategoryN1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductCategoryN1AddActivity.class);
                startActivity(intent);
            }
        });

        btnAddCategoryN2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductCategoryN2AddActivity.class);
                startActivity(intent);
            }
        });


        load_products_types();
        load_products_units();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void load_products_types(){
        ArrayList<ProductServiceType> productServiceTypes = new ProductsServicesTypes().get_list("");
        ProductsServicesTypesAdapter productsTypesAdapter = new ProductsServicesTypesAdapter(getContext(), productServiceTypes);
        txtProductType.setAdapter(productsTypesAdapter);
    }
    public void load_products_units(){
        ArrayList<ProductServiceUnit> productServiceUnits = new ProductsServicesUnits().get_list("");
        ProductsServicesUnitsAdapter productsServicesUnitsAdapter = new ProductsServicesUnitsAdapter(getContext(), productServiceUnits);
        txtProductMeasure.setAdapter(productsServicesUnitsAdapter);
    }

    public void cargar_categorias_n1(){
        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "products_services_categories"));
            params.add(new CloureParam("topic", "listar_categorias_n1"));
            cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);

            JSONObject object = new JSONObject(res);
            String error = object.getString("Error");
            if(error.equals("")){
                JSONObject response = object.getJSONObject("Response");
                JSONArray registros = response.getJSONArray("Registros");
                HashMap<Integer, String> spinnerMap = new HashMap<>();
                spinnerArray = new String[registros.length()];

                for (int i=0; i<registros.length(); i++){
                    JSONObject registro = registros.getJSONObject(i);
                    int measure_id = registro.getInt("Id");
                    spinnerMap.put(measure_id, registro.getString("Nombre"));
                    spinnerArray[i] = registro.getString("Nombre");
                    //if(Product!=null && Product.MeasureUnitId==measure_id) product_measure_pos = i;
                }

                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                //txtCategoriaN1.setAdapter(adapter);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void cargar_categorias_n2(int categoriaN1Id){
        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "products_services_categories"));
            params.add(new CloureParam("topic", "listar_categorias_n2"));
            params.add(new CloureParam("categoria_n1_id", Integer.toString(categoriaN1Id)));
            cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);

            JSONObject object = new JSONObject(res);
            String error = object.getString("Error");
            if(error.equals("")){
                JSONObject response = object.getJSONObject("Response");
                JSONArray registros = response.getJSONArray("Registros");
                HashMap<Integer, String> spinnerMap = new HashMap<>();
                spinnerArray = new String[registros.length()];

                for (int i=0; i<registros.length(); i++){
                    JSONObject registro = registros.getJSONObject(i);
                    int measure_id = registro.getInt("Id");
                    spinnerMap.put(measure_id, registro.getString("Nombre"));
                    spinnerArray[i] = registro.getString("Nombre");
                    //if(Product!=null && Product.MeasureUnitId==measure_id) product_measure_pos = i;
                }

                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                //txtCategoriaN2.setAdapter(adapter);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            //barcode
            if(requestCode==1){
                String barcode = data.getStringExtra("barcode");
                txtBarCode.setText(barcode);
            }
        }
    }
}
