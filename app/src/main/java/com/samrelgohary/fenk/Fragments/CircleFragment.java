package com.samrelgohary.fenk.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.samrelgohary.fenk.Activities.AddToCircleActivity;
import com.samrelgohary.fenk.Activities.MainActivity;
import com.samrelgohary.fenk.Activities.MyProfileActivity;
import com.samrelgohary.fenk.Adapter.MyCircleAdapter;
import com.samrelgohary.fenk.Model.UserModel;
import com.samrelgohary.fenk.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 created by sam R. El Gohary Mar 12, 2019
 */
public class CircleFragment extends Fragment {

    private GridView mGVMyCircle;
    private MyCircleAdapter mCircleAdapter;
    private ArrayList<UserModel> mUserData;

    ImageView mUserImgProfile, mAddPepole;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_circle, null);

        mUserImgProfile = view.findViewById(R.id.user_img_profile);
        mUserImgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), MyProfileActivity.class);
                startActivity(intent);
            }
        });

        Picasso.get().load(getDefaults("userImg",getApplicationContext())).into(mUserImgProfile);

        mAddPepole  = view.findViewById(R.id.add_people);
        mAddPepole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AddToCircleActivity.class);
                startActivity(intent);
            }
        });

        mGVMyCircle = (GridView) view.findViewById(R.id.gv_my_circle);
        mUserData = new ArrayList<UserModel>();
        mCircleAdapter = new MyCircleAdapter(getActivity(), R.layout.my_circle_item, mUserData);
        mGVMyCircle.setAdapter(mCircleAdapter);


        getMyCircle();

        return view;
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    public void getMyCircle() {
        //Query applesQuery = ref.child("Orders").orderByChild("orderTitle").equalTo(orderTitle);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("user");

        try{ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUserData.clear();

                for (DataSnapshot itemSnapShot : dataSnapshot.getChildren()) {

                    UserModel userModel = new UserModel();

                    userModel.setfName(itemSnapShot.child("fName").getValue(String.class));
                    userModel.setlName(itemSnapShot.child("lName").getValue(String.class));
                    userModel.setImg(itemSnapShot.child("img").getValue(String.class));
                    userModel.setSocialId(itemSnapShot.child("socialId").getValue(String.class));

                    mUserData.add(userModel);
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
