package com.nbntelecom.nbnpostemap.POJO;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nbntelecom.nbnpostemap.Cadastro_Activity;
import com.nbntelecom.nbnpostemap.ZipCodeSearchActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class ZipCodeRequest extends AsyncTask<Void, Void, Void> {
    private WeakReference<ZipCodeSearchActivity> activity;

    public ZipCodeRequest(ZipCodeSearchActivity activity ){
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
    protected Void doInBackground(Void... voids) {

        try{
            String jsonString = JsonRequest.request( activity.get().getUriZipCode() );

            JSONArray jsonArray = new JSONArray(jsonString);
            Gson gson = new Gson();
            activity.get().getAddress().clear();



            for (int i = 0 ; i< jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                activity.get().getAddress().add( gson.fromJson(jsonObject.toString(), Address.class) );
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void svoid) {
        super.onPostExecute(svoid);

        if( activity.get() != null ){
            activity.get().lockFields( false );
            activity.get().updateListView();

        }

    }

}