package com.songle.s1505883.songle;

import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import database.AppDatabase;
import database.DatabaseReadTask;
import datastructures.CurrentGameDescriptor;
import datastructures.LocationDescriptor;
import datastructures.Placemarks;
import android.location.LocationListener;
import android.view.View;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.stream.Stream;

import globals.GlobalConstants;
import globals.GlobalLambdas;
import tools.Algorithm;
import tools.DebugMessager;

public class PlayActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{

    private GoogleMap mMap;
    private DebugMessager console = DebugMessager.getInstance();
    private Placemarks placemarks;
    private Map<String, Bitmap> icon_cache;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private CurrentGameDescriptor des;

    private void _init_location_services()
    {
        if (this . mGoogleApiClient == null)
        {
            this . mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    private String _found_word(Location loc)
    {
        float[] results = new float[1];
        for (LocationDescriptor l : placemarks . getDescriptors())
        {
            String[] placemark = l . getCoordinates() . split(",");
            LatLng pos = new LatLng(
                    Double.parseDouble(placemark[1]),
                    Double.parseDouble(placemark[0])
            );

            Location.distanceBetween(
                    loc.getLatitude(),
                    loc.getLongitude(),
                    pos . latitude,
                    pos . longitude,
                    results);

            if (results[0] < GlobalConstants.SONGLE_DISTANCE_WORD_GUESSED_TOLERANCE)
            {
                return l . getWord();
            }
        }
        return null;
    }

    private LocationListener _getListener()
    {
        console . debug_trace(this, "_getListener()");
        return new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
                String found = _found_word(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {

            }

            @Override
            public void onProviderEnabled(String provider)
            {

            }

            @Override
            public void onProviderDisabled(String provider)
            {

            }


        };
    }

    private void _setupLocationListener()
    {
        console . debug_trace(this, "_setupLocationListener");
        LocationManager manager = (LocationManager) this . getSystemService(
                Context.LOCATION_SERVICE
        );

        Runnable locationManagerConsumer = () ->
        {
            manager . requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    (long) 0,
                    (float) 0.0,
                    _getListener()
            );
        };

        _requestLocationPermission(locationManagerConsumer);
    }

    public void _requestLocationPermission(Runnable runnable)
    {
        int check = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
        );

        if (check != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    GlobalConstants.SONGLE_PERMISSIONS_REQUEST_LOCATION);
        }
        else
        {
            runnable . run();
        }
    }

    private Bitmap _getMarkerForCategory(String category)
    {
        if (this . icon_cache . containsKey(category))
        {
            return this . icon_cache . get(category);
        }

        final String cat = this . placemarks . getMarkerURLForCategory(category);
        double scale = this . placemarks . getScaleForCategory(category);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Bitmap> result;
        Callable<Bitmap> call = () -> {
            try
            {
                URL conn_url = new URL(cat);
                return BitmapFactory.decodeStream(
                        conn_url.openConnection().getInputStream()
                );
            }
            catch (Exception e)
            {
                e . printStackTrace();
            }
            return null;
        };

        result = executor . submit(call);

        try
        {
            this . icon_cache . put(category, result . get());
            return result . get();
        }
        catch (Exception e)
        {
            e . printStackTrace();
        }

        return null;
    }


    private void _setMapBoundaries()
    {
        LatLngBounds bounds = new LatLngBounds(
                new LatLng(55.941617, -3.196473),
                new LatLng(55.947233, -3.180319)
        );

        this . mMap . moveCamera(
                CameraUpdateFactory.newLatLngBounds(bounds, 0)
        );
    }

    private void _addMapPoints()
    {
        final class Pair
        {
            MarkerOptions marker;
            String word;

            Pair(MarkerOptions o, String word)
            {
                this . marker = o;
                this . word = word;
            }
        }

        Stream<LocationDescriptor> s =
                this . placemarks . getDescriptors() . stream();

        Stream<Pair> points = s . map((x) -> {
           String[] split = x . getCoordinates() . split(",");
            return new Pair(new MarkerOptions()
                    .position(
                            new LatLng(
                                    Double.parseDouble(split[1]),
                                    Double.parseDouble(split[0])
                            )
                    )
                    .title(x . getCategory() . toUpperCase())
                    .icon(
                            BitmapDescriptorFactory.fromBitmap(
                                    _getMarkerForCategory(x .getCategory())
                            )
                    ),
                    x . getWord());
        });

        points . forEach(x ->
                this . mMap . addMarker(x . marker) . setTag(x . word)
        );

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this . des = getIntent() . getParcelableExtra(GlobalConstants.gameDescriptor);

        console . debug_json_singleton(this . des . getDescriptor());
        console . debug_output(this . des . getDiffs());

        new DatabaseReadTask<>(
                AppDatabase.getAppDatabase(this),
                this::havePlacemarksCallback
        ).execute(
                GlobalLambdas.plm.apply(this . des . getMapNumber(), this . des . getSongNumber())
        );
    }


    @Override
    public void onStart()
    {
        if (this . mGoogleApiClient != null)
        {
            this . mGoogleApiClient . connect();
        }
        super . onStart();
    }

    @Override
    public void onStop()
    {
        if (this . mGoogleApiClient != null)
        {
            this . mGoogleApiClient . disconnect();
        }

        super . onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        _setMapBoundaries();
        _addMapPoints();

        Runnable set = () -> {
            googleMap . setMyLocationEnabled(true);
        };

        _requestLocationPermission(set);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case GlobalConstants.SONGLE_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    try
                    {
                        this . mMap . setMyLocationEnabled(true);
                    }
                    catch (SecurityException e)
                    {
                        console . error("Impossible state");
                    }

                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        _setupLocationListener();
    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }

    private void havePlacemarksCallback(Placemarks placemarks)
    {
        this . placemarks = placemarks;
        this . icon_cache = new HashMap<>();

        _init_location_services();

        setContentView(R.layout.activity_play);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

}
