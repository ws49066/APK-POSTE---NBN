package com.nbntelecom.nbnpostemap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nbntelecom.nbnpostemap.Adapter.ProvinsiAdapter;
import com.nbntelecom.nbnpostemap.model.Provinsi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConsultarPoste extends AppCompatActivity {

    private ListView listView;



    List<Provinsi> provinsiList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_poste);

        listView = (ListView) findViewById(R.id.list_prov);

        provinsiList = new ArrayList<>();
        showList();


    }


    public void showList(){

        final String post_id;

        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/listprov.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray array = obj.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++){

                                JSONObject provObj = array.getJSONObject(i);


                                Provinsi p = new Provinsi(provObj.getString("id_poste"),provObj.getString("numero_poste"));



                                provinsiList.add(p);
                            }
                            ProvinsiAdapter adapter = new ProvinsiAdapter(provinsiList, getApplicationContext());
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    TextView text = (TextView) view.findViewById(R.id.tvIDProv);
                                    Intent intentEnviar = new Intent(ConsultarPoste.this, Detalhes_poste_consulta.class);
                                    intentEnviar.putExtra("id", text.getText() );
                                    startActivity(intentEnviar);
                                }
                            });

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

        };
        Handler.getInstance(getApplicationContext()).addToRequesteQue(request);
    }
}

