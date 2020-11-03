package com.nbntelecom.nbnpostemap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.nbntelecom.nbnpostemap.Poste.CruzetaItems;
import com.nbntelecom.nbnpostemap.Poste.PontoFixacao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ponto_fixacao_add extends AppCompatActivity {
    Button btn_cadastrar_ponto,btn_cancelar_ponto,btn_download;
    LinearLayout dinamicoLayout,LayoutTipoCabo;
    Spinner tipopontofixacao,pontotipo,bitolaponto;
    ArrayList<PontoFixacao> pontoFixacaosList;
    ArrayList<String> provedor ;
    ArrayList<String> cabo ;
    ArrayList<String> subtipo = new ArrayList<String>();
    private  final String ARQUIVO_PROVEDORES = "Arquivo_Provedores";
    private  final String ARQUIVO_Cabos = "Arquivo_Cabos";

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ponto_fixacao_add);

        SharedPreferences provedores = getSharedPreferences(ARQUIVO_PROVEDORES,0);
        Set<String> set =  provedores.getStringSet("provedores", null);
        provedor = new ArrayList<String>(set);

        SharedPreferences Cabos = getSharedPreferences(ARQUIVO_Cabos,0);
        Set<String> setCabos =  Cabos.getStringSet("cabos", null);
        cabo = new ArrayList<String>(setCabos);

        tipopontofixacao = findViewById(R.id.pontofixacao);
        pontotipo = findViewById(R.id.tipoponto);
        bitolaponto = findViewById(R.id.bitola);

        ArrayAdapter<String> adapterProvedor = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_text,provedor);
        tipopontofixacao.setAdapter(adapterProvedor);


        ArrayAdapter<String> adapterCabo = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_text,cabo);
        pontotipo.setAdapter(adapterCabo);

        btn_cadastrar_ponto = findViewById(R.id.btn_cadastrar_ponto);
        btn_cancelar_ponto = findViewById(R.id.btn_cancelar_ponto);
        btn_download = findViewById(R.id.getAnteriores);

        dinamicoLayout = findViewById(R.id.LayoutDinamico);
        LayoutTipoCabo = findViewById(R.id.LayoutTipoCabo);

        loadData();

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(ponto_fixacao_add.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                getPontoAnteriores();
            }
        });



        tipopontofixacao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Object item = parent.getItemAtPosition(position);
                if (position != 0){
                    LayoutTipoCabo.setVisibility(View.VISIBLE);
                }else{
                    LayoutTipoCabo.setVisibility(View.INVISIBLE);
                    dinamicoLayout.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        pontotipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Object item = parent.getItemAtPosition(position);
                if (position != 0){
                    getSubtipo();
                }else{
                    dinamicoLayout.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        btn_cadastrar_ponto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(ponto_fixacao_add.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                CadastrarPonto();
            }
        });

        btn_cancelar_ponto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(ponto_fixacao_add.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                Intent intent = new Intent(ponto_fixacao_add.this,Atributos_poste.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadData() {
        SharedPreferences ponto_fixacao_preference = getSharedPreferences("PontoFixacaoItem",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = ponto_fixacao_preference.getString("Ponto-fixacao",null);
        Type type = new TypeToken<ArrayList<PontoFixacao>>(){}.getType();
        pontoFixacaosList = gson.fromJson(json, type);
        if (pontoFixacaosList == null){
            pontoFixacaosList = new ArrayList<>();
        }
    }

    private void SaveData() {
        SharedPreferences ponto_fixacao_preference = getSharedPreferences("PontoFixacaoItem",MODE_PRIVATE);
        SharedPreferences beforecopy = getSharedPreferences("beforecopy",MODE_PRIVATE);
        SharedPreferences.Editor editorBefore = beforecopy.edit();
        SharedPreferences.Editor editor = ponto_fixacao_preference.edit();
        Gson gson = new Gson();
        String json = gson.toJson(pontoFixacaosList);
        editor.putString("Ponto-fixacao", json);
        editorBefore.putString("Ponto-fixacao",json);
        editorBefore.apply();
        editor.apply();
    }

    public void CadastrarPonto() {
        String Bitola = "-----";
        String PontoFixacao = tipopontofixacao.getSelectedItem().toString();
        String TipodeCabo = pontotipo.getSelectedItem().toString();
        if(bitolaponto.getSelectedItem() != null){
             Bitola = bitolaponto.getSelectedItem().toString();
        }

        if(PontoFixacao.equals("") || PontoFixacao.equals(null) ) {
            System.out.println("Ponto de fixação encontra-se Vazio: " + PontoFixacao.toString());
        }else if(TipodeCabo.equals("") || TipodeCabo.equals(null)){
            System.out.println("Ponto de fixação encontra-se Vazio: " + PontoFixacao.toString());
        } else{
            pontoFixacaosList.add(new PontoFixacao(PontoFixacao, TipodeCabo, Bitola));
        }

        System.out.println("PONTO TAMANHO = "+pontoFixacaosList.size());
        SaveData();

        Intent intent = new Intent(ponto_fixacao_add.this, Atributos_poste.class);
        startActivity(intent);
        finish();
    }

    public void getPontoAnteriores(){
        SharedPreferences ponto_fixacao_preference = getSharedPreferences("PontoFixacaoItem",MODE_PRIVATE);
        SharedPreferences.Editor editor = ponto_fixacao_preference.edit();
        SharedPreferences beforecopy = getSharedPreferences("beforecopy",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = beforecopy.getString("Ponto-fixacao",null);
        editor.putString("Ponto-fixacao", json);
        editor.apply();
        Intent intent = new Intent(ponto_fixacao_add.this, Atributos_poste.class);
        startActivity(intent);
        finish();
    }

    public void getSubtipo() {
        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/getdados/getsubtipo.php",
                new Response.Listener<String>() {
                    JSONArray arraysubtipo = new JSONArray();
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("erro")){
                            dinamicoLayout.setVisibility(View.INVISIBLE);
                        }else{
                                try {
                                    JSONObject obj = new JSONObject(response);

                                    if(obj.isNull("subtipocabo")){
                                        dinamicoLayout.setVisibility(View.INVISIBLE);
                                    }
                                    else{
                                        subtipo.clear();
                                        arraysubtipo = obj.getJSONArray("subtipocabo");
                                        System.out.println("Resposta da Requisição: "+response);
                                        for (int i=0; i< arraysubtipo.length(); i++){
                                            JSONObject jsonObject = arraysubtipo.getJSONObject(i);
                                            String subtipos = jsonObject.getString("subtipocabo");
                                            subtipo.add(subtipos);
                                        }

                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_text,subtipo);
                                        bitolaponto.setAdapter(adapter);
                                        dinamicoLayout.setVisibility(View.VISIBLE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("tipo",pontotipo.getSelectedItem().toString());
                return param;
            }
        };
        RequestQueue fila = Volley.newRequestQueue(getApplicationContext());
        fila.add(request);
    }

    public void onBackPressed(){
        progressDialog = new ProgressDialog(ponto_fixacao_add.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Intent intent = new Intent(ponto_fixacao_add.this,Atributos_poste.class);
        startActivity(intent);
        finish();
    }
}
