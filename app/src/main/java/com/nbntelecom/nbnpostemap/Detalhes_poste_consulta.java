package com.nbntelecom.nbnpostemap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Detalhes_poste_consulta extends AppCompatActivity {


    List<String> ListStringBit ;


    TextView id,camera,caixaatendimento ;
    TextView numero_post ;
    TextView  tipo_poste ;
    TextView especie ;
    TextView dimensao ;
    TextView iluminacao ;
    TextView redebaixa ;
    TextView labelisolada;
    TextView chavefusivel ;
    TextView transformador;

    TextView tipo_cruzeta,aerea_cruzeta,rede_cruzeta,labelText;
    TextView labelTextPonto,bitola_pontofixacao_view,tipocabo_pontofixacao_view,pontofixacao_pontofixacao_view;

    TextView labelTextrack,rack_view;

    TextView labelTextReservaTecnica,reservatecnica_view;

    TextView labelTextCaixaAtendimento,caixaatendimento_view;

    TextView text_estado;
    TextView text_cidade;
    TextView text_bairro;
    TextView text_rua;
    TextView text_proximo;
    TextView text_cep;
    TextView text_area;
    TextView text_localizacao;

    TextView texto_tipoIluminacao;
    TextView texto_luzproprietario;
    TextView text_luztipoproprietario;
    TextView tipodelampada;
    TextView luzpotencia;

    String id_selecionado;

    private GridView imageGride;

    int contadorCruzeta;
    int contadorRack;
    int contadorPonto;
    int contadorCaixa;
    int contadorReserva;

    public ArrayList<Bitmap> BitmapListmg;
    ArrayList<String> ListStringBitmap;

    Button pasta;

    LinearLayout LinearLayoutcruzeta,LinearLayoutPontoFixacao,LinearLayoutRack,LinearLayoutReservaTecnica,LinearLayoutCaixaAtendimento;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_poste_consulta);

        LinearLayoutcruzeta = (LinearLayout) findViewById(R.id.LinearLayoutCruzeta);
        LinearLayoutPontoFixacao = ( LinearLayout) findViewById(R.id.LinearLayoutPontoFixacao);
        LinearLayoutRack = (LinearLayout) findViewById(R.id.LinearLayoutRack);
        LinearLayoutReservaTecnica = (LinearLayout) findViewById(R.id.LinearLayoutReservaTecnica);
        LinearLayoutCaixaAtendimento = (LinearLayout) findViewById(R.id.LinearLayoutCaixaAtendimento);
        imageGride = (GridView) findViewById(R.id.gridviewa);

        BitmapListmg = new ArrayList<Bitmap>();
        ListStringBit = new ArrayList<String>();


        Bundle extras = getIntent().getExtras();

        if(extras != null){
            id_selecionado = (String) extras.get("id").toString();
        }

        id = (TextView) findViewById(R.id.idtext);
        numero_post = (TextView) findViewById(R.id.et_numPoste);
        tipo_poste = (TextView) findViewById(R.id.texto_tipoPoste);
        especie = (TextView) findViewById(R.id.texto_especie);
        dimensao = (TextView) findViewById(R.id.text_dimensao);
        redebaixa = (TextView) findViewById(R.id.labelbaixa);
        labelisolada = (TextView) findViewById(R.id.labelisolada);
        chavefusivel = (TextView) findViewById(R.id.labelfusivel);
        transformador = (TextView) findViewById(R.id.labeltrans);


        text_estado= (TextView) findViewById(R.id.texto_estado);
        text_cidade= (TextView) findViewById(R.id.texto_cidade);
        text_bairro= (TextView) findViewById(R.id.text_bairro);
        text_rua= (TextView) findViewById(R.id.text_rua);
        text_proximo= (TextView) findViewById(R.id.text_proximo);
        text_cep= (TextView) findViewById(R.id.text_cep);
        text_area= (TextView) findViewById(R.id.text_area);
        text_localizacao= (TextView) findViewById(R.id.text_localizacao);
        camera = (TextView) findViewById((R.id.text_camera));


        texto_tipoIluminacao = (TextView) findViewById((R.id.texto_tipoIluminacao));
        texto_luzproprietario = (TextView) findViewById((R.id.texto_luzproprietario));
        text_luztipoproprietario = (TextView) findViewById((R.id.text_luztipoproprietario));
        tipodelampada = (TextView) findViewById((R.id.tipodelampada));
        luzpotencia = (TextView) findViewById((R.id.luzpotencia));


        showList();


        imageGride.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              Bitmap item_bitmap = BitmapListmg.get(position);
                ShowDialoBox(item_bitmap);
            }
        });



    }

    public  void ShowDialoBox(Bitmap item_pos){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);

        ImageView image = dialog.findViewById(R.id.img);

        image.setImageBitmap(item_pos);


    }

    public void showList(){


        final String post_id;



        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/Consultar_id.php",
                new Response.Listener<String>() {




                    @Override
                    public void onResponse(String response) {


                        try {

                            JSONObject obj = new JSONObject(response);
                            JSONObject objend = new JSONObject(response);
                            JSONObject objcruzeta = new JSONObject(response);
                            JSONObject objponto = new JSONObject(response);
                            JSONObject objrack = new JSONObject(response);
                            JSONObject objreserva = new JSONObject(response);
                            JSONObject objcaixa = new JSONObject(response);
                            JSONObject objfoto = new JSONObject(response);
                            JSONObject objluz = new JSONObject(response);


                            JSONArray arrayPoste = obj.getJSONArray("data-poste");
                            JSONArray arrayEnd = objend.getJSONArray("data-end");
                            JSONArray arrayLuz = new JSONArray();

                            JSONArray arrayfotos = new JSONArray();

                            JSONArray arrayCruzeta = new JSONArray();

                            JSONArray arrayPonto = new JSONArray();
                            JSONArray arrayrRack = new JSONArray();
                            JSONArray arrayReserva = new JSONArray();
                            JSONArray arrayCaixa = new JSONArray();

                            JSONObject provObjCruzeta,provObjPonto,provObjRack,provObjReserva,provObjCaixa,provObjfotos;

                            if(!objfoto.isNull("data-img")){
                                arrayfotos = objrack.getJSONArray("data-img");
                            }

                            if(!objluz.isNull("data-luz")){
                                arrayLuz = objrack.getJSONArray("data-luz");
                                JSONObject provObjLuz = arrayLuz.getJSONObject(0);
                                texto_tipoIluminacao.setText(provObjLuz.getString("luz_tipo"));
                                texto_luzproprietario.setText(provObjLuz.getString("luz_dono"));
                                text_luztipoproprietario.setText(provObjLuz.getString("luz_tipodono"));
                                tipodelampada.setText(provObjLuz.getString("luz_tipolamp"));
                                luzpotencia.setText(provObjLuz.getString("luz_potencia"));

                            }

                            if(objcruzeta.isNull("data-cruzeta")){

                            }else{
                                 arrayCruzeta = objcruzeta.getJSONArray("data-cruzeta");

                            }

                            if(objponto.isNull("data-ponto")){

                            }else{
                                arrayPonto = objponto.getJSONArray("data-ponto");
                            }
                            if(!objrack.isNull("data-rack")){
                                arrayrRack = objrack.getJSONArray("data-rack");
                            }
                            if(!objreserva.isNull("data-reserva")){
                                arrayReserva = objreserva.getJSONArray("data-reserva");
                            }
                            if(! objcaixa.isNull("data-caixa")){
                                arrayCaixa = objcaixa.getJSONArray("data-caixa");
                            }



                            JSONObject provObjPoste = arrayPoste.getJSONObject(0);
                            JSONObject provObjEnd = arrayEnd.getJSONObject(0);


                            id.setText("ID: "+id_selecionado);
                            numero_post.setText("N. POSTE :   " + provObjPoste.getString("numero_poste"));
                            tipo_poste.setText(provObjPoste.getString("tipo_poste"));
                            especie.setText(provObjPoste.getString("secao_poste"));
                            dimensao.setText(provObjPoste.getString("dimensoes_poste"));
                            redebaixa.setText(provObjPoste.getString("rede_baixa"));
                            labelisolada.setText(provObjPoste.getString("rede_isolada"));
                            chavefusivel.setText(provObjPoste.getString("chave_fusivel"));
                            transformador.setText(provObjPoste.getString("transformador"));
                            camera.setText(provObjPoste.getString(("cameraposte")));
                            text_rua.setText(provObjEnd.getString("rua"));
                            text_estado.setText(provObjEnd.getString("estado"));
                            text_cidade.setText(provObjEnd.getString("cidade"));
                            text_bairro.setText(provObjEnd.getString("bairro"));
                            text_proximo.setText(provObjEnd.getString("numero"));
                            text_cep.setText(provObjEnd.getString("cep"));
                            text_area.setText(provObjEnd.getString("perimetro"));
                            text_localizacao.setText(provObjEnd.getString("localizacao"));



                            //FOTOS LISTAS


                            for (int i = 0; i < arrayfotos.length(); i++) {



                                provObjfotos = arrayfotos.getJSONObject(i);


                                String fotoString = provObjfotos.getString("chave");

                                ListStringBit.add(fotoString);

                                byte[] imgRecebida = Base64.decode(fotoString,Base64.DEFAULT);

                                Bitmap img = BitmapFactory.decodeByteArray(imgRecebida,0,imgRecebida.length);

                                BitmapListmg.add(img);
                                imageGride.setAdapter(new ImageAdapter(Detalhes_poste_consulta.this, BitmapListmg));
                                //float graus = 90;
                                //Matrix matrix = new Matrix();
                                //matrix.setRotate(graus);
                                //Bitmap newBitmapRotate = Bitmap.createBitmap(bitimagem, 0,0, bitimagem.getWidth(),bitimagem.getHeight(),matrix,true);


                            }




                            // REPETIÇÃO DA CRUZETA
                            for (int i = 0; i < arrayCruzeta.length(); i++) {


                                provObjCruzeta = arrayCruzeta.getJSONObject(i);


                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                                final View rowView = inflater.inflate(R.layout.consulta_cruzeta_adapter, null);
                                String tipocruzeta = provObjCruzeta.getString("tipocruzeta");
                                String aereatipo = provObjCruzeta.getString("aereatipo");
                                String redemedia = provObjCruzeta.getString("redemedia");

                                labelText = (TextView) rowView.findViewById(R.id.labelText);
                                tipo_cruzeta = (TextView) rowView.findViewById(R.id.tipo_cruzeta_view);
                                aerea_cruzeta = (TextView) rowView.findViewById(R.id.aerea_cruzeta_view);
                                rede_cruzeta = (TextView) rowView.findViewById(R.id.redemedia_cruzeta_view);

                                labelText.setText(String.valueOf(i + 1));
                                tipo_cruzeta.setText(tipocruzeta);
                                aerea_cruzeta.setText(aereatipo);
                                rede_cruzeta.setText(redemedia);

                                LinearLayoutcruzeta.addView(rowView, i);


                            }

                            // REPETICAO PONTO
                            for (int i = 0; i < arrayPonto.length(); i++) {

                                provObjPonto = arrayPonto.getJSONObject(i);


                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                                final View rowView = inflater.inflate(R.layout.consulta_pontofixacao_adapter, null);


                                String donoponto = provObjPonto.getString("donoponto");
                                String tipoponto = provObjPonto.getString("tipoponto");
                                String subtipoponto = provObjPonto.getString("subtipoponto");

                                labelTextPonto = (TextView) rowView.findViewById(R.id.labelTextPonto);
                                bitola_pontofixacao_view = (TextView) rowView.findViewById(R.id.bitola_pontofixacao_view);
                                pontofixacao_pontofixacao_view = (TextView) rowView.findViewById(R.id.pontofixacao_pontofixacao_view);
                                tipocabo_pontofixacao_view = (TextView) rowView.findViewById(R.id.tipocabo_pontofixacao_view);

                                labelTextPonto.setText(String.valueOf(i+1));
                                bitola_pontofixacao_view.setText(subtipoponto);
                                pontofixacao_pontofixacao_view.setText(donoponto);
                                tipocabo_pontofixacao_view.setText(tipoponto);

                                LinearLayoutPontoFixacao.addView(rowView,i);

                            }

                            for (int i = 0; i < arrayrRack.length(); i++) {

                                provObjRack = arrayrRack.getJSONObject(i);


                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                final View rowView = inflater.inflate(R.layout.consulta_rack_adapter, null);

                                String rack_atributo = provObjRack.getString("rack_atributo");
                                labelTextrack = (TextView) rowView.findViewById(R.id.labelTextrack);
                                rack_view = (TextView) rowView.findViewById(R.id.rack_view);
                                labelTextrack.setText(String.valueOf(i+1));
                                rack_view.setText(rack_atributo);
                                LinearLayoutRack.addView(rowView,i);

                            }

                            for (int i = 0; i < arrayReserva.length(); i++) {

                                provObjReserva = arrayReserva.getJSONObject(i);


                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                final View rowView = inflater.inflate(R.layout.consulta_reservatecnica_adapter, null);
                                String reserva_atributo = provObjReserva.getString("reserva_atributo");
                                labelTextReservaTecnica = (TextView) rowView.findViewById(R.id.labelTextReservaTecnica);
                                reservatecnica_view = (TextView) rowView.findViewById(R.id.reservatecnica_view);
                                labelTextReservaTecnica.setText(String.valueOf(i+1));
                                reservatecnica_view.setText(reserva_atributo);
                                LinearLayoutReservaTecnica.addView(rowView,i);

                            }

                            for (int i = 0; i < arrayCaixa.length(); i++) {

                                provObjCaixa = arrayCaixa.getJSONObject(i);


                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                final View rowView = inflater.inflate(R.layout.consulta_caixaatendimento_adapter, null);
                                String caixa_atributo = provObjCaixa.getString("caixa_atributo");
                                labelTextCaixaAtendimento = (TextView) rowView.findViewById(R.id.labelTextCaixaAtendimento);
                                caixaatendimento_view = (TextView) rowView.findViewById(R.id.caixaatendimento_view);
                                labelTextCaixaAtendimento.setText(String.valueOf(i+1));
                                caixaatendimento_view.setText(caixa_atributo);
                                LinearLayoutCaixaAtendimento.addView(rowView);

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


}
