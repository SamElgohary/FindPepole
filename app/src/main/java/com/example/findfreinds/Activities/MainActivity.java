package com.example.findfreinds.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.findfreinds.Fragments.ChatFragment;
import com.example.findfreinds.Fragments.FriendsFragment;
import com.example.findfreinds.Fragments.HomeFragment;
import com.example.findfreinds.Fragments.MoreFragment;
import com.example.findfreinds.R;

public class MainActivity extends AppCompatActivity {

    private TextView mTopBarTV;
    Fragment fragment = new HomeFragment();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    mTopBarTV.setText(getString(R.string.title_home));
                    break;
                case R.id.navigation_friends:
                    fragment = new FriendsFragment();
                    mTopBarTV.setText(getString(R.string.title_friends));
                    break;
                case R.id.navigation_chat:
                    fragment = new ChatFragment();
                    mTopBarTV.setText(getString(R.string.title_chat));
                    break;
                case R.id.navigation_more:
                    fragment = new MoreFragment();
                    mTopBarTV.setText(getString(R.string.title_more));
                    break;
            }
            return loadFragment(fragment);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTopBarTV = (TextView) findViewById(R.id.top_bar_tv);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(fragment);
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

}
