package com.nbntelecom.nbnpostemap;

import androidx.fragment.app.FragmentActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cadastro_Activity extends  FragmentActivity  {

    ArrayList<SharedPreferences> preferences = new ArrayList<>();


    //Variaveis do formulario
    String var_id_poste;
    Spinner tipoposte,espposte,dimposte,transposte,baixaposte;
    Button btn_parte1,btn_cancelar;
    LinearLayout layout_parte1,layout_fotos;

    LinearLayout parentLinearLayout,LinearLayoutSuporte,LinearLayoutPontoF,LinearLayoutRack,LinearLayoutResevaT, LinearLayoutCaixaAtendimento;


    private  final String DADOS_POSTE_HOMONEGEOS = "DADOS_POSTE_HOMONEGEOS";

    // Variaveis do RadioGroup
    RadioGroup chaveGroup,cameraGroup,isoladaGroup;
    RadioButton chaveButton,cameraButton,isoladaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        SharedPreferences preferences_id = getSharedPreferences("Arquivo_id",0);

        if (preferences_id.contains("id_poste")){
            var_id_poste = preferences_id.getString("id_poste",null);
        }



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
    }


    public void parte1() {

        int chaveRadioId, isoladoRadioId, cameraRadioId;
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

        String tipo = tipoposte.getSelectedItem().toString();
        String baixa = baixaposte.getSelectedItem().toString();
        String trans = transposte.getSelectedItem().toString();
        String chave = chaveButton.getText().toString();
        String dimpo = dimposte.getSelectedItem().toString();
        String esppo = espposte.getSelectedItem().toString();
        String camera = cameraButton.getText().toString();
        String isola = isoladaButton.getText().toString();

        // conectando com o banco
        System.out.println("TIPOS DO POSTE :" + tipo +" - "+ baixa +" - "+ trans +" - "+ chave +" - "+ dimpo +" - "+ esppo +" - "+ camera +" - "+ isola);
        SharedPreferences preferences_poste = getSharedPreferences(DADOS_POSTE_HOMONEGEOS, 0);
        SharedPreferences.Editor editor = preferences_poste.edit();
        editor.putString("tipoposte", tipo);
        editor.putString("baixaposte", baixa);
        editor.putString("transposte", trans);
        editor.putString("chaveposte", chave);
        editor.putString("dimposte", dimpo);
        editor.putString("espposte", esppo);
        editor.putString("cameraposte", camera);
        editor.putString("isoladaposte", isola);
        editor.apply();
        
        Intent intent = new Intent(Cadastro_Activity.this, Atributos_poste.class);
        startActivity(intent);
        finish();
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

                        }else{
                            Intent intentEnviar = new Intent(Cadastro_Activity.this, Tela2Menu_Activity.class);
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
