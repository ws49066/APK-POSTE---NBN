package com.nbntelecom.nbnpostemap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
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
import com.nbntelecom.nbnpostemap.POJO.Address;
import com.nbntelecom.nbnpostemap.POJO.Util;
import com.nbntelecom.nbnpostemap.POJO.ZipCodeListener;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nbntelecom.nbnpostemap.R.layout.color_spinner;

import static com.nbntelecom.nbnpostemap.R.layout.ponto_fixacao_layout;
import static com.nbntelecom.nbnpostemap.R.layout.spinner_dropdown_layout;

public class Cadastro_Activity extends  FragmentActivity implements OnMapReadyCallback  {


    //Variaveis do formulario
    TextView id_post_text;

    String NomeFotoTirada;

    String mCurrentPhotoPath;

    String var_id_poste;

    EditText et_numPoste1,municipio,bairro,rua,naproximado,cep;
    Spinner tipoposte,espposte,dimposte,iluminaposte,areaposte,chaveposte,transposte,suporteposte
            ,baixaposte,fixposte,rackposte,reservaposte,caixaatendimento,cameraposte,estado;


    Button btn_fotos,btn_parte1,btn_atributos,btn_endereco,add_field_reserva_tecnica,btn_caixa_atendimento;


    Button btn_abrirMap;
    LinearLayout layoutMap,layout_parte1,layout_atributo,layout_endereco,layout_fotos;

    BitmapFactory.Options option = new BitmapFactory.Options();


    List<String> Reserva_tecnica_list = new ArrayList<String>();
    List<String> Rack_lisy = new ArrayList<String>();
    List<String> Fixacao_poste_list = new ArrayList<String>();
    List<String> Suporte_cruzeta_list = new ArrayList<String>();
    List<String> Caixa_atendimento_list = new ArrayList<String>();
    Bitmap imagem ;
    List<String> ImagensStringList = new ArrayList<String>();
    List<Bitmap> BitmapListmg = new ArrayList<Bitmap>();
    LinearLayout parentLinearLayout,LinearLayoutSuporte,LinearLayoutPontoF,LinearLayoutRack,LinearLayoutResevaT, LinearLayoutCaixaAtendimento;
    int cont=0;


    Location localizacaoAtual;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    private  EditText etZipCode;
    private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        etZipCode = (EditText) findViewById(R.id.et_zip_code);
        etZipCode.addTextChangedListener( new ZipCodeListener(this));
        util = new Util(this,R.id.et_zip_code,R.id.et_street,R.id.et_complement,R.id.et_neighbor
        ,R.id.et_city,R.id.sp_state,R.id.et_number,R.id.tv_zip_code_search);

        Spinner spStates = (Spinner) findViewById(R.id.sp_state);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.states,android.R.layout.simple_spinner_item);
        spStates.setAdapter(adapter);


        id_post_text = findViewById(R.id.idtext);
        add_field_reserva_tecnica = findViewById(R.id.add_field_reserva_tecnica);
        btn_fotos = findViewById(R.id.btn_fotos);
        btn_parte1 = findViewById(R.id.btn_parte1);
        btn_atributos = findViewById(R.id.btn_atributos);
        btn_endereco = findViewById(R.id.btn_endereco);
        btn_caixa_atendimento = findViewById(R.id.btn_caixa_atendimento);
        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);
        LinearLayoutSuporte = (LinearLayout) findViewById(R.id.parent_linear_suporte_cruzeta);
        LinearLayoutPontoF = (LinearLayout) findViewById(R.id.parent_linear_ponto_fixacao);
        LinearLayoutRack = (LinearLayout) findViewById(R.id.parent_linear_rack);
        LinearLayoutResevaT = (LinearLayout) findViewById(R.id.parent_linear_reserva);
        LinearLayoutCaixaAtendimento = (LinearLayout) findViewById(R.id.parent_linear_caixa_atendimento);
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
                adicionar_fotos();
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



    // Funcao nome foto
    public File criarImagem() throws IOException{
        // criar arquivo de imagem
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        NomeFotoTirada = var_id_poste+"_"+timeStamp;
        File storagedir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(NomeFotoTirada,".jpg",storagedir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
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

        File photoFile = null;
        PositionBit = parentLinearLayout.indexOfChild((View) v.getParent());

        if(BitmapListmg.get(PositionBit)!=null){

        }else{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE );
            if(intent.resolveActivity(getPackageManager())!= null) {

                try {
                    photoFile = criarImagem();
                } catch (IOException ex) {
                    //Error occurred while creating the file
                }

                if (photoFile != null) {
                    Uri photoUri = FileProvider.getUriForFile(this, "com.nbntelecom.nbnpostemap", photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, PositionBit);

                }
            }

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);
        if( requestCode == Address.RESQUEST_ZIP_CODE_CODE
                && resultCode == RESULT_OK ){
            etZipCode.setText( intent.getStringExtra( Address.ZIP_CODE_KEY ) );
        }

        try{
            if (resultCode == RESULT_OK){
                File file = new File(mCurrentPhotoPath);
                imagem  = MediaStore.Images.Media.getBitmap(getContentResolver(),Uri.fromFile(file));
                if(imagem  != null) {
                    ImagensStringList.add(PositionBit, NomeFotoTirada);
                    Toast.makeText(getApplicationContext(), "Imagem salva com sucesso ! ", Toast.LENGTH_SHORT).show();
                    LinearLayout aux = (LinearLayout) parentLinearLayout.getChildAt(PositionBit);
                    TextView auxiliar = (TextView) aux.findViewById(R.id.number_edit_text);
                    auxiliar.setVisibility(View.VISIBLE);
                    auxiliar.setLayoutParams(new LinearLayout.LayoutParams(550, 150));
                    auxiliar.setText(NomeFotoTirada);
                    Button tira_foto = (Button) aux.findViewById(R.id.abrir_cam);
                    tira_foto.setVisibility(View.INVISIBLE);
                    tira_foto.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                    Button VerFoto = (Button) aux.findViewById(R.id.visualizar_img);
                    Button DeletarFoto = (Button) aux.findViewById(R.id.delete_button);
                    DeletarFoto.setVisibility(View.VISIBLE);
                    DeletarFoto.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
                    VerFoto.setVisibility(View.VISIBLE);
                    VerFoto.setLayoutParams(new LinearLayout.LayoutParams(100, 100));

                }

            }
        }catch (Exception error){
            error.printStackTrace();
        }

    }

    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, null);
        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView, parentLinearLayout.indexOfChild(v));
        BitmapListmg.add(null);
        Toast.makeText(getApplicationContext(),"Index: "+(parentLinearLayout.indexOfChild(v)),Toast.LENGTH_SHORT).show();
    }

    public void onvisualizar(View v){

        imagem = (Bitmap) BitmapListmg.get(parentLinearLayout.indexOfChild((View) v.getParent()));
        NomeFotoTirada = (String) ImagensStringList.get(parentLinearLayout.indexOfChild((View) v.getParent()));

            Intent intentEnviar = new Intent(Cadastro_Activity.this, VisualizarImagem.class);

            //intentEnviar.putExtra("bit_image",imagem);
            intentEnviar.putExtra("nomeFoto",NomeFotoTirada);
            startActivity(intentEnviar);



    }

    public void onDelete(View v) {


        Toast.makeText(getApplicationContext(),"REMOVENDO index: "+(parentLinearLayout.indexOfChild((View) v.getParent())+1),Toast.LENGTH_SHORT).show();
        BitmapListmg.remove(parentLinearLayout.indexOfChild((View) v.getParent()));
        ImagensStringList.remove(parentLinearLayout.indexOfChild((View ) v.getParent()));
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
            cameraposte = findViewById(R.id.cameraMonitoramento);


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
                    params.put("cameraposte",cameraposte.getSelectedItem().toString());
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

    // ---------------------------    ATRIBUTO -> CAIXA ATENDIMENTO ------------------------------------------
    public  void adicionar_caixa_atendimento(View v){

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final  View rowView = inflater.inflate(R.layout.caixa_atendimento_layout, null);
        // Add the new row before the add field button.
        LinearLayoutCaixaAtendimento.addView(rowView,LinearLayoutCaixaAtendimento.indexOfChild(v));

        //Toast.makeText(getApplicationContext(),"Index: "+(LinearLayoutResevaT.indexOfChild(v)),Toast.LENGTH_SHORT).show();
    }

    public void delete_caixa_atendimento(View v) {


        //Toast.makeText(getApplicationContext(),"REMOVENDO index: "+(LinearLayoutResevaT.indexOfChild((View) v.getParent())+1),Toast.LENGTH_SHORT).show();
        LinearLayoutCaixaAtendimento.removeView((View)v.getParent());


    }


    public void atributos(View v){

        int contador = 0 ;
        while (contador<(LinearLayoutResevaT.getChildCount()-1)){
            LinearLayout ReservaLinearLayout = (LinearLayout) LinearLayoutResevaT.getChildAt(contador);
            Spinner Cadastrasr_Reserva = ReservaLinearLayout.findViewById(R.id.reservapostes);
            Reserva_tecnica_list.add(Cadastrasr_Reserva.getSelectedItem().toString());
            contador++;
        }
        contador = 0 ;
        while (contador<(LinearLayoutCaixaAtendimento.getChildCount()-1)){
            LinearLayout RackLinearLayout = (LinearLayout) LinearLayoutCaixaAtendimento.getChildAt(contador);
            Spinner Cadastrar_Rack = RackLinearLayout.findViewById(R.id.caixa_atendimento);
            Caixa_atendimento_list.add(Cadastrar_Rack.getSelectedItem().toString());
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
                params.put("caixaatendimento",Caixa_atendimento_list.toString());
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
        estado = findViewById(R.id.sp_state);
        municipio = findViewById(R.id.et_city);
        bairro = findViewById(R.id.et_neighbor);
        rua = findViewById(R.id.et_street);
        naproximado =  findViewById(R.id.et_number);
        cep = findViewById(R.id.et_zip_code);


        areaposte = findViewById(R.id.areaposte);

        if(municipio.getText().length()==0 || bairro.getText().length() == 0 ||
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
                    params.put("estado",estado.getSelectedItem().toString());
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


    public String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }

    public void adicionar_fotos(){
        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/fotos.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            if (response.contains("1")){
                                Intent intentEnviar = new Intent(Cadastro_Activity.this, Tela2Menu_Activity.class);
                                Toast.makeText(getApplicationContext(),"Poste cadastrado com sucesso",Toast.LENGTH_LONG).show();
                                startActivity(intentEnviar);
                            }else{
                                Intent intentEnviar = new Intent(Cadastro_Activity.this, Tela2Menu_Activity.class);
                                Toast.makeText(getApplicationContext(),"Poste cadastrado com sucesso",Toast.LENGTH_LONG).show();
                                startActivity(intentEnviar);
                            }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent intentEnviar = new Intent(Cadastro_Activity.this, Tela2Menu_Activity.class);
                Toast.makeText(getApplicationContext(),"Poste cadastrado com sucesso",Toast.LENGTH_LONG).show();
                startActivity(intentEnviar);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>  params = new HashMap<>();
                params.put("var_id_poste",var_id_poste);
                params.put("imagem",imageToString(imagem));
                params.put("nomeFoto",ImagensStringList.toString() );
                return  params;
            }
        };
        RequestQueue cadastro = Volley.newRequestQueue(this);
        cadastro.add(request);
    }

    public  String getUriZipCode(){
        return "https://viacep.com.br/ws/"+etZipCode.getText()+"/json/";

    }
    public  void lockFields(boolean isToLock){
        util.lockFields(isToLock);
    }

    public void setDataViews(Address address){
        setField(R.id.et_street , address.getLogradouro());
        setField(R.id.et_complement , address.getComplemento());
        setField(R.id.et_neighbor , address.getBairro());
        setField(R.id.et_city , address.getLocalidade());
        setSpinner(R.id.sp_state, R.array.states, address.getUf());

    }
    private  void setField(int id, String data){
        ((EditText) findViewById((id))).setText(data);
    }

    private  void setSpinner(int id, int arrayId, String data){
        String[] itens = getResources().getStringArray(arrayId);

        for(int i =0; i<itens.length; i++){
            if(itens[i].endsWith("("+data+")")){
                ((Spinner) findViewById(id)).setSelection(i);
                return;
            }
        }
        ((Spinner) findViewById(id)).setSelection(0);

    }

    public void searchZipCode(View view){
        Intent intent = new Intent(this, ZipCodeSearchActivity.class);
        startActivityForResult( intent, Address.RESQUEST_ZIP_CODE_CODE );
    }
}
