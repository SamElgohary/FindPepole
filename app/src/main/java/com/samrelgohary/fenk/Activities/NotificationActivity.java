package com.samrelgohary.fenk.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.samrelgohary.fenk.Adapter.AllCircleAdapter;
import com.samrelgohary.fenk.Adapter.NotificationAdapter;
import com.samrelgohary.fenk.Model.UserModel;
import com.samrelgohary.fenk.R;

import java.util.ArrayList;
import java.util.Collections;

public class NotificationActivity extends AppCompatActivity {

    ImageView mBack;

    private GridView mGVNotification;
    private NotificationAdapter mNotificationAdapter;
    private ArrayList<UserModel> mUserData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mBack = findViewById(R.id.back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        mGVNotification = (GridView) findViewById(R.id.gv_notification);
        mUserData = new ArrayList<UserModel>();
        mNotificationAdapter = new NotificationAdapter(NotificationActivity.this, R.layout.notification_item, mUserData);
        mGVNotification.setAdapter(mNotificationAdapter);


        getRequestsId();

    }

    public void getRequestsId() {

        Query query ;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        query = ref.child("friendRequest");

        try{query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               // mUserData.clear();

                for (DataSnapshot itemSnapShot : dataSnapshot.getChildren()) {

                  //  UserModel userModel = new UserModel();

                    if (itemSnapShot.child("friendId").getValue(String.class).equals(getDefaults("socialId",getApplicationContext()))) {

                        itemSnapShot.child("id").getValue(String.class);
                        mGVNotification.setVisibility(View.VISIBLE);
                        getUserData( itemSnapShot.child("id").getValue(String.class));

                        Log.d("getRequestsId", "__" + itemSnapShot.child("id").getValue(String.class));

                        //mUserData.add(userModel);
                    }
//                    mProgressBar.setVisibility(View.GONE);

                }
                //mCircleAdapter.setGridData(mUserData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });} catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getUserData(final String key){

        Log.i("key","___"+key);

        Query query ;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        query = ref.child("user");

        try{query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot itemSnapShot : dataSnapshot.getChildren()) {

                    UserModel userModel = new UserModel();

                    if (itemSnapShot.child("socialId").getValue(String.class).equals(key)) {

                        userModel.setFullName(itemSnapShot.child("fullName").getValue(String.class));
                        userModel.setImg(itemSnapShot.child("img").getValue(String.class));
                        userModel.setSocialId(itemSnapShot.child("socialId").getValue(String.class));

                        Log.d("getRequestsNames", "__" +itemSnapShot.child("fullName").getValue(String.class));


                         mUserData.add(userModel);
                    }
//                    mProgressBar.setVisibility(View.GONE);

                }
               mNotificationAdapter.setGridData(mUserData);
               Collections.reverse(mUserData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });} catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}
