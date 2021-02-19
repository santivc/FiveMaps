package com.example.fivemaps.Fragments;

import android.app.Activity;
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

public class IrFragment extends Fragment implements View.OnClickListener {

    private EditText etOrigen;
    private EditText etDestino;
    private Button button;

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

        return v;
    }

    @Override
    public void onClick(View v) {
        if (!etOrigen.getText().toString().equals("") && !etDestino.getText().toString().equals("")) {
            Intent intent = new Intent(getContext(), MapaActivity.class);
            intent.putExtra("ORIGEN", etOrigen.getText().toString());
            intent.putExtra("DESTINO", etDestino.getText().toString());
            startActivity(intent);
        }else{
            Toast.makeText(getContext(), "No se han introducido datos", Toast.LENGTH_SHORT).show();
        }

    }
}
