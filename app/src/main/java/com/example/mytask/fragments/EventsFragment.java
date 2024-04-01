package com.example.mytask.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mytask.R;
import com.example.mytask.Utility;
import com.example.mytask.adapters.EventAdapter;
import com.example.mytask.models.Event;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class EventsFragment extends Fragment {

    private ProgressBar progressBar;
    private RecyclerView eventsRecyclerView;
    private EventAdapter eventAdapter;
    private FirebaseFirestore db;

    public EventsFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        progressBar = view.findViewById(R.id.progress_bar);
        eventsRecyclerView = view.findViewById(R.id.recycler_view);

        // Initially, show the progress bar and hide the RecyclerView
        progressBar.setVisibility(View.VISIBLE);
        eventsRecyclerView.setVisibility(View.GONE);

        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView() {
        Query query = Utility.getCollectionReferenceForEvent().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Event> options = new FirestoreRecyclerOptions.Builder<Event>()
                .setQuery(query, Event.class).build();
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventAdapter = new EventAdapter(options, getActivity());
        eventsRecyclerView.setAdapter(eventAdapter);

        // Hide the progress bar
        progressBar.setVisibility(View.GONE);
        // Show the RecyclerView
        eventsRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (eventAdapter != null) {
            eventAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (eventAdapter != null) {
            eventAdapter.stopListening();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (eventAdapter != null) {
            eventAdapter.notifyDataSetChanged();
        }
    }
}
