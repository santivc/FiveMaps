package com.example.fivemaps.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.fivemaps.MapsActivity;
import com.example.fivemaps.R;
import com.example.fivemaps.WebActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class LugaresFragment extends Fragment {

    private ListView listView;
    private ArrayList<String> listaUbicaciones;
    private ArrayAdapter<String> adapter;
    //private Set<String> setUbicaciones;
    private Map<String, ?> ubicacion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lugares, container, false);
        listView = v.findViewById(R.id.listView);

        //setUbicaciones = new HashSet<>();

        cargarRegistro();
        buildListView();

        insertUbicacion(ubicacion);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarRegistro();
        buildListView();
    }

    public void cargarRegistro() {
        SharedPreferences preferences = getActivity().getSharedPreferences("guardados", Context.MODE_PRIVATE);
        ubicacion = preferences.getAll();

        if (listaUbicaciones == null)
            listaUbicaciones = new ArrayList<>();
    }

    private void buildListView() {
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listaUbicaciones);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ubicacionSeleccionada = (String) parent.getItemAtPosition(position);
                String url = String.format("https://es.wikipedia.org/wiki/%s", ubicacionSeleccionada);
                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);
            }
        });
    }

    private void insertUbicacion(Map<String, ?> ubicacion) {
        for(Map.Entry<String,?> ele : ubicacion.entrySet()){
            listaUbicaciones.add(ele.getValue().toString());
        }
        adapter.notifyDataSetChanged();
    }

}