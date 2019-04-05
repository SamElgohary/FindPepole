package com.samrelgohary.fenk.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.samrelgohary.fenk.Activities.AboutUS;
import com.samrelgohary.fenk.Activities.ContactUs;
import com.samrelgohary.fenk.Activities.LoginActivity;
import com.samrelgohary.fenk.R;

/**
 created by samRElGohary Mar 12, 2019
 */

public class MoreFragment extends Fragment {

    LinearLayout mLogout, mContactUs,mlanguage,mShareApp,mAbout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, null);

        mContactUs    = view.findViewById(R.id.contact_us);
        mShareApp    = view.findViewById(R.id.share_app);
        mlanguage    = view.findViewById(R.id.language);
        mAbout    = view.findViewById(R.id.about_ll);
        mLogout    = view.findViewById(R.id.logout_ll);


        mContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ContactUs.class);
                startActivity(intent);

            }
        });

        mShareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://play.google.com/store/apps/details?id=com.samrelgohary.fenk";

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "تطبيق فينك بيسهل عليك تعرف اصحابك او قرايبك او زمايلك فين دلوقتي وكمان تقدر تكلمهم خلاله جرب التطبيق دلوقتي" + url;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "فينك؟");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "فينك؟"));
            }
        });

        mlanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AboutUS.class);
                startActivity(intent);
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                FirebaseAuth.getInstance().signOut();
            }
        });

        return view;
    }
}
