package com.nbntelecom.nbnpostemap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
                startActivity(new Intent(Tela2Menu_Activity.this, Cadastro_Activity.class));
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
                Toast.makeText(getApplicationContext(),"Contate o Admin..",Toast.LENGTH_SHORT).show();
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
