package com.nbntelecom.nbnpostemap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.nbntelecom.nbnpostemap.Poste.Luz;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class iluminacao_add extends AppCompatActivity {
    Button btn_cadastrar_iluminacao,btn_cancelar_iluminacao;
    Spinner tipoiluminacao,tipodono,dono,tipolampada,potencia;

    //ArrayList luz
    ArrayList<Luz> LuzList;
    ArrayList<String> provedor ;
    private  final String ARQUIVO_PROVEDORES = "Arquivo_Provedores";
    String[] arrayIluminacao = {"Simples","Dupla","Tripla","Quadrupla"};
    String[] arrayITipoDono = {"Rede Publica","Particular"};
    String[] arrayLampada = {"Led","Halogenea","Encadecente","Vapor de Mercurio","Vapor de Sodio","Iodedos Metalicos"};
    String[] arrayPotencia = {"40W","5","60W","70W","80W","90W","100W","150W","180W","200W","250W","300W","350W","400W","450W","500W","550W","1000W"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iluminacao_add);
        btn_cadastrar_iluminacao = findViewById(R.id.btn_cadastrar_iluminacao);
        btn_cancelar_iluminacao = findViewById(R.id.btn_cancelar_iluminacao);
        tipoiluminacao = findViewById(R.id.tipoiluminacao);
        tipodono = findViewById(R.id.spinnerproprietario);
        dono = findViewById(R.id.tipoproprispinner);
        tipolampada = findViewById(R.id.tipolampada);
        potencia = findViewById(R.id.potenciaspinner);

        SharedPreferences provedores = getSharedPreferences(ARQUIVO_PROVEDORES,0);
        Set<String> set =  provedores.getStringSet("provedores", null);
        provedor = new ArrayList<String>(set);
        provedor.remove(0);
        provedor.add(0,"Publica");

        //-------------ADAPTER LIST SPINNER ---------------------
        ArrayAdapter<String> adapteriluminacao = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_text,arrayIluminacao);
        tipoiluminacao.setAdapter(adapteriluminacao);

        ArrayAdapter<String> adapterprovedor = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_text,provedor);
        tipodono.setAdapter(adapterprovedor);

        ArrayAdapter<String> adapteProprietario = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_text,arrayITipoDono);
        dono.setAdapter(adapteProprietario);

        ArrayAdapter<String> adapteLampada = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_text,arrayLampada);
        tipolampada.setAdapter(adapteLampada);

        ArrayAdapter<String> adaptePotencia = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_text,arrayPotencia);
        potencia.setAdapter(adaptePotencia);


        loadData();

        btn_cadastrar_iluminacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CadastrarIluminacao();
            }
        });

        btn_cancelar_iluminacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(iluminacao_add.this,Atributos_poste.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadData() {
        SharedPreferences luz_preferences = getSharedPreferences("LuzItems", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = luz_preferences.getString("Luz",null);
        Type type = new TypeToken<ArrayList<Luz>>(){}.getType();
        LuzList = gson.fromJson(json,type);
        if(LuzList == null){
            LuzList = new ArrayList<>();
        }
    }

    private void SaveData() {
        SharedPreferences luz_preferences = getSharedPreferences("LuzItems", MODE_PRIVATE);
        SharedPreferences.Editor editor = luz_preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(LuzList);
        editor.putString("Luz", json);
        editor.apply();
    }

    public void CadastrarIluminacao() {
        String tipoluz = tipoiluminacao.getSelectedItem().toString();
        String prop = tipodono.getSelectedItem().toString();
        String t_prop = dono.getSelectedItem().toString();
        String tipolamp = tipolampada.getSelectedItem().toString();
        String pot = potencia.getSelectedItem().toString();

        LuzList.add(new Luz(tipoluz,prop,t_prop,tipolamp,pot));
        System.out.println("LUZZ TAMANHO = "+LuzList.size());
        SaveData();

        Intent intent = new Intent(iluminacao_add.this, Atributos_poste.class);
        startActivity(intent);
        finish();
    }
}
