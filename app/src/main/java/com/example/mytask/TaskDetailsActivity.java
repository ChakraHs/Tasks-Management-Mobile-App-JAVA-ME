package com.example.mytask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.mytask.models.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class TaskDetailsActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    ImageButton saveTaskbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        titleEditText = findViewById(R.id.task_title_text);
        contentEditText = findViewById(R.id.task_description_text);
        saveTaskbtn = findViewById(R.id.save_task_btn);

        saveTaskbtn.setOnClickListener(v -> saveTask());
    }

    void saveTask(){

        String taskTitle = titleEditText.getText().toString();
        String taskDescription = contentEditText.getText().toString();

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
        documentReference = Utility.getCollectionReferenceForTask().document();
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
}