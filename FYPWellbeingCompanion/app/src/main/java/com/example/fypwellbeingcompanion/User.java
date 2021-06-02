package com.example.fypwellbeingcompanion;

public class User {
    public String fullName, age, email;
    public int wellnessScore, dailyExerciseScore, socialInteractions, weeklyExerciseScore;

    public User() {
    }

    public User(String fullName, String age, String email, int wellnessScore, int dailyExerciseScore, int weeklyExerciseScore, int socialInteractions) {
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.wellnessScore = wellnessScore;
        this.dailyExerciseScore = dailyExerciseScore;
        this.socialInteractions = socialInteractions;
        this.weeklyExerciseScore = weeklyExerciseScore;

    }
}
