package com.tudresden.mobilecartoapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
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

    ////HEAT MAP////
    //Setting for tile
    private static final double TILE_RADIUS_BASE = 1.47; // default
    private static final float BASE_ZOOM = 12.0f;
    private int mRadiusZoom = (int) BASE_ZOOM;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;

    LocationManager locationManager;
    LocationListener locationListener;
    Location currentLoc;
    MapView mMapView;
    View mView;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;


    ////DATABASE////
    // Working Database
    //String db_name = "locations_db.sqlite";

    // Database for Presentation
    String db_name = "test.sqlite";

    LocationsDAO locationsdao;
    List<Locations> locations_list;
    private GoogleMap mGoogleMap;

    //Geocoder part

    /* public void addAddressToDB() {
        final File dbFile = getActivity().getDatabasePath(db_name);
        if (!dbFile.exists()) {
            try {
                copyDatabaseFile(dbFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        AppDatabase database = Room.databaseBuilder(getActivity(), AppDatabase.class, db_name)
                .allowMainThreadQueries()
                .addMigrations(MIGRATION_1_2)
                .build();
        locationsdao = database.getLocationsDAO();
        locations_list = locationsdao.getAllLocations();


        Locations addAddress = new Locations();
        for (int i = 0; i < locations_list.size(); i++) {

            String lats = locations_list.get(i).getLatitude();
            String lngs = locations_list.get(i).getLongitude();
            String time = locations_list.get(i).getTime();
            // shahtaj! String address = locations_list.get(i).setAddress();

            //convert latlng to doubles
            double lat = Double.parseDouble(lats);
            double lng = Double.parseDouble(lngs);
        //// geocoder


            //addAddress.setAddress(addressString);

        }


    } */


    //Display from Database
    public void showFromDatabase() {

        final File dbFile = getActivity().getDatabasePath(db_name);
        if (!dbFile.exists()) {
            try {
                copyDatabaseFile(dbFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        AppDatabase database = Room.databaseBuilder(getActivity(), AppDatabase.class, db_name)
                .allowMainThreadQueries()
                .addMigrations(MIGRATION_1_2)
                .build();
        locationsdao = database.getLocationsDAO();
        locations_list = locationsdao.getAllLocations();

        final List<LatLng> latLngMarkers = new ArrayList<>();

        //Loop through all locations in database
        for (int i = 0; i < locations_list.size(); i++) {
            String lats = locations_list.get(i).getLatitude();
            String lngs = locations_list.get(i).getLongitude();
            //String time = locations_list.get(i).getTime();
            // shahtaj! String address = locations_list.get(i).getAddress();

            //Convert latlng to doubles
            double lat = Double.parseDouble(lats);
            double lng = Double.parseDouble(lngs);

            latLngMarkers.add(new LatLng(lat, lng));

            mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_grey))
                    .alpha(0.5f));
        }

        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLoc.getLatitude(),currentLoc.getLongitude()), 12));

        // Set gradient of the Heat map
        int[] colors = {
                // ORANGE COLOR SCHEME
                ContextCompat.getColor(getContext(), R.color.heatmap_orange_1), // yellow
                ContextCompat.getColor(getContext(), R.color.heatmap_orange_2), //dark yellow
                ContextCompat.getColor(getContext(), R.color.heatmap_orange_3), // orange
                ContextCompat.getColor(getContext(), R.color.heatmap_orange_4), //bright orange
                ContextCompat.getColor(getContext(), R.color.heatmap_orange_5), // red
        };

        //Starting point for colors: ORANGE COLOR SCHEME
        float[] startPoints = {
                0.3f, 0.4f, 0.5f, 0.6f, 0.8f
        };

        Gradient gradient = new Gradient(colors, startPoints);

        // Create a Heat map tile provider, passing it the latlngs of markers
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
        // Add a tile overlay to the map, using the Heat map tile provider
        mOverlay = mGoogleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //getLocationPermission();
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
        //MapsInitializer.initialize(getContext()); ///what is this
        //googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        showFromDatabase();
        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.050704,13.737443),9));
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


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                currentLoc = location;

                //get initial latlng once map loads
               // LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                //mGoogleMap.clear();
                //mGoogleMap.addMarker(new MarkerOptions().position(myLatLng).title("your loc"));

                // enable current location ellipse/marker
                //mGoogleMap.setMyLocationEnabled(true);

                //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 12));

                //call functions
                //getCurrentLocation(location);
                //getUserLocationUpdate(location);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, locationListener);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1340);
        }

    }


    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
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
        updateLocationUI();
    }


    private void updateLocationUI() {
        if (mGoogleMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                mGoogleMap.setMyLocationEnabled(false);
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    //function to return current time
    public String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy HH:mm", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        return currentDateAndTime;
    }

    /////Timer - gets user latlng and updates
    public void getUserLocationUpdate(final Location location) {
        final Handler handler = new Handler();

        ////change this to change the time interval ////////////////////////
        final int timeInterval = 600000; //10 min in milliseconds = 600000

        handler.postDelayed(new Runnable() {
            public void run() {

                LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                String lat = String.valueOf(location.getLatitude());
                String lng = String.valueOf(location.getLongitude());
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 12));

                //insert lat lng
                Locations seb = new Locations();

                seb.setTime(getCurrentTime());
                seb.setLatitude(lat);
                seb.setLongitude(lng);
                locationsdao.insert(seb);
                handler.postDelayed(this, timeInterval);

            }
        }, timeInterval);

    }

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
