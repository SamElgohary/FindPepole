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
import com.samrelgohary.fenk.Model.UserModel;
import com.samrelgohary.fenk.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyCircleAdapter extends ArrayAdapter<UserModel> implements ListAdapter {

    private Context mContext;
    private int layoutResourceId;
    int type;
    private ArrayList<UserModel> mGridData = new ArrayList<UserModel>();


    //It is called in the activity contains 3 parameters MainActivity, item layout , Array list
    public MyCircleAdapter(Context mContext, int layoutResourceId, ArrayList<UserModel> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;

    }

    public MyCircleAdapter(Context mContext, int layoutResourceId, ArrayList<UserModel> mGridData, int SelectedItemImageType) {
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
    public void setGridData(ArrayList<UserModel> mGridData) {
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
            holder.chatIcon  = row.findViewById(R.id.chat_icon);
            holder.liveLocationIcon  = row.findViewById(R.id.live_location_icon);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        //Receive data here to view School item
        final UserModel userModel = mGridData.get(position);

       holder.userName.setText(userModel.getFullName());

       if (!userModel.getImg().isEmpty()) {
           Picasso.get().load(userModel.getImg()).into(holder.userProfileImg);
       }


       holder.chatIcon.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getContext(), ChatActivity.class);
               intent.putExtra("friendName",userModel.getFullName());
               intent.putExtra("friendPhoto",userModel.getImg());
               intent.putExtra("friendId",userModel.getSocialId());
               mContext.startActivity(intent);
           }
       });

        return row;
    }

    //Definition of variables
    static class ViewHolder {

        TextView userName;
        ImageView userProfileImg,chatIcon,liveLocationIcon;

    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

}
