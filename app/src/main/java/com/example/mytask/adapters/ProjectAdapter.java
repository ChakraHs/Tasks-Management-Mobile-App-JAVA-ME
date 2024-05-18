package com.example.mytask.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytask.MainActivity;
import com.example.mytask.OneProjectActivity;
import com.example.mytask.ProfileActivity;
import com.example.mytask.ProjectDetailsActivity;
import com.example.mytask.R;
import com.example.mytask.SignInActivity;
import com.example.mytask.TaskDetailsActivity;
import com.example.mytask.Utility;
import com.example.mytask.models.Project;
import com.example.mytask.models.Task;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

public class ProjectAdapter extends FirestoreRecyclerAdapter<Project, ProjectAdapter.ProjectViewHolder> {

    Context context;
    FirebaseFirestore db;
    FirebaseUser currentUser;

    private FragmentManager fragmentManager;

    private LinkedList<Project> projects;
    private static final String TAG = "ProjectAdapter";
    public ProjectAdapter(@NonNull FirestoreRecyclerOptions<Project> options, Context context) {
        super(options);
        this.context=context;
        this.db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        this.currentUser = mAuth.getCurrentUser();
        this.context = context;
    }


    public ProjectAdapter(@NonNull FirestoreRecyclerOptions<Project> options,LinkedList<Project> projects, Context context , FragmentManager fragmentManager ) {
        super(options);
        this.db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        this.currentUser = mAuth.getCurrentUser();
        this.projects = projects;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected void onBindViewHolder(@NonNull ProjectViewHolder holder, int position, @NonNull Project project) {
        holder.titleTextView.setText(project.getTitle());
        holder.descriptionTextView.setText(project.getDescription());
        holder.timestampTextView.setText("Created at " + Utility.timestampToString(project.getTimestamp()));

        // Store original colors
        int originalTextColor = holder.titleTextView.getCurrentTextColor();
        ColorStateList originalBackgroundColor = holder.itemView.getBackgroundTintList();


        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context, OneProjectActivity.class);
            intent.putExtra("title", project.getTitle());
            intent.putExtra("description", project.getDescription());
            String docId = getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);

            // Convert the list of tasks to ArrayList and pass it
            ArrayList<Task> taskList = new ArrayList<>(project.getTasks());
            intent.putExtra("tasks", taskList);

            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v->{
            holder.titleTextView.setTextColor(context.getResources().getColor(R.color.black));
            holder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D4E7F5")));

            // Show the menu
            PopupMenu popupMenu = showMenu(v, position, project);

            // Set on dismiss listener
            setOnDismissListener(popupMenu, holder, originalTextColor, originalBackgroundColor);

            return true;
        });


    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_project_item,parent,false);
        return new ProjectViewHolder(view);
    }

    class ProjectViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView,descriptionTextView,timestampTextView;
        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.project_title_text_view);
            descriptionTextView = itemView.findViewById(R.id.project_description_text_view);
            timestampTextView = itemView.findViewById(R.id.task_timestamp_text_view);
        }
    }

    private PopupMenu showMenu(View view, int position, Project project) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.getMenu().add("Edit");
        popupMenu.getMenu().add("Delete");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getTitle().toString()) {
                case "Edit":
                    editProject(project, position);
                    return true;
                case "Delete":
                    deleteProject(position);
                    return true;
                default:
                    return false;
            }
        });

        return popupMenu;
    }

    private void editProject(Project project, int position) {
        Intent intent = new Intent(context, ProjectDetailsActivity.class);
        intent.putExtra("title", project.getTitle());
        intent.putExtra("description", project.getDescription());
        String docId = getSnapshots().getSnapshot(position).getId();
        intent.putExtra("docId", docId);
        context.startActivity(intent);
    }

    private void deleteProject(int position) {
        String docId = getSnapshots().getSnapshot(position).getId();
        DocumentReference projectRef = Utility.getCollectionReferenceForProject().document(docId);
        projectRef.delete()
                .addOnSuccessListener(aVoid -> Utility.showToast(context, "Project deleted successfully"))
                .addOnFailureListener(e -> Utility.showToast(context, "Error deleting project"));
    }

    private void setOnDismissListener(PopupMenu popupMenu, ProjectViewHolder holder, int originalTextColor, ColorStateList originalBackgroundColor) {
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                // Reset colors when the menu is dismissed
                holder.titleTextView.setTextColor(originalTextColor);
                holder.itemView.setBackgroundTintList(originalBackgroundColor);
            }
        });
    }

}
