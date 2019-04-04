package com.grupomarostica.cloure.Modules.company;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.grupomarostica.cloure.BusinessTypesAdapter;
import com.grupomarostica.cloure.Core.Bitmap64;
import com.grupomarostica.cloure.Core.BusinessType;
import com.grupomarostica.cloure.Core.BusinessTypes;
import com.grupomarostica.cloure.Core.CloureModule;
import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.Core.Core;
import com.grupomarostica.cloure.Core.ModuleInfo;
import com.grupomarostica.cloure.Core.Utils;
import com.grupomarostica.cloure.Modules.countries.Countries;
import com.grupomarostica.cloure.Modules.countries.CountriesAdapter;
import com.grupomarostica.cloure.Modules.countries.Country;
import com.grupomarostica.cloure.R;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import static com.grupomarostica.cloure.Core.Utils.encodeImage;

public class company_fragment extends Fragment {
    private ModuleInfo moduleInfo;
    private int GALLERY = 1, CAMERA = 2;
    private boolean CameraPermission = false;
    protected Handler handler = new Handler();

    TextInputEditText txtCompanyName;
    TextView txtWeb;
    Spinner txtBusinessType;
    Spinner txtCountry;
    ImageView imgLogo;

    Bitmap64 imgToUpload = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company, container, false);

        txtCompanyName = view.findViewById(R.id.fragment_company_txtName);
        txtWeb = view.findViewById(R.id.fragment_company_txt_web);
        txtBusinessType = view.findViewById(R.id.fragment_company_txtBusinessType);
        txtCountry = view.findViewById(R.id.fragment_company_txtCountry);
        imgLogo = view.findViewById(R.id.fragment_company_img_profile);

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA);
                } else {
                    CameraPermission = true;
                }
                if(CameraPermission) showPictureDialog();
            }
        });

        LoadBusinessTypes();
        LoadCloureCountries();
        LoadData();

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(Menu.NONE, 1, Menu.NONE, "Guardar cambios");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case 1:
                //Guardar
                BackgroundSave backgroundSave = new BackgroundSave();
                new Thread(backgroundSave).start();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getContext());
        pictureDialog.setTitle("Seleccionar imagen");
        String[] pictureDialogItems = {
                "Seleccionar imagen de la galería",
                "Tomar una foto" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==2){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) CameraPermission=true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap photo = Utils.decodeUri(getContext(), contentURI, 800);
                    String encodedImage = encodeImage(photo);
                    imgToUpload = new Bitmap64(encodedImage, "");
                    imgLogo.setImageBitmap(photo);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            try{
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                String encodedImage = encodeImage(photo);
                imgToUpload = new Bitmap64(encodedImage, "");
                imgLogo.setImageBitmap(photo);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class BackgroundSave implements Runnable {

        @Override
        public void run() {
            JSONObject result = null;

            Country selectedCountry = (Country) txtCountry.getSelectedItem();
            BusinessType selectedBusinessType = (BusinessType) txtBusinessType.getSelectedItem();

            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "company"));
            params.add(new CloureParam("topic", "save"));
            params.add(new CloureParam("company_name", txtCompanyName.getText().toString()));
            params.add(new CloureParam("company_type", selectedBusinessType.Id));
            params.add(new CloureParam("country_id", selectedCountry.Id));

            if(imgToUpload!=null){
                try {
                    JSONObject imgObject = new JSONObject();
                    imgObject.put("Name", imgToUpload.Name);
                    imgObject.put("Data", imgToUpload.Data);
                    params.add(new CloureParam("image", imgObject.toString()));
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            CloureSDK cloureSDK = new CloureSDK();
            try{
                String res = cloureSDK.execute(params);
                result = new JSONObject(res);
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }


    private void LoadData(){
        BackgroundLoadData backgroundLoadData = new BackgroundLoadData();
        new Thread(backgroundLoadData).start();
    }

    class BackgroundLoadData implements Runnable {
        JSONObject api_result = null;
        Bitmap logo = null;
        int CountryId = 0;

        @Override
        public void run() {
            try{
                ArrayList<CloureParam> params = new ArrayList<>();
                params.add(new CloureParam("topic", "get_account_info"));
                CloureSDK cloureSDK = new CloureSDK();
                String res = cloureSDK.execute(params);
                api_result = new JSONObject(res);

                String imgUri = api_result.getString("logo");
                CountryId = api_result.getInt("country_id");

                URL url_image = new URL(imgUri);
                logo = BitmapFactory.decodeStream(url_image.openConnection().getInputStream());

            } catch (Exception ex){
                ex.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    try{
                        txtCompanyName.setText(api_result.getString("company_name"));
                        txtWeb.setText(api_result.getString("primary_domain"));
                        txtWeb.setMovementMethod(LinkMovementMethod.getInstance());
                        imgLogo.setImageBitmap(logo);

                        for (int i=0;i<txtCountry.getAdapter().getCount(); i++){
                            Country countryTmp = (Country) txtCountry.getAdapter().getItem(i);
                            if(CountryId==countryTmp.Id){
                                txtCountry.setSelection(i);
                                break;
                            }
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void LoadBusinessTypes(){
        ArrayList<BusinessType> businessTypes = BusinessTypes.get_list();
        BusinessTypesAdapter businessTypesAdapter = new BusinessTypesAdapter(getContext(), businessTypes);
        txtBusinessType.setAdapter(businessTypesAdapter);
    }

    private void LoadCloureCountries(){
        ArrayList<Country> countries = Countries.getCloureList();
        CountriesAdapter countriesAdapter = new CountriesAdapter(getContext(), countries);
        txtCountry.setAdapter(countriesAdapter);
    }
}
