package com.nbntelecom.nbnpostemap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class Tela2Menu_Activity extends AppCompatActivity {

    Button btn_novo;
    Button btn_editar;
    Button btn_consultar;
    Button btn_excluir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela2_menu);

        btn_novo = findViewById(R.id.btn_novo);

        btn_novo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/cadastro.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                String var_id_poste = (String) response;
                                Intent intentEnviar = new Intent(Tela2Menu_Activity.this, Cadastro_Activity.class);
                                intentEnviar.putExtra("var_id_poste",var_id_poste);
                                startActivity(intentEnviar);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String>  params = new HashMap<>();
                        return  params;
                    }
                };
                RequestQueue cadastro = Volley.newRequestQueue(Tela2Menu_Activity.this);
                cadastro.add(request);

            }
        });

        btn_editar = findViewById(R.id.btn_editar);
        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Contate o Admin.",Toast.LENGTH_SHORT).show();
            }
        });
        btn_consultar = findViewById(R.id.btn_consultar);
        btn_consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Tela2Menu_Activity.this, ConsultarPoste.class));
            }
        });
        btn_excluir = findViewById(R.id.btn_excluir);
        btn_excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Contate o Admin..",Toast.LENGTH_SHORT).show();
            }
        });



    }
}
