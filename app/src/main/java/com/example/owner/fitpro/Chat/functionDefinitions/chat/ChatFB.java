package com.example.owner.fitpro.Chat.functionDefinitions.chat;

/**
 * FitPro - Group #05
 * Author: Dan Epstein
 */

import android.content.Context;
import android.util.Log;

import com.example.owner.fitpro.Chat.model.Chat;
import com.example.owner.fitpro.Chat.utility.Constants;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ChatFB implements ChatInterface.Interactor {


    private static final String TAG = "ChatFB";
    private ChatInterface.OnSendMessageListener mOnSendMessageListener;
    private ChatInterface.OnGetMessagesListener mOnGetMessagesListener;


    // ChatFB constructor this will ensure that when a messege is sent or receive it will display on the activity.
    public ChatFB(ChatInterface.OnSendMessageListener onSendMessageListener,
                  ChatInterface.OnGetMessagesListener onGetMessagesListener) {
        this.mOnSendMessageListener = onSendMessageListener;
        this.mOnGetMessagesListener = onGetMessagesListener;
    }

    // This function will enable the user to sent a message.
    @Override
    public void sendMessageToFirebaseUser(final Context context, final Chat chat, final String receiverFirebaseToken) {

        // Here we are creating 2 types of rooms
        // Room one is for the user who is logged on
        // Room Two is for the other user
        final String room_one = chat.senderUid + "_" + chat.receiverUid;
        final String room_two = chat.receiverUid + "_" + chat.senderUid;

        // Getting reference info of the firebase database information.
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Getting the values of all the chat rooms in firebase
        databaseReference.child(Constants.ARG_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(room_one)) {
                    // If the chat room matches in firebase then set the chat values in the activity and update the firebase with the data that the user has sent
                    databaseReference.child(Constants.ARG_CHAT_ROOMS).child(room_one).child(String.valueOf(chat.timestamp)).setValue(chat);
                } else if (dataSnapshot.hasChild(room_two)) {
                    // If the chat room matches in firebase for room 2 then set the chat values in the activity and update the firebase with the data that the user has sent
                    databaseReference.child(Constants.ARG_CHAT_ROOMS).child(room_two).child(String.valueOf(chat.timestamp)).setValue(chat);
                } else {
                    databaseReference.child(Constants.ARG_CHAT_ROOMS).child(room_one).child(String.valueOf(chat.timestamp)).setValue(chat);
                    // Get all the chat messages
                    getMessageFromFirebaseUser(chat.senderUid, chat.receiverUid);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Display error msg if unable to send message (No network etc)
                mOnSendMessageListener.onSendMessageFailure("Unable to send message: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void getMessageFromFirebaseUser(String senderUid, String receiverUid) {

        // Here we are creating 2 types of rooms
        // Room one is for the user who is logged on
        // Room Two is for the other user
        final String room_one = senderUid + "_" + receiverUid;
        final String room_two = receiverUid + "_" + senderUid;
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Access the firebase database to get all messages
        databaseReference.child(Constants.ARG_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(room_one)) {
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(Constants.ARG_CHAT_ROOMS)
                            .child(room_one).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Chat chat = dataSnapshot.getValue(Chat.class);
                            mOnGetMessagesListener.onGetMessagesSuccess(chat);
                        }

                        // Not in use
                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {}
                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                        // Display a msg if something goes wrong
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
                        }
                    });
                } else if (dataSnapshot.hasChild(room_two)) {
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(Constants.ARG_CHAT_ROOMS)
                            .child(room_two).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Chat chat = dataSnapshot.getValue(Chat.class);
                            mOnGetMessagesListener.onGetMessagesSuccess(chat);
                        }

                        // Not in use
                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {}
                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                        // Display a msg if something goes wrong
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
                        }
                    });
                } else {
                    Log.e(TAG, "No such room available");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Display error msg if unable to send message (No network etc)
                mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
            }
        });
    }
}
