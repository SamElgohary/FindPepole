package com.samrelgohary.fenk.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.samrelgohary.fenk.Adapter.ChatAdapter;
import com.samrelgohary.fenk.Adapter.MyCircleAdapter;
import com.samrelgohary.fenk.Model.ChatModel;
import com.samrelgohary.fenk.Model.CircleModel;
import com.samrelgohary.fenk.Model.UserModel;
import com.samrelgohary.fenk.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ChatActivity extends AppCompatActivity {

    String friendName,friendPhoto,friendId;
    ImageView mUserImgProfile, mSendMessage;
    TextView mFriendName;
    EditText mMessageContent;

    private GridView mGVMyChat;
    private ChatAdapter mChatAdapter;
    private ArrayList<ChatModel> mUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle bundle = getIntent().getExtras();

        friendId = bundle.getString("friendId");
        friendName = bundle.getString("friendName");
        friendPhoto = bundle.getString("friendPhoto");

        mUserImgProfile = findViewById(R.id.user_img_profile);
        mFriendName = (TextView) findViewById(R.id.top_bar_tv);
        mSendMessage = findViewById(R.id.buttonMessage);
        mMessageContent =findViewById(R.id.editTextMessage);

        mGVMyChat = (GridView) findViewById(R.id.chat_gv);
        mUserData = new ArrayList<ChatModel>();
        mChatAdapter = new ChatAdapter(ChatActivity.this, R.layout.chat_item, mUserData);
        mGVMyChat.setAdapter(mChatAdapter);

        Picasso.get().load(friendPhoto).into(mUserImgProfile);
        mFriendName.setText(friendName);

        mSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMessageAction();

            }
        });


        getChatContent();
        //getChatContentFriend();

    }

    public void sendMessageAction(){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
        String currentTime = mdformat.format(calendar.getTime());

        String chatId = getDefaults("socialId",getApplicationContext()) + "_" +  friendId;

        Log.d("chatId", "___" + chatId);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference("chatModel");

        ChatModel chatModel =new ChatModel();

        chatModel.setMessage(mMessageContent.getText().toString());
        chatModel.setUserName(getDefaults("fullName",getApplicationContext()));
        chatModel.setUserId(getDefaults("socialId",getApplicationContext()));
        chatModel.setUserProfilePic(getDefaults("userImg",getApplicationContext()));
        chatModel.setTime(currentTime);
        chatModel.setFriendId(friendId);
        chatModel.setChatId(chatId);
        ref.push().setValue(chatModel);
        mMessageContent.setText("");
    }

    public void getChatContent(){

        Query query;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        query = ref.child("chatModel");
        try{query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUserData.clear();
                for (DataSnapshot itemSnapShot : dataSnapshot.getChildren()) {

                    ChatModel chatModel = new ChatModel();
                    ChatModel chatModel1 = new ChatModel();

                    if (itemSnapShot.child("chatId").getValue(String.class).equals
                            (getDefaults("socialId",getApplicationContext()) + "_" +  friendId)){
                        chatModel.setFriendId(itemSnapShot.child("friendId").getValue(String.class));
                        chatModel.setChatId(itemSnapShot.child("chatId").getValue(String.class));
                        chatModel.setTime(itemSnapShot.child("time").getValue(String.class));
                        chatModel.setUserProfilePic(itemSnapShot.child("userProfilePic").getValue(String.class));
                        chatModel.setUserId(itemSnapShot.child("userId").getValue(String.class));
                        chatModel.setMessage(itemSnapShot.child("message").getValue(String.class));
                        chatModel.setUserName(itemSnapShot.child("userName").getValue(String.class));
                        chatModel.setLocalFriendId(friendId);
                        chatModel.setGravity(0);
                        Log.d("messageRight", "__" +itemSnapShot.child("message").getValue(String.class));
                    }
                    if (itemSnapShot.child("chatId").getValue(String.class).equals
                            (friendId + "_" +  getDefaults("socialId",getApplicationContext()))){
                        chatModel1.setFriendId(itemSnapShot.child("friendId").getValue(String.class));
                        chatModel1.setChatId(itemSnapShot.child("chatId").getValue(String.class));
                        chatModel1.setTime(itemSnapShot.child("time").getValue(String.class));
                        chatModel1.setUserProfilePic(itemSnapShot.child("userProfilePic").getValue(String.class));
                        chatModel1.setUserId(itemSnapShot.child("userId").getValue(String.class));
                        chatModel1.setMessage(itemSnapShot.child("message").getValue(String.class));
                        chatModel1.setUserName(itemSnapShot.child("userName").getValue(String.class));
                        chatModel1.setLocalFriendId(friendId);
                        chatModel1.setGravity(1);
                        Log.d("messageLeft", "__" +itemSnapShot.child("message").getValue(String.class));
                    }



                    mUserData.add(chatModel);
                    mUserData.add(chatModel1);

                   // mProgressBar.setVisibility(View.GONE);
                }
                 mChatAdapter.setGridData(mUserData);
               // Collections.reverse(mUserData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });} catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
//        finish();
//        startActivity(getIntent());

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
