package com.reidius.lawrenceafriyie.overwatchmap.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.reidius.lawrenceafriyie.overwatchmap.R;
import com.reidius.lawrenceafriyie.overwatchmap.activity.IncidentView;
import com.reidius.lawrenceafriyie.overwatchmap.adapter.IncidentViewAdapter;
import com.reidius.lawrenceafriyie.overwatchmap.apiclient.ApiClient;
import com.reidius.lawrenceafriyie.overwatchmap.apiclient.ApiInterface;
import com.reidius.lawrenceafriyie.overwatchmap.models.ReportIncident;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClosedIncidents extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private IncidentViewAdapter incidentViewAdapter;
    private List<ReportIncident> incidentList;


    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closed_incidents);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        final String id = preferences.getString("username1", "default");

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        try{
            recyclerView = (RecyclerView)findViewById(R.id.incident_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            incidentViewAdapter = new IncidentViewAdapter();
            recyclerView.setAdapter(incidentViewAdapter);

            incidentList = new ArrayList<>();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<List<ReportIncident>> call = apiInterface.getIncidents(Integer.parseInt(id.toString()), "Closed");

            call.enqueue(new Callback<List<ReportIncident>>() {
                @Override
                public void onResponse(Call<List<ReportIncident>> call, Response<List<ReportIncident>> response) {
                    incidentList = response.body();
                    incidentViewAdapter.IncidentViewAdapter(getApplicationContext(), incidentList);

                    Log.d("IncidentView", incidentList.toString());
                }

                @Override
                public void onFailure(Call<List<ReportIncident>> call, Throwable t) {
                    Log.d("IncidentView", t.toString());
                }
            });
        }
        catch (Exception e){
            Toast.makeText(this, "Something went wrong getting Closed Incidents information from the server.", Toast.LENGTH_LONG).show();
        }
    }

    // Search for incidents
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.incident_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.incident_serach).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                incidentViewAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                incidentViewAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.incident_serach){
            return true;
        }

        if(item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(!searchView.isIconified()){
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}
