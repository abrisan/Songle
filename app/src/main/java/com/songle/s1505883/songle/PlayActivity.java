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
import database.DatabaseWriteTask;
import datastructures.CurrentGameDescriptor;
import datastructures.LocationDescriptor;
import datastructures.Placemarks;
import android.location.LocationListener;
import android.view.View;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
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
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{

    private GoogleMap mMap;
    private DebugMessager console = DebugMessager.getInstance();
    private Placemarks placemarks;
    private Map<String, Bitmap> icon_cache;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private CurrentGameDescriptor des;
    private List<LocationDescriptor> buffer = new ArrayList<>();
    private Stack<Runnable> runnableStack = new Stack<>();
    private LocationManager manager;
    private final int TOAST_DURATION = 100;
    private int picked_up = 0;

    private void _init_location_services()
    {
        console . debug_trace(this, "_init_location_services");
        if (this . mGoogleApiClient == null)
        {
            // create the client
            console . debug_trace(this, "_init_location_services", "apiClient is null");
            this . mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            // and connect it
            this . mGoogleApiClient . connect();
        }

    }


    private List<LocationDescriptor> _found_word(Location loc)
    {
        float[] results = new float[1];
        List<LocationDescriptor> ret_list = new ArrayList<>();
        double min_distance = Double.MAX_VALUE;
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

            min_distance = Math.min(min_distance, results[0]);

            if (results[0] < GlobalConstants.SONGLE_DISTANCE_WORD_GUESSED_TOLERANCE)
            {
                // add placemark if distance is lower than threshold
                ret_list.add(l);
            }
        }
        return ret_list;
    }


    private void _setupLocationListener()
    {
        console . debug_trace(this, "_setupLocationListener");
        this . manager = (LocationManager) this . getSystemService(
                Context.LOCATION_SERVICE
        );

        Runnable locationManagerConsumer = () ->
        {
            this . manager . requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    (long) 0,
                    (float) 0.0,
                    this
            );
        };

        _requestLocationPermission(locationManagerConsumer);
    }

    public void _requestLocationPermission(Runnable runnable)
    {
        console . debug_trace(this, "requestLocationPermission");

        // check permissoins
        int check = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
        );

        if (check != PackageManager.PERMISSION_GRANTED)
        {
            // push the runnable to the stack, so that we have it
            // after the user granted permissions
            this . runnableStack . push(runnable);
            // request permissions
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    GlobalConstants.SONGLE_PERMISSIONS_REQUEST_LOCATION);
        }
        else
        {
            // we have permission, call the runnable
            console . debug_trace(this, "_requestLocationPermission", "else");
            runnable . run();
        }
    }

    private Bitmap _getMarkerForCategory(String category)
    {
        // use a cache, so we download at most the number of cats that we have
        if (this . icon_cache . containsKey(category))
        {
            return this . icon_cache . get(category);
        }

        final String cat = this . placemarks . getMarkerURLForCategory(category);

        // use an executor service to download the images and populate the cache
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Bitmap> result;
        Callable<Bitmap> call = () -> {
            try
            {
                // connect and decode the input stream
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

        // submit the call
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
        // these map boundaries are predefined
        LatLngBounds bounds = new LatLngBounds(
                new LatLng(55.941617, -3.196473),
                new LatLng(55.947233, -3.180319)
        );

        // move the camera
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

        // transform to stream
        Stream<LocationDescriptor> s =
                this . placemarks . getDescriptors() . stream();

        // parse the placemarsk and create a stream of pairs
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

        // use the stream to add the placemarks
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

        // get the current placemarks for the current game
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
        // connect the api client
        super . onStart();
        if (this . mGoogleApiClient != null)
        {
            this . mGoogleApiClient . connect();
        }
    }

    @Override
    public void onStop()
    {
        // disconnect the api client
        if (this . mGoogleApiClient != null)
        {
            this . mGoogleApiClient . disconnect();
        }

        // write the buffer to the db
        if (this . buffer . size() > 0)
        {
            writeBufferToDb();
        }

        super . onStop();
    }

    @Override
    public void onPause()
    {
        super . onPause();

        // we need to synchronize this
        synchronized(this)
        {
            if (this . buffer . size() > 0)
            {
                writeBufferToDb();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        _setMapBoundaries();
        _addMapPoints();

        // runnable for setting that we want to see the location
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
                        // use a stack of runnables, in case we have multiple requests piling up
                        if (this . runnableStack . size() > 0)
                        {
                            this . runnableStack . pop() . run();
                        }
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
        console . debug_trace(this, "onConnected");
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

        // init location services
        _init_location_services();

        setContentView(R.layout.activity_play);

        // get the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public void handleFoundWord(@NonNull List<LocationDescriptor> des)
    {
        des . forEach(x -> {
            x.setDiscovered(true);
            x.setAvailable(false);
        });

        // use a buffer, so we don't put unnecessary stress on the database
        this . buffer . addAll(des);

        if (this . buffer . size() > 10)
        {
            console . debug_output_json(this.buffer);
            writeBufferToDb();
        }
    }

    public void writeBufferToDb()
    {
        // write found words buffer to db
        new DatabaseWriteTask<List<LocationDescriptor>>(
                AppDatabase.getAppDatabase(this),
                (db, lst) -> lst . forEach(x -> db.locationDao().updateLocation(x)),
                this.buffer::clear
        ).execute(this.buffer);
    }

    @Override
    public void onLocationChanged(Location location)
    {
        console . debug_trace(this, "onLocationChanged");
        // all locations within radius
        List<LocationDescriptor> des = _found_word(location);
        console . debug_output("Would have found " + this.picked_up + " so far.");
        if (des.size() > 0)
        {
            // we have found some words, so show it
            Toast.makeText(
                    this,
                    "Found words " + Algorithm.StringUtils.join(des, LocationDescriptor::getWord, ","),
                    Toast.LENGTH_SHORT
            ).show();
            console . debug_trace(this, "onLocationChanged", "nonNull");
            handleFoundWord(des);
        }
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
}
