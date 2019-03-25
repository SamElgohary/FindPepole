package com.samrelgohary.fenk.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.samrelgohary.fenk.R;

public class UserProfileActivity extends AppCompatActivity {

    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Bundle bundle = getIntent().getExtras();

         userId = bundle.getString("userId");
        Log.d("userID","__"+userId);
    }
}
