package com.example.mytask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addTaskBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addTaskBtn = findViewById(R.id.add_task_btn);
        recyclerView = findViewById(R.id.recycler_view);
        menuBtn = findViewById(R.id.menu_btn);

        addTaskBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TaskDetailsActivity.class)));
        menuBtn.setOnClickListener(v -> showMenu());

        setupRecyclerView();

    }

    void showMenu() {
        //TODO Display menu
    }

    void setupRecyclerView(){

    }

}