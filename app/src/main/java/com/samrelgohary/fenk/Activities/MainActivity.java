package com.samrelgohary.fenk.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.samrelgohary.fenk.Fragments.ChatFragment;
import com.samrelgohary.fenk.Fragments.FriendsFragment;
import com.samrelgohary.fenk.Fragments.HomeFragment;
import com.samrelgohary.fenk.Fragments.MoreFragment;
import com.samrelgohary.fenk.R;

/**
 created by sam R. El Gohary Mar 12, 2019
 */

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
