package com.example.trabalho3;

import android.content.Intent;
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

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    String prefix_url = "http://andrefelix.dynip.sapo.pt/trabalho3_jorge/index.php/api";
    // Este ID é devido a ser Multi-User
    int id = -1;

    EditText user, pass;
    boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    public void btnLogin(View view) {

        EditText user = (EditText) findViewById(R.id.user);
        EditText pass = (EditText) findViewById(R.id.pass);

        String nome = user.getText().toString();
        String password = pass.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(nome)) {
            user.setError("Please enter your username");
            user.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            pass.setError("Please enter your password");
            pass.requestFocus();
            return;
        }
        else {

            autenticacao(nome, password);
        }
    }

    public void btnRegist(View view) {

        Intent intent = new Intent(Login.this, Regist.class);
        startActivity(intent);
    }

    public void autenticacao(String nome, String password)  {

        String url = prefix_url + "/user/login";

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("nome", nome);
        String md5Str = computeMD5Hash(password);
        jsonParams.put("password", md5Str);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,

                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            boolean status = response.getBoolean("status");
                            //Toast.makeText(Login.this, "" + status, Toast.LENGTH_SHORT).show();

                            if (status) {

                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    intent.putExtra("ID", response.getInt("id"));
                                    startActivity(intent);

                            } else {
                                Toast.makeText(Login.this, R.string.error_login, Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException ex) {
                            Log.d("login", "" + ex);
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    // Função MD5
    public String computeMD5Hash(String password)
    {
        byte[] md5Input = password.getBytes();
        BigInteger md5Data = null;
        try
        {
            md5Data = new BigInteger(1, md5.encryptMD5(md5Input));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String md5Str = md5Data.toString(16);

        return md5Str;
    }

}
