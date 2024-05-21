package com.example.mytask.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytask.NoteDetailsActivity;
import com.example.mytask.R;
import com.example.mytask.TaskDetailsActivity;
import com.example.mytask.Utility;
import com.example.mytask.models.Note;
import com.example.mytask.models.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private Context context;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private List<Note> noteList;
    private static final String TAG = "NoteAdapter";

    public NoteAdapter(List<Note> noteList, Context context) {
        this.noteList = noteList;
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        this.currentUser = mAuth.getCurrentUser();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.descriptionTextView.setText(note.getDescription());
        holder.timestampTextView.setText(Utility.timestampToString(note.getTimestamp()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NoteDetailsActivity.class);
            intent.putExtra("description", note.getDescription());
            intent.putExtra("docId", note.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public void updateNotes(List<Note> newNoteList) {
        this.noteList = newNoteList;
        notifyDataSetChanged();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionTextView, timestampTextView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
//            titleTextView = itemView.findViewById(R.id.task_title_text_view);
            descriptionTextView = itemView.findViewById(R.id.task_description_text_view);
            timestampTextView = itemView.findViewById(R.id.task_timestamp_text_view);
//            endTimeTextView = itemView.findViewById(R.id.time);
//            isDone = itemView.findViewById(R.id.done_checkbox);
        }
    }
}
