package com.example.mytask;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytask.adapters.ProjectTaskAdapter;
import com.example.mytask.models.Task;

import java.util.ArrayList;

public class OneProjectActivity extends AppCompatActivity {

    private TextView titleTextView, descriptionTextView;
//    private RecyclerView tasksRecyclerView;
//    private ProjectTaskAdapter projectTaskAdapter;
//    private ArrayList<Task> tasks;
ImageButton returnBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_project);

        titleTextView = findViewById(R.id.project_title);
        descriptionTextView = findViewById(R.id.project_description);
        returnBtn = findViewById(R.id.return_btn);
//        tasksRecyclerView = findViewById(R.id.tasks_recycler_view);

        // Get the data from the Intent
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
//        tasks = (ArrayList<Task>) getIntent().getSerializableExtra("tasks");

        // Set the project title and description
        titleTextView.setText(title);
        descriptionTextView.setText(description);

        // Setup the RecyclerView
//        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        projectTaskAdapter = new ProjectTaskAdapter(this, tasks);
//        tasksRecyclerView.setAdapter(projectTaskAdapter);

        returnBtn.setOnClickListener(v -> returnBack());
    }

    void returnBack() {
        finish();
    }
}
