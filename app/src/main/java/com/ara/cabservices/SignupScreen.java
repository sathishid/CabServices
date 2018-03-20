package com.ara.cabservices;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.lang.ref.ReferenceQueue;
import java.util.*;
import java.util.Map;

public class SignupScreen extends AppCompatActivity {


    Toolbar toolbar;
    Button createNew, already;
    EditText signUser, signphone, signpass, conpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Master Screen");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));

        createNew = (Button) findViewById(R.id.create);

        signUser = (EditText) findViewById(R.id.signuser);
        signphone = (EditText) findViewById(R.id.signphone);
        signpass = (EditText) findViewById(R.id.signpass);
        conpass = (EditText) findViewById(R.id.signconpass);

        isPermissionGranted();
        if (isNetworkAvailable()) {

        } else {
            Toast.makeText(this, "please turn on internet", Toast.LENGTH_SHORT).show();
        }
        createNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
               /* Toast.makeText(SignupScreen.this, "created user "+signUser.getText().toString(), Toast.LENGTH_SHORT).show();*/
            }
        });
    }

    private void updateUser() {
        final String sname = signUser.getText().toString();
        final String spass = signpass.getText().toString();
        final String sphone = signphone.getText().toString();
        final String scposs = conpass.getText().toString();

        if (scposs.equalsIgnoreCase(spass)) {

        } else {
            Toast.makeText(this, "password did not match ", Toast.LENGTH_SHORT).show();
        }

        if (sname.isEmpty() || spass.isEmpty() || sphone.isEmpty()) {
            Toast.makeText(this, "please fill all details", Toast.LENGTH_SHORT).show();
        } else {
            String url = "http://arasoftwares.in/cabservice-app/android_atncfile.php?action=signup";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("response", "" + response);
                    Toast.makeText(SignupScreen.this, "user added", Toast.LENGTH_SHORT).show();
                    signUser.setText("");
                    signpass.setText("");
                    signphone.setText("");
                    conpass.setText("");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error", "" + error);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map map = new HashMap();
                    map.put("name", sname);
                    map.put("password", spass);
                    map.put("mobileno", sphone);
                    return map;
                }
            };
            MySingleton.getInstance(this).addToRequestQueue(stringRequest);
        }
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
        startActivity(new Intent(SignupScreen.this, LoginScreen.class));
        finish();
        super.onBackPressed();
    }
}
