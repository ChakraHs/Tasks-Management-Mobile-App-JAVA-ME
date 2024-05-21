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
import com.example.mytask.adapters.NoteAdapter;
import com.example.mytask.adapters.TaskAdapter;
import com.example.mytask.models.Note;
import com.example.mytask.models.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnCompleteListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NotesFragment extends Fragment {

    private static final String TAG = "NotesFragment";

    private ProgressBar progressBar;
    private RecyclerView notesRecyclerView;
    private NoteAdapter noteAdapter;
    private FirebaseFirestore db;
    private EditText searchEditText;
    private List<Note> noteList;

    public NotesFragment() {
        // Required empty public constructor
    }

    public static NotesFragment newInstance(String param1, String param2) {
        NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        noteList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        progressBar = view.findViewById(R.id.progress_bar);
        notesRecyclerView = view.findViewById(R.id.recycler_view);
        searchEditText = view.findViewById(R.id.searchEditText);

        progressBar.setVisibility(View.VISIBLE);
        notesRecyclerView.setVisibility(View.GONE);

        fetchNotesFromFirestore();

        return view;
    }

    private void fetchNotesFromFirestore() {
        Utility.getCollectionReferenceForNote().orderBy("timestamp", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        noteList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Note noteItem = document.toObject(Note.class);
                            noteItem.setId(document.getId()); // Store document ID
                            noteList.add(noteItem);
                        }
                        setupRecyclerView();
                        progressBar.setVisibility(View.GONE);
                        notesRecyclerView.setVisibility(View.VISIBLE);
                        Log.d("List notes", noteList.toString());
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void setupRecyclerView() {
        noteAdapter = new NoteAdapter(noteList, getActivity());
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        notesRecyclerView.setAdapter(noteAdapter);

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
        LinkedList<Note> filteredNotes = new LinkedList<>();
        for (Note note : noteList) {
            if (note.getDescription().contains(searchText)) {
                filteredNotes.add(note);
            }
        }
        noteAdapter.updateNotes(filteredNotes);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (noteAdapter != null) {
            noteAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (noteAdapter != null) {
            noteAdapter.notifyDataSetChanged();
        }
    }
}
