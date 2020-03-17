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
import android.graphics.Matrix;
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

public class endereco_Activity extends AppCompatActivity {


    private long backPressedTime;
    //BitmapFactory.Options option = new BitmapFactory.Options();

    TextView id_post_text;
    EditText et_numPoste1;


    Button btn_salvar_endereco,btn_cancelar;


    String var_id_poste;
    EditText municipio,bairro,rua,naproximado,cep,et_complement;

    Spinner areaposte,estado;



    private  EditText etZipCode;
    private Util util;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endereco_);

        //VARIAVEIS INSTANSIDAS COM OS IDS

        id_post_text = findViewById(R.id.idtext);


        btn_salvar_endereco = findViewById(R.id.btn_salvar_endereco);

        btn_cancelar = findViewById(R.id.btn_cancelar_cadastro);

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exibirConfirmacao();
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
            id_post_text.setText("ID: "+var_id_poste);
        }

        btn_salvar_endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endereco();
            }
        });


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
        et_numPoste1 = findViewById(R.id.et_numPoste);
        estado = findViewById(R.id.sp_state);
        municipio = findViewById(R.id.et_city);
        et_complement = findViewById(R.id.et_complement);
        bairro = findViewById(R.id.et_neighbor);
        rua = findViewById(R.id.et_street);
        naproximado =  findViewById(R.id.et_number);
        cep = findViewById(R.id.et_zip_code);
        areaposte = findViewById(R.id.areaposte);

        if(et_numPoste1.getText().length()==0 || municipio.getText().length()==0 || bairro.getText().length() == 0 ||
                rua.getText().length() == 0 || naproximado.getText().length() == 0 || cep.getText().length() == 0){

            Toast.makeText(getApplicationContext(),"Preencha todos os campos",Toast.LENGTH_SHORT).show();

        }else{
            StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/endereco.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("erro")) {
                                Toast.makeText(getApplicationContext(),"ERRO NA INSERÇÃO NO BANCO", Toast.LENGTH_SHORT). show();

                            }else{

                                Intent intentEnviar = new Intent(endereco_Activity.this, MapPoste.class);
                                intentEnviar.putExtra("var_id_poste",var_id_poste);
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
                    params.put("numeroposte",et_numPoste1.getText().toString());
                    params.put("areaposte",areaposte.getSelectedItem().toString());
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
