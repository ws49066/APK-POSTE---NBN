package com.nbntelecom.nbnpostemap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private final int SplashDisplay = 3000;
    public  String user,pass;

    private   final String ARQUIVO_AUTENTICACAO = "ArquivoAutentica";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final SharedPreferences preferences = getSharedPreferences(ARQUIVO_AUTENTICACAO,0);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // Start da Splash View
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (preferences.contains("user") && preferences.contains("pass")){
                    user = preferences.getString("user",null);
                    pass = preferences.getString("pass",null);
                    autenticado();
                }
                else{
                    startActivity(new Intent(SplashActivity.this,Login_Activity.class));
                    finish();
                }

            }
        },SplashDisplay);

    }

    public void autenticado(){
        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("erro")) {
                        }else{
                            Intent intent = new Intent(SplashActivity.this, Tela2Menu_Activity.class);
                            startActivity(intent);
                            finish();
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
                params.put("username",user);
                params.put("password",pass);
                return  params;
            }
        };
        RequestQueue fila = Volley.newRequestQueue(this);
        fila.add(request);
    }
}