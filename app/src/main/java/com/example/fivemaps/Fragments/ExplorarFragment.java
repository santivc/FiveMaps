package com.example.fivemaps.Fragments;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
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


import java.io.IOException;
import java.util.List;

public class ExplorarFragment extends Fragment implements View.OnClickListener {

    private EditText editText;
    private Button button;
    private String ubicacion;

    public ExplorarFragment() {}

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

        return v;
    }

    @Override
    public void onClick(View v) {
        sendLocation();
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

}
