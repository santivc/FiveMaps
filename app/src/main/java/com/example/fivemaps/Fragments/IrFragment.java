package com.example.fivemaps.Fragments;

import android.content.Intent;
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

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class IrFragment extends Fragment implements View.OnClickListener {

    private EditText etOrigen;
    private EditText etDestino;
    private Button button;
    private static int AUTOCOMPLETE_REQUEST_CODE_ORIGEN = 1;
    private static int AUTOCOMPLETE_REQUEST_CODE_DESTINO = 2;


    public IrFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ir, container, false);

        etOrigen = v.findViewById(R.id.etOrigen);
        etDestino = v.findViewById(R.id.etDestino);
        button = v.findViewById(R.id.btRuta);

        button.setOnClickListener(this::onClick);

        etOrigen.setFocusable(false);
        etOrigen.setOnClickListener(this);

        etDestino.setFocusable(false);
        etDestino.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btRuta:
                sendRoutes();
                break;
            case R.id.etOrigen:
                setAutoComplete(AUTOCOMPLETE_REQUEST_CODE_ORIGEN);
                break;
            case R.id.etDestino:
                setAutoComplete(AUTOCOMPLETE_REQUEST_CODE_DESTINO);
                break;
        }
    }

    private void sendRoutes() {
        if (!etOrigen.getText().toString().equals("") && !etDestino.getText().toString().equals("")) {
            Intent intent = new Intent(getContext(), MapsActivity.class);
            intent.putExtra("ORIGEN", etOrigen.getText().toString());
            intent.putExtra("DESTINO", etDestino.getText().toString());
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "No se han introducido datos", Toast.LENGTH_SHORT).show();
        }
    }

    private void setAutoComplete(int requestCode) {
        List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                Place.Field.LAT_LNG, Place.Field.NAME);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fieldList)
                .build(getActivity());
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE_ORIGEN) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                etOrigen.setText(place.getAddress());
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            }
        } else if (requestCode == AUTOCOMPLETE_REQUEST_CODE_DESTINO) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                etDestino.setText(place.getAddress());
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            }
        }
        return;
    }
}

