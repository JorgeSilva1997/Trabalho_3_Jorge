package com.example.trabalho3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyArrayAdapterContacto extends ArrayAdapter<Contacto_Model> {

    public MyArrayAdapterContacto (Context context, ArrayList<Contacto_Model> contacts)
    {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Contacto_Model c = getItem(position);
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_contactos, parent, false);
        }
        //((TextView) convertView.findViewById(R.id.ID)).setText(c.getId_contacto());
        ((TextView) convertView.findViewById(R.id.NOME)).setText(c.getName());
        ((TextView) convertView.findViewById(R.id.LASTNAME)).setText(c.getLastname());
        ((TextView) convertView.findViewById(R.id.PERSONAL_NUMBER)).setText(c.getPersonal_number());
        ((TextView) convertView.findViewById(R.id.COMPANY_NUMBER)).setText(c.getCompany_number());
        ((TextView) convertView.findViewById(R.id.MAIL)).setText(c.getMail());



        return convertView;
    }
}

