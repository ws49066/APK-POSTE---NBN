package com.nbntelecom.nbnpostemap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class endereco_Activity extends FragmentActivity implements OnMapReadyCallback {


    private long backPressedTime;
    //BitmapFactory.Options option = new BitmapFactory.Options();

    Bitmap bitimagem ;
    ArrayList<String> imageToString = new ArrayList<String>();
    List<String> ImagensStringList = new ArrayList<String>();
    List<String> ImagensEncodedString = new ArrayList<>();

    List<File> CaminhosFotos = new ArrayList<File>();
    Button btn_salvar_endereco,mTypeBtn,mTypeNormal;
    GoogleMap mMap;
    LinearLayout layoutMap,layoutgridimg;

    TextView Geolocalizacao;

    String var_id_poste,NomeFotoTirada,mCurrentPhotoPath,encoded_string,Nomefoto;
    EditText municipio,bairro,rua,naproximado,cep,et_complement;

    Spinner areaposte,estado;
    int quantfotos = 0;



    Location localizacaoAtual;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private  EditText etZipCode;
    private Util util;
    private GridView imageGrid;
    private ArrayList<Bitmap> BitmapListmg;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endereco_);

        //VARIAVEIS INSTANSIDAS COM OS IDS
        imageGrid = (GridView) findViewById(R.id.gridview);
        BitmapListmg = new ArrayList<Bitmap>();

        btn_salvar_endereco = findViewById(R.id.btn_salvar_endereco);
        Geolocalizacao = findViewById(R.id.Geolocalizacao);
        mTypeBtn = findViewById(R.id.btnSatelite);
        mTypeNormal = findViewById(R.id.btnNormal);
        layoutgridimg = (LinearLayout) findViewById(R.id.layout_gridmig);




        //Permissão de CAMERA
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA
            }, 0);
        }


        mTypeNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        mTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });

        imageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File caminho_img = CaminhosFotos.get(position);
                Intent intent = new Intent(endereco_Activity.this,FullView.class);
                intent.putExtra("caminho_img", caminho_img);
                startActivity(intent);
            }
        });

        etZipCode = (EditText) findViewById(R.id.et_zip_code);
        etZipCode.addTextChangedListener( new ZipCodeListener(this));
        util = new Util(this,R.id.et_zip_code,R.id.et_street,R.id.et_complement,R.id.et_neighbor,R.id.et_city,R.id.sp_state,R.id.et_number,R.id.tv_zip_code_search);

        Spinner spStates = (Spinner) findViewById(R.id.sp_state);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.states,android.R.layout.simple_spinner_item);
        spStates.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            var_id_poste = (String) extras.get("var_id_poste");
        }

        btn_salvar_endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endereco();
            }
        });

        layoutMap = (LinearLayout) findViewById(R.id.layoutMap);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        fetchLastLocation();
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
                    supportMapFragment.getMapAsync(endereco_Activity.this);
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

    public void endereco(){



        estado = findViewById(R.id.sp_state);
        municipio = findViewById(R.id.et_city);
        et_complement = findViewById(R.id.et_complement);
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
            for(int i = 0; i < BitmapListmg.size();i++){
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                BitmapListmg.get(i).compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                byte[] imgBytes = byteArrayOutputStream.toByteArray();
                encoded_string =  Base64.encodeToString(imgBytes,Base64.DEFAULT);
                Nomefoto = ImagensStringList.get(i);
                makeRequest();
            }

            StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/endereco.php",
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
                                Intent intentEnviar = new Intent(endereco_Activity.this, Tela2Menu_Activity.class);
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
                    params.put("areaposte",areaposte.getSelectedItem().toString());
                    params.put("localizacao",(localizacaoAtual.getLatitude()+","+ localizacaoAtual.getLongitude()).toString());
                    params.put("estado",estado.getSelectedItem().toString());
                    params.put("municipio",municipio.getText().toString());
                    params.put("complemento",et_complement.getText().toString());
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
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);
        if( requestCode == Address.RESQUEST_ZIP_CODE_CODE
                && resultCode == RESULT_OK ){
            etZipCode.setText( intent.getStringExtra( Address.ZIP_CODE_KEY ) );
        }
        try{
            if (resultCode == RESULT_OK){
                File file = new File(mCurrentPhotoPath);
                CaminhosFotos.add(file);
                bitimagem  = MediaStore.Images.Media.getBitmap(getContentResolver(),Uri.fromFile(file));
                if(bitimagem  != null) {
                    ImagensStringList.add(NomeFotoTirada);
                    BitmapListmg.add(bitimagem);
                    imageGrid.setAdapter(new ImageAdapter(this, this.BitmapListmg));
                    quantfotos++;
                    if(quantfotos == 1){
                        layoutgridimg.setVisibility(View.VISIBLE);
                        layoutgridimg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,500));
                    }
                    if(quantfotos == 4){
                        layoutgridimg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,1000));
                    }

                }
            }
        }catch (Exception error){
            error.printStackTrace();
        }
    }


    private void makeRequest() {
        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/fotos.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("var_id_poste",var_id_poste);
                map.put("encoded_string",encoded_string);
                map.put("image_name",Nomefoto);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }



    // Abrir CAMERA
    public void tiraFoto(View v){

        File photoFile = null;
        if(quantfotos<6){
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
                    startActivityForResult(intent, 10);
                }
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Limite de fotos atingido",Toast.LENGTH_SHORT).show();
        }

    }


    //


    // Função que vai retornar o nome da foto com dataa e hora,junto com o diretorio pra salvar a imagem
    public File criarImagem() throws IOException {
        // criar arquivo de imagem
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        NomeFotoTirada = var_id_poste+"_"+timeStamp;
        File storagedir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(NomeFotoTirada,".jpg",storagedir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
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
                            Intent intentEnviar = new Intent(endereco_Activity.this, Tela2Menu_Activity.class);
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


}
