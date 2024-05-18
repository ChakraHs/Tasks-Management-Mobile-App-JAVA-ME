package com.example.mytask.models;

import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Project {
    private String id;
    private String title;
    private String description;
    private Timestamp timestamp;
    private Boolean isDone;
    private List<String> collaborators;  // Assuming collaborators are stored by their ID or name
    private List<Task> tasks;  // List of Task objects for detailed task management

    // Default constructor
    public Project() {
        this.isDone = Boolean.FALSE;
        this.collaborators = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }

    // Full constructor for all fields
    public Project(String id, String title, String description, Timestamp timestamp, Boolean isDone) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp == null ? new Timestamp(new java.util.Date()) : timestamp;
        this.isDone = isDone;
        this.collaborators = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean isDone) {
        this.isDone = isDone;
    }

    public List<String> getCollaborators() {
        return new ArrayList<>(collaborators); // Return a copy to prevent external modification
    }

    public void setCollaborators(List<String> collaborators) {
        this.collaborators = new ArrayList<>(collaborators); // Create a new list from input to encapsulate the data
    }

    public void addCollaborator(String collaborator) {
        this.collaborators.add(collaborator);
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks); // Return a copy to protect the internal structure
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks); // Ensure encapsulation by copying input data
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }
}
