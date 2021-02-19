package com.example.fivemaps.Fragments;

import android.content.Intent;
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

import com.example.fivemaps.MapaActivity;
import com.example.fivemaps.R;

public class ExplorarFragment extends Fragment implements View.OnClickListener {

    private EditText etUbicacion;
    private Button button;

    public ExplorarFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_explorar, container, false);

        etUbicacion = v.findViewById(R.id.etUbicacion);
        button = v.findViewById(R.id.btLocalizar);

        button.setOnClickListener(this::onClick);

        return v;
    }

    @Override
    public void onClick(View v) {
        if (!etUbicacion.getText().toString().equals("")) {
            Intent intent = new Intent(getContext(), MapaActivity.class);
            intent.putExtra("UBICACION", etUbicacion.getText().toString());
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "No se han introducido datos", Toast.LENGTH_SHORT).show();
        }
    }
}
