package com.samrelgohary.fenk.Activities;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.samrelgohary.fenk.R;

public class SplashScreenActivity extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    static public final int REQUEST_LOCATION = 1;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash_screen);

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }else {
            exitSplash();
        }

     }

     public void exitSplash(){

         /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
         new Handler().postDelayed(new Runnable(){
             @Override
             public void run() {
                 /* Create an Intent that will start the Menu-Activity. */
                 Intent mainIntent = new Intent(SplashScreenActivity.this,MainActivity.class);
                 SplashScreenActivity.this.startActivity(mainIntent);
                 SplashScreenActivity.this.finish();

             }
         }, SPLASH_DISPLAY_LENGTH);

     }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               exitSplash();// <-- Start Beemray here
            } else {

                Toast.makeText(this, "لن نتمكن من الوصول الي موقعك", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
     }
}