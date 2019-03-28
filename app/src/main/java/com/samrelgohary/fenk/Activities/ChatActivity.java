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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.samrelgohary.fenk.Model.ChatModel;
import com.samrelgohary.fenk.Model.CircleModel;
import com.samrelgohary.fenk.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ChatActivity extends AppCompatActivity {

    String friendName,friendPhoto,friendId;
    ImageView mUserImgProfile, mSendMessage;
    TextView mFriendName;
    EditText mMessageContent;

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

        Picasso.get().load(friendPhoto).into(mUserImgProfile);
        mFriendName.setText(friendName);

        mSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMessageAction();

            }
        });
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
        chatModel.setUserProfilePic(getDefaults("img",getApplicationContext()));
        chatModel.setTime(currentTime);
        chatModel.setFriendId(friendId);
        chatModel.setChatId(chatId);
        ref.child(chatId).push().setValue(chatModel);
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}
