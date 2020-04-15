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
import android.widget.ListAdapter;
import android.widget.ListView;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Atributos_poste extends AppCompatActivity {

    private LinearLayout adapterSuporte,adapterPonto,adapterLuz;

    Button btn_cruzeta_add,btn_ponto_add,btn_salvaratributos,btn_cancelar,btn_iluminacao_add;

    String var_id_poste;



    LinearLayout LinearLayoutRack,LinearLayoutResevaT,
            LinearLayoutCaixaAtendimento,TabelaSuporte,TabelaPonto,TabelaLuz;

    List<String> Rack_lisy = new ArrayList<String>();
    List<String> Reserva_tecnica_list = new ArrayList<String>();
    List<String> Caixa_atendimento_list = new ArrayList<String>();

    //SUPORTE/CRUZETA
    List<String> TipoposteText = new ArrayList<String>();
    List<String> AereaSuporte = new ArrayList<String>();
    List<String> RedemediaPoste = new ArrayList<String>();

    //PONTO DE FIXAÇÃO
    List<String> PontoFixacao = new ArrayList<String>();
    List<String> TipodeCabo = new ArrayList<String>();
    List<String> Bitola = new ArrayList<String>();

    List<String> tipoluz = new ArrayList<String>();
    List<String> prop = new ArrayList<String>();
    List<String> t_prop = new ArrayList<String>();
    List<String> tipolamp = new ArrayList<String>();
    List<String> pot = new ArrayList<String>();


    int index;

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

        TabelaSuporte = (LinearLayout) findViewById(R.id.TabelaSuporte);
        TabelaPonto = (LinearLayout) findViewById(R.id.TabelaPonto);
        //TabelaLuz = (LinearLayout) findViewById(R.id.TabelaIluminacao);

        LinearLayoutResevaT = (LinearLayout) findViewById(R.id.parent_linear_reserva);
        LinearLayoutCaixaAtendimento = (LinearLayout) findViewById(R.id.parent_linear_caixa_atendimento);

        adapterSuporte = (LinearLayout) findViewById(R.id.adapterSuporte);
        adapterPonto = (LinearLayout) findViewById(R.id.adapterPonto);
        adapterLuz = (LinearLayout) findViewById(R.id.adapterLuz);


        // VARIAVEL ID POSTE PASSADA PARA ESSA PAGINA
        final Bundle extras = getIntent().getExtras();



        if(extras != null){
            var_id_poste = (String) extras.get("var_id_poste");
            TipoposteText = (List<String>) extras.getSerializable("tipocruzeta");
            AereaSuporte = (List<String>) extras.getSerializable("aereatipo");
            RedemediaPoste = (List<String>) extras.getSerializable("redemedia");

            PontoFixacao = (List<String>) extras.getSerializable("pontofixacao");
            TipodeCabo = (List<String>) extras.getSerializable("tipodecabo");
            Bitola = (List<String>) extras.getSerializable("bitola");

            tipoluz = (List<String>) extras.getSerializable ("tipoiluminacao");
            prop = (List<String>) extras.getSerializable ("proprietario");
            t_prop = (List<String>) extras.getSerializable ("t_proprietario");
            tipolamp = (List<String>) extras.getSerializable ("tipolampada");
            pot = (List<String>) extras.getSerializable ("potencia");




            if(!tipoluz.isEmpty()){
                //TabelaLuz.setVisibility(View.VISIBLE);
                btn_iluminacao_add.setVisibility(View.INVISIBLE);
                btn_iluminacao_add.setLayoutParams(new LinearLayout.LayoutParams(0,0));
                //TabelaLuz.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View rowView = inflater.inflate(R.layout.lista_adapter_luz, null);
                    TextView tipo_iluminacao = (TextView) rowView.findViewById(R.id.tipoiluminacao);
                    //TextView proprietario = (TextView) rowView.findViewById(R.id.proprietario_texto);
                    //TextView t_proprietario = (TextView) rowView.findViewById(R.id.t_prop_texto);
                    TextView tipolampada = (TextView) rowView.findViewById(R.id.tipo_lampada_texto);
                    TextView potencia = (TextView) rowView.findViewById(R.id.potencia_texto);
                    tipo_iluminacao.setText(tipoluz.get(0));
                    //proprietario.setText(prop.get(0));
                    //t_proprietario.setText(t_prop.get(0));
                    tipolampada.setText(tipolamp.get(0));
                    potencia.setText(pot.get(0));
                    adapterLuz.addView(rowView,0);
            }

            if(!PontoFixacao.isEmpty()){
                TabelaPonto.setVisibility(View.VISIBLE);
                TabelaPonto.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                for (int i=0;i< PontoFixacao.size();i++){
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View rowView = inflater.inflate(R.layout.lista_adapter_pontofixacao, null);
                    TextView pontoFixacao = (TextView) rowView.findViewById(R.id.pontofixacao_texto);
                    TextView TipodeCab = (TextView) rowView.findViewById(R.id.tipodecabo_texto);
                    TextView bitola = (TextView) rowView.findViewById(R.id.bitola_texto);
                    TipodeCab.setText(TipodeCabo.get(i));
                    pontoFixacao.setText(PontoFixacao.get(i));
                    bitola.setText(Bitola.get(i));
                    adapterPonto.addView(rowView,i);
                }
            }

            if(!TipoposteText.isEmpty()){
                TabelaSuporte.setVisibility(View.VISIBLE);
                TabelaSuporte.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                for (int i=0;i< TipoposteText.size();i++){
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View rowView = inflater.inflate(R.layout.lista_adapter_atributo, null);
                    TextView tipoposte = (TextView) rowView.findViewById(R.id.Tipoposte_texto);
                    TextView aereatipo = (TextView) rowView.findViewById(R.id.tipoaerea_texto);
                    TextView redemedia = (TextView) rowView.findViewById(R.id.redemedia_texto);
                    redemedia.setText(RedemediaPoste.get(i));
                    tipoposte.setText(TipoposteText.get(i));
                    aereatipo.setText(AereaSuporte.get(i));
                    adapterSuporte.addView(rowView,i);
                }
            }

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
        intentEnviar.putExtra("var_id_poste", var_id_poste);
        intentEnviar.putExtra("tipocruzeta",(Serializable) TipoposteText );
        intentEnviar.putExtra("aereatipo",(Serializable) AereaSuporte);
        intentEnviar.putExtra("redemedia",(Serializable) RedemediaPoste );
        intentEnviar.putExtra("pontofixacao",(Serializable) PontoFixacao );
        intentEnviar.putExtra("tipodecabo",(Serializable) TipodeCabo);
        intentEnviar.putExtra("bitola",(Serializable) Bitola );
        intentEnviar.putExtra("tipoiluminacao",(Serializable) tipoluz );
        intentEnviar.putExtra("proprietario",(Serializable)prop );
        intentEnviar.putExtra("t_proprietario",(Serializable) t_prop );
        intentEnviar.putExtra("tipolampada",(Serializable) tipolamp );
        intentEnviar.putExtra("potencia",(Serializable) pot );
        startActivity(intentEnviar);
        LinearLayoutCaixaAtendimento.removeAllViews();
        LinearLayoutResevaT.removeAllViews();
        LinearLayoutRack.removeAllViews();

    }

    public void cadastrarPonto (){
         Intent intentEnviar = new Intent(Atributos_poste.this, ponto_fixacao_add.class);
         intentEnviar.putExtra("var_id_poste",var_id_poste);
         intentEnviar.putExtra("tipocruzeta",(Serializable) TipoposteText );
        intentEnviar.putExtra("aereatipo",(Serializable) AereaSuporte);
        intentEnviar.putExtra("redemedia",(Serializable) RedemediaPoste );
         intentEnviar.putExtra("pontofixacao",(Serializable) PontoFixacao );
         intentEnviar.putExtra("tipodecabo",(Serializable) TipodeCabo);
         intentEnviar.putExtra("bitola",(Serializable) Bitola );
        intentEnviar.putExtra("tipoiluminacao",(Serializable) tipoluz );
        intentEnviar.putExtra("proprietario",(Serializable)prop );
        intentEnviar.putExtra("t_proprietario",(Serializable) t_prop );
        intentEnviar.putExtra("tipolampada",(Serializable) tipolamp );
        intentEnviar.putExtra("potencia",(Serializable) pot );
         startActivity(intentEnviar);
        LinearLayoutCaixaAtendimento.removeAllViews();
        LinearLayoutResevaT.removeAllViews();
        LinearLayoutRack.removeAllViews();
    }

    public void cadastrarIluminacao (){
        Intent intentEnviar = new Intent(Atributos_poste.this, iluminacao_add.class);
        intentEnviar.putExtra("var_id_poste",var_id_poste);
        intentEnviar.putExtra("tipocruzeta",(Serializable) TipoposteText );
        intentEnviar.putExtra("aereatipo",(Serializable) AereaSuporte);
        intentEnviar.putExtra("redemedia",(Serializable) RedemediaPoste );
        intentEnviar.putExtra("pontofixacao",(Serializable) PontoFixacao );
        intentEnviar.putExtra("tipodecabo",(Serializable) TipodeCabo);
        intentEnviar.putExtra("bitola",(Serializable) Bitola );
        intentEnviar.putExtra("tipoiluminacao",(Serializable) tipoluz );
        intentEnviar.putExtra("proprietario",(Serializable)prop );
        intentEnviar.putExtra("t_proprietario",(Serializable) t_prop );
        intentEnviar.putExtra("tipolampada",(Serializable) tipolamp );
        intentEnviar.putExtra("potencia",(Serializable) pot );
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

    public void delete_suporte(View v) {
        adapterSuporte.removeView((View) v.getParent());
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
