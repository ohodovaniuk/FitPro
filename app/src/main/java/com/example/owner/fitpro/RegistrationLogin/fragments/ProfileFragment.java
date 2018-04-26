package com.example.owner.fitpro.RegistrationLogin.fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.owner.fitpro.Chat.model.User;
import com.example.owner.fitpro.R;
import com.example.owner.fitpro.RegistrationLogin.activities.EditProfileActivity;
import com.example.owner.fitpro.RegistrationLogin.apis.APIs;
import com.example.owner.fitpro.RegistrationLogin.services.DataServices;

import java.util.function.Function;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    ImageView userProfileIV, editIV;
    TextView heightInput, weightInput, expInput, bodyInput, goalInput;
    APIs apis;
    DataServices dataServices;
    Uri imagePath;

    private final int PICK_IMAGE_REQUEST = 71;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        this.userProfileIV = (ImageView) v.findViewById(R.id.pUserProfileIV);
        this.editIV = (ImageView) v.findViewById(R.id.pEditIV);
        this.heightInput = (TextView) v.findViewById(R.id.pHeightInputTV);
        this.weightInput = (TextView) v.findViewById(R.id.pWeightInputTV);
        this.expInput = (TextView) v.findViewById(R.id.pExpInputTV);
        this.bodyInput = (TextView) v.findViewById(R.id.pBodyInputTV);
        this.goalInput = (TextView) v.findViewById(R.id.pGoalInputTV);
        this.apis = new APIs();
        this.dataServices = new DataServices();

        this.userProfileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromGallery();
            }
        });

        this.editIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), EditProfileActivity.class));
            }
        });

        this.apis.getUsersAPI().observeCurrentUser(
                new Function<User, Void>() {
                    @Override
                    public Void apply(User user) {
                        Glide
                                .with(getContext())
                                .load(user.photoURL)
                                .into(userProfileIV);
                        heightInput.setText(user.height);
                        weightInput.setText(user.weight);
                        expInput.setText(user.exp);
                        bodyInput.setText(user.bodyType);
                        goalInput.setText(user.goal);
                      //  Log.d("------------------------------USER GOAL", user.goal);
                        return null;
                    }
                }, new Function<String, Void>() {
                    @Override
                    public Void apply(String s) {
                        return null;
                    }
                });


        return v;
    }

    public void fromGallery() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Change Profile Image?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            this.imagePath = data.getData();
            if (this.imagePath != null) {
                final ProgressBar progressBar = new ProgressBar(getContext());
                progressBar.setVisibility(View.VISIBLE);

                this.dataServices.updateProfilePicture(this.apis.getUsersAPI().getCurrentUser().getUid(), this.imagePath,
                        new Function<Void, Void>() {
                            @Override
                            public Void apply(Void aVoid) {
                                Toast.makeText(getContext(), "File Uploaded!", Toast.LENGTH_LONG).show();
                                return null;
                            }
                        }, new Function<String, Void>() {
                            @Override
                            public Void apply(String s) {
                                Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
                                return null;
                            }
                        }, new Function<Integer, Void>() {
                            @Override
                            public Void apply(Integer integer) {
                                Toast.makeText(getContext(), "Uploading...", Toast.LENGTH_LONG).show();
                                return null;
                            }
                        });
            }
        }
    }
}
