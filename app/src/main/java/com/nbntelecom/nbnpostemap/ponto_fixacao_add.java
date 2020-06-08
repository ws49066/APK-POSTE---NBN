package com.nbntelecom.nbnpostemap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
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

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ponto_fixacao_add extends AppCompatActivity {

    Button btn_cadastrar_ponto,btn_cancelar_ponto;
    Spinner tipopontofixacao,pontotipo,bitolaponto;

    //ArrayList PontoFixacao
    ArrayList<PontoFixacao> pontoFixacaosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ponto_fixacao_add);
        loadData();

        btn_cadastrar_ponto = findViewById(R.id.btn_cadastrar_ponto);
        btn_cancelar_ponto = findViewById(R.id.btn_cancelar_ponto);

        tipopontofixacao = findViewById(R.id.pontofixacao);
        pontotipo = findViewById(R.id.tipoponto);
        bitolaponto = findViewById(R.id.bitola);


        btn_cadastrar_ponto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CadastrarPonto();
            }
        });

        btn_cancelar_ponto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ponto_fixacao_add.this,Atributos_poste.class);
                startActivity(intent);
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
        SharedPreferences.Editor editor = ponto_fixacao_preference.edit();
        Gson gson = new Gson();
        String json = gson.toJson(pontoFixacaosList);
        editor.putString("Ponto-fixacao", json);
        editor.apply();
    }

    public void CadastrarPonto() {
        String PontoFixacao = tipopontofixacao.getSelectedItem().toString();
        String TipodeCabo = pontotipo.getSelectedItem().toString();
        String Bitola = bitolaponto.getSelectedItem().toString();

        pontoFixacaosList.add(new PontoFixacao(PontoFixacao, TipodeCabo, Bitola));
        System.out.println("PONTO TAMANHO = "+pontoFixacaosList.size());
        SaveData();

        Intent intent = new Intent(ponto_fixacao_add.this, Atributos_poste.class);
        startActivity(intent);
    }
}
