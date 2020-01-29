package com.example.trabalho3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.ArrayList;

public class Detail extends AppCompatActivity {

    String prefix_url = "http://andrefelix.dynip.sapo.pt/trabalho3_jorge/index.php/api";
    int id;

    TextView nome, lastname, personalNumber, companyNumber, mail, postalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        id = getIntent().getIntExtra("ID", -1);

        Toast.makeText(Detail.this, "" + id, Toast.LENGTH_SHORT).show();


        detail();
    }


    public void detail() {

        nome = (TextView) findViewById(R.id.name);
        lastname = (TextView) findViewById(R.id.lastname);
        personalNumber = (TextView) findViewById(R.id.personalNumber);
        companyNumber = (TextView) findViewById(R.id.companyNumber);
        mail = (TextView) findViewById(R.id.mail);
        postalCode = (TextView) findViewById(R.id.postalCode);


        String url = prefix_url + "/contacto/" + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean status = response.getBoolean("status");
                            if (status) {

                                JSONArray array = response.getJSONArray("DATA");

                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object1 = array.getJSONObject(i);

                                    nome.setText(object1.getString("name"));
                                    lastname.setText(object1.getString("lastname"));
                                    personalNumber.setText(object1.getString("personal_number"));
                                    companyNumber.setText(object1.getString("company_number"));
                                    mail.setText(object1.getString("mail"));
                                    postalCode.setText(object1.getString("postalCode"));

                                    //arrayContacto.add(new Contacto_Model(
                                    //        object1.getString("name"), object1.getString("lastname"), object1.getString("personal_number"),
                                    //        object1.getString("company_number"), object1.getString("mail"), object1.getString("postalCode")));

                                    // TextViews para passar os parametros
                                    //MyArrayAdapterContacto itemsAdapter = new MyArrayAdapterContacto(MainActivity.this, arrayContacto);
                                    //((ListView) findViewById(R.id.lista)).setAdapter(itemsAdapter);

                                }
                            } else {
                                Toast.makeText(Detail.this, "" + status, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ex) {
                            //Toast.makeText(Detail.this, "Erro 1!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(Detail.this, "Erro 2!", Toast.LENGTH_SHORT).show();
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }
}

