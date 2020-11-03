package com.nbntelecom.nbnpostemap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nbntelecom.nbnpostemap.POJO.Address;
import com.nbntelecom.nbnpostemap.POJO.Util;
import com.nbntelecom.nbnpostemap.POJO.ZipCodeListener;
import com.nbntelecom.nbnpostemap.Poste.CruzetaItems;
import com.nbntelecom.nbnpostemap.Poste.Luz;
import com.nbntelecom.nbnpostemap.Poste.PontoFixacao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class fotosposte extends AppCompatActivity {
    Bitmap bitimagem ;
    List<String> ImagensStringList = new ArrayList<>(); // nome da foto tirada
    List<File> CaminhosFotos = new ArrayList<File>(); // caminho da foto na Raiz Celular
    Button btn_salvar_fotos,btn_cancelar;
    LinearLayout layoutgridimg;
    String var_id_poste,NomeFotoTirada,mCurrentPhotoPath,encoded_string,Nomefoto;

    int quantfotos = 0;
    private GridView imageGrid;
    private ArrayList<Bitmap> BitmapListmg;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotosposte);

        SharedPreferences preferences_id = getSharedPreferences("Arquivo_id",0);

        if (preferences_id.contains("id_poste")){
            var_id_poste = preferences_id.getString("id_poste",null);
        }
        imageGrid = (GridView) findViewById(R.id.gridview);
        BitmapListmg = new ArrayList<Bitmap>();
        layoutgridimg = (LinearLayout) findViewById(R.id.layout_gridmig);
        btn_cancelar = findViewById(R.id.btn_cancelar_cadastro);
        btn_salvar_fotos = findViewById(R.id.btn_salvar_fotos);
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exibirConfirmacao();
            }
        });

        //Permissão de CAMERA
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA
            }, 0);
        }
        
        imageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File caminho = CaminhosFotos.get(position);
                Intent intent = new Intent(fotosposte.this,FullView.class);
                intent.putExtra("caminho", caminho);
                startActivity(intent);
            }
        });

        btn_salvar_fotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                    progressDialog = new ProgressDialog(fotosposte.this);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    btn_salvar_fotos();
                }else {
                    Toast.makeText(getApplicationContext(),"Dispositivo não está conectado á Internet",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);
        try{
            if (resultCode == RESULT_OK){
                File file = new File(mCurrentPhotoPath);
                CaminhosFotos.add(file);
                bitimagem  = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));

                if(bitimagem  != null) {
                    progressDialog = new ProgressDialog(fotosposte.this);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    //====== FAZER O AJUSTE NA ORIENTAÇAO DA TELA ===//
                    ImagensStringList.add(NomeFotoTirada);
                    System.out.println("Nome da foto = "+NomeFotoTirada);
                    float graus = 90;
                    Matrix matrix = new Matrix();
                    matrix.setRotate(graus);
                    Bitmap newBitmapRotate = Bitmap.createBitmap(bitimagem, 0,0, bitimagem.getWidth(),bitimagem.getHeight(),matrix,true);
                    BitmapListmg.add(newBitmapRotate);
                    imageGrid.setAdapter(new ImageAdapter(this, this.BitmapListmg));
                    quantfotos++;
                    //===== FAZER O AJUSTE DA VIEW
                    if(quantfotos == 1){
                        layoutgridimg.setVisibility(View.VISIBLE);
                        layoutgridimg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,500));
                    }
                    if(quantfotos == 4){
                        layoutgridimg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,1000));
                    }

                    progressDialog.dismiss();
                }

            }
        }catch (Exception error){
            error.printStackTrace();
        }
    }
    
    //===============================================================================================
    private void Cadastro_endereco(){
        String ARQUIVO_ENDERECO_POSTE = "Arquivo_Endereco_Poste";
        SharedPreferences preferences_endereco = getSharedPreferences(ARQUIVO_ENDERECO_POSTE, 0);
        final String numeroPoste,estado,municipio,complemento,bairro,rua,numeroProximo,cep,areaposte;

        numeroPoste = preferences_endereco.getString("numeroPoste", null);
        estado = preferences_endereco.getString("estado",null);
        municipio = preferences_endereco.getString("municipio",null);
        complemento = preferences_endereco.getString("complemento", null);
        bairro = preferences_endereco.getString("bairro", null);
        rua = preferences_endereco.getString("rua", null);
        numeroProximo = preferences_endereco.getString("numberProximo", null);
        cep = preferences_endereco.getString("cep", null);
        areaposte = preferences_endereco.getString("areaposte",null);

        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/endereco.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("var_id_poste",var_id_poste);
                map.put("areaposte",areaposte);
                map.put("estado", estado);
                map.put("municipio",municipio);
                map.put("complemento",complemento);
                map.put("bairro",bairro);
                map.put("rua",rua);
                map.put("num_proximo",numeroProximo);
                map.put("cep",cep);
                map.put("numeroposte",numeroPoste);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    //============================================================================================
    private void Cadastrar_localizacao(){
        String ARQUIVO_GEOLOCALIZACAO  = "Arquivo_geolocalizacao";
        final String latitude,longitude;
        SharedPreferences preferences_geolocalizacao = getSharedPreferences(ARQUIVO_GEOLOCALIZACAO, 0);
        
        latitude = preferences_geolocalizacao.getString("latitude", null);
        longitude = preferences_geolocalizacao.getString("longitute", null);

        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/localizacao.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("var_id_poste",var_id_poste);
                map.put("latitude",latitude);
                map.put("longitude",longitude);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    
    //======================================================================================
    private void Cadastro_part1(){
        String DADOS_POSTE_HOMONEGEOS = "DADOS_POSTE_HOMONEGEOS";
        SharedPreferences preferences_poste1 = getSharedPreferences(DADOS_POSTE_HOMONEGEOS, 0);
        final String tipoposte,baixaposte,transposte,chaveposte,dimposte,esposte,cameraposte,isoladaposte;
        
        tipoposte = preferences_poste1.getString("tipoposte", null);
        baixaposte = preferences_poste1.getString("baixaposte",null);
        transposte = preferences_poste1.getString("transposte", null);
        chaveposte = preferences_poste1.getString("chaveposte", null);
        dimposte = preferences_poste1.getString("dimposte", null );
        esposte = preferences_poste1.getString("espposte", null);
        cameraposte = preferences_poste1.getString("cameraposte", null);
        isoladaposte = preferences_poste1.getString("isoladaposte", null);

        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/dados.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("var_id_poste",var_id_poste);
                map.put("tipoposte",tipoposte);
                map.put("dimposte",dimposte);
                map.put("espposte",esposte);
                map.put("baixaposte",baixaposte);
                map.put("chaveposte",chaveposte);
                map.put("transposte",transposte);
                map.put("cameraposte",cameraposte);
                map.put("isoladaposte",isoladaposte);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    //====================================================================================
    private void Cadastro_Atributos(){
        //Lista
        final List<String> Rack_lisy ;
        final List<String> Reserv_list ;
        final List<String> Caixa_list ;

        Gson gson = new Gson();

        SharedPreferences rack_list = getSharedPreferences("RackItems",MODE_PRIVATE);
        SharedPreferences reserva_tecnica_list = getSharedPreferences("ReservaItems",MODE_PRIVATE);
        SharedPreferences caixa_atendimento_list = getSharedPreferences("CaixaItems",MODE_PRIVATE);

        String JsonRack = rack_list.getString("Rack",null);
        String JsonReserva = reserva_tecnica_list.getString("Reserva",null);
        String JsonCaixa = caixa_atendimento_list.getString("Caixa",null);

        Type type = new TypeToken<ArrayList<String>>(){}.getType();

        Rack_lisy = gson.fromJson(JsonRack,type);
        Reserv_list = gson.fromJson(JsonReserva,type);
        Caixa_list = gson.fromJson(JsonCaixa,type);

        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/atributos_add.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("var_id_poste",var_id_poste);
                map.put("rackposte",Rack_lisy.toString());
                map.put("reservaposte",Reserv_list.toString());
                map.put("caixaatendimento",Caixa_list.toString());
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    //===============================================================================
    private void Cadastra_cruzeta(){
        ArrayList<CruzetaItems> cruzetaItensList ;
        SharedPreferences Cruzeta_data = getSharedPreferences("Cruzeta-itens", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = Cruzeta_data.getString("cruzeta", null);
        Type type = new TypeToken<ArrayList<CruzetaItems>>(){}.getType();
        cruzetaItensList = gson.fromJson(json, type);
        if (cruzetaItensList != null){
            for (CruzetaItems obj : cruzetaItensList){
                final String tipoCruzeta = obj.getTipodeCruzeta();
                final String aereaTipo = obj.getAereaTipo();
                final String redemedia = obj.getRedeMedia();
                StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/atributos/suporte_cruzeta.php",
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
                        map.put("tipocruzeta",tipoCruzeta);
                        map.put("aereatipo",aereaTipo);
                        map.put("redemedia",redemedia);
                        return map;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(request);
            }
        }
    }

    //==========================================================================================

    public void Cadastro_Ponto(){
        ArrayList<PontoFixacao> pontoFixacaosList;
        SharedPreferences ponto_fixacao_preference = getSharedPreferences("PontoFixacaoItem",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = ponto_fixacao_preference.getString("Ponto-fixacao",null);
        Type type = new TypeToken<ArrayList<PontoFixacao>>(){}.getType();
        pontoFixacaosList = gson.fromJson(json, type);
        if (pontoFixacaosList != null){
            for (PontoFixacao obj: pontoFixacaosList){
                final String pontofixacao = obj.getPonto();
                final String tipo = obj.getTipoPonto();
                final String bitola = obj.getSubtipo();

                StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/atributos/ponto_fixacao.php",
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
                        map.put("pontofixacao",pontofixacao);
                        map.put("tipo",tipo);
                        map.put("bitola",bitola);
                        return map;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(request);

            }
        }
    }

    //=========================================================================
    private void Cadastra_luz(){
        ArrayList<Luz> LuzList;
        SharedPreferences luz_preferences = getSharedPreferences("LuzItems", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = luz_preferences.getString("Luz",null);
        Type type = new TypeToken<ArrayList<Luz>>(){}.getType();
        LuzList = gson.fromJson(json,type);
        if(LuzList != null){
            for (Luz obj: LuzList){
                final String tipoilumina = obj.getTipoLuz();
                final String tipodono = obj.getTipoPropri();
                final String dono = obj.getProprietatio();
                final String tipolampada = obj.getTipoLuz();
                final String potencia = obj.getPotencia();
                StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/atributos/iluminacao.php",
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
                        map.put("tipoilumina",tipoilumina);
                        map.put("tipodono",tipodono);
                        map.put("dono",dono);
                        map.put("tipolampada",tipolampada);
                        map.put("potencia",potencia);
                        return map;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(request);

            }
        }
    }
    
    public void btn_salvar_fotos(){
            int QuantImagem = ImagensStringList.size();
            System.out.println("Imagem = "+ QuantImagem );
                    Cadastro_endereco();
                    Cadastrar_localizacao();
                    Cadastro_part1(); 
                    Cadastro_Atributos();
                    Cadastra_cruzeta();
                    Cadastro_Ponto();
                    Cadastra_luz();



        for(int i = 0; i< quantfotos; i++){
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                BitmapListmg.get(i).compress(Bitmap.CompressFormat.JPEG,20,byteArrayOutputStream);
                byte[] imgBytes = byteArrayOutputStream.toByteArray();
                encoded_string =  Base64.encodeToString(imgBytes,Base64.DEFAULT);
                Nomefoto = ImagensStringList.get(i);
                byteArrayOutputStream.close();

                StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/fotos.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("ok")){
                                    try {
                                        wait(1);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
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

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/finalizar.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("erro")) {
                            Toast.makeText(getApplicationContext(),"ERRO NA INSERÇÃO NO BANCO", Toast.LENGTH_SHORT). show();
                        }else{
                            Intent intentEnviar = new Intent(fotosposte.this, Tela2Menu_Activity.class);
                            Toast.makeText(getApplicationContext(),"Poste Cadastrado com sucesso !",Toast.LENGTH_SHORT).show();
                            startActivity(intentEnviar);
                            finish();
                        }
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
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

        public void tiraFoto(View v){

        File photoFile = null;
        if(quantfotos<3){
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
        progressDialog = new ProgressDialog(fotosposte.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/removerPoste.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("erro")) {

                        }else{
                            Intent intentEnviar = new Intent(fotosposte.this, Tela2Menu_Activity.class);
                            startActivity(intentEnviar);
                            finish();
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
