package com.example.mytask.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserRepository {
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    public UserRepository() {
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

    public LiveData<String> getUsername() {
        MutableLiveData<String> usernameLiveData = new MutableLiveData<>();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null && user.getEmail() != null) {
            mFirestore.collection("users")
                    .document(user.getEmail())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            usernameLiveData.setValue(documentSnapshot.getString("username"));
                        } else {
                            usernameLiveData.setValue("User not found");
                        }
                    })
                    .addOnFailureListener(e -> usernameLiveData.setValue(null));
        } else {
            usernameLiveData.setValue(null); // No user is logged in
        }

        return usernameLiveData;
    }
}
