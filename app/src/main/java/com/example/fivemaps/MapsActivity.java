package com.example.fivemaps;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;

import android.media.MediaPlayer;

import android.os.Build;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.snackbar.Snackbar;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends AppCompatActivity implements View.OnClickListener,
        OnMapReadyCallback, GoogleMap.OnMarkerClickListener, RoutingListener {

    private Toolbar toolbar;
    private final int REQUEST_CODE_ASK_PERMISSION = 111;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private String ubicacion, origen, destino;
    private Address direccionUbicacion, direccionOrigen, direccionDestino;
    private Marker markerUbicacion;
    private MediaPlayer mediaPlayer;
    //polyline object
    private List<Polyline> polylines = null;
    private LatLng coordOrigen = null;
    private LatLng coordDestino = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(this::onClick);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        solicitarPermisos();

        geocoder = new Geocoder(MapsActivity.this);

        Bundle bundle = getIntent().getExtras();

        ubicacion = bundle.getString("UBICACION");
        origen = bundle.getString("ORIGEN");
        destino = bundle.getString("DESTINO");

        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ninguno:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.normal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.satelital:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.terreno:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.hibrido:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        //mMap.setTrafficEnabled(true);

        if (ubicacion != null)
            setUbicacion(googleMap);
        else
            setRuta(googleMap);

    }

    private void solicitarPermisos() {
        int permissionAccessCoarseLocation = ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionAccessFineLocation = ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionAccessCoarseLocation != PackageManager.PERMISSION_GRANTED || permissionAccessFineLocation != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSION);
            }
        }
    }

    private void setUbicacion(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            List<Address> addresses = geocoder.getFromLocationName(ubicacion, 1);
            if (addresses.size() > 0) {
                direccionUbicacion = addresses.get(0);
                LatLng latLng = new LatLng(direccionUbicacion.getLatitude(), direccionUbicacion.getLongitude());

                guardarUbicacion(direccionUbicacion);

                markerUbicacion = googleMap.addMarker(new MarkerOptions()
                        .position(latLng));

                if (direccionUbicacion.getLocality() == null)
                    markerUbicacion.setTitle(direccionUbicacion.getCountryName());

                else markerUbicacion.setTitle(direccionUbicacion.getLocality());

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

                mMap.setOnMarkerClickListener(this::onMarkerClick);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void guardarUbicacion(Address address) {
        SharedPreferences prefs = getSharedPreferences("guardados", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (address.getLocality() == null) {
            //ubicaciones.add(address.getCountryName());
            editor.putString(address.getLocality(), address.getCountryName());
        } else {
            //ubicaciones.add(address.getLocality());
            editor.putString(address.getLocality(), address.getLocality());
        }
        editor.apply();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(markerUbicacion)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setTitle("Conoce " + direccionUbicacion.getCountryName())
                    .setMessage("Si quieres saber mas sobre esta ubicación has clic en informacion")
                    .setNeutralButton(getString(R.string.information), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            enviarURL();
                        }
                    })
                    .setNegativeButton(getString(R.string.stop), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mediaPlayer != null) {
                                mediaPlayer.stop();
                                mediaPlayer = null;
                                Toast.makeText(MapsActivity.this, "STOP", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MapsActivity.this, "No hay ningun audio en play", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).setCancelable(false)
                    .setPositiveButton(getString(R.string.play), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mediaPlayer == null) {
                                seleccionarAudio();
                                if (mediaPlayer != null) {
                                    mediaPlayer.start();
                                    Toast.makeText(MapsActivity.this, "PLAY", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MapsActivity.this, "No existe sonido de este país", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return false;
    }

    private void seleccionarAudio() {
        switch (direccionUbicacion.getCountryCode()) {
            case "ES":
                mediaPlayer = MediaPlayer.create(this, R.raw.spain);
                break;
            case "FR":
                mediaPlayer = mediaPlayer.create(this, R.raw.francia);
                break;
            case "DE":
                mediaPlayer = mediaPlayer.create(this, R.raw.alemania);
                break;
            case "RU":
                mediaPlayer = mediaPlayer.create(this, R.raw.rusia);
                break;
            case "JP":
                mediaPlayer = mediaPlayer.create(this, R.raw.japon);
                break;
            case "CN":
                mediaPlayer = mediaPlayer.create(this, R.raw.china);
                break;
            case "AU":
                mediaPlayer = mediaPlayer.create(this, R.raw.australia);
                break;
            case "NZ":
                mediaPlayer = mediaPlayer.create(this, R.raw.nueva_zelanda);
                break;
            case "ZU":
                mediaPlayer = MediaPlayer.create(this, R.raw.sudafrica);
                break;
            case "EG":
                mediaPlayer = MediaPlayer.create(this, R.raw.egipto);
                break;
            case "US":
                mediaPlayer = MediaPlayer.create(this, R.raw.estados_unidos);
                break;
            case "CA":
                mediaPlayer = MediaPlayer.create(this, R.raw.canada);
                break;
            case "CO":
                mediaPlayer = MediaPlayer.create(this, R.raw.colombia);
                break;
            case "BR":
                mediaPlayer = MediaPlayer.create(this, R.raw.brasil);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }

    private void enviarURL() {
        String url;
        if (direccionUbicacion.getLocality() == null) {
            url = String.format("https://es.wikipedia.org/wiki/%s", direccionUbicacion.getCountryName());
        } else {
            url = String.format("https://es.wikipedia.org/wiki/%s", direccionUbicacion.getLocality());
        }
        Intent intent = new Intent(MapsActivity.this, WebActivity.class);
        intent.putExtra("URL", url);
        startActivity(intent);
    }

    private void setRuta(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            List<Address> addresses1 = geocoder.getFromLocationName(origen, 1);

            List<Address> addresses2 = geocoder.getFromLocationName(destino, 1);

            if (addresses1.size() > 0 && addresses2.size() > 0) {
                direccionOrigen = addresses1.get(0);
                direccionDestino = addresses2.get(0);

                coordOrigen = new LatLng(direccionOrigen.getLatitude(), direccionOrigen.getLongitude());
                coordDestino = new LatLng(direccionDestino.getLatitude(), direccionDestino.getLongitude());

                findRoutes(coordOrigen, coordDestino);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // function to find Routes.
    public void findRoutes(LatLng Start, LatLng End) {
        if (Start == null || End == null) {
            Toast.makeText(MapsActivity.this, "No se ha podido obtener la ubicacion", Toast.LENGTH_LONG).show();
        } else {
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key(getString(R.string.google_maps_key))  //also define your api key here.
                    .build();
            routing.execute();
        }
    }

    //Routing call back functions.
    @Override
    public void onRoutingFailure(RouteException e) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
        snackbar.show();
        Toast.makeText(MapsActivity.this, "No se ha encontrado ninguna ruta...", Toast.LENGTH_LONG).show();
//       Findroutes(start,end);
    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(MapsActivity.this, "Buscando ruta...", Toast.LENGTH_LONG).show();
    }

    //If Route finding success..
    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordOrigen, 7));

        if (polylines != null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();

        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i < route.size(); i++) {

            if (i == shortestRouteIndex) {
                polyOptions.color(Color.BLUE);
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylines.add(polyline);
            }
        }
        mMap.addMarker(new MarkerOptions().position(coordOrigen).title(direccionOrigen.getLocality()));
        mMap.addMarker(new MarkerOptions().position(coordDestino).title(direccionDestino.getLocality()));
    }

    @Override
    public void onRoutingCancelled() {
        findRoutes(coordOrigen, coordDestino);
    }
}


