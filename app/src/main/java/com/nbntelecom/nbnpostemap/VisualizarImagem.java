package com.nbntelecom.nbnpostemap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;


public class VisualizarImagem extends AppCompatActivity {

    ImageView  imagemViewFoto ;
    Bitmap imagemEnviada ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_imagem);

        imagemViewFoto = (ImageView) findViewById(R.id.VisualizarView);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            imagemEnviada= (Bitmap) extras.get("bit_image");
            imagemViewFoto.setImageBitmap(imagemEnviada);
        }


    }
}
