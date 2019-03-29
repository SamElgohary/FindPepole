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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.samrelgohary.fenk.Activities.NotificationActivity;
import com.samrelgohary.fenk.Model.CircleModel;
import com.samrelgohary.fenk.Model.UserModel;
import com.samrelgohary.fenk.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import static com.facebook.FacebookSdk.getApplicationContext;

public class NotificationAdapter extends ArrayAdapter<UserModel> implements ListAdapter {

    private Context mContext;
    private int layoutResourceId;
    int type;
    private ArrayList<UserModel> mGridData = new ArrayList<UserModel>();


    //It is called in the activity contains 3 parameters MainActivity, item layout , Array list
    public NotificationAdapter(Context mContext, int layoutResourceId, ArrayList<UserModel> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;

    }

    public NotificationAdapter(Context mContext, int layoutResourceId, ArrayList<UserModel> mGridData, int SelectedItemImageType) {
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

            holder.mAccept = row.findViewById(R.id.accept);
            holder.mCancle = row.findViewById(R.id.cancel);
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

        holder.mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference ref = firebaseDatabase.getReference("myFriends");

                CircleModel circleModel = new CircleModel();

                circleModel.setId(getDefaults("socialId",getApplicationContext()));
                circleModel.setFriendId(userModel.getSocialId());
                circleModel.setDate(formattedDate);

                CircleModel circleModel1 = new CircleModel();
                circleModel1.setFriendId(getDefaults("socialId",getApplicationContext()));
                circleModel1.setId(userModel.getSocialId());
                circleModel1.setDate(formattedDate);

                ref.push().setValue(circleModel);
                ref.push().setValue(circleModel1);

                cancelAction(userModel.getSocialId(),getDefaults("socialId",getContext()));

            }
        });

        holder.mCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancelAction(userModel.getSocialId(),getDefaults("socialId",getContext()));
            }
        });

        //Log.i("channelName2", "___" +UserModel.getCategory_name());

        return row;
    }

    //Definition of variables
    static class ViewHolder {

        TextView userName, mAccept,mCancle;
        ImageView userProfileImg;

    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }


    public void cancelAction(final String id, final String friendId){

        Log.i("id","___"+id);
        Log.i("friendId","___"+friendId);

        Query query ;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        query = ref.child("friendRequest");

        try{query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot itemSnapShot : dataSnapshot.getChildren()) {


                    if (itemSnapShot.child("friendId").getValue(String.class).equals(friendId)&&
                            itemSnapShot.child("id").getValue(String.class).equals(id)) {


                        Log.d("friendId", "__f" +itemSnapShot.child("friendId").getValue(String.class));
                        Log.d("id", "__f" +itemSnapShot.child("id").getValue(String.class));

                        itemSnapShot.getRef().removeValue();
                        Intent intent = new Intent(getContext(),NotificationActivity.class);
                        mContext.startActivity(intent);
                        ((Activity)mContext).finish();

                    }
//                    mProgressBar.setVisibility(View.GONE);

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
