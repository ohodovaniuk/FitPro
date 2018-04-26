package com.example.owner.fitpro.RegistrationLogin.services;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.owner.fitpro.RegistrationLogin.apis.APIs;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DataServices {

    private APIs apis = new APIs();

    public DataServices() {}

    public void writeUserProfile(Map profileMap, final Function<Void, Void> onSuccess, final Function<String, Void> onFailure) {
        DatabaseReference dataRef = this.apis.getUsersAPI().getCurrentUserRef();

        dataRef.updateChildren(profileMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if ( error == null ) {
                    onSuccess.apply(null);
                } else {
                    onFailure.apply(error.getMessage());
                }
            }
        });
    }

    public void updateProfilePicture(String userID, final Uri image, final Function<Void, Void> onSuccess, final Function<String, Void> onFailure, final Function<Integer, Void> onProgress) {
        final DatabaseReference dataRef = this.apis.getUsersAPI().getUsersRef().child(userID);
        StorageReference storRef = FirebaseStorage.getInstance().getReference().child("profilePhotos").child(userID);

        storRef.putFile(image)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String imageURL = taskSnapshot.getDownloadUrl().toString();
                        Map<String, Object> imageMap = new HashMap<>();
                        imageMap.put("photoURL", imageURL);

                        dataRef.updateChildren(imageMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError error, DatabaseReference ref) {
                                if ( error == null ) {
                                    onSuccess.apply(null);
                                } else {
                                    onFailure.apply(error.getMessage());
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onFailure.apply(e.getMessage());
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        int progress = (int)(100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        onProgress.apply(progress);
                    }
                });
    }

}
