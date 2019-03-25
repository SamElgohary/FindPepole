package com.samrelgohary.fenk.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.samrelgohary.fenk.Model.UserModel;
import com.samrelgohary.fenk.R;

import java.util.ArrayList;

public class AddToCircleActivity extends AppCompatActivity {

    ImageView mBack;
    AutoCompleteTextView mSearchACTV;

    private ArrayList<String> mUsersNames;
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

        mSearchACTV = findViewById(R.id.search_atv);

        getPeopleByName();

    }

    public void getPeopleByName(){

        mUsersNames = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,R.layout.custome_actv, mUsersNames);
        mSearchACTV.setThreshold(2);
        mSearchACTV.setAdapter(adapter);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("user");

        try{ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot itemSnapShot : dataSnapshot.getChildren()) {

                    mUsersNames.add(itemSnapShot.child("fullName").getValue(String.class));
                    mUsersNames.add(itemSnapShot.child("phone").getValue(String.class));

                }

                mSearchACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {

                        mSearchACTV.requestFocus();

                        String selection = (String)parent.getItemAtPosition(position);
                        Log.i("selection","" + selection);

                        getUserProfile(selection);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });} catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getUserProfile(String key){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query Query;

        if (key.matches("0123456789")){
             Query = ref.child("user").orderByChild("phone").equalTo(key);

        }else{
             Query = ref.child("user").orderByChild("fullName").equalTo(key);
        }

            Query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSnapShot: dataSnapshot.getChildren()) {

                    Intent intent = new Intent(AddToCircleActivity.this,UserProfileActivity.class);
                    intent.putExtra("userId",itemSnapShot.child("socialId").getValue(String.class));

                    intent.putExtra("fullName",itemSnapShot.child("fullName").getValue(String.class));
                    intent.putExtra("email",itemSnapShot.child("email").getValue(String.class));
                    intent.putExtra("phone",itemSnapShot.child("phone").getValue(String.class));
                    intent.putExtra("img",itemSnapShot.child("img").getValue(String.class));
                    intent.putExtra("gender",itemSnapShot.child("gender").getValue(String.class));
                    intent.putExtra("dateOfBirth",itemSnapShot.child("dateOfBirth").getValue(String.class));

                    Log.d("userId","__"+itemSnapShot.child("socialId").getValue(String.class));
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}
