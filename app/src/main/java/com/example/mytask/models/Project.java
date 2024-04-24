package com.example.mytask.models;


import com.google.firebase.Timestamp;

public class Project {

    private String id ;
    String title;
    String description;
    Timestamp timestamp;

    Boolean isDone;

    public Project(){
        this.isDone = Boolean.FALSE;
    }

    public Project(String title, String description, Boolean isDone) {
        this.title = title;
        this.description = description;
        this.isDone = isDone;
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
}
