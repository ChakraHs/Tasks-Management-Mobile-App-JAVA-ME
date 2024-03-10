package com.example.mytask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

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
    }
}