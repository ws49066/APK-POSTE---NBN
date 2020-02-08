package com.nbntelecom.nbnpostemap;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nbntelecom.nbnpostemap.R.layout.color_spinner;

import static com.nbntelecom.nbnpostemap.R.layout.ponto_fixacao_layout;
import static com.nbntelecom.nbnpostemap.R.layout.spinner_dropdown_layout;

public class Cadastro_Activity extends  FragmentActivity implements OnMapReadyCallback  {


    //Variaveis do formulario
    TextView id_post_text;

    String var_id_poste;

    EditText et_numPoste1,estado,municipio,bairro,rua,naproximado,cep;
    Spinner tipoposte,espposte,dimposte,iluminaposte,areaposte,chaveposte,transposte,suporteposte
            ,baixaposte,fixposte,rackposte,reservaposte;


    Button btn_fotos,btn_parte1,btn_atributos,btn_endereco,add_field_reserva_tecnica;


    Button btn_abrirMap;
    LinearLayout layoutMap,layout_parte1,layout_atributo,layout_endereco,layout_fotos;

    BitmapFactory.Options option = new BitmapFactory.Options();
    Bitmap imagem ;

    List<String> Reserva_tecnica_list = new ArrayList<String>();
    List<String> Rack_lisy = new ArrayList<String>();
    List<String> Fixacao_poste_list = new ArrayList<String>();
    List<String> Suporte_cruzeta_list = new ArrayList<String>();

    List<Bitmap> bitimagens = new ArrayList<Bitmap>();
    LinearLayout parentLinearLayout,LinearLayoutSuporte,LinearLayoutPontoF,LinearLayoutRack,LinearLayoutResevaT;
    int cont=0;


    Location localizacaoAtual;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);


        id_post_text = findViewById(R.id.idtext);
        add_field_reserva_tecnica = findViewById(R.id.add_field_reserva_tecnica);
        btn_fotos = findViewById(R.id.btn_fotos);
        btn_parte1 = findViewById(R.id.btn_parte1);
        btn_atributos = findViewById(R.id.btn_atributos);
        btn_endereco = findViewById(R.id.btn_endereco);
        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);
        LinearLayoutSuporte = (LinearLayout) findViewById(R.id.parent_linear_suporte_cruzeta);
        LinearLayoutPontoF = (LinearLayout) findViewById(R.id.parent_linear_ponto_fixacao);
        LinearLayoutRack = (LinearLayout) findViewById(R.id.parent_linear_rack);
        LinearLayoutResevaT = (LinearLayout) findViewById(R.id.parent_linear_reserva);

        layout_atributo = (LinearLayout) findViewById(R.id.layout_atributos);
        layout_endereco = (LinearLayout) findViewById(R.id.layout_endereco);
        layout_parte1 = (LinearLayout) findViewById(R.id.layout_part1);
        layout_fotos = (LinearLayout) findViewById(R.id.layout_fotos);

        // VARIAVEL ID POSTE PASSADA PARA ESSA PAGINA
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            var_id_poste = (String) extras.get("var_id_poste");
            id_post_text.setText("ID: "+var_id_poste);
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA
            }, 0);
        }


        btn_fotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Poste cadastrado com sucesso !!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Cadastro_Activity.this, Tela2Menu_Activity.class));
            }
        });


        btn_parte1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parte1();
            }
        });



        btn_atributos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atributos(v);
            }
        });

        btn_endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endereco();
            }
        });






        layoutMap = (LinearLayout) findViewById(R.id.layoutMap);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


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
    int PositionBit ;

    public void tiraFoto(View v){
        PositionBit = parentLinearLayout.indexOfChild((View) v.getParent());
        if (bitimagens.get(PositionBit) != null){
            Toast.makeText(getApplicationContext(), "Já existe imagem capturada" + parentLinearLayout.indexOfChild((View) v.getParent()), Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE );
            startActivityForResult(intent, parentLinearLayout.indexOfChild((View) v.getParent()));
        }

    }



    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            imagem  = (Bitmap) extras.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imagem.compress(Bitmap.CompressFormat.JPEG,100,stream);
            bitimagens.set(PositionBit,imagem);
            Toast.makeText(getApplicationContext(),"Imagem Salva ",Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode,resultCode,data);
    }


    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, null);
        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView, parentLinearLayout.indexOfChild(v));
        bitimagens.add(null);
        Toast.makeText(getApplicationContext(),"Index: "+(parentLinearLayout.indexOfChild(v)),Toast.LENGTH_SHORT).show();
    }
    public void onvisualizar(View v){
        imagem = (Bitmap) bitimagens.get(parentLinearLayout.indexOfChild((View) v.getParent()));
        if(imagem == null){
            Toast.makeText(getApplicationContext(),"Erro: Sem imagem",Toast.LENGTH_SHORT).show();
        }else{
            Intent intentEnviar = new Intent(Cadastro_Activity.this, VisualizarImagem.class);
            intentEnviar.putExtra("bit_image",imagem);
            startActivity(intentEnviar);
        }
    }

    public void onDelete(View v) {


        Toast.makeText(getApplicationContext(),"REMOVENDO index: "+(parentLinearLayout.indexOfChild((View) v.getParent())+1),Toast.LENGTH_SHORT).show();
        bitimagens.remove(parentLinearLayout.indexOfChild((View) v.getParent()));
        parentLinearLayout.removeView((View) v.getParent());

    }

    public void parte1(){

        et_numPoste1 = findViewById(R.id.et_numPoste);

        if(et_numPoste1.getText().length() == 0){
            Toast.makeText(getApplicationContext(),"Informe o número do poste",Toast.LENGTH_SHORT).show();

        }else{
            tipoposte = findViewById(R.id.tipoposte);
            espposte = findViewById(R.id.espposte);
            dimposte = findViewById(R.id.dimposte);
            iluminaposte = findViewById(R.id.iluminaposte);
            chaveposte = findViewById(R.id.chaveposte);
            transposte = findViewById(R.id.transposte);
            baixaposte = findViewById(R.id.baixaposte);


            // conectando com o banco

            StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/dados.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("1")) {
                                layout_atributo.setVisibility(View.VISIBLE);
                                layout_atributo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                layout_parte1.setVisibility(View.INVISIBLE);
                                layout_parte1.setLayoutParams(new LinearLayout.LayoutParams(0,0));

                            }else{
                                Toast.makeText(getApplicationContext(),"ERRO NA INSERÇÃO NO BANCO", Toast.LENGTH_SHORT).show();
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
                    params.put("baixaposte",baixaposte.getSelectedItem().toString());
                    params.put("transposte",transposte.getSelectedItem().toString());
                    params.put("chaveposte",chaveposte.getSelectedItem().toString());
                    params.put("iluminaposte",iluminaposte.getSelectedItem().toString());
                    params.put("dimposte",dimposte.getSelectedItem().toString());
                    params.put("espposte",espposte.getSelectedItem().toString());
                    params.put("numeroposte",et_numPoste1.getText().toString());
                    params.put("tipoposte",tipoposte.getSelectedItem().toString());
                    return  params;
                }
            };
            RequestQueue cadastro = Volley.newRequestQueue(this);
            cadastro.add(request);

        }
    }

    // --------------------------------- adicionais de atributos---------------------------------------------------



    public void onAddSuportCruzeta(View v) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.suporte_cruzeta_layout, null);
        // Add the new row before the add field button.
        LinearLayoutSuporte.addView(rowView, LinearLayoutSuporte.indexOfChild(v));


        //Toast.makeText(getApplicationContext(),"Index: "+(LinearLayoutSuporte.indexOfChild(v)),Toast.LENGTH_SHORT).show();

    }


    public void delete_cruzeta(View v) {

        //Toast.makeText(getApplicationContext(),"REMOVENDO index: "+(LinearLayoutSuporte.indexOfChild(v)),Toast.LENGTH_SHORT).show();
        LinearLayoutSuporte.removeView((View) v.getParent());

    }



    //---------------------------------PONTO DE FIXACAO --------------------------------------------------------
    public void adicionar_ponto_fixacao(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.ponto_fixacao_layout, null);
        // Add the new row before the add field button.
        LinearLayoutPontoF.addView(rowView, LinearLayoutPontoF.indexOfChild(v));


        //Toast.makeText(getApplicationContext(),"Index: "+(LinearLayoutPontoF.indexOfChild(v)),Toast.LENGTH_SHORT).show();
    }

    public void delete_ponto(View v) {

        //Toast.makeText(getApplicationContext(),"REMOVENDO index: "+(LinearLayoutPontoF.indexOfChild((View) v.getParent())),Toast.LENGTH_SHORT).show();
        LinearLayoutPontoF.removeView((View) v.getParent());

    }

    //---------------------------------- RACK ADIÇÃO/REMOÇAO SPINNER ----------------------------------

    public void adicionar_rack(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View rowView = inflater.inflate(R.layout.rack_layout, null);
        // Add the new row before the add field button.
        LinearLayoutRack.addView(rowView, LinearLayoutRack.indexOfChild(v));


        //Toast.makeText(getApplicationContext(),"Index: "+(LinearLayoutRack.indexOfChild(v)),Toast.LENGTH_SHORT).show();
    }

    public void delete_rack(View v) {

        //Toast.makeText(getApplicationContext(),"REMOVENDO index: "+(LinearLayoutRack.indexOfChild((View) v.getParent())+1),Toast.LENGTH_SHORT).show();
        LinearLayoutRack.removeView((View) v.getParent());

    }


    // ---------------------------    ATRIBUTO -> RESERVAR TÉCNICA ------------------------------------------
    public  void adicionar_reservatecnica(View v){

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final  View rowView = inflater.inflate(R.layout.reserva_tecnica_layout, null);
                // Add the new row before the add field button.
        LinearLayoutResevaT.addView(rowView,LinearLayoutResevaT.indexOfChild(v));
        
        //Toast.makeText(getApplicationContext(),"Index: "+(LinearLayoutResevaT.indexOfChild(v)),Toast.LENGTH_SHORT).show();
    }

    public void delete_reserva(View v) {


        //Toast.makeText(getApplicationContext(),"REMOVENDO index: "+(LinearLayoutResevaT.indexOfChild((View) v.getParent())+1),Toast.LENGTH_SHORT).show();
        LinearLayoutResevaT.removeView((View)v.getParent());


    }


    public void atributos(View v){

        int contador = 0 ;
        while (contador<(LinearLayoutResevaT.getChildCount()-1)){
            LinearLayout ReservaLinearLayout = (LinearLayout) LinearLayoutResevaT.getChildAt(contador);
            Spinner Cadastrasr_Reserva = ReservaLinearLayout.findViewById(R.id.reservapostes);
            Reserva_tecnica_list.add(Cadastrasr_Reserva.getSelectedItem().toString());
            contador++;
        }
        contador = 0;
        while(contador<(LinearLayoutRack.getChildCount()-1)){
            LinearLayout RackLinearLayout = (LinearLayout) LinearLayoutRack.getChildAt(contador);
            Spinner Cadastrar_Rack = RackLinearLayout.findViewById(R.id.rackposte_drop);
            Rack_lisy.add(Cadastrar_Rack.getSelectedItem().toString());
            contador++;
        }
        contador = 0;
        while(contador<(LinearLayoutPontoF.getChildCount()-1)){
            LinearLayout RackLinearLayout = (LinearLayout) LinearLayoutPontoF.getChildAt(contador);
            Spinner Cadastrar_Rack = RackLinearLayout.findViewById(R.id.fixposte_drop);
            Fixacao_poste_list.add(Cadastrar_Rack.getSelectedItem().toString());
            contador++;
        }
        contador = 0;
        while(contador<(LinearLayoutSuporte.getChildCount()-1)){
            LinearLayout RackLinearLayout = (LinearLayout) LinearLayoutSuporte.getChildAt(contador);
            Spinner Cadastrar_Rack = RackLinearLayout.findViewById(R.id.Spinner_cruzeta_drop);
            Suporte_cruzeta_list.add(Cadastrar_Rack.getSelectedItem().toString());
            contador++;
        }

        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/atributos_add.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1")) {
                            layout_endereco.setVisibility(View.VISIBLE);
                            layout_endereco.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            layout_atributo.setVisibility(View.INVISIBLE);
                            layout_atributo.setLayoutParams(new LinearLayout.LayoutParams(0,0));
                            layoutMap.setVisibility(View.VISIBLE);
                            fetchLastLocation();

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
                params.put("reservaposte",Reserva_tecnica_list.toString());
                params.put("rackposte",Rack_lisy.toString());
                params.put("fixposte",Fixacao_poste_list.toString());
                params.put("suporteposte",Suporte_cruzeta_list.toString());
                return  params;
            }
        };
        RequestQueue cadastro = Volley.newRequestQueue(this);
        cadastro.add(request);



    }

    public void endereco(){
        estado = findViewById(R.id.estado);
        municipio = findViewById(R.id.municipio);
        bairro = findViewById(R.id.bairro);
        rua = findViewById(R.id.rua);
        naproximado =  findViewById(R.id.naproximado);
        cep = findViewById(R.id.cep);
        areaposte = findViewById(R.id.areaposte);

        if(estado.getText().length() == 0 || municipio.getText().length()==0 || bairro.getText().length() == 0 ||
                rua.getText().length() == 0 || naproximado.getText().length() == 0 || cep.getText().length() == 0){

            Toast.makeText(getApplicationContext(),"Preencha todos os campos",Toast.LENGTH_SHORT).show();

        }
        else{

            StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/endereco.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("1")) {
                                layout_fotos.setVisibility(View.VISIBLE);
                                layout_fotos.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                layout_endereco.setVisibility(View.INVISIBLE);
                                layout_endereco.setLayoutParams(new LinearLayout.LayoutParams(0,0));

                            }else{
                                Toast.makeText(getApplicationContext(),"ERRO NA INSERÇÃO NO BANCO", Toast.LENGTH_SHORT).show();
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
                    params.put("areaposte",areaposte.getSelectedItem().toString());
                    params.put("localizacao",(localizacaoAtual.getLatitude()+""+ localizacaoAtual.getLongitude()).toString());
                    params.put("numeroposte",et_numPoste1.getText().toString());
                    params.put("estado",estado.getText().toString());
                    params.put("municipio",municipio.getText().toString());
                    params.put("bairro",bairro.getText().toString());
                    params.put("rua",rua.getText().toString());
                    params.put("num_proximo",naproximado.getText().toString());
                    params.put("cep",cep.getText().toString());
                    return  params;
                }
            };
            RequestQueue cadastro = Volley.newRequestQueue(this);
            cadastro.add(request);

        }
    }


    public void adicionar_fotos(){
        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/cadastro.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1")) {
                            Toast.makeText(getApplicationContext(),"Poste cadastrado com sucesso !",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Cadastro_Activity.this, Tela2Menu_Activity.class));

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
                return  params;
            }
        };
        RequestQueue cadastro = Volley.newRequestQueue(this);
        cadastro.add(request);
    }
}
