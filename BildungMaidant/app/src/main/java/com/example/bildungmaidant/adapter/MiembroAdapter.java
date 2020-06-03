package com.example.bildungmaidant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.pojos.Usuario;

import java.util.ArrayList;

public class MiembroAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Usuario> listaMiembros;

    public MiembroAdapter (Context context, ArrayList<Usuario> listaMiembros){
        this.context=context;
        this.listaMiembros=listaMiembros;
    }

    @Override
    public int getCount() {
        return listaMiembros.size();
    }

    @Override
    public Object getItem(int position) {
        return listaMiembros.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Usuario item = (Usuario) getItem(position);

        convertView= LayoutInflater.from(context).inflate(R.layout.lista_usuario,null);
        TextView nombreCompleto=convertView.findViewById(R.id.luTVNombreCompleto);
        TextView correo=convertView.findViewById(R.id.luTVCorreo);

        nombreCompleto.setText(item.getNombres()+" "+item.getApellidos());
        correo.setText(item.getCorreo());

        return convertView;
    }
}
