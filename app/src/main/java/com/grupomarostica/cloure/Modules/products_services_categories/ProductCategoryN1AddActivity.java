package com.grupomarostica.cloure.Modules.products_services_categories;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.Bitmap64;
import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.R;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static com.grupomarostica.cloure.Core.Utils.encodeImage;

public class ProductCategoryN1AddActivity extends AppCompatActivity {
    private int GALLERY = 1, CAMERA = 2;
    private boolean CameraPermission = false;
    private ImageButton btnSave;
    private ImageView imgCategory;
    private TextInputEditText txtName;

    private ProductCategory productCategory = new ProductCategory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category_n1_add);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            this.productCategory.Id = extras.getInt("id");
            this.productCategory.Type = extras.getString("type");
        }

        btnSave = findViewById(R.id.activity_category_n1_add_save);
        imgCategory = findViewById(R.id.activity_category_n1_add_img);
        txtName = findViewById(R.id.activity_category_n1_add_name);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        imgCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ProductCategoryN1AddActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ProductCategoryN1AddActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA);
                } else {
                    CameraPermission = true;
                }
                if(CameraPermission) showPictureDialog();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(this.productCategory.Id>0) get_data();
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
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String encodedImage = encodeImage(photo);
                    productCategory.ImageToUpload = new Bitmap64(encodedImage, "");
                    imgCategory.setImageBitmap(photo);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ProductCategoryN1AddActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            try{
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                String encodedImage = encodeImage(photo);
                productCategory.ImageToUpload = new Bitmap64(encodedImage, "");
                imgCategory.setImageBitmap(photo);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void get_data(){
        ProductCategory tmp = new ProductsServicesCategories(getApplicationContext()).get_item(productCategory.Id, productCategory.Type);
        if(productCategory!=null){
            this.productCategory = tmp;
            txtName.setText(tmp.Name);
            txtName.selectAll();
            txtName.setFocusableInTouchMode(true);
            txtName.requestFocus();
        }
    }

    private void save(){
        try{
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "products_services_categories"));
            params.add(new CloureParam("topic", "guardar_categoria_n1"));
            params.add(new CloureParam("id", Integer.toString(productCategory.Id)));
            params.add(new CloureParam("nombre", txtName.getText().toString()));
            params.add(new CloureParam("descripcion", ""));

            CloureSDK cloureSDK = new CloureSDK();

            String res = cloureSDK.execute(params);
            JSONObject api_response = new JSONObject(res);
            String error = api_response.getString("Error");
            if(error.equals("")){
                JSONObject response_str = api_response.getJSONObject("Response");
                productCategory.Id = response_str.getInt("id");

                InputMethodManager imm = (InputMethodManager) ProductCategoryN1AddActivity.this.getSystemService(ProductCategoryN1AddActivity.this.INPUT_METHOD_SERVICE);
                View nview = ProductCategoryN1AddActivity.this.getCurrentFocus();
                imm.hideSoftInputFromWindow(nview.getWindowToken(), 0);
                if (nview == null) {
                    nview = new View(ProductCategoryN1AddActivity.this);
                }
                Intent intent = new Intent();
                intent.putExtra("id", productCategory.Id);
                intent.putExtra("name", txtName.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
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
}
