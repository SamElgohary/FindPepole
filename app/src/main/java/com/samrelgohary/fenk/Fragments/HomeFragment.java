package com.samrelgohary.fenk.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.samrelgohary.fenk.Activities.MapsActivity;
import com.samrelgohary.fenk.CircleTransform;
import com.samrelgohary.fenk.Model.UserModel;
import com.samrelgohary.fenk.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import static android.content.Context.LOCATION_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 created by samRElGohary Mar 12, 2019
 */
public class HomeFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    MapView mMapView;
    private GoogleMap googleMap;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);
            }
        });

        statusCheck();

        getRequestsId(getDefaults("socialId",getApplicationContext()));

        return rootView;
    }

    protected synchronized void buildGoogleApiClient(){

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }



    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        //googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker Title").snippet("Marker Description"));

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        try {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("realTimeLocation");
            GeoFire geoFire = new GeoFire(ref);
            geoFire.setLocation(userId,new GeoLocation(location.getLatitude(),location.getLongitude()));


        }catch (Exception e){

        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

//        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("realTimeLocation");
//        GeoFire geoFire = new GeoFire(ref);
//        geoFire.removeLocation(userId);
    }

    @Override
    public void onStop(){
        super.onStop();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    public void statusCheck() {

        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            displayPromptForEnablingGPS(getActivity());

        }

    }

    public static void displayPromptForEnablingGPS(final Activity activity)
    {

        final AlertDialog.Builder builder =  new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "لن نتمكن من الوصول الي مكانك ولن يعمل التطبيق بكفأه في حالة اغلاق الـ GPS من فضلك شغله";

        builder.setMessage(message)
                .setPositiveButton("شغله",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.startActivity(new Intent(action));
                                d.dismiss();
                            }
                        })
                .setNegativeButton("لا متشغلوش",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                            }
                        });
        builder.create().show();
    }


    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    public void getRequestsId(final String key) {

        Query query ;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        query = ref.child("myFriends");

        try{query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot itemSnapShot : dataSnapshot.getChildren()) {


                    if (itemSnapShot.child("id").getValue(String.class).equals(getDefaults("socialId",getApplicationContext()))) {

                        getMyCircle(itemSnapShot.child("friendId").getValue(String.class));

                        Log.d("getFriendId", "__" + itemSnapShot.child("friendId").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });} catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getMyCircle(final String id) {

        Log.i("key","___"+id);

        Query query ;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        query = ref.child("user");

        try{query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot itemSnapShot : dataSnapshot.getChildren()) {


                    if (itemSnapShot.child("socialId").getValue(String.class).equals(id)) {


                        String name =   itemSnapShot.child("fullName").getValue(String.class);
                        String img  = itemSnapShot.child("img").getValue(String.class);
                        String  id  = itemSnapShot.child("socialId").getValue(String.class);

                        LatLng friendLocation = new LatLng(30.9730107, 31.1665126);
                        MarkerOptions opt = new MarkerOptions().position(friendLocation).title(name);
                        // Add the marker to the map
                        Marker m = googleMap.addMarker(opt);
                        PicassoMarker marker = new PicassoMarker(m);
                        Picasso.get().load(img).transform(new CircleTransform()).resize(120, 120).into(marker);


                        Log.d("getFullName", "__" + itemSnapShot.child("fullName").getValue(String.class));

                     }
                 }
            }


            class PicassoMarker implements Target {
                Marker mMarker;

                public PicassoMarker(Marker marker) {
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
            public void onCancelled(DatabaseError databaseError) {

            }
        });} catch (Exception e) {
            e.printStackTrace();
        }

    }
}