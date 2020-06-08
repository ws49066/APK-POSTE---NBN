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

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class cruzeta_suporte_add extends AppCompatActivity {

    //CRUZETA ArrayList
    ArrayList<CruzetaItems> cruzetaItensList ;

    Button btn_cadastrar_cruzeta,btn_cancelar_cruzeta;
    Spinner tipocruzeta,aereatipo,redemedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cruzeta_suporte_add);

        loadData();

        btn_cadastrar_cruzeta = findViewById(R.id.btn_cadastrar_cruzeta);
        btn_cancelar_cruzeta = findViewById(R.id.btn_cancelar_cruzeta);
        tipocruzeta = findViewById(R.id.tipocruzeta);
        aereatipo = findViewById(R.id.aereatipo);
        redemedia = findViewById(R.id.redemedia);

        btn_cadastrar_cruzeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CadastrarCruzeta();
            }
        });

        btn_cancelar_cruzeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cruzeta_suporte_add.this,Atributos_poste.class);
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        SharedPreferences Cruzeta_data = getSharedPreferences("Cruzeta-itens", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = Cruzeta_data.getString("cruzeta", null);
        Type type = new TypeToken<ArrayList<CruzetaItems>>(){}.getType();
        cruzetaItensList = gson.fromJson(json, type);
        if (cruzetaItensList == null){
            cruzetaItensList = new ArrayList<>();
        }
    }

    private void SaveData() {
        SharedPreferences Cruzeta_data = getSharedPreferences("Cruzeta-itens", MODE_PRIVATE);
        SharedPreferences.Editor editor = Cruzeta_data.edit();
        Gson gson = new Gson();
        String json = gson.toJson(cruzetaItensList);
        editor.putString("cruzeta", json);
        editor.apply();
    }

    public void CadastrarCruzeta() {
        String tipo = tipocruzeta.getSelectedItem().toString();
        String AereaSuporte = aereatipo.getSelectedItem().toString();
        String RedemediaPoste = redemedia.getSelectedItem().toString();
        cruzetaItensList.add(new CruzetaItems(tipo, AereaSuporte, RedemediaPoste));
        System.out.println("CRUZETA tamanho = "+cruzetaItensList.size());
        SaveData();
        Intent intent = new Intent(cruzeta_suporte_add.this, Atributos_poste.class);
        startActivity(intent);
    }
}
