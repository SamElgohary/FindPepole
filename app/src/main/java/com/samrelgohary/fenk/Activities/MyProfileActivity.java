package com.samrelgohary.fenk.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.samrelgohary.fenk.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class MyProfileActivity extends AppCompatActivity {

    LinearLayout mUserImg;
    TextView mUserName,mUserEmail,mUserPhone,mUserGender,mUserBirthDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        mUserImg  = findViewById(R.id.user_img);
        mUserName  = findViewById(R.id.user_name);
        mUserEmail  = findViewById(R.id.user_email);
        mUserPhone  = findViewById(R.id.user_phone);
        mUserGender  = findViewById(R.id.user_gender);
        mUserBirthDay  = findViewById(R.id.user_birth_day);

            Picasso.get().load( getDefaults("userImg",getApplicationContext())).into(new Target() {
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

        mUserName.setText(getDefaults("fullName",getApplicationContext()));
        mUserEmail.setText(getDefaults("email",getApplicationContext()));
        mUserPhone.setText(getDefaults("phone",getApplicationContext()));
        mUserGender.setText(getDefaults("gender",getApplicationContext()));
        mUserBirthDay.setText(getDefaults("dateOfBirth",getApplicationContext()));

    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}
