package com.ara.cabservices;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ara.cabservices.Models.FeedbackModel;

import java.sql.Time;
import java.util.*;
import java.util.Map;

public class Feedback extends AppCompatActivity {


    //    static final int TIME_DIALOG_ID = 1111;
    EditText fName, fcNo;
    TextView pick, drop;
    TextView picktext, droptext;
    Spinner pickUpLoc;
    TextView dropLoc;
    Button submit;
    Toolbar toolbar;
    String format;
    Calendar calendar;
    TimePickerDialog timepickerdialog;
    String[] pickupareas = {"PickUp Location", "Vijaya nagar", "Perungudi", "Tnagar Bus Terminus", "Nandanam", "Mylapore", "Le Royal Merridien"};
    //    String[] dropareas = {"drop Location", "Vijaya nagar", "Perungudi", "Tnagar Bus Terminus", "Nandanam", "Mylapore", "Le Royal Merridien"};
    private int CalendarHour, CalendarMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Master Screen");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));

        fName = (EditText) findViewById(R.id.feedname);
        fcNo = (EditText) findViewById(R.id.feedno);
        pickUpLoc = (Spinner) findViewById(R.id.feedpickuploc);
        dropLoc = (TextView) findViewById(R.id.feeddroploc);
        pick = (TextView) findViewById(R.id.feedpick);

        submit = (Button) findViewById(R.id.feedsubmit);
        picktext = (TextView) findViewById(R.id.textpick);


        isPermissionGranted();

        setPickupSpinner();

        if (isNetworkAvailable()) {
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    submit();
                }
            });
        } else {
            Toast.makeText(this, "please turn on internet", Toast.LENGTH_SHORT).show();
        }


        picktext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pickupmethod();
            }
        });


        droptext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dropmethod();
            }
        });


        pickUpLoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                ((TextView) adapterView.getChildAt(i)).setTextColor(Color.WHITE);
                ((TextView) adapterView.getChildAt(i)).setTextSize(14);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void setPickupSpinner() {
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, pickupareas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickUpLoc.setPrompt("Select pickUp Location");
        pickUpLoc.setAdapter(adapter);
    }


    private void dropmethod() {
        calendar = Calendar.getInstance();
        CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
        CalendarMinute = calendar.get(Calendar.MINUTE);

        timepickerdialog = new TimePickerDialog(Feedback.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        if (hourOfDay == 0) {

                            hourOfDay += 12;

                            format = "AM";
                        } else if (hourOfDay == 12) {

                            format = "PM";

                        } else if (hourOfDay > 12) {

                            hourOfDay -= 12;

                            format = "PM";

                        } else {

                            format = "AM";
                        }


                        droptext.setText(hourOfDay + ":" + minute + format);
                    }
                }, CalendarHour, CalendarMinute, false);
        timepickerdialog.show();
    }

    private void pickupmethod() {
        calendar = Calendar.getInstance();
        CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
        CalendarMinute = calendar.get(Calendar.MINUTE);

        timepickerdialog = new TimePickerDialog(Feedback.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        if (hourOfDay == 0) {

                            hourOfDay += 12;

                            format = "AM";
                        } else if (hourOfDay == 12) {

                            format = "PM";

                        } else if (hourOfDay > 12) {

                            hourOfDay -= 12;

                            format = "PM";

                        } else {

                            format = "AM";
                        }


                        picktext.setText(hourOfDay + ":" + minute + format);
                    }
                }, CalendarHour, CalendarMinute, false);
        timepickerdialog.show();
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    public void submit() {
/*
        final FeedbackModel feedbackModel = new FeedbackModel();
        feedbackModel.setName(fName.getText().toString());
        feedbackModel.setCost(Double.parseDouble(cost.getText().toString()));
        feedbackModel.setContactNo(fcNo.getText().toString());
        feedbackModel.setPickup(pickUpLoc.getSelectedItem().toString());
        feedbackModel.setDrop(dropLoc.getSelectedItem().toString());
        feedbackModel.setDropTime(droptext.getText().toString());
        feedbackModel.setPickTime(picktext.getText().toString());*/

        final String feedname = fName.getText().toString();
        final String feedfcno = fcNo.getText().toString();
        final String pickfeed = pickUpLoc.getSelectedItem().toString();
        final String dropfeed = dropLoc.getText().toString();
        final String feedteextt = droptext.getText().toString();
        final String feedpitext = picktext.getText().toString();


        if (feedname.isEmpty() || feedfcno.isEmpty() || pickfeed.isEmpty() || dropfeed.isEmpty() || feedteextt.isEmpty() || feedpitext.isEmpty()) {
            Toast.makeText(Feedback.this, "please fill all details", Toast.LENGTH_SHORT).show();
        }


        Log.e("TAG", "" + fName.getText().toString());
        Log.e("TAG", "" + fcNo.getText().toString());
        Log.e("TAG", "" + pickUpLoc.getSelectedItem().toString());
        Log.e("TAG", "" + dropLoc.getText().toString());
        Log.e("TAG", "" + droptext.getText().toString());
        Log.e("TAG", "" + picktext.getText().toString());
        final String valid = "success";

//        Log.e("TAG", "feedbackModel-" + feedbackModel);
        String url = "http://arasoftwares.in/cabservice-app/android_atncfile.php?action=save";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", "" + response);

                Toast.makeText(Feedback.this, "thanks for your feedback", Toast.LENGTH_SHORT).show();
                fName.setText("");
                fcNo.setText("");
                droptext.setText("Drop time");
                picktext.setText("Pickup time");
                pickUpLoc.setSelection(0);
                dropLoc.setText("Drop Location");


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "" + error);
                Toast.makeText(Feedback.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("name", fName.getText().toString());
                map.put("contactNo", fcNo.getText().toString());
                map.put("pickup", pickUpLoc.getSelectedItem().toString());
                map.put("drop", dropLoc.getText().toString());
                map.put("dropTime", droptext.getText().toString());
                map.put("pickTime", picktext.getText().toString());
                return map;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


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

        startActivity(new Intent(Feedback.this, MasterScreen.class));
        finish();
        super.onBackPressed();
    }

}
