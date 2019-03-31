package com.samrelgohary.fenk.Adapter;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.preference.PreferenceManager;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentTransaction;
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
        import com.samrelgohary.fenk.Activities.AddToCircleActivity;
        import com.samrelgohary.fenk.Activities.MainActivity;
        import com.samrelgohary.fenk.Activities.NotificationActivity;
        import com.samrelgohary.fenk.Fragments.CircleFragment;
        import com.samrelgohary.fenk.Model.CircleModel;
        import com.samrelgohary.fenk.Model.UserModel;
        import com.samrelgohary.fenk.R;
        import com.squareup.picasso.Picasso;

        import java.util.ArrayList;

        import static com.facebook.FacebookSdk.getApplicationContext;

public class AllCircleAdapter extends ArrayAdapter<UserModel> implements ListAdapter {

    private Context mContext;
    private int layoutResourceId;
    int type;
    private ArrayList<UserModel> mGridData = new ArrayList<UserModel>();


    //It is called in the activity contains 3 parameters MainActivity, item layout , Array list
    public AllCircleAdapter(Context mContext, int layoutResourceId, ArrayList<UserModel> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;

    }

    public AllCircleAdapter(Context mContext, int layoutResourceId, ArrayList<UserModel> mGridData, int SelectedItemImageType) {
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
        final ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            //Connect the variables to id

            holder.userName = row.findViewById(R.id.user_name);
            holder.addToTV   = row.findViewById(R.id.addToTV);
            holder.userProfileImg = row.findViewById(R.id.user_img_profile);
            holder.chatIcon  = row.findViewById(R.id.chat_icon);
            holder.liveLocationIcon  = row.findViewById(R.id.live_location_icon);

            holder.mAddTo            = row.findViewById(R.id.add_to_ll);
            holder.addIcon  = row.findViewById(R.id.addIcon);

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

        holder.mAddTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.addToTV.getText().toString().equals(mContext.getResources().getString(R.string.add_to_my_circle))) {

                    addToMyCircle(userModel.getSocialId());

                }
                if (holder.addToTV.getText().toString().equals(mContext.getResources().getString(R.string.cancel_request))){

                    cancelAction(holder,getDefaults("socialId", getApplicationContext()),userModel.getSocialId());
                }
            }
        });

        getMyFriendsId(holder,userModel.getSocialId());
        getMyRequestsId(holder,userModel.getSocialId());
        //Log.i("channelName2", "___" +UserModel.getCategory_name());

        return row;
    }

    //Definition of variables
    static class ViewHolder {

        TextView userName, addToTV;
        ImageView userProfileImg,chatIcon,liveLocationIcon,addIcon;

        LinearLayout mAddTo;
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    public void getMyFriendsId(final ViewHolder viewHolder, final String FriendId) {

        Query query ;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        query = ref.child("myFriends");

        try{query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot itemSnapShot : dataSnapshot.getChildren()) {

                    //  UserModel userModel =
                    new UserModel();
                    if (itemSnapShot.child("id").getValue(String.class).equals(getDefaults("socialId",getApplicationContext()))) {

                        if (FriendId.equals(itemSnapShot.child("friendId").getValue(String.class))){

                            viewHolder.addToTV.setText(mContext.getResources().getString(R.string.already_added));
                            viewHolder.addToTV.setTextColor(mContext.getResources().getColor(R.color.hint));
                            viewHolder.addIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_person_gray_24dp));
                        }else {

                            viewHolder.addToTV.setText(mContext.getResources().getString(R.string.add_to_my_circle));
                            viewHolder.addToTV.setTextColor(mContext.getResources().getColor(R.color.ice));
                            viewHolder.addIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_person_add_ice_24dp));
                        }

                        Log.d("getFriendId", "__" + itemSnapShot.child("friendId").getValue(String.class));

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });} catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getMyRequestsId(final ViewHolder viewHolder, final String FriendId) {

        Query query ;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        query = ref.child("friendRequest");

        try{query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot itemSnapShot : dataSnapshot.getChildren()) {

                    //  UserModel userModel = new UserModel();

                    if (itemSnapShot.child("id").getValue(String.class).equals(getDefaults("socialId",getApplicationContext()))) {

                        if (FriendId.equals(itemSnapShot.child("friendId").getValue(String.class))){

                            viewHolder.addToTV.setText(mContext.getResources().getString(R.string.cancel_request));
                            viewHolder.addToTV.setTextColor(mContext.getResources().getColor(R.color.grayDark));
                            viewHolder.addIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_cancel_ovest_24dp));
                        }

                        Log.d("getFriendId", "__" + itemSnapShot.child("friendId").getValue(String.class));

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });} catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelAction(final ViewHolder viewHolder, final String id, final String friendId){

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
                        getMyFriendsId(viewHolder,friendId);
//                        Intent intent = new Intent(getContext(),AddToCircleActivity.class);
//                        mContext.startActivity(intent);
//                        ((Activity)mContext).finish();

                        //  getMyRequestsId(viewHolder,friendId);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });} catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void addToMyCircle(final String friendId){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference("friendRequest");
        CircleModel circleModel = new CircleModel();
        circleModel.setId(getDefaults("socialId", getApplicationContext()));
        circleModel.setFriendId(friendId);
        ref.push().setValue(circleModel);
//        Intent intent = new Intent(getContext(), AddToCircleActivity.class);
//         mContext.startActivity(intent);
//        ((Activity)mContext).finish();

    }
}
