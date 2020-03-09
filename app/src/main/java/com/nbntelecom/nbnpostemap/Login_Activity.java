package com.nbntelecom.nbnpostemap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btn_login = findViewById(R.id.btn_login);
        et_cpf = findViewById(R.id.et_cpf);
        et_senha = findViewById(R.id.et_senha);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

            }
        });
    }

    public void login(){
        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("erro")) {
                            Toast.makeText(getApplicationContext(),"Erro usuario ou senha"+response, Toast.LENGTH_SHORT).show();

                        }else{
                            String Array[] = new String[2];
                            Array = response.split(",");

                            String var_name_user = Array[0];
                            String id_login_user = Array[1];
                            Intent intentEnviar = new Intent(Login_Activity.this, Tela2Menu_Activity.class);
                            intentEnviar.putExtra("var_name_user",var_name_user);
                            intentEnviar.putExtra("id_login_user",id_login_user);
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
        //Volley.newRequestQueue(this).add(request);
    }

}


