package com.reidius.lawrenceafriyie.overwatchmap.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.reidius.lawrenceafriyie.overwatchmap.R;

public class MainActivity  extends AppCompatActivity{

    // Log
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    SharedPreferences preferences;

    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            editor = preferences.edit();

            final String id = preferences.getString("username1", "default");
            Log.d(TAG, "HeaderResponse:" + id);

            Button BtnMap = (Button) findViewById(R.id.btnMap);
            BtnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, MapActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("personID", id.toString());
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });


        }
        catch(Exception ex)
        {
        }


    }


}
