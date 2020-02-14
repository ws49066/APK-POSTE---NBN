package com.nbntelecom.nbnpostemap.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.nbntelecom.nbnpostemap.R;
import com.nbntelecom.nbnpostemap.model.Provinsi;

import java.util.List;

public class ProvinsiAdapter extends ArrayAdapter<Provinsi> {

    public ListView listView;

    private List<Provinsi> provinsiList;
    private Context mCtx;


    public ProvinsiAdapter(List<Provinsi> P, Context c){
        super(c, R.layout.activity_consultar_poste,P);
        this.provinsiList = P;
        this.mCtx = c;


    }




    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.listprovinsi,null,true);

        TextView nama = (TextView) view.findViewById(R.id.tvNameProv);
        TextView id = (TextView) view.findViewById(R.id.tvIDProv);

        Provinsi provinsi = provinsiList.get(position);
        nama.setText(provinsi.getNama());
        id.setText(provinsi.getId());

        return view;

    }


}
