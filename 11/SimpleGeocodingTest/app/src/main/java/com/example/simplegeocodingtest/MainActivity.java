package com.example.simplegeocodingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

    private Geocoder geocoder;

    EditText etLat;
    EditText etLon;
    EditText etAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etLat = findViewById(R.id.etLat);
        etLon = findViewById(R.id.etLon);
        etAddress = findViewById(R.id.etAddress);

        geocoder = new Geocoder(this, Locale.getDefault());
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFindAddress:
                double lat = Double.valueOf(etLat.getText().toString());
                double lon = Double.valueOf(etLon.getText().toString());
                List<String> address = getAddress(lat, lon);
                if (address == null) {
                    Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
                    break;
                }
                Log.d(TAG, String.format("Latitude: %f\nLongitude: %f", lat, lon));
                Log.d(TAG, "Result: " + address.get(0));
                break;
            case R.id.btnLatLon:
                String location = etAddress.getText().toString();
                List<LatLng> latLng = getLatLng(location);
                if (latLng == null) {
                    Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
                    break;
                }
                Log.d(TAG, "location: " + location);
                Log.d(TAG, String.format("Result Latitude: %f\nLongitude: %f", latLng.get(0).latitude, latLng.get(0).longitude));
                break;
            case R.id.btnClear:
                etLat.setText("");
                etLon.setText("");
                etAddress.setText("");
                break;
        }
    }

  
//    Geocoding
    private List<String> getAddress(double latitude, double longitude) {

        List<Address> addresses = null;
        ArrayList<String> addressFragments = null;

//        위도/경도에 해당하는 주소 정보를 Geocoder 에게 요청
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        if (addresses == null || addresses.size()  == 0) {
            return null;
        } else {
            Address addressList = addresses.get(0);
            addressFragments = new ArrayList<String>();

            for(int i = 0; i <= addressList.getMaxAddressLineIndex(); i++) {
                addressFragments.add(addressList.getAddressLine(i));
            }
        }

        return addressFragments;
    }


//    Reverse geocoding
    private List<LatLng> getLatLng(String targetLocation) {

        List<Address> addresses = null;
        ArrayList<LatLng> addressFragments = null;

//        주소에 해당하는 위도/경도 정보를 Geocoder 에게 요청
        try {
            addresses = geocoder.getFromLocationName(targetLocation, 1);
        } catch (IOException e) { // Catch network or other I/O problems.
            e.printStackTrace();
        } catch (IllegalArgumentException e) { // Catch invalid address values.
            e.printStackTrace();
        }

        if (addresses == null || addresses.size()  == 0) {
            return null;
        } else {
            Address addressList = addresses.get(0);
            addressFragments = new ArrayList<LatLng>();

            for(int i = 0; i <= addressList.getMaxAddressLineIndex(); i++) {
                LatLng latLng = new LatLng(addressList.getLatitude(), addressList.getLongitude());
                addressFragments.add(latLng);
            }
        }

        return addressFragments;
    }

}