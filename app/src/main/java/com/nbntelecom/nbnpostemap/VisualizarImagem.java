package com.nbntelecom.nbnpostemap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class VisualizarImagem extends AppCompatActivity {

    TextView  imagemViewFoto ;
    Bitmap imagemEnviada ;
    String TextoEnviado;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_imagem);

        imagemViewFoto = (TextView) findViewById(R.id.VisualizarView);




        Bundle extras = getIntent().getExtras();
        if(extras != null){
            imagemEnviada= (Bitmap) extras.get("bit_image");
            TextoEnviado = (String) extras.get("nomeFoto");
            imagemViewFoto.setText(TextoEnviado);

        }


    }
}
