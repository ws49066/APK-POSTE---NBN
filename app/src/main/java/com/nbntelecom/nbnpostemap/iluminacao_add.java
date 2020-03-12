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

public class iluminacao_add extends AppCompatActivity {

    String var_id_poste;
    Button btn_cadastrar_iluminacao,btn_cancelar_iluminacao;
    Spinner tipoiluminacao,tipodono,dono,tipolampada,potencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iluminacao_add);

        // VARIAVEL ID POSTE PASSADA PARA ESSA PAGINA
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            var_id_poste = (String) extras.get("var_id_poste");
        }


        btn_cadastrar_iluminacao = findViewById(R.id.btn_cadastrar_iluminacao);
        btn_cancelar_iluminacao = findViewById(R.id.btn_cancelar_iluminacao);

        tipoiluminacao = findViewById(R.id.tipoiluminacao);
        tipodono = findViewById(R.id.spinnerproprietario);
        dono = findViewById(R.id.tipoproprispinner);
        tipolampada = findViewById(R.id.tipolampada);
        potencia = findViewById(R.id.potenciaspinner);


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
                intent.putExtra("var_id_poste",var_id_poste);
                startActivity(intent);
            }
        });
    }

    public void CadastrarIluminacao(){
        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/atributos/iluminacao.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1")) {
                            Toast.makeText(getApplicationContext(),"ILUMINAÇÃO CADASTRADA COM SUCESSO!!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(iluminacao_add.this,Atributos_poste.class);
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
                params.put("tipoilumina",tipoiluminacao.getSelectedItem().toString());
                params.put("tipodono",tipodono.getSelectedItem().toString());
                params.put("dono",dono.getSelectedItem().toString());
                params.put("tipolampada",tipolampada.getSelectedItem().toString());
                params.put("potencia",potencia.getSelectedItem().toString());
                return  params;
            }
        };
        RequestQueue cadastro = Volley.newRequestQueue(this);
        cadastro.add(request);
    }
}
