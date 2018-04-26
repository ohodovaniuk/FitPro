package com.example.owner.fitpro.Chat.ui.adapters;

/**
 * FitPro - Group #05
 * Author: Dan Epstein
 */

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.fitpro.Chat.ChatDatabase.DatabaseHelper;
import com.example.owner.fitpro.Chat.ChatDatabase.DatabaseHelperMarked;
import com.example.owner.fitpro.Chat.model.User;
import com.example.owner.fitpro.Chat.ui.activities.ContactListActivity;
import com.example.owner.fitpro.MainActivity;
import com.example.owner.fitpro.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.owner.fitpro.Chat.model.Globals.AddedUserList;
import static com.example.owner.fitpro.Chat.model.Globals.AddedUserListContactList;

public class UserListingRecyclerAdapterCheckBox extends RecyclerView.Adapter<UserListingRecyclerAdapterCheckBox.ViewHolder> {

    private List<User> mUsers;
    private String userID;

    public UserListingRecyclerAdapterCheckBox(List<User> users) {
        this.mUsers = users;
    }

    public void add(User user) {
        mUsers.add(user);
        notifyItemInserted(mUsers.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_user_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = mUsers.get(position);
        // If the user email exists then set the xml data with the user details data. this is similar to the UserListingRecyclerAdapter
        if (user.email != null){
            // Setting the context to enable to set the profile picture
            Context context = holder.ProfilePic.getContext();
            // Using Uri parse to set the photoURL
            Uri uri = Uri.parse(user.photoURL);
            Picasso.with(context).load(uri).into(holder.ProfilePic);
            holder.txtUsername.setText(user.name);

            // Setting a listener for the checkbox if the user checkbox is clicked then it will add to the database of users
            // and will redirect you to the chat (main activity) page from there you will see the newly added user on the list.
            // Then you can be able to chat with them by tapping on them.
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    // Opening the database "Marked"
                    DatabaseHelperMarked mDatabaseHelper =   new DatabaseHelperMarked(context);
                    Intent intent = new Intent (context,MainActivity.class);
                    // puting the number 2 which is chat to tabSelected this will redirect to the chat tab
                    intent.putExtra("tabSelected", "2");

                    mDatabaseHelper.addDataEmailMarked(user.email);
                    mDatabaseHelper.close();
                    //get the id associated with that email
                    Cursor data = mDatabaseHelper.getUserMarkedStatus(user.email);
                    while(data.moveToNext()){
                        int index = data.getColumnIndex("Marked");
                        userID = data.getString(index);
                    }
                    data.close();
                    mDatabaseHelper.close();

                    //Log.d("DATA", userID.toString());

                    if(user.email.toString().equals("MARKED")){
                      //  Log.d("DATA", userID.toString());
                    }
                    //Log.d("GET EMAILS",user.email);

                    // Go to Main Activity
                    context.startActivity(intent);
                }
            });

            DatabaseHelperMarked mDatabaseHelper =   new DatabaseHelperMarked(context);
            DatabaseHelper mDatabaseHelperuser =   new DatabaseHelper(context);

            // opening the database to get the userID to check if the user checkbox has been ever checked.
            Cursor data = mDatabaseHelper.getID(user.email); //get the id associated with that name
            while(data.moveToNext()){
                int index = data.getColumnIndex("ID");
                userID = data.getString(index);
            }
            data.close();
            mDatabaseHelper.close();
            // Validates if the user exists and is equal to the avaliable u
            if(userID != null && userID.equals(user.email)) {
                //Log.d("YES",user.email);
                // Disable the checkbox aka the user cannot click on it anymore
                holder.checkBox.setEnabled(false);
            }else{
                //Log.d("NOT", user.email);
            }
            // Setting a click listener to the delete buttun
            holder.deleteUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // If the user tap on the delete button it will open a dialog box with cancel or remove option
                    final Dialog dialog = new Dialog(context);
                    // inflate the layout
                    dialog.setContentView(R.layout.delete_user_dialog);
                    Button btCancel = (Button)dialog.findViewById(R.id.btCancel);
                    Button btNext = (Button)dialog.findViewById(R.id.btNext);

                    btCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                    // If the user choose to remove the other user then it will remove that user from the database
                    btNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        mDatabaseHelper.deleteName(user.email);
                        mDatabaseHelperuser.deleteName(user.email);

                        AddedUserList.remove(user.email);
                        AddedUserListContactList.remove(user.email);
                            Intent intent = new Intent (v.getContext(), ContactListActivity.class);
                            context.startActivity(intent);
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mUsers != null) {
            return mUsers.size();
        }
        return 0;
    }

    // Setting the Views with the XML ids
    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtUsername;
        private ImageView ProfilePic, deleteUser;
        private CheckBox checkBox;
        ViewHolder(View itemView) {
            super(itemView);
            deleteUser = (ImageView) itemView.findViewById(R.id.ibDeleteUser);
            ProfilePic = (ImageView) itemView.findViewById(R.id.userProfileImage);
            txtUsername = (TextView) itemView.findViewById(R.id.text_view_username);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }
    }
}
