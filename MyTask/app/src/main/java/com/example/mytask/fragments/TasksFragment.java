package com.example.mytask.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.mytask.MainActivity;
import com.example.mytask.R;
import com.example.mytask.TaskDetailsActivity;
import com.example.mytask.Utility;
import com.example.mytask.adapters.TaskAdapter;
import com.example.mytask.models.Task;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;

public class TasksFragment extends Fragment {


    private static final String TAG = "TasksRecyclerView";

    public TasksFragment() {
        // Required empty public constructor
    }

        private ProgressBar progressBar;
        RecyclerView tasksRecyclerView;

        TaskAdapter taskAdapter;

        private LinkedList<Task> tasks;

        private FirebaseFirestore db;

    public static TasksFragment newInstance(String param1, String param2) {
        TasksFragment fragment = new TasksFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        progressBar = view.findViewById(R.id.progress_bar);

        tasksRecyclerView = view.findViewById(R.id.recycler_view);

        // Initially, show the progress bar and hide the RecyclerView
        progressBar.setVisibility(View.VISIBLE);
        tasksRecyclerView.setVisibility(View.GONE);

        setupRecyclerView();
        // Inflate the layout for this fragment
        return view;
    }

        void setupRecyclerView(){
            Query query = Utility.getCollectionReferenceForTask().orderBy("timestamp", Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                    .setQuery(query,Task.class).build();
            tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            taskAdapter = new TaskAdapter(options,getActivity());
            tasksRecyclerView.setAdapter(taskAdapter);

            // Hide the progress bar
            progressBar.setVisibility(View.GONE);
            // Show the RecyclerView
            tasksRecyclerView.setVisibility(View.VISIBLE);

        }


    @Override
    public void onStart() {
        super.onStart();
        if (taskAdapter != null) {
            taskAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (taskAdapter != null) {
            taskAdapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (taskAdapter != null) {
            taskAdapter.notifyDataSetChanged();
        }
    }

}