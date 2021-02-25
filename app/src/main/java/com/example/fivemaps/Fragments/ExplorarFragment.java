package com.example.fivemaps.Fragments;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fivemaps.MapsActivity;
import com.example.fivemaps.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class ExplorarFragment extends Fragment implements View.OnClickListener {

    private EditText editText;
    private Button button;
    private String ubicacion;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 100;

    public ExplorarFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_explorar, container, false);

        editText = v.findViewById(R.id.etUbicacion);
        button = v.findViewById(R.id.btLocalizar);

        button.setOnClickListener(this);

        editText.setFocusable(false);
        editText.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btLocalizar:
                sendLocation();
                break;
            case R.id.etUbicacion:
                setAutocomplete(AUTOCOMPLETE_REQUEST_CODE);
        }
    }

    private void sendLocation() {
        ubicacion = editText.getText().toString();
        if (!ubicacion.equals("")) {
            Intent intent = new Intent(getContext(), MapsActivity.class);
            intent.putExtra("UBICACION", ubicacion);
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "No se han introducido datos", Toast.LENGTH_SHORT).show();
        }
    }

    private void setAutocomplete(int requestCode) {
        List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                Place.Field.LAT_LNG, Place.Field.NAME);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fieldList)
                .build(getActivity());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                editText.setText(place.getAddress());
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
