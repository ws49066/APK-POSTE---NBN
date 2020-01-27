package com.nbntelecom.nbnpostemap;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static com.nbntelecom.nbnpostemap.R.layout.color_spinner;
import static com.nbntelecom.nbnpostemap.R.layout.spinner_dropdown_layout;


public class Cadastro_Activity extends FragmentActivity implements OnMapReadyCallback {
    Button btn_abrirMap;
    LinearLayout layoutMap;

    Location localizacaoAtual;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Spinner spinnertposte = (Spinner) findViewById(R.id.tipoposte);
        ArrayAdapter<String> adaptertposte = new ArrayAdapter<String>(Cadastro_Activity.this,
                color_spinner,
                getResources().getStringArray(R.array.tipoposte));
        adaptertposte.setDropDownViewResource(spinner_dropdown_layout);
        spinnertposte.setAdapter(adaptertposte);

        Spinner spinnerespposte = (Spinner) findViewById(R.id.espposte);
        ArrayAdapter<String> adapterespposte = new ArrayAdapter<String>(Cadastro_Activity.this,
                color_spinner, getResources().getStringArray(R.array.espposte));
        adapterespposte.setDropDownViewResource(spinner_dropdown_layout);
        spinnerespposte.setAdapter(adapterespposte);

        Spinner spinnerdimposte = (Spinner) findViewById(R.id.dimposte);
        ArrayAdapter<String> adapterdimposte = new ArrayAdapter<String>(Cadastro_Activity.this,
                color_spinner, getResources().getStringArray(R.array.dimposte));
        adapterdimposte.setDropDownViewResource(spinner_dropdown_layout);
        spinnerdimposte.setAdapter(adapterdimposte);

        Spinner spinnerareaposte = (Spinner) findViewById(R.id.areaposte);
        ArrayAdapter<String> adapterareaposte = new ArrayAdapter<String>(Cadastro_Activity.this,
                color_spinner, getResources().getStringArray(R.array.areaposte));
        adapterareaposte.setDropDownViewResource(spinner_dropdown_layout);
        spinnerareaposte.setAdapter(adapterareaposte);

        Spinner spinneriluminaposte = (Spinner) findViewById(R.id.iluminaposte);
        ArrayAdapter<String> adapteriluminaposte = new ArrayAdapter<String>(Cadastro_Activity.this,
                color_spinner, getResources().getStringArray(R.array.iluminaposte));
        adapteriluminaposte.setDropDownViewResource(spinner_dropdown_layout);
        spinneriluminaposte.setAdapter(adapteriluminaposte);

        Spinner spinnerchaveposte = (Spinner) findViewById(R.id.chaveposte);
        ArrayAdapter<String> adapterchaveposte = new ArrayAdapter<String>(Cadastro_Activity.this,
                color_spinner, getResources().getStringArray(R.array.chaveposte));
        adapterchaveposte.setDropDownViewResource(spinner_dropdown_layout);
        spinnerchaveposte.setAdapter(adapterchaveposte);

        Spinner spinnertransposte = (Spinner) findViewById(R.id.transposte);
        ArrayAdapter<String> adaptertransposte = new ArrayAdapter<String>(Cadastro_Activity.this,
                color_spinner, getResources().getStringArray(R.array.transposte));
        adaptertransposte.setDropDownViewResource(spinner_dropdown_layout);
        spinnertransposte.setAdapter(adaptertransposte);

        Spinner spinnersuporteposte = (Spinner) findViewById(R.id.suporteposte);
        ArrayAdapter<String> adaptersuporteposte = new ArrayAdapter<String>(Cadastro_Activity.this,
                color_spinner, getResources().getStringArray(R.array.suporteposte));
        adaptersuporteposte.setDropDownViewResource(spinner_dropdown_layout);
        spinnersuporteposte.setAdapter(adaptersuporteposte);

        Spinner spinnerbaixaposte = (Spinner) findViewById(R.id.baixaposte);
        ArrayAdapter<String> adapterbaixaposte = new ArrayAdapter<String>(Cadastro_Activity.this,
                color_spinner, getResources().getStringArray(R.array.baixaposte));
        adapterbaixaposte.setDropDownViewResource(spinner_dropdown_layout);
        spinnerbaixaposte.setAdapter(adapterbaixaposte);

        Spinner spinnerfixposte = (Spinner) findViewById(R.id.fixposte);
        ArrayAdapter<String> adapterfixposte = new ArrayAdapter<String>(Cadastro_Activity.this,
                color_spinner, getResources().getStringArray(R.array.fixposte));
        adapterfixposte.setDropDownViewResource(spinner_dropdown_layout);
        spinnerfixposte.setAdapter(adapterfixposte);

        Spinner spinnerrackposte = (Spinner) findViewById(R.id.rackposte);
        ArrayAdapter<String> adapterrackposte = new ArrayAdapter<String>(Cadastro_Activity.this,
                color_spinner, getResources().getStringArray(R.array.rackposte));
        adapterrackposte.setDropDownViewResource(spinner_dropdown_layout);
        spinnerrackposte.setAdapter(adapterrackposte);

        Spinner spinnerreservaposte = (Spinner) findViewById(R.id.reservaposte);
        ArrayAdapter<String> adapterreservaposte = new ArrayAdapter<String>(Cadastro_Activity.this,
                color_spinner, getResources().getStringArray(R.array.reservaposte));
        adapterreservaposte.setDropDownViewResource(spinner_dropdown_layout);
        spinnerreservaposte.setAdapter(adapterreservaposte);

        btn_abrirMap =(Button) findViewById(R.id.abrirMap);
        layoutMap = (LinearLayout) findViewById(R.id.layoutMap);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btn_abrirMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutMap.setVisibility(View.VISIBLE);
                fetchLastLocation();
            }
        });

    }
        private void fetchLastLocation(){
            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
                return;

            }

            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        localizacaoAtual = location;
                        Toast.makeText(getApplicationContext(), localizacaoAtual.getLatitude()+""+localizacaoAtual.getLongitude()
                                ,Toast.LENGTH_SHORT).show();
                        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
                        supportMapFragment.getMapAsync(Cadastro_Activity.this);
                    }
                }
            });
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng latLng = new LatLng(localizacaoAtual.getLatitude(),localizacaoAtual.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Eu estou aqui.");
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19));
            googleMap.addMarker(markerOptions);
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode){
                case REQUEST_CODE:
                    if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        fetchLastLocation();
                    }
                    break;
            }
        }

}
