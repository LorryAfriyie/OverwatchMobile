package com.reidius.lawrenceafriyie.overwatchmap.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.reidius.lawrenceafriyie.overwatchmap.R;
import com.reidius.lawrenceafriyie.overwatchmap.adapter.EmergencyServiceAdapter;
import com.reidius.lawrenceafriyie.overwatchmap.apiclient.ApiClient;
import com.reidius.lawrenceafriyie.overwatchmap.apiclient.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmergencyService extends AppCompatActivity {
    final static String TAG = "EmergecyService";

    List<com.reidius.lawrenceafriyie.overwatchmap.models.EmergencyService> emergencyServiceList;
    EmergencyServiceAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_service);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        emergencyServiceList = new ArrayList<>();

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<com.reidius.lawrenceafriyie.overwatchmap.models.EmergencyService>>call = apiService.getEmergencyService();
        call.enqueue(new Callback<List<com.reidius.lawrenceafriyie.overwatchmap.models.EmergencyService>>() {
            @Override
            public void onResponse(Call<List<com.reidius.lawrenceafriyie.overwatchmap.models.EmergencyService>> call, Response<List<com.reidius.lawrenceafriyie.overwatchmap.models.EmergencyService>> response) {
                emergencyServiceList = response.body();
                Log.d(TAG, "onResponse:" + emergencyServiceList.toString());
                adapter = new EmergencyServiceAdapter(getApplicationContext(), emergencyServiceList);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<com.reidius.lawrenceafriyie.overwatchmap.models.EmergencyService>> call, Throwable t) {
                Log.d(TAG, "onFailure: Response: " + t.toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }
}
