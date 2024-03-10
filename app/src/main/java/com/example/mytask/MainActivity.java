package com.example.mytask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.example.mytask.adapters.TaskAdapter;
import com.example.mytask.models.Task;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addTaskBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;

    TaskAdapter taskAdapter;

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
        Query query = Utility.getCollectionReferenceForTask().orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query,Task.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(options,this);
        recyclerView.setAdapter(taskAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        taskAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        taskAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskAdapter.notifyDataSetChanged();
    }
}