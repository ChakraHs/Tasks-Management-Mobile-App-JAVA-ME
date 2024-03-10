package com.example.mytask.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytask.R;
import com.example.mytask.TaskDetailsActivity;
import com.example.mytask.Utility;
import com.example.mytask.models.Task;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class TaskAdapter extends FirestoreRecyclerAdapter<Task, TaskAdapter.TaskViewHolder> {

    Context context;
    public TaskAdapter(@NonNull FirestoreRecyclerOptions<Task> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull TaskViewHolder holder, int position, @NonNull Task task) {
        holder.titleTextView.setText(task.getTitle());
        holder.descriptionTextView.setText(task.getDescription());
        holder.timestampTextView.setText(Utility.timestampToString(task.getTimestamp()));

        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context, TaskDetailsActivity.class);
            intent.putExtra("title",task.getTitle());
            intent.putExtra("description",task.getDescription());
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_task_item,parent,false);
        return new TaskViewHolder(view);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView,descriptionTextView,timestampTextView;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.task_title_text_view);
            descriptionTextView = itemView.findViewById(R.id.task_description_text_view);
            timestampTextView = itemView.findViewById(R.id.task_timestamp_text_view);
        }
    }
}
