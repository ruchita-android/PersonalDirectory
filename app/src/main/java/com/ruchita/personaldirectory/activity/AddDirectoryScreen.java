package com.ruchita.personaldirectory.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ruchita.personaldirectory.R;
import com.ruchita.personaldirectory.adapter.PlaceAutocompleteAdapter;
import com.ruchita.personaldirectory.models.User;

import java.util.List;

/**
 * Created by ruchita on 2/3/18.
 */

public class AddDirectoryScreen extends AppCompatActivity  {

    private EditText etName;
    private EditText etPhone;
    private AutoCompleteTextView autocomplete_places;
    private GeoDataClient mGeoDataClient;
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));
    private PlaceAutocompleteAdapter mAdapter;
    private double latitude;
    private double longitude;
    private String placeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory_screen);

        Bundle bun = getIntent().getExtras();

        etName = (EditText) findViewById(R.id.etName);
        autocomplete_places = (AutoCompleteTextView) findViewById(R.id.autocomplete_places);
        etPhone = (EditText)findViewById(R.id.etPhone);

        if(bun!=null){
            User user = (User) bun.getSerializable("user_data");
            etName.setText(user.getName());
            etPhone.setText(user.getPhone());
            autocomplete_places.setText(user.getAddress());
            etPhone.setEnabled(false);
            latitude= user.getLat();
            longitude = user.getLang();
            placeName = user.getAddress();
        }

        autocomplete_places.setOnItemClickListener(mAutocompleteClickListener);

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);
        mAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient, BOUNDS_INDIA, null);
        autocomplete_places.setAdapter(mAdapter);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:

                if (!validateName() || !validatePhone()
                        || !validateAdd()) return;

                String name = etName.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();

                 long count = User.count(User.class, "phone = ?", new String[] {phone});

                if(count==0) {
                    User user = new User();
                    user.setName(name);
                    user.setPhone(phone);
                    user.setAddress(placeName);
                    user.setLat(latitude);
                    user.setLang(longitude);
                    user.save();
                    Toast.makeText(getApplicationContext(),R.string.saved, Toast.LENGTH_SHORT).show();

                }else{
                    List<User> userL = User.find(User.class, "phone=?",phone);
                    User user = userL.get(0);
                    user.setName(name);
                    user.setPhone(phone);
                    user.setAddress(placeName);
                    user.setLat(latitude);
                    user.setLang(longitude);
                    user.save();
                    Toast.makeText(getApplicationContext(),R.string.already_present, Toast.LENGTH_LONG).show();

                }

                setResult(RESULT_OK);
                finish();

                break;
        }
    }

    private boolean validateName() {
        if (etName.getText().toString().trim().isEmpty()) {
            etName.setError(getString(R.string.enter_name));
            requestFocus(etName);
            return false;
        }

        return true;
    }


    private boolean validatePhone() {
        String mob = etPhone.getText().toString().trim();
        if (mob.isEmpty() || mob.length() != 10) {
            etPhone.setError(getString(R.string.enter_phone));
            requestFocus(etPhone);
            return false;
        }

        return true;
    }

    private boolean validateAdd() {
        if (autocomplete_places.getText().toString().trim().isEmpty()) {
            autocomplete_places.setError(getString(R.string.enter_loc));
            requestFocus(autocomplete_places);
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();

            Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
            placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);
        }
    };

    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallback
            = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            try {
                PlaceBufferResponse places = task.getResult();

                final Place place = places.get(0);

                placeName = place.getName().toString();
                LatLng latLang = place.getLatLng();
                latitude = latLang.latitude;
                longitude = latLang.longitude;

                places.release();
            } catch (RuntimeRemoteException e) {
               return;
            }
        }
    };

   }

