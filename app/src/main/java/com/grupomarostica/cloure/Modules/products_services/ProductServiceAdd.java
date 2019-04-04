package com.grupomarostica.cloure.Modules.products_services;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.Bitmap64;
import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.Core.Core;
import com.grupomarostica.cloure.Modules.products_services_types.ProductServiceType;
import com.grupomarostica.cloure.Modules.products_services_units.ProductServiceUnit;
import com.grupomarostica.cloure.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductServiceAdd extends AppCompatActivity {
    private Handler handler = new Handler();

    private ProductService productService;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Integer Id = 0;
    private TabGeneral tabGeneral;
    private TabPrices tabPrices;
    private TabStock tabStock;
    private TabImages tabImages;
    private ImageButton btnSave;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_service_add);

        productService = new ProductService();

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            this.Id = extras.getInt("Id");
        }

        btnSave = findViewById(R.id.products_services_add_btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });

        tabGeneral = new TabGeneral();
        tabPrices = new TabPrices();
        tabStock = new TabStock();
        tabImages = new TabImages();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        TabLayout tabLayout = findViewById(R.id.tabs);
        mViewPager = findViewById(R.id.container);

        mSectionsPagerAdapter.AddFragment(tabGeneral);
        mSectionsPagerAdapter.AddFragment(tabPrices);
        mSectionsPagerAdapter.AddFragment(tabStock);
        mSectionsPagerAdapter.AddFragment(tabImages);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        listView = findViewById(R.id.products_services_tab_stock_lst_stock);

        if(Id>0){
            productService.Id = Id;
            BackgroundLoad backgroundLoad = new BackgroundLoad();
            new Thread(backgroundLoad).start();
        }
    }

    class BackgroundLoad implements Runnable {
        @Override
        public void run() {
            productService = ProductsServices.get(productService.Id);

            try {
                //Set views content relative to product loaded
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tabGeneral.txtTitulo.setText(productService.Title);
                        tabGeneral.txtDescripcion.setText(productService.Description);

                        tabPrices.txtIVA.setText(productService.IVA.toString());
                        tabPrices.txtCostoPrecio.setText(productService.PrecioCosto.toString());
                        tabPrices.txtCostoImporte.setText(productService.ImporteCosto.toString());
                        tabPrices.txtVentaPrecio.setText(productService.PrecioVenta.toString());
                        tabPrices.txtVentaImporte.setText(productService.ImporteVenta.toString());
                    }
                });
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    /**
     * Function to Save product in the CloureAPI, this function calls to BackgroundSave class that
     * permit realize the tasks in another thread for better performance (and required for graphics)
     */
    private void Save(){
        BackgroundSave backgroundSave = new BackgroundSave();
        new Thread(backgroundSave).start();
    }


    class BackgroundSave implements Runnable {
        String errormsg = "";
        @Override
        public void run() {
            try{
                ProductServiceType productServiceType = (ProductServiceType) tabGeneral.txtProductType.getSelectedItem();
                ProductServiceUnit productServiceUnit = (ProductServiceUnit) tabGeneral.txtProductMeasure.getSelectedItem();
                productService.imagesToUpload = tabImages.imagesToUpload;

                ArrayList<CloureParam> params = new ArrayList<>();
                params.add(new CloureParam("module", "products_services"));
                params.add(new CloureParam("topic", "guardar"));
                params.add(new CloureParam("id", Integer.toString(productService.Id)));
                params.add(new CloureParam("titulo", tabGeneral.txtTitulo.getText().toString()));
                params.add(new CloureParam("descripcion", tabGeneral.txtDescripcion.getText().toString()));
                params.add(new CloureParam("tipo_producto_id", Integer.toString(productServiceType.Id)));
                params.add(new CloureParam("sistema_medida_id", Integer.toString(productServiceUnit.Id)));

                params.add(new CloureParam("iva", tabPrices.txtIVA.getText().toString()));
                params.add(new CloureParam("costo_precio", tabPrices.txtCostoPrecio.getText().toString()));
                params.add(new CloureParam("costo_importe", tabPrices.txtCostoImporte.getText().toString()));
                params.add(new CloureParam("venta_precio", tabPrices.txtVentaPrecio.getText().toString()));
                params.add(new CloureParam("venta_importe", tabPrices.txtVentaImporte.getText().toString()));

                //CREATE ITEMS IN JSON ARRAY FORMAT
                JSONArray imagesArr = new JSONArray();
                for (int i = 0; i < productService.imagesToUpload.size(); i++)
                {
                    Bitmap64 cartItem = productService.imagesToUpload.get(i);
                    JSONObject imgObj = new JSONObject();
                    imgObj.put("Name", cartItem.Name);
                    imgObj.put("Data", cartItem.Data);
                    imagesArr.put(imgObj);
                }
                //END ITEMS ARRAY CREATION

                if(productService.imagesToUpload.size()>0){
                    params.add(new CloureParam("images", imagesArr.toString()));
                }

                CloureSDK cloureSDK = new CloureSDK();
                String res = cloureSDK.execute(params);
                JSONObject object = new JSONObject(res);
                String error = object.getString("Error");
                if(error.equals("")){
                    JSONObject response = object.getJSONObject("Response");
                    productService.Id=response.getInt("id");

                    Intent intent = new Intent();
                    intent.putExtra("id", productService.Id);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    errormsg = error;
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(!errormsg.equals("")){
                            Toast.makeText(getBaseContext(), errormsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
        public void AddFragment(Fragment fragment){
            fragmentList.add(fragment);
        }
    }
}
