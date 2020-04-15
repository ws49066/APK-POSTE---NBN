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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class cruzeta_suporte_add extends AppCompatActivity {

    String var_id_poste;
    Button btn_cadastrar_cruzeta,btn_cancelar_cruzeta;
    Spinner tipocruzeta,aereatipo,redemedia;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cruzeta_suporte_add);

        // VARIAVEL ID POSTE PASSADA PARA ESSA PAGINA
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            var_id_poste = (String) extras.get("var_id_poste");
            PontoFixacao = (List<String>) extras.getSerializable("pontofixacao");
            TipodeCabo = (List<String>) extras.getSerializable("tipodecabo");
            Bitola = (List<String>) extras.getSerializable("bitola");
            TipoposteText = (List<String>) extras.getSerializable("tipocruzeta");
            AereaSuporte = (List<String>) extras.getSerializable("aereatipo");
            RedemediaPoste = (List<String>) extras.getSerializable("redemedia");
            tipoluz = (List<String>) extras.getSerializable ("tipoiluminacao");
            prop = (List<String>) extras.getSerializable ("proprietario");
            t_prop = (List<String>) extras.getSerializable ("t_proprietario");
            tipolamp = (List<String>) extras.getSerializable ("tipolampada");
            pot = (List<String>) extras.getSerializable ("potencia");

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
                intent.putExtra("pontofixacao",(Serializable) PontoFixacao );
                intent.putExtra("tipodecabo",(Serializable) TipodeCabo);
                intent.putExtra("bitola",(Serializable) Bitola );
                intent.putExtra("tipocruzeta",(Serializable) TipoposteText );
                intent.putExtra("aereatipo",(Serializable) AereaSuporte);
                intent.putExtra("redemedia",(Serializable) RedemediaPoste );
                intent.putExtra("tipoiluminacao",(Serializable) tipoluz );
                intent.putExtra("proprietario",(Serializable)prop );
                intent.putExtra("t_proprietario",(Serializable) t_prop );
                intent.putExtra("tipolampada",(Serializable) tipolamp );
                intent.putExtra("potencia",(Serializable) pot );
                startActivity(intent);
            }
        });
    }

    public void CadastrarCruzeta(){
        TipoposteText.add(tipocruzeta.getSelectedItem().toString());
        AereaSuporte.add(aereatipo.getSelectedItem().toString());
        RedemediaPoste.add(redemedia.getSelectedItem().toString());

        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/atributos/suporte_cruzeta.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1")) {
                            Intent intent = new Intent(cruzeta_suporte_add.this,Atributos_poste.class);
                            intent.putExtra("var_id_poste",var_id_poste);
                            intent.putExtra("pontofixacao",(Serializable) PontoFixacao );
                            intent.putExtra("tipodecabo",(Serializable) TipodeCabo);
                            intent.putExtra("bitola",(Serializable) Bitola );
                            intent.putExtra("tipocruzeta",(Serializable) TipoposteText );
                            intent.putExtra("aereatipo",(Serializable) AereaSuporte);
                            intent.putExtra("redemedia",(Serializable) RedemediaPoste );
                            intent.putExtra("tipoiluminacao",(Serializable) tipoluz );
                            intent.putExtra("proprietario",(Serializable)prop );
                            intent.putExtra("t_proprietario",(Serializable) t_prop );
                            intent.putExtra("tipolampada",(Serializable) tipolamp );
                            intent.putExtra("potencia",(Serializable) pot );

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
