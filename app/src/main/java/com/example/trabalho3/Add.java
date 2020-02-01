package com.example.trabalho3;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Add extends AppCompatActivity {

    String prefix_url = "http://andrefelix.dynip.sapo.pt/trabalho3_jorge/index.php/api";
    int id;
    EditText name, lastname, personalNumber, companyNumber, mail, postalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        id = getIntent().getIntExtra("ID", -1);
        Toast.makeText(Add.this, "" + id, Toast.LENGTH_SHORT).show();


    }

    public void btnAdd (View view){

        EditText name = (EditText) findViewById(R.id.nome);
        EditText lastname = (EditText) findViewById(R.id.lastname);
        EditText personalNumber = (EditText) findViewById(R.id.personalNumber);
        EditText companyNumber = (EditText) findViewById(R.id.companyNumber);
        EditText mail = (EditText) findViewById(R.id.mail);
        EditText postalCode = (EditText) findViewById(R.id.postalCode);

        String nome = name.getText().toString();
        String lname = lastname.getText().toString();
        String pnumber = personalNumber.getText().toString();
        String cnumber = companyNumber.getText().toString();
        String email = mail.getText().toString();
        String pcode = postalCode.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(nome)) {
            name.setError("Please enter  username");
            name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(lname)) {
            lastname.setError("Please enter lastname");
            lastname.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pnumber)) {
            personalNumber.setError("Please enter personal number");
            personalNumber.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(cnumber)) {
            companyNumber.setError("Please enter company number");
            companyNumber.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            mail.setError("Please enter email");
            mail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pcode)) {
            postalCode.setError("Please enter postal code");
            postalCode.requestFocus();
            return;
        }
        else {

            insert(nome, lname, pnumber, cnumber, email, pcode, id);
        }
    }

    //Metodo INSERT

    public void insert(String nome, String lname, String pnumber, String cnumber, String email, String pcode, int id)

    {

        String url = prefix_url + "/insert";
        Map<String, String> jsonParams = new HashMap<String, String>();

        String tipoId = String.valueOf(id);

        jsonParams.put("name", nome);
        jsonParams.put("lastname", lname);
        jsonParams.put("personal_number", pnumber);
        jsonParams.put("company_number", cnumber);
        jsonParams.put("mail", email);
        jsonParams.put("postalCode", pcode);
        jsonParams.put("user_id", tipoId);


        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,

                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            boolean status = response.getBoolean("status");
                            //Toast.makeText(Regist.this, "" + status, Toast.LENGTH_SHORT).show();

                            if (status) {

                                //Bloco de codigo
                                Toast.makeText(Add.this, R.string.regist_add, Toast.LENGTH_SHORT).show();
                                finish();
                            } else {

                                //Bloco de codigo

                                Toast.makeText(Add.this, R.string.regist_error, Toast.LENGTH_SHORT).show();


                            }
                        } catch (JSONException ex) {
                            Log.d("regist", "" + ex);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Add.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User_Model-agent", System.getProperty("http.agent"));

                return headers;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(postRequest);

    }
}
