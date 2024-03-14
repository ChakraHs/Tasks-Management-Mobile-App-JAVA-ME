package com.example.mytask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ProfileActivity extends AppCompatActivity {


    //Set on click listner on image view to open uploadprofilrpicactivity
    ImageView imageView;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        imageView = findViewById(R.id.profile_image);
        saveBtn = findViewById(R.id.save_profile_btn);


        imageView.setOnClickListener(v -> redirect());



    }

    void redirect(){
        Intent intent = new Intent(ProfileActivity.this,UploadProfilePicActivity.class);
        startActivity(intent);
        finish();
    }
}

