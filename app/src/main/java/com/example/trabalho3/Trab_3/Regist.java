package com.example.trabalho3.Trab_3;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
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

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Regist extends AppCompatActivity {

    String prefix_url = "http://andrefelix.dynip.sapo.pt/trabalho3_jorge/index.php/api";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist);

    }

    public void btnRegist(View view){

        EditText name = (EditText) findViewById(R.id.nome);
        EditText mail = (EditText) findViewById(R.id.mail);
        EditText pass = (EditText) findViewById(R.id.pass);

        String nome = name.getText().toString();
        String email = mail.getText().toString();
        String password = pass.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(nome)) {
            name.setError("Please enter your username");
            name.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(email)) {
            mail.setError("Please enter your email");
            mail.requestFocus();
            return;
        }


        else {

            insert(nome, email, password);
        }
    }

    // Código para verificar se a PASSWORD cumpre os requisitos
    public String checkPass(String password)
    {
        Pattern PASSWORD_PATTERN = Pattern.compile("^" +
                "(?=.*[0-9])" +
                "(?=.*[a-z])" +
                "(?=.*[A-Z])" +
                "(?=.*[@#$%^&+=_])" +
                "(?=\\S+$)" +
                ".{6,15}" +
                "$");
        if (!PASSWORD_PATTERN.matcher(password).matches())
        {
            Toast.makeText(Regist.this, R.string.error_pass, Toast.LENGTH_SHORT).show();
        }
        else
        {

        }   return password;
    }

    // Encriptação da PASSWORD
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

    // Check Email is Valid
    public String validateEmail(String mail)
    {
        final EditText email = (EditText) findViewById(R.id.mail);
        String emailInput = email.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches())
        {
            email.setError("Please enter a valid email address");
        }
        else
        {
            email.setError(null);
        }   return mail;
    }

    public static final Pattern EMAIL_ADDRESS
            = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    //Metodo INSERT

    public void insert(String nome, String email, String password)

    {

        String url = prefix_url + "/user/insert";
        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("nome", nome);
        String emailcheck = validateEmail(email);
        jsonParams.put("mail", emailcheck);
        String passCheck = checkPass(password);
        String passMd5 = computeMD5Hash(passCheck);
        jsonParams.put("password", passMd5);


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
                                Toast.makeText(Regist.this, R.string.regist_add, Toast.LENGTH_SHORT).show();
                                finish();
                            } else {

                                //Bloco de codigo

                                Toast.makeText(Regist.this, R.string.regist_error, Toast.LENGTH_SHORT).show();


                            }
                        } catch (JSONException ex) {
                            Log.d("regist", "" + ex);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Regist.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
