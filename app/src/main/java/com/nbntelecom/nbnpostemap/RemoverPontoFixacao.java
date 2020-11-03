package com.nbntelecom.nbnpostemap;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nbntelecom.nbnpostemap.Poste.PontoFixacao;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RemoverPontoFixacao extends AppCompatActivity {
    ListView listView;
    ArrayList<String> arrayList;
    ArrayList<PontoFixacao> pontoFixacaosList = new ArrayList<PontoFixacao>();
    private ArrayAdapter adapter;
    private Button voltar;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remover_ponto_fixacao);

        listView = (ListView) findViewById(R.id.list_item_ponto);
        arrayList = new ArrayList<String>();
        voltar = findViewById(R.id.voltar);

        //ADAPTAR O CADASTRO DE PONTO FIXAÇÃO
        SharedPreferences ponto_fixacao_preference = getSharedPreferences("PontoFixacaoItem",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = ponto_fixacao_preference.getString("Ponto-fixacao",null);
        Type type = new TypeToken<ArrayList<PontoFixacao>>(){}.getType();
        pontoFixacaosList = gson.fromJson(json, type);

        if (pontoFixacaosList != null){
            for(PontoFixacao obj: pontoFixacaosList){
                String pontofixacao = obj.getPonto();
                String tipo = obj.getTipoPonto();
                String bitola = obj.getSubtipo();

                String singleParsed = "Provedor: " + pontofixacao+"\n"+
                        "Tipo: " + tipo+"\n"+"Subtipo: "+bitola;
                arrayList.add(singleParsed);
            }
        }

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { progressDialog = new ProgressDialog(RemoverPontoFixacao.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                Intent intent = new Intent(RemoverPontoFixacao.this, Atributos_poste.class);
                startActivity(intent);
                finish();
            }
        });
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int item = position;
                new AlertDialog.Builder(RemoverPontoFixacao.this).setIcon(android.R.drawable.ic_delete).setTitle("Are you sure?")
                        .setMessage("Do you want to delete this item").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        arrayList.remove(item);
                        pontoFixacaosList.remove(item);
                        adapter.notifyDataSetChanged();

                        SharedPreferences ponto_fixacao_preference = getSharedPreferences("PontoFixacaoItem",MODE_PRIVATE);
                        SharedPreferences beforecopy = getSharedPreferences("beforecopy",MODE_PRIVATE);
                        SharedPreferences.Editor editorBefore = beforecopy.edit();
                        SharedPreferences.Editor editor = ponto_fixacao_preference.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(pontoFixacaosList);
                        editor.putString("Ponto-fixacao", json);
                        editorBefore.putString("Ponto-fixacao",json);
                        editorBefore.apply();
                        editor.apply();
                    }
                }).setNegativeButton("No",null)
                .show();
                return true;
            }
        });
    }

    public void onBackPressed(){
        progressDialog = new ProgressDialog(RemoverPontoFixacao.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Intent intent = new Intent(RemoverPontoFixacao.this,Atributos_poste.class);
        startActivity(intent);
        finish();
    }
}