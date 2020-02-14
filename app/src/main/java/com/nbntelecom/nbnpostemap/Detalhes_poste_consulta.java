package com.nbntelecom.nbnpostemap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nbntelecom.nbnpostemap.Adapter.ProvinsiAdapter;
import com.nbntelecom.nbnpostemap.model.Provinsi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Detalhes_poste_consulta extends AppCompatActivity {

    TextView id,camera,caixaatendimento ;
    TextView numero_post ;
    TextView  tipo_poste ;
    TextView especie ;
    TextView dimensao ;
    TextView iluminacao ;
    TextView redebaixa ;
    TextView chavefusivel ;
    TextView transformador;

    TextView text_cruzeta;
    TextView text_ponto_fixacao;
    TextView text_rack;
    TextView text_RESERVA;

    TextView text_estado;
    TextView text_cidade;
    TextView text_bairro;
    TextView text_rua;
    TextView text_proximo;
    TextView text_cep;
    TextView text_area;
    TextView text_localizacao;

    String id_selecionado;

    String SCAN_PATH;
    File[] allFiles ;

    Button pasta;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_poste_consulta);

        File foldes = new File(Environment.getDataDirectory().getParent()+"/sdcard/Android/data/com.nbntelecom.nbnpostemap/files/Pictures");

        allFiles = foldes.listFiles();



        pasta = ((Button) findViewById(R.id.button1));
                pasta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0;i<2;i++){
                            new SingleMediaScanner(Detalhes_poste_consulta.this,allFiles[i]);
                        }

                    }
                });





        Bundle extras = getIntent().getExtras();

        if(extras != null){
            id_selecionado = (String) extras.get("id").toString();
        }

        id = (TextView) findViewById(R.id.idtext);
        numero_post = (TextView) findViewById(R.id.et_numPoste);
        tipo_poste = (TextView) findViewById(R.id.texto_tipoPoste);
        especie = (TextView) findViewById(R.id.texto_especie);
        dimensao = (TextView) findViewById(R.id.text_dimensao);
        iluminacao = (TextView) findViewById(R.id.textilumina);
        redebaixa = (TextView) findViewById(R.id.labelbaixa);
        chavefusivel = (TextView) findViewById(R.id.labelfusivel);
        transformador = (TextView) findViewById(R.id.labeltrans);
        text_cruzeta = (TextView) findViewById(R.id.texto_cruzeta);
        text_ponto_fixacao= (TextView) findViewById(R.id.texto_ponto_fixacao);
        text_rack= (TextView) findViewById(R.id.text_rack);
        text_RESERVA= (TextView) findViewById(R.id.text_RESERVA);
        text_estado= (TextView) findViewById(R.id.texto_estado);
        text_cidade= (TextView) findViewById(R.id.texto_cidade);
        text_bairro= (TextView) findViewById(R.id.text_bairro);
        text_rua= (TextView) findViewById(R.id.text_rua);
        text_proximo= (TextView) findViewById(R.id.text_proximo);
        text_cep= (TextView) findViewById(R.id.text_cep);
        text_area= (TextView) findViewById(R.id.text_area);
        text_localizacao= (TextView) findViewById(R.id.text_localizacao);
        camera = (TextView) findViewById((R.id.text_camera));
        caixaatendimento = (TextView) findViewById(R.id.text_caixaatendimento);



        showList();

    }

    public void showList(){

        final String post_id;

        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/Consultar_id.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray array = obj.getJSONArray("data");

                            for (int i = 0; i < array.length(); i++){

                                JSONObject provObj = array.getJSONObject(i);

                                id.setText("ID: "+id_selecionado);
                                numero_post.setText("N. POSTE :   " + provObj.getString("numero_poste"));
                                tipo_poste.setText(provObj.getString("tipo_poste"));
                                especie.setText(provObj.getString("secao_poste"));
                                dimensao.setText(provObj.getString("dimensoes_poste"));
                                iluminacao.setText(provObj.getString("iluminacao"));
                                redebaixa.setText(provObj.getString("rede_baixa"));
                                chavefusivel.setText(provObj.getString("chave_fusivel"));
                                transformador.setText(provObj.getString("transformador"));
                                camera.setText(provObj.getString(("cameraposte")));

                                text_cruzeta.setText(provObj.getString("cruz_atributo"));
                                text_ponto_fixacao.setText(provObj.getString("ponto_atributo"));
                                text_rack.setText(provObj.getString("rack_atributo"));
                                text_RESERVA.setText(provObj.getString("reserva_atributo"));
                                caixaatendimento.setText(provObj.getString("caixa_atributo"));

                                text_estado.setText(provObj.getString("estado"));
                                text_cidade.setText(provObj.getString("municipio"));
                                text_bairro.setText(provObj.getString("bairro"));
                                text_rua.setText(provObj.getString("rua"));
                                text_proximo.setText(provObj.getString("num_proximo"));
                                text_cep.setText(provObj.getString("cep"));
                                text_area.setText(provObj.getString("perimetro"));
                                text_localizacao.setText(provObj.getString("localizacao"));


                            }


                        }catch (JSONException e){
                            e.printStackTrace();
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
                params.put("id_selecionado",id_selecionado);
                return  params;
            }
        };
        Handler.getInstance(getApplicationContext()).addToRequesteQue(request);
    }

    public class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

        private MediaScannerConnection mMs;
        private File mFile;

        public SingleMediaScanner(Context context, File f) {
            mFile = f;
            mMs = new MediaScannerConnection(context, this);
            mMs.connect();
        }

        public void onMediaScannerConnected() {
            mMs.scanFile(mFile.getAbsolutePath(), null);
        }

        public void onScanCompleted(String path, Uri uri) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            startActivity(intent);
            mMs.disconnect();
        }

    }
}
