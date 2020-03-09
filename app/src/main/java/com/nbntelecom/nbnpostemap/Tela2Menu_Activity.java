package com.nbntelecom.nbnpostemap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
    TextView textUserName;

    private long backPressedTime;

    String var_name_user,id_login_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela2_menu);

        btn_novo = findViewById(R.id.btn_novo);
        textUserName = findViewById(R.id.textUserName);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            var_name_user = (String) extras.get("var_name_user");
            id_login_user = (String) extras.get("id_login_user") ;
            textUserName.setText("Usuário: "+var_name_user);
        }

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
                        params.put("id_login_user",id_login_user.toString());
                        return  params;
                    }
                };
                RequestQueue cadastro = Volley.newRequestQueue(Tela2Menu_Activity.this);
                cadastro.add(request);

            }
        });

         /*btn_editar = findViewById(R.id.btn_editar);
        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Contate o Admin.",Toast.LENGTH_SHORT).show();
            }
        });*/
        btn_consultar = findViewById(R.id.btn_consultar);
        btn_consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEnviar = new Intent(Tela2Menu_Activity.this, ConsultarPoste.class);
                intentEnviar.putExtra("id_login_user",id_login_user);
                startActivity(intentEnviar);
            }
        });
        /*btn_excluir = findViewById(R.id.btn_excluir);
        btn_excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Contate o Admin..",Toast.LENGTH_SHORT).show();
            }
        });
        */


    }

    public void exibirConfirmacao(){
        AlertDialog.Builder msgbox = new AlertDialog.Builder(this);
        msgbox.setTitle("Sair da aplicação....");
        msgbox.setIcon(android.R.drawable.ic_menu_delete);
        msgbox.setMessage("Tem certeza que deseja sair do aplicativo?");

        msgbox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               finishAffinity();
               System.exit(0);
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


}
