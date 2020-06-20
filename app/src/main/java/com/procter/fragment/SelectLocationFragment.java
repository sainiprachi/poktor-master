package com.procter.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.procter.R;
import com.procter.Singleton.MyCustomMessage;
import com.procter.interfaces.GetLocation;
import com.procter.utils.PermissionAll;
import com.procter.utils.Utils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.LOCATION_SERVICE;
import static com.procter.utils.Constant.MY_PERMISSIONS_REQUEST_LOCATION;

@SuppressLint("ValidFragment")
public class SelectLocationFragment extends BaseFragment implements OnMapReadyCallback, View.OnClickListener, LocationListener {
    private static final String TAG = "SelectLocationFragment";
    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private GetLocation getLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private PermissionAll permissionAll;
    private Double latitude, longitude;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    public boolean canGetLocation = false;

    Location location;


    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    protected LocationManager locationManager;

    private EditText edtAddress;

    SelectLocationFragment(CompleteProfileFragment completeProfileFragment) {
        super();
        this.getLocation = completeProfileFragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.select_location_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Utils.hideKeyboard(Objects.requireNonNull(getActivity()));
        initView(view);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapView);
//        assert mapFragment != null;

        permissionAll = new PermissionAll();
        if (permissionAll.checkLocationPermission(getActivity())) mapFragment.getMapAsync(this);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

    }

    private void initView(View view) {
        RelativeLayout rlNotification = view.findViewById(R.id.rlNotification);
        ImageView ivImgBack = view.findViewById(R.id.ivImgBack);
        ivImgBack.setVisibility(View.VISIBLE);
        ivImgBack.setOnClickListener(this);
        rlNotification.setVisibility(View.GONE);
        TextView txtHeader = view.findViewById(R.id.txtHeader);
        txtHeader.setText("Select Address");
        edtAddress = view.findViewById(R.id.edtAddress);
        Button btnNext = view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (map != null) return;
        map = googleMap;
        intiMapView();
        try {
            if (permissionAll.checkLocationPermission(getActivity())) location();
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.setOnCameraChangeListener(cameraPosition -> {
            if (cameraPosition != null) {
                latitude = Double.valueOf(String.valueOf(cameraPosition.target.latitude));
                longitude = Double.valueOf(String.valueOf(cameraPosition.target.longitude));
                Log.d(TAG, "onCameraChange: latitude " + latitude + " longitude" + longitude);
                if (!(latitude == 0.0) && !(longitude == 0.0)) {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14.5f);
//                        map.animateCamera(cameraUpdate);
                    createMarkerCurrent(latitude, longitude);
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(mContext, Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();
                        edtAddress.setText(address);// Only if available else return NULL

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }

    private void intiMapView() {
        map.getUiSettings().setMyLocationButtonEnabled(true);
        // MapsInitializer.initialize(Objects.requireNonNull(getActivity()));
    }


    private void createMarkerCurrent(Double latitude, Double longitude) {
        if (map == null) {
            return;
        }

        final LatLng latLng = new LatLng(latitude, longitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("current Location");

/*
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_placeholder));
        Marker marker = map.addMarker(markerOptions);
*/
        //marker.setTag(-1);
    }

    private void location() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionAll.checkLocationPermission(getActivity());
        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(Objects.requireNonNull(getActivity()), location -> {
                        if (location != null) {
                            latitude = Double.valueOf(String.valueOf(location.getLatitude()));
                            longitude = Double.valueOf(String.valueOf(location.getLongitude()));
                            Log.d(TAG, "getLastLocation: latitude " + latitude + " longitude" + longitude);
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14.5f);
                            map.animateCamera(cameraUpdate);
                            createMarkerCurrent(latitude, longitude);
                            Geocoder geocoder;
                            List<Address> addresses;
                            geocoder = new Geocoder(mContext, Locale.getDefault());

                            try {
                                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName();
                                edtAddress.setText(address);// Only if available else return NULL

                                Log.d("address", address);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            LocationManager locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

                            if (locationManager != null) {
                                boolean gpsIsEnabled = locationManager
                                        .isProviderEnabled(LocationManager.GPS_PROVIDER);
                                boolean networkIsEnabled = locationManager
                                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                                if (gpsIsEnabled) {
                                    locationManager.requestLocationUpdates(
                                            LocationManager.GPS_PROVIDER, 5000L, 10F,
                                            (android.location.LocationListener) this);
                                } else if (networkIsEnabled) {
                                    locationManager.requestLocationUpdates(
                                            LocationManager.NETWORK_PROVIDER, 5000L, 10F,
                                            (android.location.LocationListener) this);
                                } else {
                                    // Show an error dialog that GPS is disabled...
                                }
                            } else {
                                // Show some generic error dialog because something must have gone
                                // wrong with location manager.
                            }

                        }
                    });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mapFragment.getMapAsync(this);
                }
            }
        }
    }

  /*  @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
        if (!isGPSEnabled()) {
            Utils.showGPSDisabledAlertToUser(mContext);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }*/

    private boolean isGPSEnabled() {
        final LocationManager manager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(LOCATION_SERVICE);
        assert manager != null;
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER) || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivImgBack:
                backPress(R.id.frame);
                break;

            case R.id.btnNext:
                if (TextUtils.isEmpty(edtAddress.getText())) {
                    MyCustomMessage.getInstance(mContext).showCustomAlert("", "Please Enter Address");
                } else {
//                    replaceFragment(R.id.frame, CompleteProfileFragment.newInstance("", edtAddress.getText().toString().trim()));
                    getLocation.setLocation(edtAddress.getText().toString().trim(), String.valueOf(latitude), String.valueOf(longitude));
                    backPress(R.id.frame);
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

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


}
