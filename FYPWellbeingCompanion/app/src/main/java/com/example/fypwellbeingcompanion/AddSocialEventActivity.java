package com.example.fypwellbeingcompanion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddSocialEventActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseUser user;
    private DatabaseReference activityReference, profileReference;
    private String userID;
    private int currentSocialInteractions;
    private int wellnessScore;
    String dateStr;
    private TextView cancel;
    private Button submitButton;
    private EditText editTextTitle, editTextDescription;
    private ProgressBar wellnessScoreBar;
    String TAG = "SocialEvent";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_social_event);

        initSocialEvent();



        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        dateStr = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        profileReference = FirebaseDatabase.getInstance("https://fypwellbeingcompanion-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(userID);
        activityReference = FirebaseDatabase.getInstance("https://fypwellbeingcompanion-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(userID).child("Activities").child(dateStr);
        getWellnessScores();
    }

    private void updateProfile() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        //wellnessScore = wellnessScoreBar.getProgress();

        Log.d(TAG, "Retrieved wellness score" + wellnessScore);

        if (title.isEmpty()){
            editTextTitle.setError("A title is required");
            editTextTitle.requestFocus();
        }
        if (description.isEmpty()){
            editTextDescription.setError("A description is required");
            editTextDescription.requestFocus();
        }
        activityReference.child("Social - " + title).child("Title").setValue(title);
        activityReference.child("Social - " + title).child("Description").setValue(description);
        activityReference.child("Social - " + title).child("Points").setValue(20);
        profileReference.child("wellnessScore").setValue(wellnessScore + 20);
        profileReference.child("socialInteractions").setValue(currentSocialInteractions +1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submitButton:
                updateProfile();
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.cancelText:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }

    }

    private void getWellnessScores() {
        profileReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if(userProfile != null){
                    wellnessScore = userProfile.wellnessScore;
                    currentSocialInteractions = userProfile.socialInteractions;
                    //wellnessScoreBar.setProgress(wellnessScore);
                    Log.d(TAG, "Progress set to: " + wellnessScore);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddSocialEventActivity.this, "Something Wrong Happened", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void initSocialEvent(){
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);

        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);

        cancel = (TextView) findViewById(R.id.cancelText);
        cancel.setOnClickListener(this);

        //wellnessScoreBar = (ProgressBar) findViewById(R.id.wellnessScoreBar);
    }
}