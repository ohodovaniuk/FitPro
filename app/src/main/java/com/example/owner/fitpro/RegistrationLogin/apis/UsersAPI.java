package com.example.owner.fitpro.RegistrationLogin.apis;

import android.util.Log;

import com.example.owner.fitpro.Chat.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.function.Function;

/**
 * Created by Shirwa on 2018-02-17.
 */

public class UsersAPI {
    private final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

    public UsersAPI() {}

    public DatabaseReference getUsersRef() {
        return this.usersRef;
    }

    public final FirebaseUser getCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            return user;
        } else {
            return null;
        }
    }

    public final DatabaseReference getCurrentUserRef() {
        if (this.getCurrentUser() != null) {
            return this.usersRef.child(this.getCurrentUser().getUid());
        } else {
            return null;
        }
    }

    public final void observeCurrentUser(final Function<User, Void> onSuccess, final Function<String, Void> onFailure) {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("-----USER", dataSnapshot.getKey());
                onSuccess.apply(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onFailure.apply(databaseError.getMessage());
            }
        };

        if (this.getCurrentUserRef() != null) {
            this.getCurrentUserRef().addValueEventListener(listener);
        }
    }

    public final void observeUser(String userID, final Function<User, Void> onSuccess, final Function<String, Void> onFailure) {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onSuccess.apply(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onFailure.apply(databaseError.getMessage());
            }
        };

        this.getUsersRef().child(userID).addValueEventListener(listener);
    }







}
