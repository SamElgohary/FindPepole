package com.samrelgohary.fenk.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

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
        UserModel userModel = mGridData.get(position);

       holder.userName.setText(userModel.getfName()+ " " + userModel.getlName());

       if (!userModel.getImg().isEmpty()) {
           Picasso.get().load(userModel.getImg()).into(holder.userProfileImg);
       }



        //Log.i("channelName2", "___" +UserModel.getCategory_name());

        return row;
    }

    //Definition of variables
    static class ViewHolder {

        TextView userName;
        ImageView userProfileImg,chatIcon,liveLocationIcon;
    }

}
