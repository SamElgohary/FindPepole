package com.samrelgohary.fenk.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.samrelgohary.fenk.Model.CircleModel;
import com.samrelgohary.fenk.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class UserProfileActivity extends AppCompatActivity {

    LinearLayout mUserImg;
    TextView mUserName,mUserEmail,mUserPhone,mUserGender,mUserBirthDay;
    String userId,fullName,email,phone,img,gender,dateOfBirth;
    LinearLayout mAddToCircle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Bundle bundle = getIntent().getExtras();

        userId = bundle.getString("userId");
        fullName = bundle.getString("fullName");
        email = bundle.getString("email");
        phone = bundle.getString("phone");
        img = bundle.getString("img");
        gender = bundle.getString("gender");
        dateOfBirth = bundle.getString("dateOfBirth");
        Log.d("userID","__"+userId);

        mUserImg  = findViewById(R.id.user_img);
        mUserName  = findViewById(R.id.user_name);
        mUserEmail  = findViewById(R.id.user_email);
        mUserPhone  = findViewById(R.id.user_phone);
        mUserGender  = findViewById(R.id.user_gender);
        mUserBirthDay  = findViewById(R.id.user_birth_day);

        mAddToCircle  = findViewById(R.id.add_to);

        Picasso.get().load(img).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                mUserImg.setBackground(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        mUserName.setText(fullName);
        mUserEmail.setText(email);
        mUserPhone.setText(phone);
        mUserGender.setText(gender);
        mUserBirthDay.setText(dateOfBirth);

        mAddToCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addToCircle();
            }
        });
    }

    public void addToCircle(){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference("friendRequest");

        CircleModel circleModel = new CircleModel();

        circleModel.setId(getDefaults("socialId",getApplicationContext()));
        circleModel.setFriendId(userId);

        ref.push().setValue(circleModel);
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}
