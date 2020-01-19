package com.example.trabalho3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String prefix_url = "http://andrefelix.dynip.sapo.pt/trabalho3_jorge/index.php/api";
    ListView lista;
    int id;
    ArrayList<Contacto_Model> arrayContacto= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = getIntent().getIntExtra("ID", -1);

        Toast.makeText(MainActivity.this, "" + id, Toast.LENGTH_SHORT).show();
        lista = (ListView) findViewById(R.id.lista);
        filllista();
    }

    private void filllista(){

        String url = prefix_url + "/contactos/" + id ;

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
                                    arrayContacto.add(new Contacto_Model(
                                            object1.getString("name"), object1.getString("lastname"), object1.getString("personal_number"),
                                            object1.getString("company_number"), object1.getString("mail"), object1.getString("postalCode")));

                                    // TextViews para passar os parametros
                                    MyArrayAdapterContacto itemsAdapter = new MyArrayAdapterContacto(MainActivity.this, arrayContacto);
                                    ((ListView) findViewById(R.id.lista)).setAdapter(itemsAdapter);

                                }
                            } else {
                                Toast.makeText(MainActivity.this, "" + status, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ex) {
                            Toast.makeText(MainActivity.this, "Erro 1!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Erro 2!", Toast.LENGTH_SHORT).show();
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}
