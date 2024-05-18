package com.example.mytask.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytask.R;
import com.example.mytask.TaskDetailsActivity;
import com.example.mytask.Utility;
import com.example.mytask.models.Task;

import java.util.ArrayList;
import java.util.List;

public class ProjectTaskAdapter extends RecyclerView.Adapter<ProjectTaskAdapter.ProjectTaskViewHolder> {

    private Context context;
    private List<Task> tasks;

    public ProjectTaskAdapter(Context context, ArrayList<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public ProjectTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_task_item, parent, false);
        return new ProjectTaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectTaskViewHolder holder, int position) {
        Task task = tasks.get(position);

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

        holder.isDone.setOnCheckedChangeListener(null);

        holder.isDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Update your task in the local list or database here
            task.setIsDone(isChecked);
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TaskDetailsActivity.class);
            intent.putExtra("title", task.getTitle());
            intent.putExtra("description", task.getDescription());
            intent.putExtra("docId", task.getId()); // Ensure Task has an ID field
            context.startActivity(intent);
        });

        int originalTextColor = holder.titleTextView.getCurrentTextColor();
        ColorDrawable background = (ColorDrawable) holder.itemView.getBackground();
        int originalBackgroundColor = (background != null) ? background.getColor() : Color.TRANSPARENT;

        holder.itemView.setOnLongClickListener(v -> {
            holder.titleTextView.setTextColor(context.getResources().getColor(R.color.black));
            holder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D4E7F5")));

            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.getMenu().add("Edit");
            popupMenu.getMenu().add("Delete");
            popupMenu.show();

            popupMenu.setOnDismissListener(menu -> {
                holder.titleTextView.setTextColor(originalTextColor);
                holder.itemView.setBackgroundTintList(ColorStateList.valueOf(originalBackgroundColor));
            });

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getTitle().toString()) {
                    case "Edit":
                        editTask(task);
                        return true;
                    case "Delete":
                        deleteTask(position);
                        return true;
                    default:
                        return false;
                }
            });

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    private void editTask(Task task) {
        Intent intent = new Intent(context, TaskDetailsActivity.class);
        intent.putExtra("title", task.getTitle());
        intent.putExtra("description", task.getDescription());
        intent.putExtra("docId", task.getId()); // Ensure Task has an ID field
        context.startActivity(intent);
    }

    private void deleteTask(int position) {
        tasks.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, tasks.size());
        // Optionally, remove the task from the database here
    }

    class ProjectTaskViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView, descriptionTextView, timestampTextView, endTimeTextView;
        CheckBox isDone;

        public ProjectTaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.task_title_text_view);
            descriptionTextView = itemView.findViewById(R.id.task_description_text_view);
            timestampTextView = itemView.findViewById(R.id.task_timestamp_text_view);
            endTimeTextView = itemView.findViewById(R.id.time);
            isDone = itemView.findViewById(R.id.done_checkbox);
        }
    }
}
