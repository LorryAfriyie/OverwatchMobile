package com.reidius.lawrenceafriyie.overwatchmap.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.reidius.lawrenceafriyie.overwatchmap.R;
import com.reidius.lawrenceafriyie.overwatchmap.apiclient.ApiClient;
import com.reidius.lawrenceafriyie.overwatchmap.apiclient.ApiInterface;
import com.reidius.lawrenceafriyie.overwatchmap.models.Login;
import com.reidius.lawrenceafriyie.overwatchmap.models.Person;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    // Log
    private static final String TAG = "SigninActivity";
    EditText username;
    EditText password;
    TextView textView;
    CheckBox mCheckbox;

    // Global static constant
    // The error a use will/ or handle get when they do not have the correct version of google play services
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        textView = (TextView) findViewById(R.id.register);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        mCheckbox = (CheckBox) findViewById(R.id.checkbox);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        checkSharedPreferences();

        // Method to launch Main activity
        if (isServicesOK()) {
            Button BtnMap = (Button) findViewById(R.id.btnLogin);
            BtnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mCheckbox.isChecked())
                    {
                        editor.putString(getString(R.string.checkbox), "True");
                        editor.commit();

                        String user = username.getText().toString();
                        editor.putString(getString(R.string.user), user);
                        editor.commit();

                        String pass = password.getText().toString();
                        editor.putString(getString(R.string.pass), pass);
                        editor.commit();

                        String username1 = username.getText().toString();
                        editor.putString("username1", username1);
                        editor.commit();

                    }
                    else{
                        editor.putString(getString(R.string.checkbox), "False");
                        editor.commit();

                        editor.putString(getString(R.string.user), "");
                        editor.commit();

                        editor.putString(getString(R.string.pass), "");
                        editor.commit();

                        String username1 = username.getText().toString();
                        editor.putString("username1", username1);
                        editor.commit();
                    }

                    if(username.getText().toString().isEmpty())
                    {
                        username.setError("Student No cannot be empty!");
                        username.requestFocus();
                    }
                    else if(password.getText().toString().isEmpty())
                    {
                        password.setError("Password cannot be empty!");
                        password.requestFocus();
                    }
                    else
                    {
                        try {
                            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

                            Call<Login> call = apiInterface.getLogin(Integer.parseInt(username.getText().toString()), password.getText().toString());

                            call.enqueue(new Callback<Login>() {
                                @Override
                                public void onResponse(Call<Login> call, Response<Login> response) {
                                    Log.d(TAG, "onResponse:" + response.body());
                                    if(response.body() != null)
                                    {
                                        Toast.makeText(com.reidius.lawrenceafriyie.overwatchmap.activity.SigninActivity.this, "Successfully Logged In ", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                        Toast.makeText(com.reidius.lawrenceafriyie.overwatchmap.activity.SigninActivity.this, "Incorrect Student No or Password ", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onFailure(Call<Login> call, Throwable t) {
                                    Toast.makeText(com.reidius.lawrenceafriyie.overwatchmap.activity.SigninActivity.this, "Incorrect Student No or Password ", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        catch(Throwable t){
                            t.printStackTrace();
                        }
                    }
                }
            });
        } else {
            Toast.makeText(com.reidius.lawrenceafriyie.overwatchmap.activity.SigninActivity.this, "Please Check your Internet Connection", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_splash);
        }

        if (onClick()) {
            onClick();
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        super.onBackPressed();
    }

    private void checkSharedPreferences()
    {
        try{
            String checkbox = preferences.getString(getString(R.string.checkbox), "False");
            String user = preferences.getString(getString(R.string.user), "");
            String pass = preferences.getString(getString(R.string.pass), "");

            username.setText(user);
            password.setText(pass);

            if(checkbox.equals("True"))
            {
                mCheckbox.setChecked(true);
            }
            else{
                mCheckbox.setChecked(false);
            }
        }
        catch(Exception ex)
        {

        }

    }


    // This method will be used to check the version of google play services
    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: check google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(SigninActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //The google play service version is supported and the user can make map request
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        // When an error occurs but it can be resolved
        // This error may occur when the user has the incorrect version but it can be fixed
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServicesOK: An error occurred but it can be be fixed");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(SigninActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Map request could not be made.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean onClick() {
        TextView registration = (TextView) findViewById(R.id.register);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(SigninActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
        return true;
    }

}
