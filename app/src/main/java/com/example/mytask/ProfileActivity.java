package com.example.mytask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mytask.dao.FirebaseHelper;
import com.example.mytask.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {


    //Set on click listner on image view to open uploadprofilrpicactivity
    CircleImageView imageView;
    Button closeButton, saveButton;

    EditText usernameEditText;
    Uri uri;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        imageView = findViewById(R.id.profile_image);
        saveButton = findViewById(R.id.save_profile_btn);
        closeButton = findViewById(R.id.close_btn);
        usernameEditText = findViewById(R.id.username_edit_text);

        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.getUserDisplayName(
                new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String username) {
                        // Handle username retrieval success
                        // Update UI with the username
                        usernameEditText.setText(username);
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        // Display an error message or handle the failure appropriately
                    }
                }
        );

        // Set circular shape as background
        imageView.setBackgroundResource(R.drawable.circle_shape);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uri = firebaseUser.getPhotoUrl();
        //image view set image uri should not be used with regular URIs. So we are using Picasso

        if(uri != null){
            Picasso.get().load(uri).into(imageView);
        }


        imageView.setOnClickListener(v -> redirect());


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveButton.setOnClickListener(v->saveUser());



    }

    void redirect(){
        Intent intent = new Intent(ProfileActivity.this,UploadProfilePicActivity.class);
        startActivity(intent);
        finish();
    }

    public void saveUser(){
        String username = usernameEditText.getText().toString();
        // Get current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Access Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new user profile document in Firestore
        db.collection("users")
                .document(currentUser.getEmail())
                .set(new User(username,"test@gmail.com"))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Profile saved successfully
                        Toast.makeText(ProfileActivity.this, "Profile saved", Toast.LENGTH_SHORT).show();
                        // Close the activity or perform any other actions
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to save profile
                        Toast.makeText(ProfileActivity.this, "Failed to save profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

