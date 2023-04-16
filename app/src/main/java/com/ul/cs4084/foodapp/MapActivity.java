package com.ul.cs4084.foodapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ul.cs4084.foodapp.databinding.ActivityMapBinding;


public class MapActivity extends DrawerBaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private ActivityMapBinding binding;
    private FusedLocationProviderClient client;
    SupportMapFragment mapFragment;
    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private double lat, lng;
    ImageButton atm, bank, hosp, rest;

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mMap.setMyLocationEnabled(true);


        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Map");

        atm = findViewById(R.id.atm);
        bank = findViewById(R.id.bank);
        hosp = findViewById(R.id.hospital);
        rest = findViewById(R.id.restaurant);

        if (!mLocationPermissionGranted) {
            getLocationPermission();
        }


        atm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMap != null) {
                    mMap.clear();
                    LatLng atm1 = new LatLng(52.6516337, -8.6993649);
                    mMap.addMarker(new MarkerOptions().position(atm1).title("Bank of Ireland ATM"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(atm1));

                    LatLng atm2 = new LatLng(52.6648882, -8.6640565);
                    mMap.addMarker(new MarkerOptions().position(atm2).title("AIB ATM"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(atm2));


                    LatLng atm3 = new LatLng(52.6579321, -8.6666723);
                    mMap.addMarker(new MarkerOptions().position(atm3).title("CashZone ATM"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(atm3));

                    LatLng atm6 = new LatLng(52.6501758, -8.6637664);
                    mMap.addMarker(new MarkerOptions().position(atm6).title("CashZone ATM"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(atm6));

                    LatLng atm4 = new LatLng(52.6515515, -8.4969928);
                    mMap.addMarker(new MarkerOptions().position(atm4).title("Bank of Ireland ATM"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(atm4));

                    LatLng atm5 = new LatLng(52.6674692, -8.7050128);
                    mMap.addMarker(new MarkerOptions().position(atm5).title("CashZone ATM"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(atm5));
                } else {
                    initMap();
                }
            }
        });


        //hosptial --------------------------------------------------
        hosp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap != null) {
                    mMap.clear();

                    LatLng hosp1 = new LatLng(52.6555897, -8.6240402);
                    mMap.addMarker(new MarkerOptions().position(hosp1).title("Saint Joseph's Hospital"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(hosp1));


                    LatLng hosp2 = new LatLng(52.6555897, -8.6240402);
                    mMap.addMarker(new MarkerOptions().position(hosp2).title("St Anne's Day Hospital"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(hosp2));

                    LatLng hosp3 = new LatLng(52.667627, -8.6503681);
                    mMap.addMarker(new MarkerOptions().position(hosp3).title("University Maternity Hospital Limerick"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(hosp3));

                    LatLng hosp6 = new LatLng(52.6522828, -8.650382);
                    mMap.addMarker(new MarkerOptions().position(hosp6).title("St Brendan's Health Centre"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(hosp6));

                    LatLng hosp4 = new LatLng(52.6522828, -8.650382);
                    mMap.addMarker(new MarkerOptions().position(hosp4).title("Barrack View Primary Care Centre"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(hosp4));
                } else {
                    initMap();
                }

            }
        });

        //restaurant --------------------------------------------------
        rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap != null) {
                    mMap.clear();
                    LatLng res1 = new LatLng(52.6489504, -8.4399714);
                    mMap.addMarker(new MarkerOptions().position(res1).title("Rua"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(res1));

                    LatLng res2 = new LatLng(52.6648882, -8.6640565);
                    mMap.addMarker(new MarkerOptions().position(res2).title("Lana Castletroy Asian Street Food"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(res2));

                    LatLng res3 = new LatLng(52.6645216, -8.5989246);
                    mMap.addMarker(new MarkerOptions().position(res3).title("Kebab Corner"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(res3));

                    LatLng res6 = new LatLng(52.664514, -8.6298243);
                    mMap.addMarker(new MarkerOptions().position(res6).title("McDonald's"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(res6));

                    LatLng res4 = new LatLng(52.6645316, -8.6309962);
                    mMap.addMarker(new MarkerOptions().position(res4).title("Burger King"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(res4));

                    LatLng res5 = new LatLng(52.663089, -8.632164);
                    mMap.addMarker(new MarkerOptions().position(res5).title("Hamptons Grill"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(res5));

                    LatLng res7 = new LatLng(52.6613619, -8.6513858);
                    mMap.addMarker(new MarkerOptions().position(res7).title("Boojum"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(res7));
                } else {
                    initMap();
                }
            }
        });


        //bank--------------------------------------------------
        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap != null) {
                    mMap.clear();
                    LatLng res3 = new LatLng(52.6692513, -8.6192189);
                    mMap.addMarker(new MarkerOptions().position(res3).title("Permanent TSB"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(res3));

                    LatLng res6 = new LatLng(52.6692513, -8.6192189);
                    mMap.addMarker(new MarkerOptions().position(res6).title("Bank of Ireland"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(res6));

                    LatLng res4 = new LatLng(52.6624756, -8.6348725);
                    mMap.addMarker(new MarkerOptions().position(res4).title("Ulster Bank (Limerick)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(res4));

                    LatLng res5 = new LatLng(52.6624756, -8.6348725);
                    mMap.addMarker(new MarkerOptions().position(res5).title("AIB"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(res5));

                    LatLng res7 = new LatLng(52.6473321, -8.6879748);
                    mMap.addMarker(new MarkerOptions().position(res7).title("Bank of Ireland"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(res7));
                } else {
                    initMap();
                }
            }
        });

    }

    private void getDeviceLocation() {
        Log.d(TAG, "getLocation: getting the current devices current location");
        client = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                //from google gms library:
                Task location = client.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: found location!");
                        Location currentLocation = (Location) task.getResult();

                        lat = currentLocation.getLatitude();
                        lng = currentLocation.getLongitude();

                        moveCamera(new LatLng(lat, lng), DEFAULT_ZOOM);

                    } else {
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(MapActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "GetDeviceLocation: SecurityException: " + e.getMessage());

        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to lat:" + latLng.latitude + ",lng:" + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void initMap() {
        Log.d(TAG, "initMap: initialise Map");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
    }


    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                mLocationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /* request permission for location*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionResult: called.");
        mLocationPermissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionResult:permission failed.");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionResult: permission Granted.");
                    mLocationPermissionGranted = true;
                    //initialize map
                    initMap();
                }
            }
        }
    }

}