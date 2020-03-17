package com.nbntelecom.nbnpostemap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nbntelecom.nbnpostemap.POJO.Address;
import com.nbntelecom.nbnpostemap.POJO.Util;
import com.nbntelecom.nbnpostemap.POJO.ZipCodeListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class fotosposte extends AppCompatActivity {


    private long backPressedTime;
    //BitmapFactory.Options option = new BitmapFactory.Options();

    TextView id_post_text;
    EditText et_numPoste1;

    Bitmap bitimagem ;
    ArrayList<String> imageToString = new ArrayList<String>();
    List<String> ImagensStringList = new ArrayList<String>();

    List<File> CaminhosFotos = new ArrayList<File>();
    Button btn_salvar_fotos,btn_cancelar;
    LinearLayout layoutgridimg;


    String var_id_poste,NomeFotoTirada,mCurrentPhotoPath,encoded_string,Nomefoto;

    int quantfotos = 0;



    private GridView imageGrid;
    private ArrayList<Bitmap> BitmapListmg;
    private Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotosposte);

        //VARIAVEIS INSTANSIDAS COM OS IDS
        imageGrid = (GridView) findViewById(R.id.gridview);
        BitmapListmg = new ArrayList<Bitmap>();


        layoutgridimg = (LinearLayout) findViewById(R.id.layout_gridmig);

        btn_cancelar = findViewById(R.id.btn_cancelar_cadastro);

        btn_salvar_fotos = findViewById(R.id.btn_salvar_fotos);

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exibirConfirmacao();
            }
        });


        //Permissão de CAMERA
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA
            }, 0);
        }


        imageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File caminho = CaminhosFotos.get(position);
                Intent intent = new Intent(fotosposte.this,FullView.class);
                intent.putExtra("caminho", caminho);
                startActivity(intent);
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            var_id_poste = (String) extras.get("var_id_poste");
        }

        btn_salvar_fotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_salvar_fotos();
            }
        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);
        try{
            if (resultCode == RESULT_OK){
                File file = new File(mCurrentPhotoPath);
                CaminhosFotos.add(file);
                bitimagem  = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                if(bitimagem  != null) {
                    ImagensStringList.add(NomeFotoTirada);
                    float graus = 90;
                    Matrix matrix = new Matrix();
                    matrix.setRotate(graus);
                    Bitmap newBitmapRotate = Bitmap.createBitmap(bitimagem, 0,0, bitimagem.getWidth(),bitimagem.getHeight(),matrix,true);
                    BitmapListmg.add(newBitmapRotate);
                    imageGrid.setAdapter(new ImageAdapter(this, this.BitmapListmg));
                    quantfotos++;
                    if(quantfotos == 1){
                        layoutgridimg.setVisibility(View.VISIBLE);
                        layoutgridimg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,500));
                    }
                    if(quantfotos == 4){
                        layoutgridimg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,1000));
                    }

                }
            }
        }catch (Exception error){
            error.printStackTrace();
        }
    }


    public void btn_salvar_fotos(){
            for(int i = 0; i < BitmapListmg.size();i++){
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                BitmapListmg.get(i).compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                byte[] imgBytes = byteArrayOutputStream.toByteArray();
                encoded_string =  Base64.encodeToString(imgBytes,Base64.DEFAULT);
                Nomefoto = ImagensStringList.get(i);
                makeRequest();
            }

        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/finalizar.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("erro")) {
                            Toast.makeText(getApplicationContext(),"ERRO NA INSERÇÃO NO BANCO", Toast.LENGTH_SHORT). show();
                        }else{
                            String Array[] = new String[2];
                            Array = response.split(",");
                            String var_name_user = Array[0];
                            String id_login_user = Array[1];
                            Intent intentEnviar = new Intent(fotosposte.this, Tela2Menu_Activity.class);
                            intentEnviar.putExtra("var_name_user",var_name_user);
                            intentEnviar.putExtra("id_login_user",id_login_user);
                            Toast.makeText(getApplicationContext(),"Poste Cadastrado com sucesso !",Toast.LENGTH_SHORT).show();
                            startActivity(intentEnviar);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("var_id_poste",var_id_poste);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);


    }




    private void makeRequest() {
        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/fotos.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("var_id_poste",var_id_poste);
                map.put("encoded_string",encoded_string);
                map.put("image_name",Nomefoto);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }



    // Abrir CAMERA
    public void tiraFoto(View v){

        File photoFile = null;
        if(quantfotos<6){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE );
            if(intent.resolveActivity(getPackageManager())!= null) {
                try {
                    photoFile = criarImagem();
                } catch (IOException ex) {
                    //Error occurred while creating the file
                }

                if (photoFile != null) {
                    Uri photoUri = FileProvider.getUriForFile(this, "com.nbntelecom.nbnpostemap", photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, 10);
                }
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Limite de fotos atingido",Toast.LENGTH_SHORT).show();
        }

    }


    //


    // Função que vai retornar o nome da foto com dataa e hora,junto com o diretorio pra salvar a imagem
    public File criarImagem() throws IOException {
        // criar arquivo de imagem
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        NomeFotoTirada = var_id_poste+"_"+timeStamp;
        File storagedir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(NomeFotoTirada,".jpg",storagedir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void exibirConfirmacao(){
        AlertDialog.Builder msgbox = new AlertDialog.Builder(this);
        msgbox.setTitle("Excluindo....");
        msgbox.setIcon(android.R.drawable.ic_menu_delete);
        msgbox.setMessage("Tem certeza que deseja cancelar o cadastro?");

        msgbox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RemoverPoste();
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

    public void RemoverPoste(){
        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/removerPoste.php",
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
                            Intent intentEnviar = new Intent(fotosposte.this, Tela2Menu_Activity.class);
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
                params.put("var_id_poste",var_id_poste);
                return  params;
            }
        };
        RequestQueue cadastro = Volley.newRequestQueue(this);
        cadastro.add(request);
    }


}
