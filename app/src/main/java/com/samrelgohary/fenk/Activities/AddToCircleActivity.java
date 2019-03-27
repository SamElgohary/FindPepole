package com.samrelgohary.fenk.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.samrelgohary.fenk.Adapter.AllCircleAdapter;
import com.samrelgohary.fenk.Adapter.MyCircleAdapter;
import com.samrelgohary.fenk.Model.UserModel;
import com.samrelgohary.fenk.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddToCircleActivity extends AppCompatActivity {

    ImageView mBack;
    private GridView mGVMyCircle;
    private AllCircleAdapter mCircleAdapter;
    private ArrayList<UserModel> mUserData;

    AutoCompleteTextView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_circle);

        mBack = findViewById(R.id.back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        mGVMyCircle = (GridView) findViewById(R.id.gv_circle);
        mUserData = new ArrayList<UserModel>();
        mCircleAdapter = new AllCircleAdapter(AddToCircleActivity.this, R.layout.all_circle_item, mUserData);
        mGVMyCircle.setAdapter(mCircleAdapter);

        searchBar = findViewById(R.id.search_atv);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mGVMyCircle.setVisibility(View.VISIBLE);
                getMyCircle(searchBar.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        
    }

    public void getMyCircle(final String key) {

        Log.i("key","___"+key);

        Query query ;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        query = ref.child("user");

        try{query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUserData.clear();

                for (DataSnapshot itemSnapShot : dataSnapshot.getChildren()) {

                    UserModel userModel = new UserModel();

                    if (itemSnapShot.child("fullName").getValue(String.class).contains(key)) {

                        userModel.setFullName(itemSnapShot.child("fullName").getValue(String.class));
                        userModel.setImg(itemSnapShot.child("img").getValue(String.class));
                        userModel.setSocialId(itemSnapShot.child("socialId").getValue(String.class));

                        mUserData.add(userModel);
                    }
//                    mProgressBar.setVisibility(View.GONE);

                }
                mCircleAdapter.setGridData(mUserData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });} catch (Exception e) {
            e.printStackTrace();
        }

    }
}
