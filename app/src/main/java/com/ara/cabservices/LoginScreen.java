package com.ara.cabservices;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class LoginScreen extends AppCompatActivity {
    EditText userName, passWord;
    Button login;
    String log;
    SharedPreferences sp;
    TextView signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        userName = findViewById(R.id.user);
        passWord = findViewById(R.id.pass);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.signup);


        isPermissionGranted();
        sp = getSharedPreferences("login", MODE_PRIVATE);
        if (sp.contains("status")) {
            startActivity(new Intent(LoginScreen.this, MasterScreen.class));
            finish();   //finish current activity
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    masterScreen();
                } else {
                    Toast.makeText(LoginScreen.this, "please turn on mobile internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginScreen.this,SignupScreen.class));
            }
        });

    }

    private void masterScreen() {
       /* startActivity(new Intent(LoginScreen.this,MasterScreen.class));*/
        final String userN = userName.getText().toString();
        final String userP = passWord.getText().toString();
        String url = "http://arasoftwares.in/cabservice-app/android_atncfile.php?action=login_details";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "" + response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        log = json.getString("login");
                        SharedPreferences.Editor e = sp.edit();
                        e.putString("status", log);
                        e.commit();
                    }
                    if(userN.isEmpty() || userP.isEmpty()) {
                        Toast.makeText(LoginScreen.this,"Please Fill all details",Toast.LENGTH_SHORT).show();
                    }
                    if (log.equalsIgnoreCase("success")) {
                        startActivity(new Intent(LoginScreen.this, MasterScreen.class));
                    } else {
                        Toast.makeText(LoginScreen.this, "Unable to login", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("json error ---", "" + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "" + error);
                Toast.makeText(LoginScreen.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                java.util.Map map = new HashMap();
                map.put("username", userN);
                map.put("password", userP);
                return map;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {
                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
