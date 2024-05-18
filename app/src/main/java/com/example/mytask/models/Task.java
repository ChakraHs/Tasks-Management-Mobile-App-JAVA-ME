package com.example.mytask.models;


import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Task implements Serializable {

    private String id ;
    String title;
    String description;
    Timestamp timestamp, endTimestamp;

    Boolean isDone;

    public Task(){
        this.isDone = Boolean.FALSE;
    }

    public Task(String title, String description, Boolean isDone) {
        this.title = title;
        this.description = description;
        this.isDone = isDone;
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

    public Timestamp getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(Timestamp endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean isDone) {
        this.isDone = isDone;
    }
}
