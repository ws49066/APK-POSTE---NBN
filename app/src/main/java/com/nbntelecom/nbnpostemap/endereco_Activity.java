package com.nbntelecom.nbnpostemap;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.gson.Gson;
import com.nbntelecom.nbnpostemap.POJO.Address;
import com.nbntelecom.nbnpostemap.POJO.Util;
import com.nbntelecom.nbnpostemap.POJO.ZipCodeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class endereco_Activity extends AppCompatActivity {
    TextView id_post_text;
    EditText et_numPoste1;
    Button btn_salvar_endereco,btn_cancelar;
    EditText municipio,bairro,rua,naproximado,cep,et_complement;
    Spinner areaposte,estado;
    String var_id_poste;
    private  EditText etZipCode;
    private Util util;
    ArrayList<String> provedor = new ArrayList<String>();
    ArrayList<String> cabo = new ArrayList<String>();

    private   final String ARQUIVO_ENDERECO_POSTE = "Arquivo_Endereco_Poste";
    private   final String ARQUIVO_PROVEDORES = "Arquivo_Provedores";
    private   final String ARQUIVO_Cabos = "Arquivo_Cabos";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endereco_);
        getProvedor();
        getCabo();
        SharedPreferences preferences_id = getSharedPreferences("Arquivo_id",0);

        //VARIAVEIS INSTANSIDAS COM OS IDS
        id_post_text = findViewById(R.id.idtext);

        //CANCELAR REGISTRO
        btn_cancelar = findViewById(R.id.btn_cancelar_cadastro);
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exibirConfirmacao();
            }
        });
        etZipCode = (EditText) findViewById(R.id.et_zip_code);
        etZipCode.addTextChangedListener( new ZipCodeListener(this));
        util = new Util(this,R.id.et_zip_code,R.id.et_street,R.id.et_complement,R.id.et_neighbor,R.id.et_city,R.id.sp_state,R.id.et_number,R.id.tv_zip_code_search);


        Spinner spStates = (Spinner) findViewById(R.id.sp_state);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.states,android.R.layout.simple_spinner_item);
        spStates.setAdapter(adapter);

        //Id armazenado

        if (preferences_id.contains("id_poste")){
             var_id_poste = preferences_id.getString("id_poste",null);
             System.out.println("ID POSTE: "+var_id_poste);
             id_post_text.setText("ID: "+var_id_poste);
        }


        // SALVAR DADOS
        btn_salvar_endereco = findViewById(R.id.btn_salvar_endereco);
        btn_salvar_endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endereco();
            }
        });
    }

    public void getProvedor() {
        StringRequest request = new StringRequest(Request.Method.GET, "http://177.91.235.146/poste/getdados/getprovedor.php",
                new Response.Listener<String>() {
                    JSONArray arrayprovedor = new JSONArray();
                    @Override
                    public void onResponse(String response) {
                        if (response.isEmpty()) {
                            Toast.makeText(getApplicationContext(),"VAZIO", Toast.LENGTH_SHORT).show();
                        }else{
                            provedor.add("");
                            try {
                                JSONObject obj = new JSONObject(response);

                                if (obj.isNull("operadora")){

                                }else{
                                    arrayprovedor = obj.getJSONArray("operadora");
                                    for (int i=0; i< arrayprovedor.length(); i++){
                                        JSONObject jsonObject = arrayprovedor.getJSONObject(i);
                                        String placa = jsonObject.getString("operadora");
                                        provedor.add(placa);
                                    }
                                    Set<String> setProvedor = new HashSet<String>();
                                    setProvedor.addAll(provedor);

                                    SharedPreferences provedores = getSharedPreferences(ARQUIVO_PROVEDORES,0);
                                    SharedPreferences.Editor editoProvedor = provedores.edit();
                                    editoProvedor.putStringSet("provedores",setProvedor);
                                    editoProvedor.commit();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
        };
        RequestQueue fila = Volley.newRequestQueue(this);
        fila.add(request);
    }

    public void getCabo() {
        StringRequest request = new StringRequest(Request.Method.GET, "http://177.91.235.146/poste/getdados/getcabo.php",
                new Response.Listener<String>() {
                    JSONArray arraycabo = new JSONArray();
                    @Override
                    public void onResponse(String response) {
                        if (response.isEmpty()) {
                            Toast.makeText(getApplicationContext(),"VAZIO", Toast.LENGTH_SHORT).show();
                        }else{
                            cabo.add("");
                            try {
                                JSONObject obj = new JSONObject(response);

                                if (obj.isNull("cabo")){
                                }else{
                                    arraycabo = obj.getJSONArray("cabo");
                                    for (int i=0; i< arraycabo.length(); i++){
                                        JSONObject jsonObject = arraycabo.getJSONObject(i);
                                        String placa = jsonObject.getString("cabo");
                                        cabo.add(placa);
                                    }
                                    Set<String> setCabos = new HashSet<String>();
                                    setCabos.addAll(cabo);
                                    SharedPreferences cabo = getSharedPreferences(ARQUIVO_Cabos,0);
                                    SharedPreferences.Editor editoProvedor = cabo.edit();
                                    editoProvedor.putStringSet("cabos",setCabos);
                                    editoProvedor.commit();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

        };
        RequestQueue fila = Volley.newRequestQueue(this);
        fila.add(request);
    }

    public  String getUriZipCode(){
        return "https://viacep.com.br/ws/"+etZipCode.getText()+"/json/";
    }

    public  void lockFields(boolean isToLock){
        util.lockFields(isToLock);
    }



    public void setDataViews(Address address){
        setField(R.id.et_street , address.getLogradouro());
        setField(R.id.et_complement , address.getComplemento());
        setField(R.id.et_neighbor , address.getBairro());
        setField(R.id.et_city , address.getLocalidade());
        setSpinner(R.id.sp_state, R.array.states, address.getUf());
    }
    private  void setField(int id, String data){
        ((EditText) findViewById((id))).setText(data);
    }
    private  void setSpinner(int id, int arrayId, String data){
        String[] itens = getResources().getStringArray(arrayId);

        for(int i =0; i<itens.length; i++){
            if(itens[i].endsWith("("+data+")")){
                ((Spinner) findViewById(id)).setSelection(i);
                return;
            }
        }
        ((Spinner) findViewById(id)).setSelection(0);
    }

    public void searchZipCode(View view){
        Intent intent = new Intent(this, ZipCodeSearchActivity.class);
        startActivityForResult( intent, Address.RESQUEST_ZIP_CODE_CODE );
    }

    public void endereco() {
        et_numPoste1 = findViewById(R.id.et_numPoste);
        estado = findViewById(R.id.sp_state);
        municipio = findViewById(R.id.et_city);
        et_complement = findViewById(R.id.et_complement);
        bairro = findViewById(R.id.et_neighbor);
        rua = findViewById(R.id.et_street);
        naproximado = findViewById(R.id.et_number);
        cep = findViewById(R.id.et_zip_code);
        areaposte = findViewById(R.id.areaposte);

        if (et_numPoste1.getText().length() == 0 || municipio.getText().length() == 0 || bairro.getText().length() == 0 ||
                rua.getText().length() == 0 || naproximado.getText().length() == 0 || cep.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();

        } else {
            SharedPreferences preferences_endereco = getSharedPreferences(ARQUIVO_ENDERECO_POSTE, 0);
            SharedPreferences.Editor editor_endereco = preferences_endereco.edit();
            editor_endereco.putString("numeroPoste", et_numPoste1.getText().toString());
            editor_endereco.putString("estado", estado.getSelectedItem().toString());
            editor_endereco.putString("municipio", municipio.getText().toString());
            editor_endereco.putString("complemento", et_complement.getText().toString());
            editor_endereco.putString("bairro", bairro.getText().toString());
            editor_endereco.putString("rua", rua.getText().toString());
            editor_endereco.putString("numberProximo", naproximado.getText().toString());
            editor_endereco.putString("cep", cep.getText().toString());
            editor_endereco.putString("areaposte", areaposte.getSelectedItem().toString());
            editor_endereco.apply();
            Intent intentEnviar = new Intent(endereco_Activity.this, MapPoste.class);
            startActivity(intentEnviar);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);
        if( requestCode == Address.RESQUEST_ZIP_CODE_CODE
                && resultCode == RESULT_OK ){
            etZipCode.setText( intent.getStringExtra( Address.ZIP_CODE_KEY ) );
        }
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

                        }else{
                            Intent intentEnviar = new Intent(endereco_Activity.this, Tela2Menu_Activity.class);
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
