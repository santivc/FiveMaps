package com.example.fivemaps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.fivemaps.Fragments.MapsFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.appbar.MaterialToolbar;

public class MapaActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private final int REQUEST_CODE_ASK_PERMISSION = 111;
    private MapsFragment mapsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(this::onClick);

        Fragment fragment = new MapsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.containerMap, fragment).commit();
        solicitarPermisos();
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
        // Handle item selection
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.ninguno:
                //intent.putExtra("TIPO", GoogleMap.MAP_TYPE_NONE);
                //startActivity(intent);
                break;
            case R.id.normal:
                //intent.putExtra("TIPO", GoogleMap.MAP_TYPE_NORMAL);
                //startActivity(intent);
                break;
            case R.id.satelital:
                //intent.putExtra("TIPO", GoogleMap.MAP_TYPE_SATELLITE);
                //startActivity(intent);
                break;
            case R.id.terreno:
                //intent.putExtra("TIPO", GoogleMap.MAP_TYPE_TERRAIN);
                //startActivity(intent);
                break;
            case R.id.hibrido:
                //intent.putExtra("TIPO", GoogleMap.MAP_TYPE_HYBRID);
                //startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void solicitarPermisos() {
        int permissionAccessCoarseLocation = ActivityCompat.checkSelfPermission(MapaActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionAccessFineLocation = ActivityCompat.checkSelfPermission(MapaActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionAccessCoarseLocation != PackageManager.PERMISSION_GRANTED || permissionAccessFineLocation != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSION);
            }
        }
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }
}
