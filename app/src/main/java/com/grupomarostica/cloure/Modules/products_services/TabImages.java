package com.grupomarostica.cloure.Modules.products_services;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.Bitmap64;
import com.grupomarostica.cloure.Core.ImageItem;
import com.grupomarostica.cloure.R;

import java.util.ArrayList;

import static com.grupomarostica.cloure.Core.Utils.encodeImage;

public class TabImages extends android.support.v4.app.Fragment {
    private int GALLERY = 1, CAMERA = 2;
    boolean CameraPermission = false;
    FloatingActionButton btnAddImage;
    GridView grdImages;
    ArrayList<ImageItem> images = new ArrayList<>();
    ArrayList<Bitmap64> imagesToUpload = new ArrayList<>();

    ProductImagesAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_products_services_tab_images, container, false);

        btnAddImage = rootView.findViewById(R.id.products_services_tab_images_btn_add);
        grdImages = rootView.findViewById(R.id.products_services_tab_images_lst_img);

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA);
                } else {
                    CameraPermission = true;
                }
                if(CameraPermission) showPictureDialog();
            }
        });

        adapter = new ProductImagesAdapter(getContext(), images);

        return rootView;
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
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
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), GALLERY);
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
        getActivity();
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if(data!=null){
            if (requestCode == GALLERY) {
                Uri contentURI = data.getData();
                try {
                    Bitmap photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    //String encodedImage = encodeImage(photo);
                    ImageItem imageItem = new ImageItem();
                    imageItem.image = photo;
                    images.add(imageItem);
                    grdImages.setAdapter(adapter);

                    String encodedImage = encodeImage(photo);
                    imagesToUpload.add(new Bitmap64(encodedImage, ""));

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == CAMERA) {
                try{
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    String encodedImage = encodeImage(photo);
                    //user.ImageToUpload = new Bitmap64(encodedImage, "");
                    //imgPhoto.setImageBitmap(photo);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
