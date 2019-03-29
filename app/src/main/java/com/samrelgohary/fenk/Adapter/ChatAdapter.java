package com.samrelgohary.fenk.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.samrelgohary.fenk.Activities.ChatActivity;
import com.samrelgohary.fenk.Model.CircleModel;
import com.samrelgohary.fenk.Model.ChatModel;
import com.samrelgohary.fenk.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ChatAdapter extends ArrayAdapter<ChatModel> implements ListAdapter {

    private Context mContext;
    private int layoutResourceId;
    int type;
    private ArrayList<ChatModel> mGridData = new ArrayList<ChatModel>();


    //It is called in the activity contains 3 parameters MainActivity, item layout , Array list
    public ChatAdapter(Context mContext, int layoutResourceId, ArrayList<ChatModel> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;

    }

    public ChatAdapter(Context mContext, int layoutResourceId, ArrayList<ChatModel> mGridData, int SelectedItemImageType) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
        type = SelectedItemImageType;
    }

    /**
     * Updates grid data and refresh grid items.
     *
     * @param mGridData
     */
    public void setGridData(ArrayList<ChatModel> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }


    //Receive data here to view School item
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            //Connect the variables to id

            holder.contentMessageChatLeft = row.findViewById(R.id.contentMessageChatLeft);
            holder.timestampLeft = row.findViewById(R.id.timestampLeft);
            holder.ivUserChatLeft  = row.findViewById(R.id.ivUserChatLeft);
            holder.contentMessageChatRight  = row.findViewById(R.id.contentMessageChatRight);
            holder.timestampRight  = row.findViewById(R.id.timestampRight);
            holder.ivUserChatRight  = row.findViewById(R.id.ivUserChatRight);
            holder.chatLeft = row.findViewById(R.id.chatLeft);
            holder.chatRight = row.findViewById(R.id.chatRight);


            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        //Receive data here to view School item
        final ChatModel chatModel = mGridData.get(position);

        Log.d("gravityValue","___"+chatModel.getGravity());


        if (chatModel.getGravity()==0) {
            holder.chatRight.setVisibility(View.VISIBLE);
            holder.contentMessageChatRight.setText(chatModel.getMessage());
            holder.timestampRight.setText(chatModel.getTime());
            Picasso.get().load(chatModel.getUserProfilePic()).into(holder.ivUserChatRight);

            }
            else {

            holder.chatRight.setVisibility(View.VISIBLE);
            holder.contentMessageChatRight.setText(chatModel.getMessage());
            holder.timestampRight.setText(chatModel.getTime());
            Picasso.get().load(chatModel.getUserProfilePic()).into(holder.ivUserChatRight);
        }


        if (chatModel.getGravity()==1) {
            holder.chatLeft.setVisibility(View.VISIBLE);
            holder.contentMessageChatLeft.setText(chatModel.getMessage());
            holder.timestampLeft.setText(chatModel.getTime());
            Picasso.get().load(chatModel.getUserProfilePic()).into(holder.ivUserChatLeft);
        }

        return row;
    }

    //Definition of variables
    static class ViewHolder {

        TextView  contentMessageChatLeft,timestampLeft,  contentMessageChatRight, timestampRight ;
        ImageView ivUserChatLeft, ivUserChatRight;
        RelativeLayout chatLeft,chatRight;
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }



}
