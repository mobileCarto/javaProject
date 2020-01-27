package com.tudresden.mobilecartoapp;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.tudresden.mobilecartoapp.AppDatabase.MIGRATION_1_2;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    ///////////////////////heatmap variables///////////////
    private static final double TILE_RADIUS_BASE = 1.47; // default
    private static final float BASE_ZOOM = 12.0f;
    private int mRadiusZoom = (int) BASE_ZOOM;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;


    MapView mMapView;
    View mView;


    ///////////////////////user location variables///////////////
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    //private static final String DEFAULT_ZOOM = "15";

    private boolean mLocationPermissionGranted;
    private static final String KEY_LOCATION = "location";
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private CameraPosition mCameraPosition;


    ///////////////////////database variables///////////////
    //String db_name = "locations_db.sqlite"; database to capture user location
    String db_name = "test.sqlite"; //database for presentation
    LocationsDAO locationsdao;
    List<Locations> locations_list;
    private GoogleMap mGoogleMap;

    ////show points from database
    public void showFromDatabase() {

        //get database
        final File dbFile = getActivity().getDatabasePath(db_name);
        if (!dbFile.exists()) {
            try {
                copyDatabaseFile(dbFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //setup access for database
        AppDatabase database = Room.databaseBuilder(getActivity(), AppDatabase.class, db_name)
                .allowMainThreadQueries()
                .addMigrations(MIGRATION_1_2)
                .build();
        locationsdao = database.getLocationsDAO();
        locations_list = locationsdao.getAllLocations();

        final List<LatLng> latLngMarkers = new ArrayList<>();

        //loop through all locations in database
        for (int i = 0; i < locations_list.size(); i++) {
            String lats = locations_list.get(i).getLatitude();
            String lngs = locations_list.get(i).getLongitude();

            //convert latlng to doubles
            double lat = Double.parseDouble(lats);
            double lng = Double.parseDouble(lngs);

            latLngMarkers.add(new LatLng(lat, lng));

            mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_grey))
                    .alpha(0.5f));
        }

        // Set gradient
        int[] colors = {
                //Color.rgb(79, 195, 247), // blue

                //Color.rgb(255,255,255), //white
                //Color.rgb(240,255,80), //light yellow

                //Color.rgb(255, 253, 2), // yellow

                //Color.rgb(255, 152, 0), // orange

                //Color.rgb	(238,62,50), //bright red
                //Color.rgb(244, 67, 54)   // red

                //Color.rgb(0,0,255), // blue
                //Color.rgb(0,255,255), // cyan
                //Color.rgb(50, 205, 50),//lime
                Color.rgb(255,255,0),//yellow
                Color.rgb(251, 176, 33), //dark yellow
                Color.rgb(246, 136, 56),//bright orange
                Color.rgb(255,0,0)//red


//                // ORANGE COLOR SCHEME
//                ContextCompat.getColor(getContext(), R.color.heatmap_orange_1), // yellow
//                ContextCompat.getColor(getContext(), R.color.heatmap_orange_2), //dark yellow
//                ContextCompat.getColor(getContext(), R.color.heatmap_orange_3), // orange
//                ContextCompat.getColor(getContext(), R.color.heatmap_orange_4), //bright orange
//                ContextCompat.getColor(getContext(), R.color.heatmap_orange_5), // red

                // WHITE - BLUE COLOR SCHEME
               // ContextCompat.getColor(getContext(), R.color.heatmap_blue_1), // light blue
                //ContextCompat.getColor(getContext(), R.color.heatmap_blue_2), //
                //ContextCompat.getColor(getContext(), R.color.heatmap_blue_3), //
                //ContextCompat.getColor(getContext(), R.color.heatmap_blue_4), // dark blue
        };

//        //starting point for colors: ORANGE COLOR SCHEME
//        float[] startPoints = {
//                0.3f, 0.4f, 0.5f, 0.6f, 0.8f
//        };

        //starting point for colors: WHITE - BLUE COLOR SCHEME
        float[] startPoints = {

                0.4f, 0.5f, 0.7f, 1.0f

        };

        Gradient gradient = new Gradient(colors, startPoints);

        // Create a heat map tile provider, passing it the latlngs of the police stations
        int radius = (int) Math.floor(Math.pow(TILE_RADIUS_BASE, mRadiusZoom));
        mProvider = new HeatmapTileProvider.Builder()
                .data(latLngMarkers)
                .gradient(gradient)
                .opacity(0.8f)
                .build();
        mProvider.setRadius(radius);
        if (mOverlay != null) {
            mOverlay.clearTileCache();
        }
        // Add a tile overlay to the map, using the heat map tile provider
        mOverlay = mGoogleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getLocationPermission();
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mGoogleMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mGoogleMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, container, false);

        return mView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mGoogleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity(), R.raw.style_map));

            if (!success) {
                Log.e("MapsActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivity", "Can't find style. Error: ", e);
        }


        //MapsInitializer.initialize(getContext()); ///what is this

        //call permissions to get location
        //getLocationPermission();

        //function to show data points from database
        showFromDatabase();
        getDeviceLocation();

    }


    /*gets users locations and sends it to getUserLocationUpdate() function to update database,
     zooms to user location to start */
    private void getDeviceLocation() {
        // log message in console to see function execution
        Log.d("userLocation", "getting user device location");
        
        try {
            // check if location is granted
            if (mLocationPermissionGranted) {
                // get user's last location of object type Task
                Task location = mFusedLocationProviderClient.getLastLocation();

                // listen for users location
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(Task task) {
                        if (task.isSuccessful()) {

                            // log current location success
                            Log.d("seb", "onComplete: got your location!");

                            // get result of current location
                            Location currentLocation = (Location) task.getResult();
                            mLastKnownLocation = (Location) task.getResult();

                            //move camera to users location
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 12));

                            //uncomment for using the app - sends location to function to save to database every 10 minutes
                            //getUserLocationUpdate(currentLocation);

                        } else {
                            // log
                            Log.d("seb", "onComplete: Can't find you");
                            // display current location error as toast message
                            Toast.makeText(getContext(), "Unable to get current location", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

            }

        } catch (SecurityException e) {
            // log error to console
            Log.d("securityError", "Getting device location security message" + e.getMessage());
        }

    }

    //check permissions for location
    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    //function to return current time
    public String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy HH:mm", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        return currentDateAndTime;
    }

    //adds current location of user to the database every 10 minutes
    public void getUserLocationUpdate(final Location location) {
        final Handler handler = new Handler();

        final int timeInterval = 600000; //10 min in milliseconds = 600000

        handler.postDelayed(new Runnable() {
            public void run() {

                LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                String lat = String.valueOf(location.getLatitude());
                String lng = String.valueOf(location.getLongitude());
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 12));

                //insert lat lng
                Locations currentLocationInsert = new Locations();

                currentLocationInsert.setTime(getCurrentTime());
                currentLocationInsert.setLatitude(lat);
                currentLocationInsert.setLongitude(lng);
                locationsdao.insert(currentLocationInsert);
                handler.postDelayed(this, timeInterval);

            }
        }, timeInterval);

    }

    //helper function to copy database file
    private void copyDatabaseFile(String destinationPath) throws IOException {
        InputStream assetsDB = getActivity().getAssets().open(db_name);
        OutputStream dbOut = new FileOutputStream(destinationPath);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = assetsDB.read(buffer)) > 0) {
            dbOut.write(buffer, 0, length);
        }
        dbOut.flush();
        dbOut.close();
    }
}
