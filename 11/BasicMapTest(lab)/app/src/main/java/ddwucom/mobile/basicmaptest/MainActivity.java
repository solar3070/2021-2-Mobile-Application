package ddwucom.mobile.basicmaptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "Marker";

    final static int PERMISSIONS_REQ_LOC = 100;

    private GoogleMap mGoogleMap;
    private LocationManager locationManager;

    private Marker centerMarker;
    private ArrayList<Marker> markers = new ArrayList<>();
    private PolylineOptions pOptions;

    private AddressResultReceiver addressResultReceiver;

    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapReadyCallBack);

        pOptions = new PolylineOptions();
        pOptions.color(Color.RED);
        pOptions.width(5);

        //        IntentService??? ???????????? ?????? ????????? ResultReceiver
        addressResultReceiver = new AddressResultReceiver(new Handler());
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                locationUpdate();
                break;
            case R.id.btnStop:
                locationManager.removeUpdates(locationListener);
                break;
        }
    }

    private void locationUpdate() {
        if (checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    3000, 5, locationListener);
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            LatLng currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));
            centerMarker.setPosition(currentLoc);

            pOptions.add(currentLoc);
            mGoogleMap.addPolyline(pOptions);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {		}

        @Override
        public void onProviderEnabled(String provider) {		}

        @Override
        public void onProviderDisabled(String provider) {		}

    };

    /*?????? ?????? ?????? Provider ????????? ?????? ?????? ?????? ????????? ?????? ?????? - ?????? ?????? ??????*/
    private void getLastLocation() {
        if (checkPermission()) {
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                latitude = lastLocation.getLatitude();
                longitude = lastLocation.getLongitude();
            } else {
                // ??????
                latitude = 37.606537;
                longitude = 127.041758;
            }
        }
    }

    OnMapReadyCallback mapReadyCallBack = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;
            getLastLocation();
            LatLng currentLoc = new LatLng(latitude, longitude);
//            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));

            MarkerOptions options = new MarkerOptions();
            options.position(currentLoc);
            options.title("?????? ??????");
            options.snippet("?????????");
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            centerMarker = mGoogleMap.addMarker(options);
            centerMarker.showInfoWindow(); // ????????? ?????? ????????? ???????????? ????????? ?????????

            mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
//                    Toast.makeText(MainActivity.this, "?????? ?????????: " + marker.getId(), Toast.LENGTH_SHORT).show();
                    LatLng latLng = marker.getPosition();
                    startAddressService(latLng.latitude, latLng.longitude);
                }
            });

            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Toast.makeText(MainActivity.this, "??????: " + latLng.latitude +
                            "\n??????: " + latLng.longitude, Toast.LENGTH_SHORT).show();
                }
            });

            mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    MarkerOptions option = new MarkerOptions();
                    option.position(new LatLng(latLng.latitude, latLng.longitude));
                    option.title("????????? ??????");
                    option.snippet(String.format("%.6f", latLng.latitude) + ", " + String.format("%.6f", latLng.longitude));
                    option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

                    Marker mk = mGoogleMap.addMarker(option);
                    markers.add(mk);
                }
            });

        }
    };

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQ_LOC);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQ_LOC) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /*????????? ??????????????? ??? ??????????????? ?????? ?????? ??????*/
                locationUpdate();
            } else {
                /*??????????????? ?????? ????????? ?????? ??????*/
                Toast.makeText(this, "??? ????????? ?????? ?????? ????????? ?????????", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /* ??????/?????? ??? ?????? ?????? IntentService ?????? */
    private void startAddressService(double latitude, double longitude) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, addressResultReceiver);
        intent.putExtra(Constants.LAT_DATA_EXTRA, latitude);
        intent.putExtra(Constants.LNG_DATA_EXTRA, longitude);
        startService(intent);
    }

    /* ??????/?????? ??? ?????? ?????? ResultReceiver */
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            String addressOutput = null;

            if (resultCode == Constants.SUCCESS_RESULT) {
                if (resultData == null) return;
                addressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
                if (addressOutput == null) addressOutput = "";
                Toast.makeText(MainActivity.this, "??????: " + addressOutput, Toast.LENGTH_SHORT).show();
            }
        }
    }
}