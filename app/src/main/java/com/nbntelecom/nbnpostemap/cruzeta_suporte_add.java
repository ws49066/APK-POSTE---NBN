package com.nbntelecom.nbnpostemap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;

public class cruzeta_suporte_add extends AppCompatActivity {

    String var_id_poste;
    Button btn_cadastrar_cruzeta,btn_cancelar_cruzeta;
    Spinner tipocruzeta,aereatipo,redemedia;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cruzeta_suporte_add);

        // VARIAVEL ID POSTE PASSADA PARA ESSA PAGINA
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            var_id_poste = (String) extras.get("var_id_poste");
        }


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
                intent.putExtra("var_id_poste",var_id_poste);
                startActivity(intent);
            }
        });
    }

    public void CadastrarCruzeta(){
        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/atributos/suporte_cruzeta.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1")) {
                            Toast.makeText(getApplicationContext(),"CRUZETA CADASTRADA !!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(cruzeta_suporte_add.this,Atributos_poste.class);
                            intent.putExtra("var_id_poste",var_id_poste);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(),"Erro:"+response,Toast.LENGTH_SHORT).show();
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
                params.put("tipocruzeta",tipocruzeta.getSelectedItem().toString());
                params.put("aereatipo",aereatipo.getSelectedItem().toString());
                params.put("redemedia",redemedia.getSelectedItem().toString());
                return  params;
            }
        };
        RequestQueue cadastro = Volley.newRequestQueue(this);
        cadastro.add(request);
    }
}
