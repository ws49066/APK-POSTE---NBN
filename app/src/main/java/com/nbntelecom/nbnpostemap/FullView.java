package com.nbntelecom.nbnpostemap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class FullView extends AppCompatActivity {


    Bitmap bit_img;
    File caminho_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_view);

        PhotoView imageView = findViewById(R.id.img_full);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            caminho_img = (File) extras.get("caminho_img");

        }


        bit_img = BitmapFactory.decodeFile(caminho_img.getAbsolutePath());

        imageView.setImageBitmap(bit_img);


    }
}
