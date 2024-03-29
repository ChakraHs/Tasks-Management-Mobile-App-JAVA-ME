package com.example.mytask.dao;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseHelper {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    public FirebaseHelper() {
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

    // Method to get user email
    public String getUserEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            return user.getEmail();
        } else {
            return null; // Return null if user is not signed in
        }
    }

    // Method to get user display name (username) from Firestore
    public void getUserDisplayName(OnSuccessListener<String> onSuccessListener, OnFailureListener onFailureListener) {
        mFirestore.collection("users")
                .document(this.getUserEmail())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");
                        onSuccessListener.onSuccess(username);
                    } else {
                        onFailureListener.onFailure(new Exception("User document not found"));
                    }
                })
                .addOnFailureListener(onFailureListener);
    }

    // You can add more methods to retrieve additional user information as needed
}
