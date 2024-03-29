package com.example.mytask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {


    //Set on click listner on image view to open uploadprofilrpicactivity
    CircleImageView imageView;
    Button saveBtn;

    Uri uri;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        imageView = findViewById(R.id.profile_image);
        saveBtn = findViewById(R.id.save_profile_btn);

        // Set circular shape as background
        imageView.setBackgroundResource(R.drawable.circle_shape);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uri = firebaseUser.getPhotoUrl();
        //image view set image uri should not be used with regular URIs. So we are using Picasso

        if(uri != null){
            Picasso.get().load(uri).into(imageView);
        }


        imageView.setOnClickListener(v -> redirect());



    }

    void redirect(){
        Intent intent = new Intent(ProfileActivity.this,UploadProfilePicActivity.class);
        startActivity(intent);
        finish();
    }
}

