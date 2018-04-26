package com.example.owner.fitpro.Chat.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.fitpro.Chat.ChatDatabase.DatabaseHelperMarked;
import com.example.owner.fitpro.Chat.model.Chat;
import com.example.owner.fitpro.Chat.utility.Constants;
import com.example.owner.fitpro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * FitPro - Group #05
 * Author: Dan Epstein
 */

public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // Enable to hold the int value until the app shutdown or the user quit
    // This will identify which item chat will display
    private static final int CHAT_VIEW_TYPE_ME = 1;
    private static final int CHAT_VIEW_TYPE_OTHER_USER = 2;
    private List<Chat> mChats;

    // Setter
    public ChatRecyclerAdapter(List<Chat> chats) {
        mChats = chats;
    }

    // Add chat on the correct position
    public void add(Chat chat) {
        mChats.add(chat);
        notifyItemInserted(mChats.size() - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the activity context view (chat activity)
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        // Depends on which view is found, this will constantly switch between views that will display my chat or the other user chat context
        switch (viewType) {
            case CHAT_VIEW_TYPE_ME:
                View viewChatMine = layoutInflater.inflate(R.layout.item_chat_mine, parent, false);
                viewHolder = new MyChatViewHolder(viewChatMine);
                break;
            case CHAT_VIEW_TYPE_OTHER_USER:
                View viewChatOther = layoutInflater.inflate(R.layout.item_chat_other, parent, false);
                viewHolder = new OtherChatViewHolder(viewChatOther);
                break;
        }
        return viewHolder;
    }

    // Gets the item position and the holder data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (TextUtils.equals(mChats.get(position).senderUid,
                FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            configureMyChatViewHolder((MyChatViewHolder) holder, position);
        } else {
            configureOtherChatViewHolder((OtherChatViewHolder) holder, position);
        }
    }

    private void configureMyChatViewHolder(MyChatViewHolder myChatViewHolder, int position) {
        Chat chat = mChats.get(position);
        myChatViewHolder.txtChatMessage.setText(chat.message);
        myChatViewHolder.currentTime.setText(chat.CurrentTime);
        //Log.d("My Msg", chat.message.toString());
    }

    // Configuring other view holder this will set their photo, time, message, profile picture
    private void configureOtherChatViewHolder(OtherChatViewHolder otherChatViewHolder, int position) {
        final Chat chat = mChats.get(position);
        Context context = otherChatViewHolder.ProfilePic.getContext();
        Uri uri = Uri.parse(Constants.ARG_FIREBASE_PHOTO);
        Picasso.with(context).load(uri).into(otherChatViewHolder.ProfilePic);
        otherChatViewHolder.currentTime.setText(chat.CurrentTime);
        otherChatViewHolder.txtChatMessage.setText(chat.message);

        // Get last msg from other user
        DatabaseHelperMarked mDatabaseHelper =   new DatabaseHelperMarked(context);
        // Updates the last messege sent by the other user
        mDatabaseHelper.updateMsg(otherChatViewHolder.txtChatMessage.getText().toString(), chat.sender.toString());
        //mDatabaseHelper.close();
    }

    @Override
    public int getItemCount() {
        if (mChats != null) {
            return mChats.size();
        }
        return 0;
    }

    // This function enables to identify by using the firebase getinstance which user we are currently logged as
    // and the other user we are trying to text with.
    @Override
    public int getItemViewType(int position) {
        if (TextUtils.equals(mChats.get(position).senderUid,
                FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return CHAT_VIEW_TYPE_ME;
        } else {
            return CHAT_VIEW_TYPE_OTHER_USER;
        }
    }
    // Below we have 2 classes which hold the properties for the views
    private static class MyChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage, currentTime;
        public MyChatViewHolder(View itemView) {
            super(itemView);
            currentTime = (TextView) itemView.findViewById(R.id.time_current);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
        }
    }

    // Holds the properties data for the other user chat
    private static class OtherChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage,  currentTime;
        private ImageView ProfilePic;
        public OtherChatViewHolder(View itemView) {
            super(itemView);
            ProfilePic = (ImageView) itemView.findViewById(R.id.userProfileImage);
            currentTime = (TextView) itemView.findViewById(R.id.time_current);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
        }
    }
}
