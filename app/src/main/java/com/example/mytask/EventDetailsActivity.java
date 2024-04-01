package com.example.mytask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mytask.models.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EventDetailsActivity extends AppCompatActivity {

    EditText titleEditText, descriptionEditText;
    ImageButton saveEventBtn, returnBtn;
    ImageView eventImageView;

    TextView pageTitleTextView;
    String title, description, docId;
    Boolean isEditMode = false;

    TextView deleteEventTextViewBtn;



    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    FirebaseUser currentUser;
    Uri uri;

    Uri uriImage;

    static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        titleEditText = findViewById(R.id.event_title_text);
        descriptionEditText = findViewById(R.id.event_description_text);
        saveEventBtn = findViewById(R.id.save_event_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteEventTextViewBtn = findViewById(R.id.delete_event_text_view_btn);
        returnBtn = findViewById(R.id.return_btn);
        eventImageView = findViewById(R.id.event_image);
        
        eventImageView.setOnClickListener(v->openFileChosen());

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference("EventsPics");

        uri = currentUser.getPhotoUrl();

        //Set user's image in profile ImageView if already uploaded

//        Picasso.get().load(uri).into(imageView);


        // Receive data
        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        docId = getIntent().getStringExtra("docId");

        if (docId != null && !docId.isEmpty()) {
            isEditMode = true;
        }

        // Populate the EditText fields and set the page title
        if (isEditMode) {
            titleEditText.setText(title);
            descriptionEditText.setText(description);
            pageTitleTextView.setText("Edit event");

            deleteEventTextViewBtn.setVisibility(View.VISIBLE);
        }

        // Set onClickListeners for the buttons
        saveEventBtn.setOnClickListener(v -> saveEvent());
        deleteEventTextViewBtn.setOnClickListener(v -> deleteEventFromFirebase());
        returnBtn.setOnClickListener(v -> returnBack());
    }

    // Method to save or update the event
    void saveEvent() {
        String eventTitle = titleEditText.getText().toString();
        String eventDescription = descriptionEditText.getText().toString();

        if (eventTitle.isEmpty()) {
            titleEditText.setError("Title is required");
            return;
        }

        Event event = new Event();
        event.setTitle(eventTitle);
        event.setDescription(eventDescription);
        event.setTimestamp(Timestamp.now());

        uploadPic(event);
    }

    // Method to save the event to Firebase
    void saveEventToFirebase(Event event) {
        DocumentReference documentReference;

        if (isEditMode) {
            // Update the event
            documentReference = Utility.getCollectionReferenceForEvent().document(docId);
        } else {
            // Create new event
            documentReference = Utility.getCollectionReferenceForEvent().document();
        }

        documentReference.set(event).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Event is added or updated successfully
                Utility.showToast(EventDetailsActivity.this, "Event saved successfully");
                finish();
            } else {
                // Failed to add or update event
                Utility.showToast(EventDetailsActivity.this, "Failed to save event");
            }
        });
    }

    // Method to delete the event from Firebase
    void deleteEventFromFirebase() {
        DocumentReference documentReference = Utility.getCollectionReferenceForEvent().document(docId);
        documentReference.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Event is deleted successfully
                Utility.showToast(EventDetailsActivity.this, "Event deleted successfully");
                finish();
            } else {
                // Failed to delete event
                Utility.showToast(EventDetailsActivity.this, "Failed to delete event");
            }
        });
    }

    // Method to finish the activity
    void returnBack() {
        finish();
    }

    void  openFileChosen(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            uriImage = data.getData();
            eventImageView.setImageURI(uriImage);
        }
    }


    void uploadPic(Event event){
        if(uriImage != null){
            //save the image with uid of current user
            // Get a reference to store the image
            StorageReference fileReference = storageReference.child(currentUser.getUid() + "_" + System.currentTimeMillis() + "." + getFileExtension(uriImage));

            //Upload image to storage
            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            event.setImageUrl(uri.toString());
                            saveEventToFirebase(event);
                        }
                    });

                    Utility.showToast(EventDetailsActivity.this,"Upload successful!");

                    Intent intent = new Intent(EventDetailsActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Utility.showToast(EventDetailsActivity.this,e.getMessage());
                }
            });
        }else{
            Utility.showToast(EventDetailsActivity.this,"No file selected!");
        }
    }

    String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}
