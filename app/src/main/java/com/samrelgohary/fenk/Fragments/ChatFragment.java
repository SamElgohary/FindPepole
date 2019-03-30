package com.samrelgohary.fenk.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.samrelgohary.fenk.Activities.AddToCircleActivity;
import com.samrelgohary.fenk.Activities.MyProfileActivity;
import com.samrelgohary.fenk.Adapter.ChatListAdapter;
import com.samrelgohary.fenk.Adapter.MyCircleAdapter;
import com.samrelgohary.fenk.Model.ChatModel;
import com.samrelgohary.fenk.Model.UserModel;
import com.samrelgohary.fenk.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
    created by sam R. El Gohary Mar 12, 2019
 */
public class ChatFragment extends Fragment {

    private GridView mGVChatList;
    private ChatListAdapter mChatlistAdapter;
    private ArrayList<ChatModel> mUserData;

    ImageView mUserImgProfile;

    AutoCompleteTextView searchBar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, null);

        mUserImgProfile = view.findViewById(R.id.user_img_profile);
        mUserImgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), MyProfileActivity.class);
                startActivity(intent);
            }
        });

        Picasso.get().load(getDefaults("userImg",getApplicationContext())).into(mUserImgProfile);


        mGVChatList = (GridView) view.findViewById(R.id.gv_my_circle);
        mUserData = new ArrayList<ChatModel>();
        mChatlistAdapter = new ChatListAdapter(getActivity(), R.layout.chat_list_item, mUserData);
        mGVChatList.setAdapter(mChatlistAdapter);

        searchBar = view.findViewById(R.id.top_bar_tv);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                getRequestsId(searchBar.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getRequestsId("");

        return view;
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    public void getRequestsId(final String key) {

        Query query ;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        query = ref.child("myFriends");

        try{query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUserData.clear();

                for (DataSnapshot itemSnapShot : dataSnapshot.getChildren()) {

                    //  UserModel userModel = new UserModel();

                    if (itemSnapShot.child("id").getValue(String.class).equals(getDefaults("socialId",getApplicationContext()))) {

                        mGVChatList.setVisibility(View.VISIBLE);

                        if(key.equals("")) {
                            getChatList();
                        }else {
                            getMyCircle(key, itemSnapShot.child("friendId").getValue(String.class));
                        }

                        Log.d("getFriendId", "__" + itemSnapShot.child("friendId").getValue(String.class));

                        //mUserData.add(userModel);
                    }
//                    mProgressBar.setVisibility(View.GONE);
                }
                //mChatlistAdapter.setGridData(mUserData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });} catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getMyCircle(final String key, final String id) {

        Log.i("key","___"+key);

        Query query ;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        query = ref.child("user");

        try{query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot itemSnapShot : dataSnapshot.getChildren()) {

                    ChatModel chatModel = new ChatModel();

                    if (itemSnapShot.child("socialId").getValue(String.class).equals(id)) {

                        if (itemSnapShot.child("fullName").getValue(String.class).contains(key)) {

                           chatModel.setUserName(itemSnapShot.child("fullName").getValue(String.class));
                            chatModel.setUserProfilePic(itemSnapShot.child("img").getValue(String.class));
                            chatModel.setUserId(itemSnapShot.child("socialId").getValue(String.class));

                            Log.d("getFullName", "__" + itemSnapShot.child("fullName").getValue(String.class));

                            mUserData.add(chatModel);
                        }
                    }
//                    mProgressBar.setVisibility(View.GONE);

                }
                mChatlistAdapter.setGridData(mUserData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });} catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getChatList(){

        Query query;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        query = ref.child("chatModel");
        try{query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUserData.clear();
                for (DataSnapshot itemSnapShot : dataSnapshot.getChildren()) {

                    ChatModel chatModel = new ChatModel();

                    if (itemSnapShot.child("friendId").getValue(String.class).equals
                            (getDefaults("socialId", getApplicationContext()))) {

                            chatModel.setTime(itemSnapShot.child("time").getValue(String.class));
                            chatModel.setUserProfilePic(itemSnapShot.child("userProfilePic").getValue(String.class));
                            chatModel.setMessage(itemSnapShot.child("message").getValue(String.class));
                            chatModel.setUserName(itemSnapShot.child("userName").getValue(String.class));
                            chatModel.setUserId(itemSnapShot.child("userId").getValue(String.class));
                            Log.d("messageFromMe", "__" + itemSnapShot.child("message").getValue(String.class));
                            Log.d("nameFromFragment", "___" + itemSnapShot.child("userName").getValue(String.class));
                            Log.d("message", "__" + itemSnapShot.child("message").getValue(String.class));

                       // ArrayList<String> values=new ArrayList<String>();
                        HashSet<ChatModel> hashSet = new HashSet<ChatModel>();
                        hashSet.addAll(Collections.singleton(chatModel));
                        mUserData.clear();
                        mUserData.addAll(hashSet);

                            //mUserData.add(chatModel);
                    }
                mChatlistAdapter.setGridData(mUserData);
                Collections.reverse(mUserData);
            }
        }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });} catch (Exception e) {
            e.printStackTrace();
        }

    }


}
