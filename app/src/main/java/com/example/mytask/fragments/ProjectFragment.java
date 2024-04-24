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
import com.example.mytask.adapters.ProjectAdapter;
import com.example.mytask.adapters.TaskAdapter;
import com.example.mytask.models.Project;
import com.example.mytask.models.Task;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;

public class ProjectFragment extends Fragment {


    private static final String TAG = "ProjectRecyclerView";

    public ProjectFragment() {
        // Required empty public constructor
    }

    private ProgressBar progressBar;
    RecyclerView ProjectRecyclerView;

    ProjectAdapter projectAdapter;

    private LinkedList<Task> tasks;

    private FirebaseFirestore db;
    public static ProjectFragment newInstance(String param1, String param2) {
        ProjectFragment fragment = new ProjectFragment();
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
        View view = inflater.inflate(R.layout.fragment_project, container, false);

        progressBar = view.findViewById(R.id.progress_bar);

        ProjectRecyclerView = view.findViewById(R.id.recycler_view);

        // Initially, show the progress bar and hide the RecyclerView
        progressBar.setVisibility(View.VISIBLE);
        ProjectRecyclerView.setVisibility(View.GONE);

        setupRecyclerView();
        // Inflate the layout for this fragment
        return view;
    }

    void setupRecyclerView(){
        Query query = Utility.getCollectionReferenceForProject().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Project> options = new FirestoreRecyclerOptions.Builder<Project>()
                .setQuery(query,Project.class).build();
        ProjectRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        projectAdapter = new ProjectAdapter(options,getActivity());
        ProjectRecyclerView.setAdapter(projectAdapter);

        // Hide the progress bar
        progressBar.setVisibility(View.GONE);
        // Show the RecyclerView
        ProjectRecyclerView.setVisibility(View.VISIBLE);

    }


    @Override
    public void onStart() {
        super.onStart();
        if (projectAdapter != null) {
            projectAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (projectAdapter != null) {
            projectAdapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (projectAdapter != null) {
            projectAdapter.notifyDataSetChanged();
        }
    }

}