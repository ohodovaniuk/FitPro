package com.example.owner.fitpro.Chat.ui.fragments;

/**
 * FitPro - Group #05
 * Author: Dan Epstein
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.owner.fitpro.Chat.functionDefinitions.chat.ChatDefinition;
import com.example.owner.fitpro.Chat.functionDefinitions.chat.ChatInterface;
import com.example.owner.fitpro.Chat.model.Chat;
import com.example.owner.fitpro.Chat.ui.adapters.ChatRecyclerAdapter;
import com.example.owner.fitpro.Chat.utility.Constants;
import com.example.owner.fitpro.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;


public class ChatFragment extends Fragment implements ChatInterface.View {

    // Creating views to hold the values and for manipulation in the functions below.
    private RecyclerView mRecyclerViewChat;
    private EditText mETxtMessage;
    private ChatRecyclerAdapter mChatRecyclerAdapter;
    private ChatDefinition mChatDefinition;

    // Creating the fragment instance to set the string of the user
    public static ChatFragment newInstance(String receiver,
                                           String receiverUid,
                                           String firebaseToken) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_RECEIVER, receiver);
        args.putString(Constants.ARG_RECEIVER_UID, receiverUid);
        args.putString(Constants.ARG_FIREBASE_TOKEN, firebaseToken);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_chat, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    // Setting the views
    private void bindViews(View view) {
        mRecyclerViewChat = (RecyclerView) view.findViewById(R.id.recycler_view_chat);
        mETxtMessage = (EditText) view.findViewById(R.id.edit_text_message);
        ImageButton sendButton = view.findViewById(R.id.send_messege);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    // Initializing variables
    private void init() {
        mChatDefinition = new ChatDefinition(this);
        mChatDefinition.getMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                getArguments().getString(Constants.ARG_RECEIVER_UID));
    }

    // Getting the current time
    private String getReminingTime() {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
    }

    private void sendMessage() {
        // Setters
        String message = mETxtMessage.getText().toString();
        String receiver = getArguments().getString(Constants.ARG_RECEIVER);
        String receiverUid = getArguments().getString(Constants.ARG_RECEIVER_UID);
        String sender = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String receiverFirebaseToken = getArguments().getString(Constants.ARG_FIREBASE_TOKEN);
        String currentTime = getReminingTime();

        // If msg is empty then do nothing else  update to the firebase data
        if(message.isEmpty()){
            //Log.d("STATE", "ERROR EMPTY");
        } else {
            Chat chat = new Chat(sender,
                    receiver,
                    senderUid,
                    receiverUid,
                    message,
                    currentTime,
                    System.currentTimeMillis());
            mChatDefinition.sendMessage(getActivity().getApplicationContext(),
                    chat,
                    receiverFirebaseToken);
            // Set msg text content empty
            mETxtMessage.setText("");
        }
    }

    // Display error msg if send isn't working
    @Override
    public void onSendMessageFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    // Sets the adapter
    @Override
    public void onGetMessagesSuccess(Chat chat) {
        if (mChatRecyclerAdapter == null) {
            mChatRecyclerAdapter = new ChatRecyclerAdapter(new ArrayList<Chat>());
            mRecyclerViewChat.setAdapter(mChatRecyclerAdapter);
        }
        mChatRecyclerAdapter.add(chat);
        mRecyclerViewChat.smoothScrollToPosition(mChatRecyclerAdapter.getItemCount() - 1);
    }

    // Display error msg if can't retrieve msg
    @Override
    public void onGetMessagesFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
