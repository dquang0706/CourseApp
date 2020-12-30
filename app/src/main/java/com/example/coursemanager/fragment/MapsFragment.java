package com.example.coursemanager.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.coursemanager.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    Button btnDN, btnCT, btnTN;
    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        btnDN = view.findViewById(R.id.btnDN);
        btnCT = view.findViewById(R.id.btnCT);
        btnTN = view.findViewById(R.id.btnTN);
        btnDN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LatLng toado = new LatLng(16.0756721, 108.167584);
                Marker danang = mMap.addMarker(
                        new MarkerOptions()
                                .position(toado)
                                .title("Fpoly Đà Nẵng")
                                .snippet("Cao dang Fpoly Đà Nẵng xin  chào các bạn")
                                .icon(BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_YELLOW)));


                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(toado, 17));

            }
        });
        btnCT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LatLng toado = new LatLng(10.0267108, 105.7572724);
                Marker danang = mMap.addMarker(
                        new MarkerOptions()
                                .position(toado)
                                .title("Fpoly Cần Thơ")
                                .snippet("Cao dang Fpoly Cần thơ xin chao ban")
                                .icon(BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_BLUE)));


                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(toado, 17));

            }
        });
        btnTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng toado = new LatLng(21.0185937, 105.7671758);
                Marker TayNguyen = mMap.addMarker(
                        new MarkerOptions()
                                .position(toado)
                                .title("Fpoly Hà Nội")
                                .snippet("Cao Đẳng FPOLY Hà Nội Xin chào Các bạn")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                );
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(toado, 17));
            }
        });
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportMapFragment mapFragment= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        UiSettings uisetting = mMap.getUiSettings();
        uisetting.setCompassEnabled(true);
        uisetting.setZoomControlsEnabled(true);
        uisetting.setScrollGesturesEnabled(true);
        uisetting.setTiltGesturesEnabled(true);

        uisetting.setMyLocationButtonEnabled(true);
        // Add a marker in Sydney and move the camera
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        LatLng cs2 = new LatLng(10.824901, 106.6725885);
        mMap.addMarker(new MarkerOptions().position(cs2).title("Marker in Cơ sở 2 Nguyễn kiệm"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cs2, 17));
    }
}