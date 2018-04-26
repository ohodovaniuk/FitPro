package com.example.owner.fitpro.Chat.functionDefinitions.chat;

/**
 * FitPro - Group #05
 * Author: Dan Epstein
 */


import android.content.Context;

import com.example.owner.fitpro.Chat.model.Chat;


public class ChatDefinition implements ChatInterface.Presenter, ChatInterface.OnSendMessageListener,ChatInterface.OnGetMessagesListener {

    /**
     * This Class implements the chatInterface
     * The use  of the function is to display error msg, send and receive a msg from the firebase
     */

    private ChatInterface.View mView;
    private ChatFB mChatFB;

    // Constructor set the view and firebase
    public ChatDefinition(ChatInterface.View view) {
        this.mView = view;
        mChatFB = new ChatFB(this, this);
    }

    // Send the msg to the user
    @Override
    public void sendMessage(Context context, Chat chat, String receiverFirebaseToken) {
        // In order to sent the msg we need to know receiverFirebaseToken which is the identity in FB of the user we are texting.
        mChatFB.sendMessageToFirebaseUser(context, chat, receiverFirebaseToken);
    }

    // Get the msg from the user
    @Override
    public void getMessage(String senderUid, String receiverUid) {
        //In order to get the right messages we need to know our User id and the sender uid.
        mChatFB.getMessageFromFirebaseUser(senderUid, receiverUid);
    }



    // Display an error msg if it has not been successfully sent.
    @Override
    public void onSendMessageFailure(String message) {
        mView.onSendMessageFailure(message);
    }

    // Display a msg if all messages has been retrieved successfully sent.
    @Override
    public void onGetMessagesSuccess(Chat chat) {
        mView.onGetMessagesSuccess(chat);
    }

    // Display a msg if all messages has not retrieved from Firebase.
    @Override
    public void onGetMessagesFailure(String message) {
        mView.onGetMessagesFailure(message);
    }
}
