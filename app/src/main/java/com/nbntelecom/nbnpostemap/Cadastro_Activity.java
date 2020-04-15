package com.nbntelecom.nbnpostemap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nbntelecom.nbnpostemap.R.layout.color_spinner;

import static com.nbntelecom.nbnpostemap.R.layout.ponto_fixacao_layout;
import static com.nbntelecom.nbnpostemap.R.layout.spinner_dropdown_layout;

public class Cadastro_Activity extends  FragmentActivity  {


    private long backPressedTime;

    //Variaveis do formulario



    String var_id_poste;

    Spinner tipoposte,espposte,dimposte,transposte,baixaposte;


    Button btn_parte1,btn_cancelar;


    LinearLayout layout_parte1,layout_fotos;

    //BitmapFactory.Options option = new BitmapFactory.Options();

    LinearLayout parentLinearLayout,LinearLayoutSuporte,LinearLayoutPontoF,LinearLayoutRack,LinearLayoutResevaT, LinearLayoutCaixaAtendimento;

    String var_name_user,id_login_user;

    List<String> TipoposteText = new ArrayList<String>();
    List<String> AereaSuporte = new ArrayList<String>();
    List<String> RedemediaPoste = new ArrayList<String>();
    List<String> PontoFixacao = new ArrayList<String>();
    List<String> TipodeCabo = new ArrayList<String>();
    List<String> Bitola = new ArrayList<String>();
    List<String> tipoluz = new ArrayList<String>();
    List<String> prop = new ArrayList<String>();
    List<String> t_prop = new ArrayList<String>();
    List<String> tipolamp = new ArrayList<String>();
    List<String> pot = new ArrayList<String>();

    int cont=0;

    private static final int REQUEST_CODE = 101;


    // Variaveis do RadioGroup
    RadioGroup chaveGroup,cameraGroup,isoladaGroup;
    RadioButton chaveButton,cameraButton,isoladaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);


        //Instancias dos RadioGruop e RadioButton
        chaveGroup = findViewById(R.id.chaveGroup);
        cameraGroup = findViewById(R.id.cameraGroup);
        isoladaGroup = findViewById(R.id.isoladaGroup);
        btn_cancelar = findViewById(R.id.btn_cancelar_cadastro);


        //hide action bar



        btn_parte1 = findViewById(R.id.btn_parte1);
        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);
        LinearLayoutSuporte = (LinearLayout) findViewById(R.id.parent_linear_suporte_cruzeta);
        LinearLayoutPontoF = (LinearLayout) findViewById(R.id.parent_linear_ponto_fixacao);
        LinearLayoutRack = (LinearLayout) findViewById(R.id.parent_linear_rack);
        LinearLayoutResevaT = (LinearLayout) findViewById(R.id.parent_linear_reserva);
        LinearLayoutCaixaAtendimento = (LinearLayout) findViewById(R.id.parent_linear_caixa_atendimento);
        layout_parte1 = (LinearLayout) findViewById(R.id.layout_part1);
        layout_fotos = (LinearLayout) findViewById(R.id.layout_fotos);

        // VARIAVEL ID POSTE PASSADA PARA ESSA PAGINA
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            var_id_poste = (String) extras.get("var_id_poste");

        }


        btn_parte1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parte1();
            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exibirConfirmacao();
            }
        });

       /* Spinner AdapterTipoPoste = (Spinner) findViewById(R.id.tipoposte);
        ArrayAdapter<String> adapterTipoposte = new ArrayAdapter<String>(Cadastro_Activity.this, color_spinner,
                getResources().getStringArray(R.array.tipoposte));
        AdapterTipoPoste.setAdapter(adapterTipoposte);
       */

    }


    public void parte1(){


        int chaveRadioId,iluminacaoRadioId,isoladoRadioId,cameraRadioId;
        chaveRadioId = chaveGroup.getCheckedRadioButtonId();
        isoladoRadioId = isoladaGroup.getCheckedRadioButtonId();
        cameraRadioId = cameraGroup.getCheckedRadioButtonId();

        chaveButton = findViewById(chaveRadioId);
        isoladaButton = findViewById(isoladoRadioId);
        cameraButton = findViewById(cameraRadioId);


            tipoposte = findViewById(R.id.tipoposte);
            espposte = findViewById(R.id.espposte);
            dimposte = findViewById(R.id.dimposte);
            transposte = findViewById(R.id.transposte);
            baixaposte = findViewById(R.id.baixaposte);


            // conectando com o banco

            StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/dados.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("1")) {
                                Intent intentEnviar = new Intent(Cadastro_Activity.this, Atributos_poste.class);
                                intentEnviar.putExtra("var_id_poste",var_id_poste);
                                intentEnviar.putExtra("tipocruzeta",(Serializable) TipoposteText );
                                intentEnviar.putExtra("aereatipo",(Serializable) AereaSuporte);
                                intentEnviar.putExtra("redemedia",(Serializable) RedemediaPoste );
                                intentEnviar.putExtra("pontofixacao",(Serializable) PontoFixacao );
                                intentEnviar.putExtra("tipodecabo",(Serializable) TipodeCabo);
                                intentEnviar.putExtra("bitola",(Serializable) Bitola );
                                intentEnviar.putExtra("tipoiluminacao", (Serializable) tipoluz);
                                intentEnviar.putExtra("proprietario",(Serializable) prop );
                                intentEnviar.putExtra("t_proprietario", (Serializable) t_prop );
                                intentEnviar.putExtra("tipolampada", (Serializable) tipolamp );
                                intentEnviar.putExtra("potencia", (Serializable) pot );
                                startActivity(intentEnviar);
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
                    params.put("chaveposte",chaveButton.getText().toString());
                    params.put("dimposte",dimposte.getSelectedItem().toString());
                    params.put("espposte",espposte.getSelectedItem().toString());
                    params.put("tipoposte",tipoposte.getSelectedItem().toString());
                    params.put("cameraposte",cameraButton.getText().toString());
                    params.put("isoladaposte",isoladaButton.getText().toString());
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
                            Intent intentEnviar = new Intent(Cadastro_Activity.this, Tela2Menu_Activity.class);
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
