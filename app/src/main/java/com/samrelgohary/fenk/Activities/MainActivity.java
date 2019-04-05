package com.samrelgohary.fenk.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.samrelgohary.fenk.Fragments.ChatFragment;
import com.samrelgohary.fenk.Fragments.CircleFragment;
import com.samrelgohary.fenk.Fragments.HomeFragment;
import com.samrelgohary.fenk.Fragments.MoreFragment;
import com.samrelgohary.fenk.Model.ChatModel;
import com.samrelgohary.fenk.R;
import com.squareup.picasso.Picasso;

import java.util.Map;

/**
 created by sam R. El Gohary Mar 12, 2019
 */

public class MainActivity extends AppCompatActivity {

    private TextView mTopBarTV;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;


    ConstraintLayout mConstraintLayout;

    private String userID;
    private String mProfileImageUrl;
    ImageView mUserImgProfile, mNotificationIcon;


    Fragment fragment = new HomeFragment();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    mConstraintLayout.setVisibility(View.VISIBLE);
                    mTopBarTV.setText(getString(R.string.title_home));
                    break;
                case R.id.navigation_circle:
                    fragment = new CircleFragment();
                    mConstraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.navigation_chat:
                    fragment = new ChatFragment();
                    mConstraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.navigation_more:
                    fragment = new MoreFragment();
                    mConstraintLayout.setVisibility(View.VISIBLE);
                    mTopBarTV.setText(getString(R.string.title_more));
                    break;
            }
            return loadFragment(fragment);

        }
    };

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mNotificationIcon = findViewById(R.id.notification_icon);
        mNotificationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,NotificationActivity.class);
                startActivity(intent);
            }
        });


        mConstraintLayout = findViewById(R.id.constraintLayout);

        mUserImgProfile = findViewById(R.id.user_img_profile);
        mUserImgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,MyProfileActivity.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("user").child(userID);

        mTopBarTV = (TextView) findViewById(R.id.top_bar_tv);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Log.d("testApi", "___"+ String.valueOf(R.string.google_maps_key));

        loadFragment(fragment);
         getUserData();
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public void getUserData(){

        Query query ;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        query = ref.child("user");

        try{query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot itemSnapShot : dataSnapshot.getChildren()) {

                    Log.d("newsocialId","__"+itemSnapShot.child("socialId").getValue(String.class));
                    Log.d("localnewsocialId","___"+userID);


                    if (itemSnapShot.child("socialId").getValue(String.class).equals(userID)) {

                        if (itemSnapShot.child("img").getValue(String.class) != null) {
                            mProfileImageUrl = itemSnapShot.child("img").getValue(String.class);
                            setDefaults("userImg", itemSnapShot.child("img").getValue(String.class), MainActivity.this);
                            Picasso.get().load(mProfileImageUrl).into(mUserImgProfile);
                        }
                        setDefaults("fullName", itemSnapShot.child("fullName").getValue(String.class), MainActivity.this);
                        setDefaults("email", itemSnapShot.child("email").getValue(String.class), MainActivity.this);
                        setDefaults("dateOfBirth", itemSnapShot.child("dateOfBirth").getValue(String.class), MainActivity.this);
                        setDefaults("gender", itemSnapShot.child("gender").getValue(String.class), MainActivity.this);
                        setDefaults("phone", itemSnapShot.child("phone").getValue(String.class), MainActivity.this);
                        setDefaults("socialId", itemSnapShot.child("socialId").getValue(String.class), MainActivity.this);

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

    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (String.valueOf(fragment).contains("Home") ){

            finish();
        }
        else {
            finish();
            startActivity(getIntent());
        }
    }
}
