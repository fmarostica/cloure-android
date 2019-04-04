package com.grupomarostica.cloure.Modules.shows;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.Core.DatePickerFragment;
import com.grupomarostica.cloure.Modules.bands_artists.BandArtist;
import com.grupomarostica.cloure.Modules.bands_artists.BandsArtists;
import com.grupomarostica.cloure.Modules.bands_artists.BandsArtistsAddActivity;
import com.grupomarostica.cloure.Modules.bands_artists.BandsArtistsFilterAdapter;
import com.grupomarostica.cloure.Modules.places.Place;
import com.grupomarostica.cloure.Modules.places.Places;
import com.grupomarostica.cloure.Modules.places.PlacesActivity;
import com.grupomarostica.cloure.Modules.places.PlacesFilterAdapter;
import com.grupomarostica.cloure.Modules.users.User;
import com.grupomarostica.cloure.Modules.users.UserSelection;
import com.grupomarostica.cloure.Modules.users.UsersSimplifiedAdapter;
import com.grupomarostica.cloure.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class ShowAddActivity extends AppCompatActivity {
    Show show = new Show();

    //UI Components
    TextInputEditText txtDate;
    AutoCompleteTextView txtBandArtist;
    AutoCompleteTextView txtPlace;
    ImageButton btnSave;
    ImageButton btnAddBandArtist;
    ImageButton btnAddPlace;
    ImageButton btnSelectPhotographer;
    ListView lstPhotographers;

    ArrayList<BandArtist> bandsArtists = new ArrayList<>();
    ArrayList<Place> places = new ArrayList<>();
    ArrayList<User> photographers = new ArrayList<>();

    BandsArtistsFilterAdapter bandsArtistsAdapter;
    PlacesFilterAdapter placesFilterAdapter;
    UsersSimplifiedAdapter _usersAdapter;

    boolean loading_from_api=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_add);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            show.Id = extras.getInt("id");
        }

        txtDate = findViewById(R.id.show_add_txt_date);
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        txtBandArtist = findViewById(R.id.show_add_txt_band_artist);
        txtBandArtist.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0 && !loading_from_api){
                    load_bands_artists(s.toString());
                    txtBandArtist.showDropDown();
                    txtBandArtist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try{
                                BandArtist tmp = (BandArtist)parent.getItemAtPosition(position);
                                show.BandArtistId = tmp.Id;
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    txtBandArtist.dismissDropDown();
                }
            }
        });

        txtPlace = findViewById(R.id.show_add_txt_place);
        txtPlace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0 && !loading_from_api){
                    load_places(s.toString());
                    txtPlace.showDropDown();
                    txtPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try{
                                Place tmp = (Place)parent.getItemAtPosition(position);
                                show.PlaceId = tmp.Id;
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    txtPlace.dismissDropDown();
                }
            }
        });

        btnAddBandArtist = findViewById(R.id.show_add_btn_add_band);
        btnAddBandArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowAddActivity.this, BandsArtistsAddActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        btnAddPlace = findViewById(R.id.show_add_btn_add_place);
        btnAddPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowAddActivity.this, PlacesActivity.class);
                startActivityForResult(intent, 2);
            }
        });

        btnSelectPhotographer = findViewById(R.id.shows_add_add_photographer);
        btnSelectPhotographer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowAddActivity.this, UserSelection.class);
                startActivityForResult(intent, 3);
            }
        });

        btnSave = findViewById(R.id.shows_add_btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        lstPhotographers = findViewById(R.id.shows_add_lst_photographers);

        _usersAdapter = new UsersSimplifiedAdapter(this, photographers);
        lstPhotographers.setAdapter(_usersAdapter);

        if(show.Id>0) load_data(show.Id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //BandArtist RC = 1
        if(resultCode==RESULT_OK){
            if(requestCode==1){
                load_bands_artists("");
                show.BandArtistId = data.getIntExtra("band_artist_id", 0);
                show.BandArtist = data.getStringExtra("band_artist_name");
                txtBandArtist.setText(show.BandArtist);
            }
            //Place RC=2
            if(requestCode==2){
                load_places("");
                show.PlaceId = data.getIntExtra("place_id", 0);
                show.Place = data.getStringExtra("place_name");
                txtPlace.setText(show.Place);
            }
            //User (Photographer) RC=3
            if(requestCode==3){
                //load_places("");
                try{
                    User photographer = new User();
                    photographer.id = data.getIntExtra("user_id",0);
                    photographer.nombre = data.getStringExtra("user_name");
                    boolean found = false;
                    for(int i=0;i<photographers.size();i++){
                        if(photographer.id==photographers.get(i).id){
                            found=true;
                            Toast.makeText(getBaseContext(), R.string.the_user_is_in_list, Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    if(!found){
                        photographers.add(photographer);
                        _usersAdapter = new UsersSimplifiedAdapter(this, photographers);
                        lstPhotographers.setAdapter(_usersAdapter);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void showDatePickerDialog(){
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                final String selectedDate = String.format("%02d", day) + "/" + String.format("%02d", (month+1)) + "/" + String.format("%04d", year);
                txtDate.setText(selectedDate);
            }
        });

        newFragment.show(this.getSupportFragmentManager(), "datePicker");
    }

    private void load_data(int id){
        try {
            loading_from_api=true;
            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "shows"));
            params.add(new CloureParam("topic", "obtener"));
            params.add(new CloureParam("id", Integer.toString(id)));
            CloureSDK cloureSDK = new CloureSDK();
            String res = cloureSDK.execute(params);

            JSONObject object = new JSONObject(res);
            String error = object.getString("Error");
            if(error.equals("")){
                JSONObject response = object.getJSONObject("Response");
                show.BandArtistId = response.getInt("ArtistaId");
                show.BandArtist = response.getString("Artista");
                show.PlaceId = response.getInt("LugarId");
                show.Place = response.getString("Lugar");
                show.DateStr = response.getString("FechaStr");

                txtDate.setText(show.DateStr);
                txtBandArtist.setText(show.BandArtist);
                txtPlace.setText(show.Place);

            } else {
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
            }
            loading_from_api=false;
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void load_bands_artists(String filtro){
        bandsArtists = new BandsArtists().get_list(filtro);
        bandsArtistsAdapter = new BandsArtistsFilterAdapter(getBaseContext(), bandsArtists);
        txtBandArtist.setAdapter(bandsArtistsAdapter);
    }
    private void load_places(String filtro){
        places = new Places().get_list(filtro);
        placesFilterAdapter = new PlacesFilterAdapter(getBaseContext(), places);
        txtPlace.setAdapter(placesFilterAdapter);
    }

    private void save(){
        try{
            String api_response_str="";
            String err_msg="";

            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for(int i = 0; i<photographers.size(); i++){
                sb.append("{\"id\":\""+photographers.get(i).id+"\"}");
                if(i<photographers.size()-1) sb.append(",");
            }
            sb.append("]");

            ArrayList<CloureParam> params = new ArrayList<>();
            params.add(new CloureParam("module", "shows"));
            params.add(new CloureParam("topic", "guardar"));
            params.add(new CloureParam("id", Integer.toString(show.Id)));
            params.add(new CloureParam("fecha", txtDate.getText().toString()));
            params.add(new CloureParam("artista_id", Integer.toString(show.BandArtistId)));
            params.add(new CloureParam("lugar_id", Integer.toString(show.PlaceId)));
            params.add(new CloureParam("fotografos", sb.toString()));

            CloureSDK cloureSDK = new CloureSDK();

            api_response_str = cloureSDK.execute(params);

            if(!api_response_str.equals("")){
                JSONObject api_response = new JSONObject(api_response_str);
                JSONObject api_error = api_response.getJSONObject("Error");
                err_msg = api_error.getString("message");

                if(err_msg.equals("")){
                    show.Id = api_response.getInt("Response");

                    InputMethodManager imm = (InputMethodManager) ShowAddActivity.this.getSystemService(ShowAddActivity.this.INPUT_METHOD_SERVICE);
                    View nview = ShowAddActivity.this.getCurrentFocus();
                    imm.hideSoftInputFromWindow(nview.getWindowToken(), 0);
                    if (nview == null) nview = new View(ShowAddActivity.this);
                    Intent intent = new Intent();
                    intent.putExtra("show_id", show.Id);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), err_msg, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getBaseContext(), R.string.api_response_empty_value, Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
