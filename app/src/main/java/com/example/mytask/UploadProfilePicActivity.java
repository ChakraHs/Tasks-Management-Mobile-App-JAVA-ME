package com.example.mytask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UploadProfilePicActivity extends AppCompatActivity {

    ProgressBar progressBar;
    ImageView imageView;

    Button chooseImageBtn,uploadImageBtn;

    FirebaseAuth firebaseAuth;

    StorageReference storageReference;
    FirebaseUser currentUser;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_pic);


        getSupportActionBar().setTitle("Upload profile picture");

        chooseImageBtn = findViewById(R.id.choose_image_btn);
        uploadImageBtn = findViewById(R.id.upload_image_btn);
        progressBar = findViewById(R.id.progress_bar);
        imageView = findViewById(R.id.chosen_image_view);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference("DisplayPics");

        uri = currentUser.getPhotoUrl();

        //Set user's image in profile ImageView if already uploaded



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}