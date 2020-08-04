package com.reidius.lawrenceafriyie.overwatchmap.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crash.FirebaseCrash;
import com.reidius.lawrenceafriyie.overwatchmap.R;
import com.reidius.lawrenceafriyie.overwatchmap.activities.PlaceInfo;
import com.reidius.lawrenceafriyie.overwatchmap.adapter.EmergencyServiceAdapter;
import com.reidius.lawrenceafriyie.overwatchmap.apiclient.ApiClient;
import com.reidius.lawrenceafriyie.overwatchmap.apiclient.ApiInterface;
import com.reidius.lawrenceafriyie.overwatchmap.models.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener{
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    // safezone and hotspot list
    public List<com.reidius.lawrenceafriyie.overwatchmap.models.Safezone> safezonelist;
    public List<com.reidius.lawrenceafriyie.overwatchmap.models.Hotspot> hotspotlist;
    // user details
    //Log
    private static final String TAG = "MapActivity";

    // Menu
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    //  Widget Section
    private AutoCompleteTextView mSearchText;
    private ImageView mGps, mInfo, mPlacePicker, mLocation;

    //Toggle switch
    private Switch safezoneSwitch;
    private  Switch hotspotSwitch;

    Circle shape;

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Menu
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Navigation View
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Switch Functions
        Menu menu = navigationView.getMenu();
        MenuItem item = menu.findItem(R.id.safezone);
        MenuItem item1 = menu.findItem(R.id.hotspot);
        MenuItem item2 = menu.findItem(R.id.location);
        View safeZoneToggle = MenuItemCompat.getActionView(item);
        View hotspotToggle = MenuItemCompat.getActionView(item1);
        safezoneSwitch = safeZoneToggle.findViewById(R.id.safezone_switch);
        hotspotSwitch = hotspotToggle.findViewById(R.id.hotspot_switch);

        safezoneSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    Toast.makeText(getApplicationContext(), "Overwatch Safe Zones On. ",
                            Toast.LENGTH_SHORT).show();

                    safezonelist = new ArrayList<>();
                    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                    Call<List<com.reidius.lawrenceafriyie.overwatchmap.models.Safezone>>
                            call = apiService.getSafezones();
                    call.enqueue(new Callback<List<com.reidius.lawrenceafriyie.overwatchmap.models.Safezone>>() {
                        @Override
                        public void onResponse(Call<List<com.reidius.lawrenceafriyie.overwatchmap.models.Safezone>> call, Response<List<com.reidius.lawrenceafriyie.overwatchmap.models.Safezone>> response) {
                            safezonelist = response.body();
                            Log.d(TAG, "onResponse:" + safezonelist.toString());
                            if(safezonelist.size() != 0)
                            {
                                for(int i = 0; i <safezonelist.size(); i++)
                                {
                                    Double longi = safezonelist.get(i).getLongitude();
                                    Double lati  = safezonelist.get(i).getLatidude();
                                    String name = safezonelist.get(i).getName();

                                    SafeZones[] SAFE_ZONES = new SafeZones[]{
                                            new SafeZones(new LatLng(longi, lati), new String(name))
                                    };

                                    LatLng latLng = new LatLng(lati, longi);
                                    CircleOptions circleOptions = new CircleOptions();
                                    for(int ix = 0; ix < SAFE_ZONES.length; ix++){
                                        mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker()).title(SAFE_ZONES[ix].mId));
                                        circleOptions.strokeWidth(3);
                                        circleOptions.strokeColor(Color.GREEN);
                                        circleOptions.center(latLng);
                                        circleOptions.radius(50);
                                        circleOptions.fillColor(0x330000FF);
                                        shape = mMap.addCircle(circleOptions);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<com.reidius.lawrenceafriyie.overwatchmap.models.Safezone>> call, Throwable t) {
                            Log.d(TAG, "onFailure: Response: " + t.toString());
                        }
                    });

                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Overwatch Safe Zones Off. ", Toast.LENGTH_SHORT).show();
                    mMap.clear();
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });


        hotspotSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(getApplicationContext(), "Overwatch Hotspots On.", Toast.LENGTH_SHORT).show();

                    hotspotlist = new ArrayList<>();
                    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                    Call<List<com.reidius.lawrenceafriyie.overwatchmap.models.Hotspot>> call = apiService.getHotspots();
                    call.enqueue(new Callback<List<com.reidius.lawrenceafriyie.overwatchmap.models.Hotspot>>() {
                        @Override
                        public void onResponse(Call<List<com.reidius.lawrenceafriyie.overwatchmap.models.Hotspot>> call, Response<List<com.reidius.lawrenceafriyie.overwatchmap.models.Hotspot>> response) {
                            hotspotlist = response.body();
                            Log.d(TAG, "onResponse:" + hotspotlist.toString());
                            if(hotspotlist.size() != 0)
                            {
                                for(int i = 0; i <hotspotlist.size(); i++)
                                {
                                    Double longi = hotspotlist.get(i).getLongitude();
                                    Double lati  = hotspotlist.get(i).getLatitude();


                                    Hotspots[] HOTSPOT = new Hotspots[]{
                                            new Hotspots(new LatLng(longi, lati), new String(""))
                                    };

                                    LatLng latLng = new LatLng(lati, longi);
                                    CircleOptions circleOptions = new CircleOptions();
                                    for(int ix = 0; ix < HOTSPOT.length; ix++){
                                        mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker()).title(HOTSPOT[ix].mId));
                                        circleOptions.strokeWidth(3);
                                        circleOptions.strokeColor(Color.RED);
                                        circleOptions.center(latLng);
                                        circleOptions.radius(50);
                                        circleOptions.fillColor(0x330000FF);
                                        shape = mMap.addCircle(circleOptions);
                                    }
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<List<com.reidius.lawrenceafriyie.overwatchmap.models.Hotspot>> call, Throwable t) {
                            Log.d(TAG, "onFailure: Response: " + t.toString());
                        }
                    });

                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Overwatch Hotspots Off", Toast.LENGTH_SHORT).show();
                    mMap.clear();
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });

        //Map
        // Location Permission

        //Search Text
        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
        //Navigation pin
        mGps = (ImageView) findViewById(R.id.ic_gps);
        //Location Information
        mInfo = (ImageView) findViewById(R.id.place_info);
        //Place Picker
        mPlacePicker = (ImageView) findViewById(R.id.place_picker);
        //Location on/off
        mLocation = (ImageView) findViewById(R.id.location);
        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationPermission();
                mLocation.setClickable(false);
            }
        });

        //Safezone and hotspot switch
        safezoneSwitch = (Switch) findViewById(R.id.safezone_switch);
        hotspotSwitch = (Switch)findViewById(R.id.hotspot);

        // Geofence Section

    }


    // Menu Open/Close
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;


        }
        return super.onOptionsItemSelected(item);
    }
    // Open the selected menu option
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.emergency_service:
                Intent intent1 = new Intent(this, EmergencyService.class);
                startActivity(intent1);
                break;

            case R.id.report_incident:
                Intent intent = new Intent(this, ReportIncident.class);
                startActivity(intent);
                break;

            case R.id.help:
                Intent intent2 = new Intent(this, About.class);
                startActivity(intent2);
                break;
            case R.id.view_incident:
                Intent intent3 = new Intent(this, IncidentView.class);
                startActivity(intent3);
                break;
            case R.id.inprogress:
                Intent intent4 = new Intent(this, InProgress.class);
                startActivity(intent4);
                break;
            case R.id.closed:
                Intent intent5 = new Intent(this, ClosedIncidents.class);
                startActivity(intent5);
                break;
            case R.id.logout:
                AlertDialog.Builder confirm = new AlertDialog.Builder(MapActivity.this);
                confirm.setMessage("Are you sure you want to Logout?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent6 = new Intent(MapActivity.this, SigninActivity.class);
                        startActivity(intent6);
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = confirm.create();
                alert.setTitle("Logout Confirmation");
                alert.show();
                break;
            case R.id.help_hint:
                Intent intent6 = new Intent(this,HelpHint.class);
                startActivity(intent6);
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        if(!mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            //Close the application
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    // Map Activities
    //---------------
    // Map Variables
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 17f;
    private static final int PLACE_PICKER_REQUEST = 1;
    // Latitude and Longitude bounds
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168),
            new LatLng(71, 130));

    // Variables
    private boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace;
    private Marker mMarker;

    // Safe Zone
    static class SafeZones {
        private LatLng mLatLng;
        private String mId;
        SafeZones(LatLng latLng, String id) {
            mLatLng = latLng;
            mId = id;
        }
    }
    // Hotspots
    static class Hotspots {
        private LatLng mLatLng;
        private String mId;
        Hotspots(LatLng latLng, String id) {
            mLatLng = latLng;
            mId = id;
        }
    }
    // GeoLocate
    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.d(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: Found location" + address.toString());
            // Move the camera to the searched for location
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
        }
    }
    
    // Init Method
    private void init(){

        Log.d(TAG, "init: initializing");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        mSearchText.setOnItemClickListener(mAutoCompleteClickListener);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, LAT_LNG_BOUNDS
        ,null);

        mSearchText.setAdapter(mPlaceAutocompleteAdapter);
        
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            // Override the Enter Key
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId ==EditorInfo.IME_ACTION_DONE  ||
                        event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    
                    // Execute searching
                    // GeoLocating the search
                    geoLocate();
                }
                return false;
            }
        });
        hideSoftKeyboard();
        // Navigate back to the device location
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked on GPS icon");
                getDeviceLocation();
                mMap.clear();
            }
        });
        // Display information about a searched for location
        mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked place information");
                try{
                    if(mMarker.isInfoWindowShown()){
                        mMarker.hideInfoWindow();
                    } else {
                        mMarker.showInfoWindow();
                        Log.d(TAG, "onClick: Place Information: " + mPlace.toString());
                    }
                } catch (NullPointerException e){
                    Log.d(TAG, "onClick: NullPointerException: " + e.getMessage());
                }
            }
        });
        // Open a place picker window
        mPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(MapActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.d(TAG, "onClick: GooglePlayServicesRepairableException: " + e.getMessage() );
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.d(TAG, "onClick: GooglePlayServicesRepairableException: " + e.getMessage() );
                }
            }
        });
        // Add a marker when a location on a map is clicked

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace( this, data);

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, place.getId());
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            }
        }
    }

    // Getting the device's location
    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: Getting the device's current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Current location");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM, "My Location");

                            // Move the camera to the device's current location
                        } else {
                            Log.d(TAG, "onComplete: Current location not found");
                            Toast.makeText(MapActivity.this,
                                    "Unable to get the device current location",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    // Move the camera to the current location of the device, this method will be used by other functions such as searching for a location
    private void moveCamera(LatLng latLng, float zoom, PlaceInfo placeInfo) {
        Log.d(TAG, "moveCamera: Moving the camera to : lat: " + latLng.latitude + "lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        //Clear the map
        mMap.clear();

        // Custom Information window display
        mMap.setInfoWindowAdapter(new ReidiusInfoWindowAdapter(MapActivity.this));

        if(placeInfo != null){
            try{
                String snippet = "Address: " + placeInfo.getAddress() + "\n" +
                        "Phone Number: " + placeInfo.getPhoneNumber() + "\n" +
                        "Website: " + placeInfo.getWebsiteUri() + "\n";

                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(placeInfo.getName())
                        .snippet(snippet);

                mMarker = mMap.addMarker(options);

            }catch (NullPointerException e){
                Log.d(TAG, "moveCamera: NullPointerException " + e.getMessage());
            }
        } else {
            mMap.addMarker(new MarkerOptions().position(latLng));
        }
        hideSoftKeyboard();
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: Moving the camera to : lat: " + latLng.latitude + "lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if(!title.equals("My Location")) {

            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }
        hideSoftKeyboard();
    }

    // Initialize Map Method
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Overwatch Map has initiated", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Map is Ready");
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation();
            // Check if permission has been granted
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            // Disable the default location button
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            //Geolocate search
            init();


        }
    }
    private void initMap(){
        Log.d(TAG, "initMap: Initializing Map");
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);


    }

    // Check for permission
    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: Getting location permission");
        // Send permission request
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    // Look for a permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionResult: This method was called");
        mLocationPermissionGranted = false;
        // Check request code
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionResult: Permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionResult: Permission Granted");
                    mLocationPermissionGranted = true;
                    // initialize the map
                    initMap();
                }
            }
        }
    }
    // Hide the Keyboard when searching
    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /*
        Google Places API autocomplete suggestions
     */

    // Move to the location suggested on the autocomplete suggestion list
    private AdapterView.OnItemClickListener mAutoCompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            hideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };
    // When the callback is successful, the location will be retrieved
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            // A place was got successfully gotten
            if(!places.getStatus().isSuccess()){
                Log.d(TAG, "onResult: Place query did not complete successfully: " +
                        places.getStatus().toString());
                // To prevent memory leaks
                places.release();
                return;
            }
            final Place place = places.get(0);

            try{
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                mPlace.setAddress(place.getAddress().toString());
                //mPlace.setAttributions(place.getAttributions().toString());
                mPlace.setId(place.getId());
                mPlace.setLatlng(place.getLatLng());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                mPlace.setWebsiteUri(place.getWebsiteUri());
                Log.d(TAG, "onResult: place: " + mPlace.toString());
            } catch(NullPointerException e) {
                Log.d(TAG, "onResult: NullPointerException: " + e.getMessage());
            }

            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace);
            places.release();
        }
    };

    /// Geofencing

}
