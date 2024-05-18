package com.example.mytask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.mytask.models.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TaskDetailsActivity extends AppCompatActivity {

    EditText titleEditText, descriptionEditText;
    ImageButton saveTaskbtn,returnBtn;

    TextView pageTitleTextView;
    String title,description,docId;
    Boolean isEditMode = false;

    TextView deleteTaskTextViewBtn;

    Button dateButton, timeButton;
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

        dateButton = findViewById(R.id.taskDate);
        timeButton = findViewById(R.id.taskTime);

        dateButton.setOnClickListener(v->openDateDialog());
        timeButton.setOnClickListener(v->openTimeDialog());


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

        // Get selected date and time from buttons
        String date = dateButton.getText().toString();
        String time = timeButton.getText().toString();

        if(time.isEmpty()){
            timeButton.setError("time is required");
            return;
        }
        if(date.isEmpty()){
            dateButton.setError("date is required");
            return;
        }

        if(taskTitle.isEmpty()){
            titleEditText.setError("Title is required");
            return;
        }

        Task task = new Task();
        task.setTitle(taskTitle);
        task.setDescription(taskDescription);
        task.setTimestamp(Timestamp.now());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC+1")); // Set time zone to UTC+1
        try {
            Date dateTime = sdf.parse(date + " " + time);
            if (dateTime != null) {
                task.setEndTimestamp(new Timestamp(dateTime));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

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

    void openDateDialog(){
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                dateButton.setText(String.format(Locale.getDefault(), "%d.%02d.%02d", year, month + 1, dayOfMonth));
            }
        },2024, 2, 24);
        dialog.show();
    }

    void openTimeDialog() {
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                // Display the selected time
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
            }
        }, 12, 0, false); // Default time is 12:00 AM (false indicates 12-hour format)
        dialog.show();
    }
}