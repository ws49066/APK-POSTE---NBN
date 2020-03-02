package com.nbntelecom.nbnpostemap.POJO;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.nbntelecom.nbnpostemap.Cadastro_Activity;

import java.lang.ref.WeakReference;

public class AddressRequest extends AsyncTask<Void, Void, Address> {
    private WeakReference<Cadastro_Activity> activity;

    public AddressRequest( Cadastro_Activity activity ){
        this.activity = new WeakReference<>( activity );
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(activity.get() != null){
            activity.get().lockFields( true );
        }
    }

    @Override
    protected Address doInBackground(Void... voids) {

        try{
            String jsonString = JsonRequest.request( activity.get().getUriZipCode() );
            Gson gson = new Gson();

            return gson.fromJson(jsonString, Address.class);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Address address) {
        super.onPostExecute(address);

        if( activity.get() != null ){
            activity.get().lockFields( false );

            if( address != null ){
                activity.get().setDataViews(address);
            }
        }
    }
}