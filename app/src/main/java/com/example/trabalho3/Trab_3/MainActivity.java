package com.example.trabalho3.Trab_3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.trabalho3.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String prefix_url = "http://andrefelix.dynip.sapo.pt/trabalho3_jorge/index.php/api";
    ListView lista;
    int id;
    ArrayList<Contacto_Model> arrayContacto= new ArrayList<>();
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = getIntent().getIntExtra("ID", -1);

        lista = (ListView) findViewById(R.id.lista);
        arrayContacto.removeAll(arrayContacto);


        // SPINNER
        spinner = (Spinner) findViewById(R.id.spinner);

        String[] options = {getResources().getString(R.string.def), getResources().getString(R.string.oder_name), getResources().getString(R.string.oreder_number), getResources().getString(R.string.recente), getResources().getString(R.string.perfil)};
        ArrayAdapter a = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, options);
        a.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(a);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id1) {

                if (position == 0) {
                    // Default
                    arrayContacto.removeAll(arrayContacto);
                    filllista();
                }

                if (position == 1) {
                    // Função ordenar por nome
                    arrayContacto.removeAll(arrayContacto);
                    orderByName();
                }
                if (position == 2) {
                    // Função ordenar por numero
                    arrayContacto.removeAll(arrayContacto);
                    orderByNumber();
                }
                if (position == 3) {
                    // Função recentes (últimos 5)
                    //lastFive();
                }
                if (position == 4) {
                    // Função perfil
                    Intent intent = new Intent(MainActivity.this, Perfil.class);
                    intent.putExtra("ID", id);
                    startActivity(intent);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // INICIAÇÃO DA CLASSE DETAIL
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> c1, View view, int position, long id1) {

                //Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Detail.class);
                intent.putExtra("ID", id);
                startActivity(intent);
            }
        });

        registerForContextMenu(lista);

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
                                            object1.getString("id"), object1.getString("name"), object1.getString("lastname"), object1.getString("personal_number"),
                                            object1.getString("company_number"), object1.getString("mail"), object1.getString("postalCode")));

                                    // TextViews para passar os parametros
                                    MyArrayAdapterContacto itemsAdapter = new MyArrayAdapterContacto(MainActivity.this, arrayContacto);
                                    ((ListView) findViewById(R.id.lista)).setAdapter(itemsAdapter);

                                }
                            } else {
                                Toast.makeText(MainActivity.this, "" + status, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ex) {
                            //Toast.makeText(MainActivity.this, "Erro 1!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MainActivity.this, "Erro 2!", Toast.LENGTH_SHORT).show();
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    // CLASSE EDIT

    //Gestão dos menus contextuais
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
    }
    // Bloco de código do EDITAR e REMOVER um contacto
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Context mContext = this;
        switch (item.getItemId()){
            case R.id.editar:
                Intent intent = new Intent(MainActivity.this, Edit.class);
                int itemPosition = info.position;
                String id = arrayContacto.get(itemPosition).id;
                intent.putExtra("id", id);
                intent.putExtra("name", arrayContacto.get(itemPosition).name);
                intent.putExtra("lastname", arrayContacto.get(itemPosition).lastname);
                intent.putExtra("personal_number", arrayContacto.get(itemPosition).personal_number);
                intent.putExtra("company_number", arrayContacto.get(itemPosition).company_number);
                intent.putExtra("mail", arrayContacto.get(itemPosition).mail);
                intent.putExtra("postalCode", arrayContacto.get(itemPosition).postalCode);

                startActivity(intent);
                return true;

            case R.id.remover:
                // Pedir confirmação
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setCancelable(true);
                builder.setMessage(R.string.certeza);
                builder.setPositiveButton(R.string.confirmar,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int itemPosition = info.position;
                                String idremove = arrayContacto.get(itemPosition).id;
                                deleteFromBD(idremove);
                                filllista();
                            }
                        });
                builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
        }
        return super.onContextItemSelected(item);
    }
    private void deleteFromBD(String id){
        arrayContacto.removeAll(arrayContacto);
        String url = prefix_url + "/delete/" + id ;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean resultado = response.getBoolean("status");
                            if (resultado) {
                                filllista();
                            } else {
                                Toast.makeText(MainActivity.this, R.string.error_delete, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ex) {
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Access the RequestQueue through your singleton class.
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }


    // Bloco de código para o LOGOUT

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.LogOut:
                SharedPreferences mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.remove("name");
                editor.remove("password");
                editor.commit();
                finish();
            case R.id.Refresh:
                arrayContacto.removeAll(arrayContacto);
                filllista();
            case R.id.Add:
                Intent intent = new Intent(MainActivity.this, Add.class);
                intent.putExtra("ID", id);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void orderByName() {

        String url = prefix_url + "/orderName/contactos/" + id ;

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
                                            object1.getString("id"), object1.getString("name"), object1.getString("lastname"), object1.getString("personal_number"),
                                            object1.getString("company_number"), object1.getString("mail"), object1.getString("postalCode")));

                                    // TextViews para passar os parametros
                                    MyArrayAdapterContacto itemsAdapter = new MyArrayAdapterContacto(MainActivity.this, arrayContacto);
                                    ((ListView) findViewById(R.id.lista)).setAdapter(itemsAdapter);

                                }
                            } else {
                                Toast.makeText(MainActivity.this, "" + status, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ex) {
                            //Toast.makeText(MainActivity.this, "Erro 1!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MainActivity.this, "Erro 2!", Toast.LENGTH_SHORT).show();
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

    private void orderByNumber() {

        String url = prefix_url + "/orderNumber/contactos/" + id ;

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
                                            object1.getString("id"), object1.getString("name"), object1.getString("lastname"), object1.getString("personal_number"),
                                            object1.getString("company_number"), object1.getString("mail"), object1.getString("postalCode")));

                                    // TextViews para passar os parametros
                                    MyArrayAdapterContacto itemsAdapter = new MyArrayAdapterContacto(MainActivity.this, arrayContacto);
                                    ((ListView) findViewById(R.id.lista)).setAdapter(itemsAdapter);

                                }
                            } else {
                                Toast.makeText(MainActivity.this, "" + status, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ex) {
                            //Toast.makeText(MainActivity.this, "Erro 1!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MainActivity.this, "Erro 2!", Toast.LENGTH_SHORT).show();
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }
}
