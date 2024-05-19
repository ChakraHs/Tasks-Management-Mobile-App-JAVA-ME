package com.example.mytask.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytask.R;
import com.example.mytask.TaskDetailsActivity;
import com.example.mytask.Utility;
import com.example.mytask.models.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private List<Task> taskList;
    private static final String TAG = "TaskAdapter";

    public TaskAdapter(List<Task> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        this.currentUser = mAuth.getCurrentUser();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.titleTextView.setText(Utility.setTextViewMaxCharacters(task.getTitle(), 18));
        holder.descriptionTextView.setText(task.getDescription());
        holder.isDone.setChecked(task.getIsDone());

        if (task.getEndTimestamp() != null) {
            holder.endTimeTextView.setText(Utility.timestampToTime(task.getEndTimestamp()));
            holder.timestampTextView.setText(Utility.timestampToString(task.getEndTimestamp()));
        } else {
            holder.endTimeTextView.setText("18:00");
            holder.timestampTextView.setText(Utility.timestampToString(task.getTimestamp()));
        }

        holder.isDone.setOnCheckedChangeListener(null); // Prevent unwanted triggering

        holder.isDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            DocumentReference userTasksRef;
            String docId = task.getId();
            userTasksRef = Utility.getCollectionReferenceForTask().document(docId);
            userTasksRef.update("isDone", isChecked)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TaskDetailsActivity.class);
            intent.putExtra("title", task.getTitle());
            intent.putExtra("description", task.getDescription());
            intent.putExtra("docId", task.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void updateTasks(List<Task> newTaskList) {
        this.taskList = newTaskList;
        notifyDataSetChanged();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, timestampTextView, endTimeTextView;
        CheckBox isDone;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.task_title_text_view);
            descriptionTextView = itemView.findViewById(R.id.task_description_text_view);
            timestampTextView = itemView.findViewById(R.id.task_timestamp_text_view);
            endTimeTextView = itemView.findViewById(R.id.time);
            isDone = itemView.findViewById(R.id.done_checkbox);
        }
    }
}
