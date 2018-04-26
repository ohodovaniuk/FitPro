package com.example.owner.fitpro.Chat.functionDefinitions.chat;

/**
 * FitPro - Group #05
 * Author: Dan Epstein
 */

import android.content.Context;

import com.example.owner.fitpro.Chat.model.Chat;


public interface ChatInterface {

    /**
     * These are function interfaces which gets implemented in ChatDefinition class
     * Interface View will display msg on the app screen
     * Interface Presenter able to send and get msg from the database and also to it.
     * Interface Interactor is what updates the chat view screen with the messages.
     * Interface onSend and OnGet display msg on app screen.
     */

    interface View {
        void onSendMessageFailure(String message);
        void onGetMessagesSuccess(Chat chat);
        void onGetMessagesFailure(String message);
    }

    interface Presenter {
        void sendMessage(Context context, Chat chat, String receiverFirebaseToken);
        void getMessage(String senderUid, String receiverUid);
    }

    interface Interactor {
        void sendMessageToFirebaseUser(Context context, Chat chat, String receiverFirebaseToken);
        void getMessageFromFirebaseUser(String senderUid, String receiverUid);
    }

    interface OnSendMessageListener {
        void onSendMessageFailure(String message);
    }

    interface OnGetMessagesListener {
        void onGetMessagesSuccess(Chat chat);
        void onGetMessagesFailure(String message);
    }
}
