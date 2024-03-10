package com.example.mytask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addTaskBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addTaskBtn = findViewById(R.id.add_task_btn);

        addTaskBtn.setOnClickListener(v->startActivity(new Intent(MainActivity.this,TaskDetailsActivity.class)));

    }
}