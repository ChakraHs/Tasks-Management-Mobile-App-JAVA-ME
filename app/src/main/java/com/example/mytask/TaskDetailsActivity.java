package com.example.mytask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mytask.models.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class TaskDetailsActivity extends AppCompatActivity {

    EditText titleEditText, descriptionEditText;
    ImageButton saveTaskbtn,returnBtn;

    TextView pageTitleTextView;
    String title,description,docId;
    Boolean isEditMode = false;

    TextView deleteTaskTextViewBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        titleEditText = findViewById(R.id.task_title_text);
        descriptionEditText = findViewById(R.id.task_description_text);
        saveTaskbtn = findViewById(R.id.save_task_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteTaskTextViewBtn = findViewById(R.id.delete_task_text_view_btn);
        returnBtn = findViewById(R.id.return_btn);


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


        saveTaskbtn.setOnClickListener(v -> saveTask());

        deleteTaskTextViewBtn.setOnClickListener( v -> deleteTaskFromFirebase());

        returnBtn.setOnClickListener(v-> returnBack());
    }

    void saveTask(){

        String taskTitle = titleEditText.getText().toString();
        String taskDescription = descriptionEditText.getText().toString();

        if(taskTitle.isEmpty()){
            titleEditText.setError("Title is required");
            return;
        }

        Task task = new Task();
        task.setTitle(taskTitle);
        task.setDescription(taskDescription);
        task.setTimestamp(Timestamp.now());

        saveTaskToFirebase(task);

    }

    void saveTaskToFirebase(Task task){
        DocumentReference documentReference;

        if(isEditMode){
            //update the task
            documentReference = Utility.getCollectionReferenceForTask().document(docId);
        }else{
            //create new task
            documentReference = Utility.getCollectionReferenceForTask().document();
        }
        documentReference.set(task).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                if(task.isSuccessful()){
                    //task is added
                    Utility.showToast(TaskDetailsActivity.this,"Task added successfully");
                    finish();
                }else{
                    //failed
                    Utility.showToast(TaskDetailsActivity.this,"Fail while adding task");
                }
            }
        });
    }

    void deleteTaskFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForTask().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                if(task.isSuccessful()){
                    //task is deleted
                    Utility.showToast(TaskDetailsActivity.this,"Task deleted successfully");
                    finish();
                }else{
                    //failed
                    Utility.showToast(TaskDetailsActivity.this,"Fail while deleting task");
                }
            }
        });

    }

    void returnBack(){
        finish();
    }
}