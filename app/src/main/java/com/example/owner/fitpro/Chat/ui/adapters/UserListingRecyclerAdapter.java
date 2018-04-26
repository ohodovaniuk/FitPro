package com.example.owner.fitpro.Chat.ui.adapters;

/**
 * FitPro - Group #05
 * Author: Dan Epstein
 */

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.fitpro.Chat.ChatDatabase.DatabaseHelperMarked;
import com.example.owner.fitpro.Chat.model.User;
import com.example.owner.fitpro.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class UserListingRecyclerAdapter extends RecyclerView.Adapter<UserListingRecyclerAdapter.ViewHolder> {

    private List<User> mUsers; // holds the user object item
    private  String userID; // holds the user email
    private  String userIDMsg; // hold the user


    public UserListingRecyclerAdapter(List<User> users) {
        this.mUsers = users;
    }

    public void add(User user) {
        mUsers.add(user);
        notifyItemInserted(mUsers.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_user_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = mUsers.get(position);
        // If the user email exists then set the xml data with the user details data.
        if (user.email != null){
            // Setting the context to enable to set the profile picture
            Context context = holder.ProfilePic.getContext();
            // Using Uri parse to set the photoURL
            Uri uri = Uri.parse(user.photoURL);
            Picasso.with(context).load(uri).into(holder.ProfilePic);
            // Default set the message tab with no messages note this will change if there are messeges.
            holder.lastmsg.setText("No Messages");
            // Opening the database helper to get the user ID
            DatabaseHelperMarked mDatabaseHelper =   new DatabaseHelperMarked(context);
            Cursor data = mDatabaseHelper.getID(user.email); //get the id associated with that name
            while(data.moveToNext()){
                int index = data.getColumnIndex("ID");
                userID = data.getString(index);
            }
            // Close to eliminate crash and data leak
            data.close();
            mDatabaseHelper.close();

            // By getting the user email we can get the msg
            Cursor msg = mDatabaseHelper.getMsg(user.email); //get the id associated with that name
            while(msg.moveToNext()){
                int index = msg.getColumnIndex("Msg");
                userIDMsg = msg.getString(index);
            }
            msg.close();
            if(userIDMsg != null && userID != null && userID.equals(user.email)) {
                //Log.d("YES",user.email);
                holder.lastmsg.setText(userIDMsg);
            }else{
               //Log.d("NOT", "CANT FIND");

            }
            // Sets the username
            holder.txtUsername.setText(user.name);
          }
    }

    @Override
    public int getItemCount() {
        if (mUsers != null) {
            return mUsers.size();
        }
        return 0;
    }

    public User getUser(int position) {
        return mUsers.get(position);
    }

    // Setting the Views with the XML ids
    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView  txtUsername, lastmsg;
        private ImageView ProfilePic;
        ViewHolder(View itemView) {
            super(itemView);
            lastmsg = (TextView) itemView.findViewById(R.id.lastMsg);
            ProfilePic = (ImageView) itemView.findViewById(R.id.userProfileImage);
            txtUsername = (TextView) itemView.findViewById(R.id.text_view_username);
        }
    }
}
