package com.example.mytask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mytask.adapters.EventAdapter;
import com.example.mytask.adapters.UserAdapter;
import com.example.mytask.models.Event;
import com.example.mytask.models.Project;
import com.example.mytask.models.Task;
import com.example.mytask.models.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ProjectDetailsActivity extends AppCompatActivity {

    EditText titleEditText, descriptionEditText;

    private RecyclerView usersRecyclerView;
    ImageButton saveProjectBtn,returnBtn;

    TextView pageTitleTextView;
    String title,description,docId;
    Boolean isEditMode = false;

    TextView deleteTaskTextViewBtn;

    String TAG = "Users";

    private UserAdapter userAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        titleEditText = findViewById(R.id.project_title_text);
        descriptionEditText = findViewById(R.id.project_description_text);
        saveProjectBtn = findViewById(R.id.save_project_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteTaskTextViewBtn = findViewById(R.id.delete_project_text_view_btn);
        returnBtn = findViewById(R.id.return_btn);

//        dialogButton = findViewById(R.id.dialog_button);


//        dialogButton.setOnClickListener(v->openDialog());


        //receive data
        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        docId = getIntent().getStringExtra("docId");

        if(docId != null && !docId.isEmpty()){
            isEditMode = true;
        }

        if(isEditMode){
            titleEditText.setText(title);
            descriptionEditText.setText(description);
            pageTitleTextView.setText("Edit task");

            deleteTaskTextViewBtn.setVisibility(View.VISIBLE);
        }


        saveProjectBtn.setOnClickListener(v -> saveProject());

        deleteTaskTextViewBtn.setOnClickListener( v -> deleteProjectFromFirebase());

        returnBtn.setOnClickListener(v-> returnBack());

        usersRecyclerView = findViewById(R.id.recyclerViewUsers);

        // Initialize views here...
        setupUsersRecyclerView(); // Load users
    }

    void saveProject(){

        String projectTitle = titleEditText.getText().toString();
        String projectDescription = descriptionEditText.getText().toString();

        if(projectTitle.isEmpty()){
            titleEditText.setError("Title is required");
            return;
        }

        // Example method to handle save action
        HashSet<String> selectedEmails = userAdapter.getSelectedUsers();  // Retrieve selected emails

        Project project = new Project();
        // Convert HashSet to List when needed
        List<String> collaboratorsList = new ArrayList<>(selectedEmails);
        project.setCollaborators(collaboratorsList);
        project.setTitle(projectTitle);
        project.setDescription(projectDescription);
        project.setTimestamp(Timestamp.now());

        saveProjectToFirebase(project);

    }

    void saveProjectToFirebase(Project project){
        DocumentReference documentReference;

        if(isEditMode){
            //update the task
            documentReference = Utility.getCollectionReferenceForProject().document(docId);
        }else{
            //create new task
            documentReference = Utility.getCollectionReferenceForProject().document();
        }
        documentReference.set(project).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                if(task.isSuccessful()){
                    //task is added
                    Utility.showToast(ProjectDetailsActivity.this,"Project added successfully");
                    finish();
                }else{
                    //failed
                    Utility.showToast(ProjectDetailsActivity.this,"Fail while adding task");
                }
            }
        });
    }

    void deleteProjectFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForProject().document(docId);
        documentReference.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Event is deleted successfully
                Utility.showToast(ProjectDetailsActivity.this, "Project deleted successfully");
                finish();
            } else {
                // Failed to delete event
                Utility.showToast(ProjectDetailsActivity.this, "Failed to delete project");
            }
        });

    }

    void returnBack(){
        finish();
    }




    private void setupUsersRecyclerView() {
        Query query = Utility.getCollectionReferenceForUser();
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class).build();

        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(options, this);
        usersRecyclerView.setAdapter(userAdapter);

        // Hide the progress bar
//        progressBar.setVisibility(View.GONE);
        // Show the RecyclerView
//        usersRecyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (userAdapter != null) {
            userAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (userAdapter != null) {
            userAdapter.stopListening();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (userAdapter != null) {
            userAdapter.notifyDataSetChanged();
        }
    }
}