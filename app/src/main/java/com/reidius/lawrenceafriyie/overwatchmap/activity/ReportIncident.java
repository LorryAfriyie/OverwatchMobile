package com.reidius.lawrenceafriyie.overwatchmap.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.reidius.lawrenceafriyie.overwatchmap.R;
import com.reidius.lawrenceafriyie.overwatchmap.apiclient.ApiClient;
import com.reidius.lawrenceafriyie.overwatchmap.apiclient.ApiInterface;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportIncident extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView textView;
    EditText comment;

    // GPS
    TextView text;
    Button gps;
    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    Geocoder geocoder;
    List<Address> addresses;

    //for testing
    Double longitude = 0.00;
    Double latitude = 0.00;
    Integer campusID = 0;


    private String TAG = "ReportIncident";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);



        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        final EditText personID = (EditText) findViewById(R.id.personID);
        final String id = preferences.getString("username1", "default");


        //Spinner
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.campus, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // GPS Coordinates
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        getLocation();


        try {
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable ex) {
                    FirebaseCrash.report(ex);
                }
            });
            //final EditText personID = (EditText) findViewById(R.id.personID);
            final Calendar calendar = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy / MM / dd / HH:mm:ss");
            final String dateTime = df.format(calendar.getTime());


            /////////////////////////////////
            radioGroup = findViewById(R.id.radio_group);
            textView = findViewById(R.id.text_view_incident);
            comment = findViewById(R.id.comment);

            Button report = findViewById(R.id.btnReport);
            report.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if(comment.getText().equals(null) || comment.getText().toString().equals("")){
                        comment.setError("Please enter your comment!");
                        comment.requestFocus();
                    }
                    else{

                        AlertDialog.Builder confirm = new AlertDialog.Builder(ReportIncident.this);
                        confirm.setMessage("Are you sure you want to report the Incident?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                try {
                                    Integer typeID = -99;
                                    int radioId = radioGroup.getCheckedRadioButtonId();

                                    radioButton = findViewById(radioId);
                                    textView.setText("Your choice: " + radioButton.getText());

                                    if (radioButton.getText().equals("Sexual Assault"))
                                        typeID = 1;
                                    else if (radioButton.getText().equals("Theft"))
                                        typeID = 2;
                                    else if (radioButton.getText().equals("Hijack"))
                                        typeID = 3;
                                    else if (radioButton.getText().equals("Accident"))
                                        typeID = 4;
                                    else if (radioButton.getText().equals("Other"))
                                        typeID = 5;

                                    //Spinner logic
                                    if(spinner.getSelectedItem().equals("South Campus"))
                                        campusID = 2;
                                    else if(spinner.getSelectedItem().equals("North Campus"))
                                        campusID = 1;
                                    else if(spinner.getSelectedItem().equals("2nd Ave Campus"))
                                        campusID = 3;
                                    else if (spinner.getSelectedItem().equals("Missionvale Campus"))
                                        campusID = 4;

                                    //////////////////////////////////////////////////////////////////////
                                    com.reidius.lawrenceafriyie.overwatchmap.models.ReportIncident reportIncident =
                                            new com.reidius.lawrenceafriyie.overwatchmap.models.ReportIncident(
                                                    radioButton.getText().toString(), Integer.parseInt(id.toString()), ((EditText)findViewById(R.id.etLongitude)).getText().toString(),
                                                    ((EditText)findViewById(R.id.etLatitude)).getText().toString(), dateTime, "Open", Integer.parseInt(typeID.toString()), comment.getText().toString(),
                                                    Integer.parseInt(campusID.toString()),  ((EditText)findViewById(R.id.address_edit_text)).getText().toString()
                                            );
                                    sendReport(reportIncident);
                                    personID.setText("");

                                } catch (Exception ex) {
                                    FirebaseCrash.report(new Exception("App Name : My first Android non-fatal error"));

                                    Toast.makeText(ReportIncident.this, "Check if Student No is Registered ", Toast.LENGTH_LONG).show();
                                }
                                finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alert = confirm.create();
                        alert.setTitle("Report Incident Confirmation");
                        alert.show();
                    }


                }

                public void sendReport(com.reidius.lawrenceafriyie.overwatchmap.models.ReportIncident reportIncident) {

                    try {
                        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                        final Call<com.reidius.lawrenceafriyie.overwatchmap.models.ReportIncident> call = apiService.sendReport(reportIncident);

                        call.enqueue(new Callback<com.reidius.lawrenceafriyie.overwatchmap.models.ReportIncident>() {
                            @Override
                            public void onResponse(Call<com.reidius.lawrenceafriyie.overwatchmap.models.ReportIncident> call, Response<com.reidius.lawrenceafriyie.overwatchmap.models.ReportIncident> response) {
                                Toast.makeText(ReportIncident.this, "Incident Successfully Reported ", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<com.reidius.lawrenceafriyie.overwatchmap.models.ReportIncident> call, Throwable t) {
                                Toast.makeText(ReportIncident.this, "Check if Student No is Registered ", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }

            });
        } catch (Exception ex) {
            FirebaseCrash.report(new Exception("App Name : My first Android non-fatal error"));
            finish();
            startActivity(getIntent());
        }
    }

    void getLocation(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {

                double lat = location.getLatitude();
                double longi = location.getLongitude();
                geocoder = new Geocoder(this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(lat, longi, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String address = addresses.get(0).getAddressLine(0);

                ((EditText)findViewById(R.id.address_edit_text)).setText(address);
                ((EditText)findViewById(R.id.etLatitude)).setText("" + lat);
                ((EditText)findViewById(R.id.etLongitude)).setText("" + longi);
            } else {
                ((EditText)findViewById(R.id.etLatitude)).setText("Unable to find correct location");
                ((EditText)findViewById(R.id.etLongitude)).setText("Unable to find correct location");
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void checkButton(View v){

        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        Toast.makeText(this,"Selected Radio Button: " + radioButton.getText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "You have selected " + text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
