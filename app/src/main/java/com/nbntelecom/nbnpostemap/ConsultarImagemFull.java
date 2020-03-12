package com.nbntelecom.nbnpostemap;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Base64;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

public class ConsultarImagemFull extends AppCompatActivity {


    Bitmap bit_img;
    String ListStringBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_imagem_full);

        PhotoView imageView = findViewById(R.id.img_Consulta_full);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            ListStringBitmap = (String) extras.get("bitmap-img");

            byte[] imgRecebida = Base64.decode(ListStringBitmap,Base64.DEFAULT);

            Bitmap img = BitmapFactory.decodeByteArray(imgRecebida,0,imgRecebida.length);


            float graus = 90;
            Matrix matrix = new Matrix();
            matrix.setRotate(graus);

            Bitmap newBitmapRotate = Bitmap.createBitmap(img, 0,0, img.getWidth(),img.getHeight(),matrix,true);

            imageView.setImageBitmap(newBitmapRotate);

        }




    }
}
