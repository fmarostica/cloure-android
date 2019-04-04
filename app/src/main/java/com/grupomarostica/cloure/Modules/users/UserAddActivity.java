package com.grupomarostica.cloure.Modules.users;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.Bitmap64;
import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.Core.Utils;
import com.grupomarostica.cloure.Modules.users_groups.UserGroup;
import com.grupomarostica.cloure.Modules.users_groups.UsersGroups;
import com.grupomarostica.cloure.Modules.users_groups.UsersGroupsFilterAdapter;
import com.grupomarostica.cloure.R;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static com.grupomarostica.cloure.Core.Utils.encodeImage;

public class UserAddActivity extends AppCompatActivity {

    private int GALLERY = 1, CAMERA = 2;
    private boolean CameraPermission = false;

    private static final String IMAGE_DIRECTORY = "/cuartetovip";
    private User user;
    private UsersGroups usersGroups = new UsersGroups();
    private ArrayList<UserGroup> users_groups_list;
    private UsersGroupsFilterAdapter ugf;

    private Handler handler = new Handler();

    //Views
    private ImageButton btnConfirm;
    private TextInputEditText txtName;
    private TextInputEditText txtLastName;
    private TextInputEditText txtAddress;
    private TextInputEditText txtEmail;
    private TextInputEditText txtPhone;
    private Spinner txtUserGroup;
    private ImageView imgPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add);

        //Set controls
        imgPhoto = findViewById(R.id.user_add_img);
        txtName = findViewById(R.id.users_add_txt_name);
        txtLastName = findViewById(R.id.users_add_txt_last_name);
        txtPhone = findViewById(R.id.users_add_txt_phone);
        txtAddress = findViewById(R.id.users_add_txt_address);
        txtEmail = findViewById(R.id.users_add_txt_email);
        txtUserGroup = findViewById(R.id.users_add_txt_users_group);
        btnConfirm = findViewById(R.id.activity_user_add_save);

        load_groups();

        user = new User();



        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
        txtUserGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UserGroup ug = (UserGroup)parent.getItemAtPosition(position);
                user.grupo_id = ug.Id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(UserAddActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UserAddActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA);
                } else {
                    CameraPermission = true;
                }
                if(CameraPermission) showPictureDialog();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            user.id = extras.getInt("id");
            get_data(user.id);
        } else {
            set_txtUserGroup("user");
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==2){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) CameraPermission=true;
        }
    }

    public void get_data(int id){
        BackgroundLoad backgroundLoad = new BackgroundLoad(id);
        new Thread(backgroundLoad).start();
    }

    class BackgroundLoad implements Runnable{
        int id = 0;
        public BackgroundLoad(int id){
            this.id = id;
        }

        @Override
        public void run() {
            user = Users.getById(this.id);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    txtName.setText(user.nombre);
                    txtLastName.setText(user.apellido);
                    txtAddress.setText(user.direccion);
                    txtEmail.setText(user.email);
                    txtPhone.setText(user.telefono);

                    int pos = 0;
                    for (int i=0; i<users_groups_list.size(); i++){
                        if(user.grupo_id.equals(users_groups_list.get(i).Id)) pos = i;
                    }
                    txtUserGroup.setSelection(pos);

                    if(user.Image!=null) imgPhoto.setImageBitmap(user.Image);
                }
            });
        }
    }

    public void load_groups(){
        users_groups_list = usersGroups.get_list();
        ugf = new UsersGroupsFilterAdapter(this, users_groups_list);
        txtUserGroup.setAdapter(ugf);

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
                    Bitmap photo = Utils.decodeUri(getApplicationContext(), contentURI, 800);
                    String encodedImage = Utils.encodeImage(photo);
                    user.ImageToUpload = new Bitmap64(encodedImage, "");
                    imgPhoto.setImageBitmap(photo);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(UserAddActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            try{
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                String encodedImage = encodeImage(photo);
                user.ImageToUpload = new Bitmap64(encodedImage, "");
                imgPhoto.setImageBitmap(photo);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void save(){
        try{
            JSONObject jsonObject = null;

            if(user.ImageToUpload!=null){
                jsonObject = new JSONObject();
                jsonObject.put("Name", user.ImageToUpload.Name);
                jsonObject.put("Data", user.ImageToUpload.Data);
            }

            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "users"));
            params.add(new CloureParam("topic", "guardar"));
            params.add(new CloureParam("id", Integer.toString(user.id)));
            params.add(new CloureParam("nombre", txtName.getText().toString()));
            params.add(new CloureParam("apellido", txtLastName.getText().toString()));
            params.add(new CloureParam("grupo_id", user.grupo_id));
            params.add(new CloureParam("mail", txtEmail.getText().toString()));
            params.add(new CloureParam("telefono", txtPhone.getText().toString()));
            params.add(new CloureParam("direccion", txtAddress.getText().toString()));
            if(jsonObject!=null) params.add(new CloureParam("uploaded_image", jsonObject.toString()));

            CloureSDK cloureSDK = new CloureSDK();

            String res = cloureSDK.execute(params);
            JSONObject object = new JSONObject(res);
            String error = object.getString("Error");
            if(error.equals("")){
                JSONObject response = object.getJSONObject("Response");
                user.id = response.getInt("id");

                InputMethodManager imm = (InputMethodManager) UserAddActivity.this.getSystemService(UserAddActivity.this.INPUT_METHOD_SERVICE);
                View nview = UserAddActivity.this.getCurrentFocus();
                imm.hideSoftInputFromWindow(nview.getWindowToken(), 0);
                if (nview == null) {
                    nview = new View(UserAddActivity.this);
                }
                Intent intent = new Intent();
                intent.putExtra("user_id", user.id);
                intent.putExtra("user_full_name", txtLastName.getText().toString()+", "+txtName.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void set_txtUserGroup(String group_id){
        int pos = 0;
        for (int i=0; i<users_groups_list.size(); i++){
            if(!group_id.equals("")){
                if(group_id.equals(users_groups_list.get(i).Id)) pos = i;
            } else {
                pos=1;
            }
        }
        txtUserGroup.setSelection(pos);
    }
}
