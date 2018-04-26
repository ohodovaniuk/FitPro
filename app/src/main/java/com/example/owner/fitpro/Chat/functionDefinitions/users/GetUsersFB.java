package com.example.owner.fitpro.Chat.functionDefinitions.users;

/**
 * FitPro - Group #05
 * Author: Dan Epstein
 */

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.example.owner.fitpro.Chat.model.Globals;
import com.example.owner.fitpro.Chat.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.example.owner.fitpro.Chat.model.Globals.AddedUserList;
import static com.example.owner.fitpro.Chat.model.Globals.AddedUserListContactList;


public class GetUsersFB extends AppCompatActivity implements GetUsersInterface.Interactor  {

    private GetUsersInterface.OnGetAllUsersListener mOnGetAllUsersListener;
    public GetUsersFB(GetUsersInterface.OnGetAllUsersListener onGetAllUsersListener) {
        this.mOnGetAllUsersListener = onGetAllUsersListener;
    }

    //This function will display the added user on the chat activity
    @Override
    public void getAllUsersFromFirebase() {
        FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                List<User> users = new ArrayList<>();
                ArrayList<String> obj = new ArrayList<String>();

                //Remove Duplicates
                Set<String> hs = new HashSet<>();
                hs.addAll(AddedUserList);
                AddedUserList.clear();
                AddedUserList.addAll(hs);

                //Add all none duplicates users
                for(int i = 0; i<AddedUserList.size(); i++){
                    obj.add(AddedUserList.get(i).toString());
                }

                // Loop trough the data and only add the checked marked users
                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    User user = dataSnapshotChild.getValue(User.class);

                    if (!TextUtils.equals(user.uid, FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        if( Globals.getInstance().UserArray != null) {
                        for(int i = 0; i<obj.size(); i++) {
                            if (user.email.equals(obj.get(i)) ) {
                                users.add(user); // Add the user to the chat list
                            }
                        }
                        }
                    }
                }
                mOnGetAllUsersListener.onGetAllUsersSuccess(users);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                mOnGetAllUsersListener.onGetAllUsersFailure(databaseError.getMessage());
            }
        });
    }

    //This function will display the added user on the ContactListActivity
    @Override
    public void getChatUsersFromFirebase() {
        FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                List<User> users = new ArrayList<>();
                ArrayList<String> obj = new ArrayList<String>();


                //Remove Duplicates
                Set<String> hs = new HashSet<>();
                hs.addAll(AddedUserListContactList);
                AddedUserListContactList.clear();
                AddedUserListContactList.addAll(hs);

                //Add all none duplicates users
                for(int i = 0; i<AddedUserListContactList.size(); i++){
                    obj.add(AddedUserListContactList.get(i).toString());
                }

                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    User user = dataSnapshotChild.getValue(User.class);
                    if (!TextUtils.equals(user.uid, FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        if( Globals.getInstance().UserArray != null) {
                            for(int i = 0; i<obj.size(); i++) {
                                if (user.email.equals(obj.get(i))) {
                                    users.add(user); // Add the user to the contact list
                                }
                            }
                        }
                    }
                }
                mOnGetAllUsersListener.onGetAllUsersSuccess(users);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                mOnGetAllUsersListener.onGetAllUsersFailure(databaseError.getMessage());
            }
        });
    }
}
