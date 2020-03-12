package com.nbntelecom.nbnpostemap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class MapPoste extends FragmentActivity implements OnMapReadyCallback {

    Location localizacaoAtual;
    Button mTypeNormal,mTypeBtn;
    Button btn_salvar_localizacao,btn_cancelar;
    String var_id_poste;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView Geolocalizacao,textgeolocalicao;
    private static final int REQUEST_CODE = 101;
    GoogleMap mMap;

    GoogleApiClient client;
    LatLng startLatLng,endLatlng;
    LocationRequest request;
    Marker currentMarker;
    Marker destinationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_poste);

        mTypeBtn = findViewById(R.id.btnSatelite);
        mTypeNormal = findViewById(R.id.btnNormal);
        Geolocalizacao = findViewById(R.id.Geolocalizacao);
        textgeolocalicao = findViewById(R.id.textGeolocalizacao);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            var_id_poste = (String) extras.get("var_id_poste");
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        fetchLastLocation();

        mTypeNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                Geolocalizacao.setTextColor(getResources().getColor(R.color.MapNormal));
                textgeolocalicao.setTextColor(getResources().getColor(R.color.MapNormal));

            }
        });

        mTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                Geolocalizacao.setTextColor(getResources().getColor(R.color.MapSatelite));
                textgeolocalicao.setTextColor(getResources().getColor(R.color.MapSatelite));
            }
        });

        btn_cancelar = findViewById(R.id.btn_cancelar_cadastro);

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exibirConfirmacao();
            }
        });

        btn_salvar_localizacao = findViewById(R.id.btn_salvar_localizacao);
    }

    private void fetchLastLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;

        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    localizacaoAtual = location;
                    Geolocalizacao.setText(localizacaoAtual.getLatitude()+""+localizacaoAtual.getLongitude());
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
                    supportMapFragment.getMapAsync(MapPoste.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(localizacaoAtual.getLatitude(),localizacaoAtual.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("POSTE ID: "+var_id_poste);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19));
        mMap.addMarker(markerOptions);
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

    public void CadastrarLocalizacao(){

            StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/localizacao.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("erro")) {
                                Toast.makeText(getApplicationContext(),"ERRO NA INSERÇÃO NO BANCO", Toast.LENGTH_SHORT). show();

                            }else{
                                String Array[] = new String[2];
                                Array = response.split(",");
                                String var_name_user = Array[0];
                                String id_login_user = Array[1];
                                Intent intentEnviar = new Intent(MapPoste.this, MapPoste.class);
                                intentEnviar.putExtra("var_name_user",var_name_user);
                                intentEnviar.putExtra("id_login_user",id_login_user);
                                Toast.makeText(getApplicationContext(),"Poste Cadastrado com sucesso!!!",Toast.LENGTH_LONG).show();
                                startActivity(intentEnviar);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String>  params = new HashMap<>();
                    params.put("var_id_poste",var_id_poste);
                    return  params;
                }
            };
            RequestQueue cadastro = Volley.newRequestQueue(this);
            cadastro.add(request);

    }
    public void RemoverPoste(){
        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/removerPoste.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("erro")) {
                            Toast.makeText(getApplicationContext(),"Erro usuario ou senha"+response, Toast.LENGTH_SHORT).show();

                        }else{
                            String Array[] = new String[2];
                            Array = response.split(",");
                            String var_name_user = Array[0];
                            String id_login_user = Array[1];
                            Intent intentEnviar = new Intent(MapPoste.this, Tela2Menu_Activity.class);
                            intentEnviar.putExtra("var_name_user",var_name_user);
                            intentEnviar.putExtra("id_login_user",id_login_user);
                            startActivity(intentEnviar);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>  params = new HashMap<>();
                params.put("var_id_poste",var_id_poste);
                return  params;
            }
        };
        RequestQueue cadastro = Volley.newRequestQueue(this);
        cadastro.add(request);
    }
    public void exibirConfirmacao(){
        AlertDialog.Builder msgbox = new AlertDialog.Builder(this);
        msgbox.setTitle("Excluindo....");
        msgbox.setIcon(android.R.drawable.ic_menu_delete);
        msgbox.setMessage("Tem certeza que deseja cancelar o cadastro?");

        msgbox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RemoverPoste();
            }

        });

        msgbox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        msgbox.show();
    }

    public void onBackPressed(){
        exibirConfirmacao();

    }

}
