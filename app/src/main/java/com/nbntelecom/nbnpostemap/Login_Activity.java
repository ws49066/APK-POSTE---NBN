package com.nbntelecom.nbnpostemap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class Login_Activity extends Activity {

    Button btn_login;
    EditText et_cpf,et_senha;
    CheckBox salvarAutenticacao;

    ProgressDialog progressDialog;

    public  String user,pass;
    private   final String ARQUIVO_AUTENTICACAO = "ArquivoAutentica";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences preferences = getSharedPreferences(ARQUIVO_AUTENTICACAO,0);

        if (preferences.contains("user") && preferences.contains("pass")){
            user = preferences.getString("user",null);
            pass = preferences.getString("pass",null);
        }

        setContentView(R.layout.activity_main);

        btn_login = findViewById(R.id.btn_login);
        et_cpf = findViewById(R.id.et_cpf);
        et_senha = findViewById(R.id.et_senha);
        salvarAutenticacao = findViewById(R.id.salvarAutenticacao);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_cpf.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Login está vazio",Toast.LENGTH_LONG).show();
                    return;
                }
                if(et_senha.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Senha está vazio",Toast.LENGTH_LONG).show();
                    return;
                }

                login();
            }
        });


    }

    // Função sera chamado caso o Sqlite não retorne usuario True
    public void login(){
        progressDialog = new ProgressDialog(Login_Activity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("erro")) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Erro usuario ou senha"+response, Toast.LENGTH_SHORT).show();
                        }else{
                            String Array[] = new String[2];
                            Array = response.split(",");
                            String var_name_user = Array[0];
                            String id_login_user = Array[1];
                            String usuario = et_cpf.getText().toString();
                            String senha = et_senha.getText().toString();
                            SharedPreferences preferences = getSharedPreferences(ARQUIVO_AUTENTICACAO,0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("nome",var_name_user);
                            if(salvarAutenticacao.isChecked()){
                                editor.putString("user",usuario);
                                editor.putString("pass",senha);
                            }
                            editor.putString("id",id_login_user);
                            editor.commit();
                            Intent intentEnviar = new Intent(Login_Activity.this, Tela2Menu_Activity.class);
                            startActivity(intentEnviar);
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
                params.put("username",et_cpf.getText().toString());
                params.put("password",et_senha.getText().toString());
                return  params;
            }
        };
        RequestQueue fila = Volley.newRequestQueue(this);
        fila.add(request);
    }

}


