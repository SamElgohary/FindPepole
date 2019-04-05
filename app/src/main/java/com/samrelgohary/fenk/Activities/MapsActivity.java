package com.samrelgohary.fenk.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.samrelgohary.fenk.CircleTransform;
import com.samrelgohary.fenk.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    String friendName, friendPhoto, friendId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        Bundle bundle = getIntent().getExtras();

        friendId = bundle.getString("friendId");
        friendName = bundle.getString("friendName");
        friendPhoto = bundle.getString("friendPhoto");

        Log.i("friendPhoto", "___" + friendPhoto);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng latLng = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(latLng).title("المنشأة التعليمية").icon( BitmapDescriptorFactory.fromResource(R.drawable.school_map_pin)));
        //googleMap.addMarker(new MarkerOptions().position(latLng).title(friendName).snippet("Marker Description"));
        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        MarkerOptions opt = new MarkerOptions()
                .position(latLng)
                .title(friendName);

        // Add the marker to the map
        Marker m = mMap.addMarker(opt);

        PicassoMarker marker = new PicassoMarker(m);
        Picasso.get().load(friendPhoto).transform(new CircleTransform()).resize(120, 120).into(marker);
    }

    public class PicassoMarker implements Target {
        Marker mMarker;

        PicassoMarker(Marker marker) {
            mMarker = marker;
        }

        @Override
        public int hashCode() {
            return mMarker.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof PicassoMarker) {
                Marker marker = ((PicassoMarker) o).mMarker;
                return mMarker.equals(marker);
            } else {
                return false;
            }
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
