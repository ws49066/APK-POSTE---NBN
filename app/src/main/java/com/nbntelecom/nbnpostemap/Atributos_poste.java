package com.nbntelecom.nbnpostemap;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nbntelecom.nbnpostemap.Poste.CruzetaItems;
import com.nbntelecom.nbnpostemap.Poste.Luz;
import com.nbntelecom.nbnpostemap.Poste.PontoFixacao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Atributos_poste extends AppCompatActivity {
    private LinearLayout adapterSuporte,adapterPonto,adapterLuz;
    Button btn_cruzeta_add,btn_ponto_add,btn_salvaratributos,btn_cancelar,btn_iluminacao_add;
    String var_id_poste;
    LinearLayout LinearLayoutRack,LinearLayoutResevaT,LinearLayoutCaixaAtendimento,TabelaSuporte,TabelaPonto;

    Spinner rackposte_drop;
    //Lista
    List<String> Rack_lisy = new ArrayList<>();
    List<String> Reserva_tecnica_list = new ArrayList<>();
    List<String> Caixa_atendimento_list = new ArrayList<>();

    ArrayList<CruzetaItems> cruzetaItensList ;
    ArrayList<PontoFixacao> pontoFixacaosList;
    ArrayList<Luz> LuzList;

    private   final String ARQUIVO_PROVEDORES = "Arquivo_Provedores";
    ArrayList<String> provedor,provedoraux ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atributos_poste);

        SharedPreferences provedores = getSharedPreferences(ARQUIVO_PROVEDORES,0);
        Set<String> set =  provedores.getStringSet("provedores", null);
        provedor = new ArrayList<String>(set);
        provedoraux = provedor;
        provedoraux.remove(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_text,provedor);

        adapterSuporte = (LinearLayout) findViewById(R.id.adapterSuporte);
        adapterPonto = (LinearLayout) findViewById(R.id.adapterPonto);
        adapterLuz = (LinearLayout) findViewById(R.id.adapterLuz);

        TabelaSuporte = (LinearLayout) findViewById(R.id.TabelaSuporte);
        TabelaPonto = (LinearLayout) findViewById(R.id.TabelaPonto);
        //TabelaLuz = (LinearLayout) findViewById(R.id.TabelaIluminacao);

        //ADAPTAR OS CADASTRO DE CRUZETAS
        SharedPreferences Cruzeta_data = getSharedPreferences("Cruzeta-itens", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = Cruzeta_data.getString("cruzeta", null);
        Type type = new TypeToken<ArrayList<CruzetaItems>>(){}.getType();
        cruzetaItensList = gson.fromJson(json, type);
        if (cruzetaItensList != null){
            int cont = 0;
            TabelaSuporte.setVisibility(View.VISIBLE);
            TabelaSuporte.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            for(CruzetaItems obj: cruzetaItensList){
                final String tipoCruzeta = obj.getTipodeCruzeta();
                final String aereaTipo = obj.getAereaTipo();
                final String redemedia = obj.getRedeMedia();
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.lista_adapter_atributo, null);
                TextView tipoposte = (TextView) rowView.findViewById(R.id.Tipoposte_texto);
                TextView aereatipo = (TextView) rowView.findViewById(R.id.tipoaerea_texto);
                TextView redeme = (TextView) rowView.findViewById(R.id.redemedia_texto);
                redeme.setText(redemedia);
                tipoposte.setText(tipoCruzeta);
                aereatipo.setText(aereaTipo);
                adapterSuporte.addView(rowView,cont);
                cont++;
            }
        }

        //ADAPTAR O CADASTRO DE PONTO FIXAÇÃO
        SharedPreferences ponto_fixacao_preference = getSharedPreferences("PontoFixacaoItem",MODE_PRIVATE);
        gson = new Gson();
        json = ponto_fixacao_preference.getString("Ponto-fixacao",null);
        type = new TypeToken<ArrayList<PontoFixacao>>(){}.getType();
        pontoFixacaosList = gson.fromJson(json, type);
        if (pontoFixacaosList != null){
            int cont = 0;
            TabelaPonto.setVisibility(View.VISIBLE);
            TabelaPonto.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            for(PontoFixacao obj: pontoFixacaosList){
                final String pontofixacao = obj.getPonto();
                final String tipo = obj.getTipoPonto();
                final String bitola = obj.getSubtipo();
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.lista_adapter_pontofixacao, null);
                TextView pontoFixacao = (TextView) rowView.findViewById(R.id.pontofixacao_texto);
                TextView TipodeCab = (TextView) rowView.findViewById(R.id.tipodecabo_texto);
                TextView bitolapino = (TextView) rowView.findViewById(R.id.bitola_texto);
                TipodeCab.setText(tipo);
                pontoFixacao.setText(pontofixacao);
                bitolapino.setText(bitola);
                adapterPonto.addView(rowView,cont);
                cont++;
            }
        }

        //ADAPTAR OS CADASTRO DE LUZ

        SharedPreferences luz_preferences = getSharedPreferences("LuzItems", MODE_PRIVATE);
        gson = new Gson();
        json = luz_preferences.getString("Luz",null);
        type = new TypeToken<ArrayList<Luz>>(){}.getType();
        LuzList = gson.fromJson(json,type);
        if(LuzList != null) {
            for (Luz obj : LuzList) {
                final String tipoilumina = obj.getTipoLuz();
                final String tipodono = obj.getTipoPropri();
                final String dono = obj.getProprietatio();
                final String tipolampada = obj.getTipoLampada();
                final String potencia = obj.getPotencia();
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.lista_adapter_luz, null);
                TextView tipo_iluminacao = (TextView) rowView.findViewById(R.id.tipoiluminacao);
                //TextView proprietario = (TextView) rowView.findViewById(R.id.proprietario_texto);
                //TextView t_proprietario = (TextView) rowView.findViewById(R.id.t_prop_texto);
                TextView tipolam = (TextView) rowView.findViewById(R.id.tipo_lampada_texto);
                TextView poten = (TextView) rowView.findViewById(R.id.potencia_texto);
                tipo_iluminacao.setText(tipoilumina);
                //proprietario.setText(prop.get(0));
                //t_proprietario.setText(t_prop.get(0));
                tipolam.setText(tipolampada);
                poten.setText(potencia);
                adapterLuz.addView(rowView,0);
            }
        }



        SharedPreferences preferences_id = getSharedPreferences("Arquivo_id",0);

        if (preferences_id.contains("id_poste")){
            var_id_poste = preferences_id.getString("id_poste",null);
        }

        btn_salvaratributos = findViewById(R.id.btn_atributos);
        btn_cruzeta_add = findViewById(R.id.add_field_suporte_cruzeta);
        btn_iluminacao_add = findViewById(R.id.add_field_iluminacao);
        btn_ponto_add = findViewById(R.id.add_field_ponto_fixacao);
        btn_cancelar = findViewById(R.id.btn_cancelar_cadastro);
        LinearLayoutRack = (LinearLayout) findViewById(R.id.parent_linear_rack);
        TabelaSuporte = (LinearLayout) findViewById(R.id.TabelaSuporte);
        TabelaPonto = (LinearLayout) findViewById(R.id.TabelaPonto);
        LinearLayoutResevaT = (LinearLayout) findViewById(R.id.parent_linear_reserva);
        LinearLayoutCaixaAtendimento = (LinearLayout) findViewById(R.id.parent_linear_caixa_atendimento);
        adapterSuporte = (LinearLayout) findViewById(R.id.adapterSuporte);

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
        startActivity(intentEnviar);
    }

    public void cadastrarPonto (){
         Intent intentEnviar = new Intent(Atributos_poste.this, ponto_fixacao_add.class);
         startActivity(intentEnviar);
    }

    public void cadastrarIluminacao (){
        Intent intentEnviar = new Intent(Atributos_poste.this, iluminacao_add.class);
        startActivity(intentEnviar);
    }


    //------------------- SPINNER RACK -----------------------
    public void adicionar_rack(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View rowView = inflater.inflate(R.layout.rack_layout, null);
        // Add the new row before the add field button.
        Spinner spinner = (Spinner) rowView.findViewById(R.id.rackposte_drop);
        ArrayAdapter<String> adapterrack = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_text,provedoraux);
        spinner.setAdapter(adapterrack);
        LinearLayoutRack.addView(rowView, LinearLayoutRack.indexOfChild(v));

    }
    public void delete_rack(View v) {
        LinearLayoutRack.removeView((View) v.getParent());
        System.out.println("Foi clicado aqui no RACK");
    }

    //-------------------- SPINNER RESERVA ---------------------
    public  void adicionar_reservatecnica(View v){

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final  View rowView = inflater.inflate(R.layout.reserva_tecnica_layout, null);

        Spinner spinner = (Spinner) rowView.findViewById(R.id.reservapostes);
        ArrayAdapter<String> adapterrack = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_text,provedoraux);
        spinner.setAdapter(adapterrack);

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

        Spinner spinner = (Spinner) rowView.findViewById(R.id.caixa_atendimento);
        ArrayAdapter<String> adapterrack = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_text,provedoraux);
        spinner.setAdapter(adapterrack);
        // Add the new row before the add field button.
        LinearLayoutCaixaAtendimento.addView(rowView,LinearLayoutCaixaAtendimento.indexOfChild( v));
    }

    public void delete_caixa_atendimento(View v) {
        LinearLayoutCaixaAtendimento.removeView((View)v.getParent());
    }


    public void atributos(View v) {
        System.out.println("CAIXA = "+LinearLayoutCaixaAtendimento.getChildCount());
        System.out.println("RESERVA = "+LinearLayoutResevaT.getChildCount());
        System.out.println("RACK = "+LinearLayoutRack.getChildCount());

        for(int i=0; i<(LinearLayoutCaixaAtendimento.getChildCount()); i++){
            LinearLayout RackLinearLayout = (LinearLayout) LinearLayoutCaixaAtendimento.getChildAt(i);
            Spinner Cadastrar_Rack = RackLinearLayout.findViewById(R.id.caixa_atendimento);
            Caixa_atendimento_list.add(Cadastrar_Rack.getSelectedItem().toString());
        }

        for(int i= 0 ; i<(LinearLayoutResevaT.getChildCount()); i++){
            LinearLayout ReservaLinearLayout = (LinearLayout) LinearLayoutResevaT.getChildAt(i);
            Spinner Cadastrasr_Reserva = ReservaLinearLayout.findViewById(R.id.reservapostes);
            Reserva_tecnica_list.add(Cadastrasr_Reserva.getSelectedItem().toString());
        }

        for (int i=0; i<(LinearLayoutRack.getChildCount());i++){
            LinearLayout RackLinearLayout = (LinearLayout) LinearLayoutRack.getChildAt(i);
            Spinner Cadastrar_Rack = RackLinearLayout.findViewById(R.id.rackposte_drop);
            Rack_lisy.add(Cadastrar_Rack.getSelectedItem().toString());
        }

        Gson gson = new Gson();
        String JsonCaixa = gson.toJson(Caixa_atendimento_list);
        String JsonReserva = gson.toJson(Reserva_tecnica_list);
        String JsonRack = gson.toJson(Rack_lisy);

        SharedPreferences rack_list = getSharedPreferences("RackItems",MODE_PRIVATE);
        SharedPreferences.Editor editorRack = rack_list.edit();

        SharedPreferences reserva_tecnica_list = getSharedPreferences("ReservaItems",MODE_PRIVATE);
        SharedPreferences.Editor editorReserva = reserva_tecnica_list.edit();

        SharedPreferences caixa_atendimento_list = getSharedPreferences("CaixaItems",MODE_PRIVATE);
        SharedPreferences.Editor editorCaixa = caixa_atendimento_list.edit();

        editorCaixa.putString("Caixa",JsonCaixa);
        editorRack.putString("Rack",JsonRack);
        editorReserva.putString("Reserva", JsonReserva);

        editorReserva.apply();
        editorRack.apply();
        editorCaixa.apply();

        Intent intentEnviar = new Intent(Atributos_poste.this, fotosposte.class);
        startActivity(intentEnviar);
        finish();
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
                            Intent intentEnviar = new Intent(Atributos_poste.this, Tela2Menu_Activity.class);
                            startActivity(intentEnviar);
                            finish();
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
