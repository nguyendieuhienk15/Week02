package com.example.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        ClusterManager.OnClusterClickListener<MyItem>,
        ClusterManager.OnClusterItemClickListener<MyItem>,
        ClusterManager.OnClusterItemInfoWindowClickListener<MyItem> {

    private static final int PERMISSION_REQUEST_LOCATION = 1;

    private GoogleMap mMap;
    private ClusterManager<MyItem> mClusterManager;
    private List<MyItem> mLocations = new ArrayList<MyItem>();;
    private Marker currentLocationMarker;
    private LatLng currentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        currentLocation = null;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        initialLocationManagement();

        /* VIEW MODE */
        Switch switch_button = (Switch) findViewById(R.id.switch1);

        // Set a checked change listener for switch button
        switch_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Intent intent= getPackageManager().getLaunchIntentForPackage("com.example.android.xyztouristattractions");
                    startActivity(intent);

                    // Show the switch button checked status as toast message
                    Toast.makeText(getApplicationContext(),
                            "Showing list", Toast.LENGTH_LONG).show();

                    finish();
                    System.exit(0);
                }
                else {
                    // Show the switch button checked status as toast message
                    Toast.makeText(getApplicationContext(),
                            "Switch is off", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        styleMap();
    }

    /*
    *   MAP STYLING, CHECK PERMISSION, ...
    * */

    private void styleMap(){
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));
        } catch (Resources.NotFoundException e) {

        }
    }

    private void updateCurrentLocation(double lat, double lng) {
        currentLocation = new LatLng(lat, lng);
        if (currentLocationMarker != null)
            currentLocationMarker.remove();
        currentLocationMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("You are here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
    }

    private void initialLocationManagement() {
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if (currentLocation == null) {
                    // First time
                    LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                    GetNearbyPlacesTask getNearbyPlacesTask = new GetNearbyPlacesTask();
                    getNearbyPlacesTask.execute(NearbyPlacesHelper.getRequestString(current, 5000, "lodging"));

                }
                updateCurrentLocation(location.getLatitude(), location.getLongitude());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_LOCATION);
            return;
        }
        assert locationManager != null;
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }

    @Override
    public boolean onClusterClick(Cluster<MyItem> cluster) {
        return false;
    }

    @Override
    public boolean onClusterItemClick(MyItem item) {
        //if (direction == null)
        //    direction = new Direction(mMap);
        //else
         //   path.remove();

        Direction direction = new Direction(mMap);
        direction.drawDirection(item.getPosition(), currentLocation);
        //direction.setDirection(direction);
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(MyItem item) {
        Toast.makeText(getApplicationContext(),
                "Loading info", Toast.LENGTH_LONG).show();
        Intent intent= getPackageManager().getLaunchIntentForPackage("com.example.android.xyztouristattractions");
        startActivity(intent);
    }

    private class GetNearbyPlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                data = NearbyPlacesHelper.downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParsePlacesTask parserTask = new ParsePlacesTask();
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places JSON
     */
    private class ParsePlacesTask extends AsyncTask<String, Void, List<MyItem>> {

        @Override
        protected List<MyItem> doInBackground(String... strings) {
            List<MyItem> locationList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(strings[0]);
                JSONArray results = jsonObject.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject jsonResult = results.getJSONObject(i);
                    JSONObject jsonLocation = jsonResult.getJSONObject("geometry").getJSONObject("location");
                    LatLng location = new LatLng(jsonLocation.getDouble("lat"), jsonLocation.getDouble("lng"));
                    String name = jsonResult.getString("name");
                    double rating = 0;
                    if (jsonResult.has("rating"))
                        rating = jsonResult.getDouble("rating");
                    MyItem myItem = new MyItem(location, name, String.valueOf(rating));
                    locationList.add(myItem);
                }
                return locationList;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<MyItem> locations) {
            Log.d("SIZE", String.valueOf(locations.size()));
            for (MyItem item : locations) {
                //mMap.addMarker(new MarkerOptions()
                //        .position(item.getPosition()));
                mLocations.add(item);
            }

            mMap.animateCamera( CameraUpdateFactory.zoomTo( 13 ) );
            initialClusterManager();
            mClusterManager.cluster();
        }
    }


    /*
    *       MARKERS
    * */

    public void initialClusterManager(){
        mClusterManager = new ClusterManager<MyItem>(this, mMap);
        mClusterManager.setRenderer(new MyMarker(this, mMap, mClusterManager));
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        mClusterManager.addItems(mLocations);
    }

    /*public void drawMarkers(){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        mClusterManager = new ClusterManager<MyItem>(this, mMap);
        mClusterManager.setRenderer(new MyMarker(this, mMap, mClusterManager));
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        try {
            readItems();
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show();
        }

        mClusterManager.cluster();
    }

    private void readItems() throws JSONException {
        InputStream inputStream = getResources().openRawResource(R.raw.places);
        List<MyItem> items = new MyItemReader().read(inputStream);

        mClusterManager.addItems(items);
    }*/
}
