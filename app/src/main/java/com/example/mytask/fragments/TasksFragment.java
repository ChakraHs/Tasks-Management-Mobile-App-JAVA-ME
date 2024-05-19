package com.example.mytask.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.mytask.R;
import com.example.mytask.Utility;
import com.example.mytask.adapters.TaskAdapter;
import com.example.mytask.models.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnCompleteListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TasksFragment extends Fragment {

    private static final String TAG = "TasksFragment";

    private ProgressBar progressBar;
    private RecyclerView tasksRecyclerView;
    private TaskAdapter taskAdapter;
    private FirebaseFirestore db;
    private EditText searchEditText;
    private List<Task> taskList;

    public TasksFragment() {
        // Required empty public constructor
    }

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
        taskList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        progressBar = view.findViewById(R.id.progress_bar);
        tasksRecyclerView = view.findViewById(R.id.recycler_view);
        searchEditText = view.findViewById(R.id.searchEditText);

        progressBar.setVisibility(View.VISIBLE);
        tasksRecyclerView.setVisibility(View.GONE);

        fetchTasksFromFirestore();

        return view;
    }

    private void fetchTasksFromFirestore() {
        Utility.getCollectionReferenceForTask().orderBy("timestamp", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        taskList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Task taskItem = document.toObject(Task.class);
                            taskItem.setId(document.getId()); // Store document ID
                            taskList.add(taskItem);
                        }
                        setupRecyclerView();
                        progressBar.setVisibility(View.GONE);
                        tasksRecyclerView.setVisibility(View.VISIBLE);
                        Log.d("List tasks", taskList.toString());
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void setupRecyclerView() {
        taskAdapter = new TaskAdapter(taskList, getActivity());
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        tasksRecyclerView.setAdapter(taskAdapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "filtres récupérées avec succès : afterTextChanged");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "filtres récupérées avec succès : beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "filtres récupérées avec succès : onTextChanged " + s.toString());
                filterTasks(s.toString());
            }
        });
    }

    private void filterTasks(String searchText) {
        LinkedList<Task> filteredTasks = new LinkedList<>();
        for (Task task : taskList) {
            if (task.getTitle().contains(searchText) || task.getDescription().contains(searchText)) {
                filteredTasks.add(task);
            }
        }
        taskAdapter.updateTasks(filteredTasks);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (taskAdapter != null) {
            taskAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (taskAdapter != null) {
            taskAdapter.notifyDataSetChanged();
        }
    }
}
