package com.example.mytask;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mytask.models.Note;
import com.example.mytask.models.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText descriptionEditText;
    ImageButton saveNoteBtn,returnBtn;

    TextView pageTitleTextView;
    String description,docId;
    Boolean isEditMode = false;

    TextView deleteNoteTextViewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        descriptionEditText = findViewById(R.id.note_description_text);
        saveNoteBtn = findViewById(R.id.save_task_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteNoteTextViewBtn = findViewById(R.id.delete_note_text_view_btn);
        returnBtn = findViewById(R.id.return_btn);


        //receive data
        description = getIntent().getStringExtra("description");
        docId = getIntent().getStringExtra("docId");

        if(docId != null && !docId.isEmpty()){
            isEditMode = true;
        }

        if(isEditMode){
            descriptionEditText.setText(description);
            pageTitleTextView.setText("Edit note");

            deleteNoteTextViewBtn.setVisibility(View.VISIBLE);
        }


        saveNoteBtn.setOnClickListener(v -> saveNote());

        deleteNoteTextViewBtn.setOnClickListener( v -> deleteNoteFromFirebase());

        returnBtn.setOnClickListener(v-> returnBack());
    }

    void saveNote(){

        String noteDescription = descriptionEditText.getText().toString();


        if(noteDescription.isEmpty()){
            descriptionEditText.setError("Description is required");
            return;
        }

        Note note = new Note();
        note.setDescription(noteDescription);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);

    }

    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;

        if(isEditMode){
            //update the task
            documentReference = Utility.getCollectionReferenceForNote().document(docId);
        }else{
            //create new task
            documentReference = Utility.getCollectionReferenceForNote().document();
        }
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                if(task.isSuccessful()){
                    //task is added
                    Utility.showToast(NoteDetailsActivity.this,"Note added successfully");
                    finish();
                }else{
                    //failed
                    Utility.showToast(NoteDetailsActivity.this,"Fail while adding note");
                }
            }
        });
    }

    void deleteNoteFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNote().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                if(task.isSuccessful()){
                    //task is deleted
                    Utility.showToast(NoteDetailsActivity.this,"Note deleted successfully");
                    finish();
                }else{
                    //failed
                    Utility.showToast(NoteDetailsActivity.this,"Fail while deleting note");
                }
            }
        });

    }

    void returnBack(){
        finish();
    }

}