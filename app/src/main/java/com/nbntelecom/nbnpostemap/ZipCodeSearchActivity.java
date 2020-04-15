package com.nbntelecom.nbnpostemap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.nbntelecom.nbnpostemap.POJO.Address;
import com.nbntelecom.nbnpostemap.POJO.AddressAdapter;
import com.nbntelecom.nbnpostemap.POJO.Util;
import com.nbntelecom.nbnpostemap.POJO.ZipCodeRequest;

import java.util.ArrayList;
import java.util.List;

public class ZipCodeSearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Spinner spStates;
    private ListView lvAddress;
    private List<Address> address;
    private Util util;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip_code_search);

        address = new ArrayList<>();
        lvAddress = (ListView) findViewById(R.id.lv_address);
        AddressAdapter adapter = new AddressAdapter(this,address);
        lvAddress.setAdapter(adapter);
        lvAddress.setOnItemClickListener(this);

        spStates = (Spinner) findViewById(R.id.sp_state);
        spStates.setAdapter(ArrayAdapter.createFromResource(this, R.array.states,android.R.layout.simple_spinner_item));

        util = new Util(this, R.id.et_street, R.id.et_city,R.id.sp_state);

    }


    public  void lockFields(boolean isToLock){
        util.lockFields(isToLock);
    }
    private String getCity(){
        return ((EditText) findViewById(R.id.et_city)).getText().toString();
    }
    private String getStreet(){
        return ((EditText) findViewById(R.id.et_street)).getText().toString();
    }
    private String getState(){
        String state = (String) spStates.getSelectedItem();
        String[] parts = state.split("\\(");
        parts = parts[ parts.length - 1 ].split("\\)");
        state = parts[0];

        return state;

    }

    public  String getUriZipCode(){
        String uri = getState()+"/";
        uri += getCity()+"/";
        uri += getStreet()+"/";
        uri += "json/";

        return "https://viacep.com.br/ws/"+uri;

    }

    public List<Address> getAddress(){

        return address;
    }


    public void updateListView(){
        ((AddressAdapter) lvAddress.getAdapter()).notifyDataSetChanged();
    }

    public void searchAddress(View view){
        new ZipCodeRequest(this).execute();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String[] zipCodeArray = address.get( i ).getCep().split("-");
        String zipCode = zipCodeArray[0] + zipCodeArray[1];

        Intent intent = new Intent();
        intent.putExtra( Address.ZIP_CODE_KEY, zipCode );
        setResult(RESULT_OK, intent);
        finish();
    }
}
