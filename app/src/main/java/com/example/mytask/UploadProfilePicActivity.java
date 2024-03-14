package com.example.mytask;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UploadProfilePicActivity extends AppCompatActivity {

    ProgressBar progressBar;
    ImageView imageView;

    Button chooseImageBtn,uploadImageBtn;

    FirebaseAuth firebaseAuth;

    StorageReference storageReference;
    FirebaseUser currentUser;
    Uri uri;

    Uri uriImage;

    static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_pic);


//        getSupportActionBar().setTitle("Upload profile picture");

        chooseImageBtn = findViewById(R.id.choose_image_btn);
        uploadImageBtn = findViewById(R.id.upload_image_btn);
        progressBar = findViewById(R.id.progress_bar);
        imageView = findViewById(R.id.chosen_image_view);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference("DisplayPics");

        uri = currentUser.getPhotoUrl();

        //Set user's image in profile ImageView if already uploaded

        Picasso.get().load(uri).into(imageView);

        chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChosen();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            uriImage = data.getData();
            imageView.setImageURI(uriImage);
        }
    }

    void  openFileChosen(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
}