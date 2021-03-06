package com.nbntelecom.nbnpostemap.POJO;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import com.nbntelecom.nbnpostemap.Cadastro_Activity;
import com.nbntelecom.nbnpostemap.endereco_Activity;

public class ZipCodeListener implements TextWatcher {
    private Context context;

    public ZipCodeListener(Context context){

        this.context = context;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }



    @Override
    public void afterTextChanged(Editable editable) {
        String zipCode = editable.toString();
        if (zipCode.length()==8) {
            new AddressRequest((endereco_Activity) context).execute();
        }
    }
}
