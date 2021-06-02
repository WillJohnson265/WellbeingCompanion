package com.example.fypwellbeingcompanion;

public class Activity {
    private String title,description;
    private int points;

    public Activity() {
    }

    public Activity(String title, String description, int points) {
        this.title = title;
        this.description = description;
        this.points = points;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
