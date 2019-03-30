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
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.samrelgohary.fenk.Activities.ChatActivity;
import com.samrelgohary.fenk.Model.CircleModel;
import com.samrelgohary.fenk.Model.ChatModel;
import com.samrelgohary.fenk.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ChatListAdapter extends ArrayAdapter<ChatModel> implements ListAdapter {

    private Context mContext;
    private int layoutResourceId;
    int type;
    private ArrayList<ChatModel> mGridData = new ArrayList<ChatModel>();


    //It is called in the activity contains 3 parameters MainActivity, item layout , Array list
    public ChatListAdapter(Context mContext, int layoutResourceId, ArrayList<ChatModel> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;

    }

    public ChatListAdapter(Context mContext, int layoutResourceId, ArrayList<ChatModel> mGridData, int SelectedItemImageType) {
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

            holder.userName = row.findViewById(R.id.user_name);
            holder.userProfileImg = row.findViewById(R.id.user_img_profile);
            holder.userItem  = row.findViewById(R.id.user_item_ll);
            holder.lastMessage  = row.findViewById(R.id.message);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        //Receive data here to view School item
        final ChatModel chatModel = mGridData.get(position);

                holder.userName.setText(chatModel.getUserName());
                holder.lastMessage.setText(chatModel.getMessage());

                Log.d("nameFromdapter","___"+chatModel.getUserName());

                if (!chatModel.getUserProfilePic().isEmpty()) {
                    Picasso.get().load(chatModel.getUserProfilePic()).into(holder.userProfileImg);
                }


                holder.userItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ChatActivity.class);
                        intent.putExtra("friendName",chatModel.getUserName());
                        intent.putExtra("friendPhoto",chatModel.getUserProfilePic());
                        intent.putExtra("friendId",chatModel.getUserId());
                        mContext.startActivity(intent);
                        //((Activity)mContext).finish();

                    }
                });


        return row;
    }

    //Definition of variables
    static class ViewHolder {

        TextView userName, lastMessage;
        ImageView userProfileImg;
        LinearLayout userItem;

    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

}

