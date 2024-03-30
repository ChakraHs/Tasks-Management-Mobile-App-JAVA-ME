package com.example.mytask.models;

import com.google.firebase.Timestamp;

public class Event {

    private String title;
    private String description;
    private Timestamp timestamp;

    private String imageUrl;

    // Empty constructor required for Firestore serialization
    public Event() {
    }

    // Constructor with parameters
    public Event(String title, String description, Timestamp timestamp) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
    }

    public Event(String title, String description, Timestamp timestamp, String imageUrl) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
    }
    // Getters and setters


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
}
