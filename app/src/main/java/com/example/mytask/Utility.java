package com.example.mytask;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Utility {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static CollectionReference getCollectionReferenceForTask() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("tasks").document(currentUser.getUid()).collection("my_tasks");
    }

    public static CollectionReference getCollectionReferenceForProject() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("projects").document(currentUser.getUid()).collection("my_projects");
    }

    public static CollectionReference getCollectionReferenceForUser() {
        return FirebaseFirestore.getInstance().collection("users");
    }

    // Get Collection Reference for Events
    public static CollectionReference getCollectionReferenceForEvent() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("events").document(currentUser.getUid()).collection("my_events");
    }

    public static String timestampToString(Timestamp timestamp) {
        return new SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());
    }

    public static String timestampToTime(Timestamp timestamp) {
        return new SimpleDateFormat("HH:mm").format(timestamp.toDate());
    }

    public static String setTextViewMaxCharacters(String text, int maxCharacters) {
        if (text.length() > maxCharacters) {
            return text.substring(0, maxCharacters - 3) + "...";
        }
        return text;
    }
}
