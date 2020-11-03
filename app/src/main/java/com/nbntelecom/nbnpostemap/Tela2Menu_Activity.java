package com.nbntelecom.nbnpostemap;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import java.net.IDN;
import java.util.HashMap;
import java.util.Map;

public class Tela2Menu_Activity extends AppCompatActivity {

    Button btn_novo;
    //Button btn_editar;
    Button btn_consultar;
    Button btn_logout;
    //Button btn_excluir;
    TextView textUserName;

    String var_name_user ;
    String id_login_user ;

    ProgressDialog progressDialog;

    private   final String ARQUIVO_ID = "Arquivo_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela2_menu);

        btn_novo = findViewById(R.id.btn_novo);
        btn_consultar = findViewById(R.id.btn_consultar);
        btn_logout = findViewById(R.id.btn_sair);
        textUserName = findViewById(R.id.textUserName);



        SharedPreferences preferences = getSharedPreferences("ArquivoAutentica",0);

        if (preferences.contains("nome") && preferences.contains("id")){
            var_name_user = preferences.getString("nome",null);
            id_login_user = preferences.getString("id",null);
        }

        textUserName.setText("Usuário: "+var_name_user);


        btn_novo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(Tela2Menu_Activity.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                    StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/cadastro.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String var_id_poste = (String) response;
                                    SharedPreferences preferences = getSharedPreferences(ARQUIVO_ID, 0);
                                    SharedPreferences Cruzeta_data = getSharedPreferences("Cruzeta-itens", MODE_PRIVATE);
                                    SharedPreferences ponto_fixacao_preference = getSharedPreferences("PontoFixacaoItem", MODE_PRIVATE);
                                    SharedPreferences luz_preferences = getSharedPreferences("LuzItems", MODE_PRIVATE);
                                    SharedPreferences.Editor editor_cruzeta = Cruzeta_data.edit();
                                    SharedPreferences.Editor editor_ponto = ponto_fixacao_preference.edit();
                                    SharedPreferences.Editor editor_luz = luz_preferences.edit();
                                    editor_luz.clear().commit();
                                    editor_cruzeta.clear().commit();
                                    editor_ponto.clear().commit();
                                    SharedPreferences.Editor editor_id = preferences.edit();
                                    editor_id.putString("id_poste", var_id_poste);
                                    editor_id.commit();
                                    Intent intentEnviar = new Intent(Tela2Menu_Activity.this, endereco_Activity.class);
                                    startActivity(intentEnviar);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            System.out.println("ID USER: " + id_login_user);
                            params.put("id_login_user", id_login_user);
                            return params;
                        }
                    };
                    RequestQueue cadastro = Volley.newRequestQueue(Tela2Menu_Activity.this);
                    cadastro.add(request);

                }else {
                    Toast.makeText(getApplicationContext(),"Dispositivo não está conectado á Internet",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }
        });

         /*btn_editar = findViewById(R.id.btn_editar);
        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Contate o Admin.",Toast.LENGTH_SHORT).show();
            }
        });*/
        btn_consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEnviar = new Intent(Tela2Menu_Activity.this, ConsultarPoste.class);
                startActivity(intentEnviar);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder msgbox = new AlertDialog.Builder(Tela2Menu_Activity.this);
                msgbox.setTitle("Logout...");
                msgbox.setIcon(android.R.drawable.ic_lock_idle_alarm);
                msgbox.setMessage("Tem certeza que deseja Deslogar do aplicativo?");
                msgbox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { SharedPreferences preferences = getSharedPreferences("ArquivoAutentica",0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();
                        Intent loginview = new Intent(Tela2Menu_Activity.this,Login_Activity.class);
                        startActivity(loginview);
                    }
                });
                msgbox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                msgbox.show();

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
        msgbox.setIcon(android.R.drawable.ic_lock_power_off);
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
