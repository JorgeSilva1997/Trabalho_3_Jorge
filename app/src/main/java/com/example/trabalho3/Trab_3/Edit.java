package com.example.trabalho3.Trab_3;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.trabalho3.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Edit extends AppCompatActivity {

    String prefix_url = "http://andrefelix.dynip.sapo.pt/trabalho3_jorge/index.php/api";
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);

        EditText name = (EditText) findViewById(R.id.name);
        EditText lastname = (EditText) findViewById(R.id.lastname);
        EditText personalNumber = (EditText) findViewById(R.id.personalNumber);
        EditText companyNumber = (EditText) findViewById(R.id.companyNumber);
        EditText mail = (EditText) findViewById(R.id.mail);
        EditText postalCode = (EditText) findViewById(R.id.postalCode);

        id = getIntent().getStringExtra("id");
        name.setText(getIntent().getStringExtra("name"));
        lastname.setText(getIntent().getStringExtra("lastname"));
        personalNumber.setText(getIntent().getStringExtra("personal_number"));
        companyNumber.setText(getIntent().getStringExtra("company_number"));
        mail.setText(getIntent().getStringExtra("mail"));
        postalCode.setText(getIntent().getStringExtra("postalCode"));
    }

    public void btnEdit(View view){

        EditText name = (EditText) findViewById(R.id.name);
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

        Button edit = (Button) findViewById(R.id.btnEdit);

        //validating inputs
        if (TextUtils.isEmpty(nome)) {
            name.setError("Please enter your name");
            name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(lname)) {
            lastname.setError("Please enter your last name");
            lastname.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pnumber)) {
            personalNumber.setError("Please enter your personal number");
            personalNumber.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(cnumber)) {
            companyNumber.setError("Please enter your company number");
            companyNumber.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            mail.setError("Please enter your mail");
            mail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pcode)) {
            postalCode.setError("Please enter your postal code");
            postalCode.requestFocus();
            return;
        }
        else {
            update(nome, lname, pnumber, cnumber, email, pcode);
        }

    }

    //Metodo update
    public void update(String nome, String lname, String pnumber, String cnumber, String email, String pcode){

        String url = prefix_url + "/update/" + id;
        Toast.makeText(Edit.this, "" + id, Toast.LENGTH_SHORT).show();
        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("name", nome);
        jsonParams.put("lastname", lname);
        jsonParams.put("personal_number", pnumber);
        jsonParams.put("company_number", cnumber);
        jsonParams.put("mail", email);
        jsonParams.put("postalCode", pcode);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PUT, url,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            boolean status = response.getBoolean("status");
                            if (status) {
                                //Bloco de codigo
                                //Toast.makeText(Edit_Equipa.this, "Inserido com sucesso!", Toast.LENGTH_SHORT).show();
                                finish();

                            } else {

                                //Bloco de codigo
                                //Toast.makeText(Edit_Equipa.this, "Erro na inserção!", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        } catch (JSONException ex) {
                            Log.d("regist", "Erro 1!" + ex);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.d("regist3", "" + error.getMessage());
                        //Toast.makeText(Edit_Equipa.this, "Erro 2!", Toast.LENGTH_SHORT).show();
                        finish();
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
