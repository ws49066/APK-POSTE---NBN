package com.nbntelecom.nbnpostemap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Atributos_poste extends AppCompatActivity {

    Button btn_cruzeta_add,btn_ponto_add,btn_salvaratributos,btn_cancelar,btn_iluminacao_add;

    String var_id_poste;

    LinearLayout LinearLayoutRack,LinearLayoutResevaT,LinearLayoutCaixaAtendimento;

    List<String> Rack_lisy = new ArrayList<String>();
    List<String> Reserva_tecnica_list = new ArrayList<String>();
    List<String> Caixa_atendimento_list = new ArrayList<String>();

    private long backPressedTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atributos_poste);



        btn_salvaratributos = findViewById(R.id.btn_atributos);
        btn_cruzeta_add = findViewById(R.id.add_field_suporte_cruzeta);
        btn_iluminacao_add = findViewById(R.id.add_field_iluminacao);
        btn_ponto_add = findViewById(R.id.add_field_ponto_fixacao);
        btn_cancelar = findViewById(R.id.btn_cancelar_cadastro);


        LinearLayoutRack = (LinearLayout) findViewById(R.id.parent_linear_rack);
        LinearLayoutResevaT = (LinearLayout) findViewById(R.id.parent_linear_reserva);
        LinearLayoutCaixaAtendimento = (LinearLayout) findViewById(R.id.parent_linear_caixa_atendimento);



        // VARIAVEL ID POSTE PASSADA PARA ESSA PAGINA
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            var_id_poste = (String) extras.get("var_id_poste");
        }


        btn_cruzeta_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarCruzeta();
            }
        });

        btn_ponto_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarPonto();
            }
        });

        btn_iluminacao_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarIluminacao();
            }
        });

        btn_salvaratributos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atributos(v);
            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exibirConfirmacao();
            }
        });

    }
    public void cadastrarCruzeta(){

        Intent intentEnviar = new Intent(Atributos_poste.this, cruzeta_suporte_add.class);
        intentEnviar.putExtra("var_id_poste",var_id_poste);
        startActivity(intentEnviar);
        LinearLayoutCaixaAtendimento.removeAllViews();
        LinearLayoutResevaT.removeAllViews();
        LinearLayoutRack.removeAllViews();
    }

    public void cadastrarPonto (){
         Intent intentEnviar = new Intent(Atributos_poste.this, ponto_fixacao_add.class);
         intentEnviar.putExtra("var_id_poste",var_id_poste);
         startActivity(intentEnviar);
        LinearLayoutCaixaAtendimento.removeAllViews();
        LinearLayoutResevaT.removeAllViews();
        LinearLayoutRack.removeAllViews();
    }

    public void cadastrarIluminacao (){
        Intent intentEnviar = new Intent(Atributos_poste.this, iluminacao_add.class);
        intentEnviar.putExtra("var_id_poste",var_id_poste);
        startActivity(intentEnviar);
        LinearLayoutCaixaAtendimento.removeAllViews();
        LinearLayoutResevaT.removeAllViews();
        LinearLayoutRack.removeAllViews();
    }


    //------------------- SPINNER RACK -----------------------
    public void adicionar_rack(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View rowView = inflater.inflate(R.layout.rack_layout, null);
        // Add the new row before the add field button.
        LinearLayoutRack.addView(rowView, LinearLayoutRack.indexOfChild(v));

    }
    public void delete_rack(View v) {
        LinearLayoutRack.removeView((View) v.getParent());

    }


    //-------------------- SPINNER RESERVA ---------------------
    public  void adicionar_reservatecnica(View v){

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final  View rowView = inflater.inflate(R.layout.reserva_tecnica_layout, null);
        // Add the new row before the add field button.
        LinearLayoutResevaT.addView(rowView,LinearLayoutResevaT.indexOfChild(v));
    }

    public void delete_reserva(View v) {

        LinearLayoutResevaT.removeView((View)v.getParent());
    }


    //-------------------- SPINNER CAIXA ATENDIMENTO -------------
    public  void adicionar_caixa_atendimento(View v){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final  View rowView = inflater.inflate(R.layout.caixa_atendimento_layout, null);
        // Add the new row before the add field button.
        LinearLayoutCaixaAtendimento.addView(rowView,LinearLayoutCaixaAtendimento.indexOfChild( v));
    }

    public void delete_caixa_atendimento(View v) {
        LinearLayoutCaixaAtendimento.removeView((View)v.getParent());
    }



    public void atributos(View v){

       int contador = 0 ;


        while (contador<(LinearLayoutCaixaAtendimento.getChildCount())){
            LinearLayout RackLinearLayout = (LinearLayout) LinearLayoutCaixaAtendimento.getChildAt(contador);
            Spinner Cadastrar_Rack = RackLinearLayout.findViewById(R.id.caixa_atendimento);
            Caixa_atendimento_list.add(Cadastrar_Rack.getSelectedItem().toString());
            contador++;
        }

        contador = 0 ;
        while (contador<(LinearLayoutResevaT.getChildCount())){
            LinearLayout ReservaLinearLayout = (LinearLayout) LinearLayoutResevaT.getChildAt(contador);
            Spinner Cadastrasr_Reserva = ReservaLinearLayout.findViewById(R.id.reservapostes);
            Reserva_tecnica_list.add(Cadastrasr_Reserva.getSelectedItem().toString());
            contador++;
        }

        contador = 0;
        while(contador<(LinearLayoutRack.getChildCount())){
            LinearLayout RackLinearLayout = (LinearLayout) LinearLayoutRack.getChildAt(contador);
            Spinner Cadastrar_Rack = RackLinearLayout.findViewById(R.id.rackposte_drop);
            Rack_lisy.add(Cadastrar_Rack.getSelectedItem().toString());
            contador++;
        }



        StringRequest request = new StringRequest(Request.Method.POST, "http://177.91.235.146/poste/atributos_add.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1")) {
                            Intent intentEnviar = new Intent(Atributos_poste.this, fotosposte.class);
                            intentEnviar.putExtra("var_id_poste",var_id_poste);
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
                params.put("caixaatendimento",Caixa_atendimento_list.toString());
                params.put("reservaposte",Reserva_tecnica_list.toString());
                params.put("rackposte",Rack_lisy.toString());
                return  params;
            }
        };
        RequestQueue cadastro = Volley.newRequestQueue(this);
        cadastro.add(request);
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
                            Intent intentEnviar = new Intent(Atributos_poste.this, Tela2Menu_Activity.class);
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
