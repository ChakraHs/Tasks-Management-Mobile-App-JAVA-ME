package com.example.mytask.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mytask.Utility;
import com.example.mytask.models.Project;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ProjectRepository {
    private FirebaseFirestore db;

    public ProjectRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public LiveData<List<Project>> getAllProjects() {
        MutableLiveData<List<Project>> liveData = new MutableLiveData<>();

        Query query = Utility.getCollectionReferenceForProject().orderBy("timestamp", Query.Direction.DESCENDING);
        query.addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                // handle the error, possibly update LiveData to notify UI about the error
                liveData.setValue(null);
                return;
            }

            List<Project> projects = new ArrayList<>();
            if (snapshots != null) {
                for (DocumentSnapshot doc : snapshots.getDocuments()) {
                    projects.add(doc.toObject(Project.class));
                }
            }
            liveData.setValue(projects);
        });

        return liveData;
    }

    public void updateProjectStatus(String projectId, boolean isDone) {
        DocumentReference projectRef = Utility.getCollectionReferenceForProject().document(projectId);
        projectRef.update("isDone", isDone)
                .addOnSuccessListener(aVoid -> Log.d("ProjectRepo", "Project updated successfully!"))
                .addOnFailureListener(e -> Log.w("ProjectRepo", "Error updating project", e));
    }
}
