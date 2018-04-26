package com.example.owner.fitpro.RegistrationLogin.services;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.owner.fitpro.Chat.model.User;
import com.example.owner.fitpro.RegistrationLogin.apis.APIs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.function.Function;

public class AuthServices {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private APIs apis = new APIs();
    private DataServices dataServices = new DataServices();

    public AuthServices() {}

    public FirebaseAuth getAuth() {
        return this.auth;
    }

    public void login(Activity activity, String email, String password, final Function<Void, Void> onSuccess, final Function<String, Void> onFailure) {
        this.getAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if ( task.isSuccessful() ) {
                            onSuccess.apply(null);
                        } else {
                            onFailure.apply(task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    public void register(final Activity activity, final Uri image, final String name, final String email, final String phone, final String password, final Function<Void, Void> onSuccess, final Function<String, Void> onFailure) {
        this.getAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if ( task.isSuccessful() ) {
                            final FirebaseUser user = apis.getUsersAPI().getCurrentUser();

                            if ( user != null ) {
                                final DatabaseReference dataRef = apis.getUsersAPI().getUsersRef().child(user.getUid());
                                final StorageReference storRef = FirebaseStorage.getInstance().getReference().child("profilePhotos").child(user.getUid());

                                storRef.putFile(image)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                if (taskSnapshot.getDownloadUrl() != null) {
                                                    String profileURL = taskSnapshot.getDownloadUrl().toString();
                                                    dataRef.setValue(new User(user.getUid(), profileURL, name, email, phone), new DatabaseReference.CompletionListener() {
                                                        @Override
                                                        public void onComplete(DatabaseError error, DatabaseReference ref) {
                                                            if ( error != null ) {
                                                                onFailure.apply(error.getMessage());
                                                            } else {
                                                                onSuccess.apply(null);
                                                            }
                                                        }
                                                    });
                                                }
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
                                            }
                                        });
                            }
                        } else {
                            onFailure.apply(task.getException().getMessage());
                        }
                    }
                });
    }

    public void reset(Activity activity, String email, final Function<Void, Void> onSuccess, final Function<String, Void> onFailure) {
        this.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if ( task.isSuccessful() ) {
                            onSuccess.apply(null);
                        } else {
                            onFailure.apply(task.getException().getMessage());
                        }
                    }
                });
    }

    public void logout(final Function<Void, Void> onSuccess, final Function<String, Void> onFailure) {
        this.auth.signOut();
        if (this.apis.getUsersAPI().getCurrentUser() == null) {
            onSuccess.apply(null);
        } else {
            onFailure.apply("Logout Failed...");
        }
    }
}
