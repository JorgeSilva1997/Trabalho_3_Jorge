package com.example.trabalho3.Trab_3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.trabalho3.Google_Api.Main;
import com.example.trabalho3.R;

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

    Context mContext = this;
    CheckBox checkBox;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String PREF_NAME = "login";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_USERNAME = "nome";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        user = (EditText)findViewById(R.id.user);
        pass = (EditText)findViewById(R.id.pass);

        checkBox = (CheckBox)findViewById(R.id.box);
        boolean logout = getIntent().getBooleanExtra("logout", false);
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if(sharedPreferences.getBoolean(KEY_REMEMBER, false)) {
            checkBox.setChecked(true);
        }
        else {
            checkBox.setChecked(false);
        }

        user.setText(sharedPreferences.getString(KEY_USERNAME,""));
        pass.setText(sharedPreferences.getString(KEY_PASSWORD,""));
        if(logout == true) {
            checkBox.setChecked(false);
            editor.putBoolean(KEY_REMEMBER, false);
            user.setText("");
            pass.setText("");
        }

        if(checkBox.isChecked()) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            sharedPreferences.getInt(KEY_ID, -1);
            intent.putExtra("ID", sharedPreferences.getInt(KEY_ID, -1));
            startActivity(intent);
            finish();
        }

        managePrefs();
    }

    private final void managePrefs() {

        if(checkBox.isChecked()){
            String username = user.getText().toString();
            String password = pass.getText().toString();
            String pwc = computeMD5Hash(password);
            editor.putString(KEY_USERNAME, username);
            editor.putString(KEY_PASSWORD, pwc);
            editor.putInt(KEY_ID, id);
            editor.putBoolean(KEY_REMEMBER, true);
            editor.apply();
        } else {
            editor.putBoolean(KEY_REMEMBER, false);
            editor.remove(KEY_PASSWORD);
            editor.remove(KEY_USERNAME);
            editor.apply();
        }

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
            managePrefs();
        }
    }

    public void btnRegist(View view) {

        Intent intent = new Intent(Login.this, Regist.class);
        startActivity(intent);
    }

    public void autenticacao(String nome, String password)  {

        String url = prefix_url + "/users/login";

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
                                    managePrefs();
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

    public void btnGoGoogle(View view){

        Intent intent = new Intent(Login.this, Main.class);
        startActivity(intent);

    }

}
