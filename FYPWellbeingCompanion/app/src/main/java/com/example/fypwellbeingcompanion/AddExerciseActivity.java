package com.example.fypwellbeingcompanion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
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

public class AddExerciseActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseUser user;
    private DatabaseReference activityReference, profileReference;
    private String userID;
    String dateStr;
    private TextView cancel;
    private Button submitButton;
    private EditText editTextTitle, editTextDescription;
    private TextView exerciseScore, scoreDescription;
    SeekBar effortScore;
    String TAG = "Exercise";
    String exerciseScoreStr;
    int exerciseScoreGained, weeklyScore, dailyScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        initExercise();

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        dateStr = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        profileReference = FirebaseDatabase.getInstance("https://fypwellbeingcompanion-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(userID);
        activityReference = FirebaseDatabase.getInstance("https://fypwellbeingcompanion-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(userID).child("Activities").child(dateStr);

        getExerciseScores();

        effortScore.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                exerciseScoreGained = progress;
                exerciseScoreStr = String.valueOf(exerciseScoreGained);
                exerciseScore.setText(exerciseScoreStr);
                handleEffortDesc(exerciseScoreGained);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void handleEffortDesc(int exerciseScoreGained) {
        if(exerciseScoreGained < 33){
            scoreDescription.setText("Low Effort: Could have a conversation");
        }
        if(exerciseScoreGained > 34 && exerciseScoreGained < 66){
            scoreDescription.setText("Medium Effort: Could only speak in shortt sentences");
        }
        if(exerciseScoreGained > 67){
            scoreDescription.setText("High Effort: Could hardly speak");
        }
    }

    private void initExercise() {
        editTextTitle = (EditText) findViewById(R.id.editTextTitleExercise);
        editTextDescription = (EditText) findViewById(R.id.editTextDescriptionEx);


        submitButton = (Button) findViewById(R.id.exerciseSubmit);
        submitButton.setOnClickListener(this);

        cancel = (TextView) findViewById(R.id.cancelExercise);
        cancel.setOnClickListener(this);

        effortScore = (SeekBar) findViewById(R.id.effortScore);

        scoreDescription = (TextView) findViewById(R.id.effortDescription);
        exerciseScore = (TextView) findViewById(R.id.exercisePoints);
    }

    private void addExercise(){
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (title.isEmpty()){
            editTextTitle.setError("A title is required");
            editTextTitle.requestFocus();
        }
        if (description.isEmpty()){
            editTextDescription.setError("A description is required");
            editTextDescription.requestFocus();
        }

        dailyScore = dailyScore + exerciseScoreGained;
        weeklyScore = weeklyScore + exerciseScoreGained;
        activityReference.child("Exercise - " + title).child("Title").setValue(title);
        activityReference.child("Exercise - " + title).child("Description").setValue(description);
        activityReference.child("Exercise - " + title).child("Points").setValue(exerciseScoreGained);
        profileReference.child("dailyExerciseScore").setValue(dailyScore);
        profileReference.child("weeklyExerciseScore").setValue(weeklyScore);
    }

    private void getExerciseScores(){
        profileReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if(userProfile != null){
                    dailyScore = userProfile.dailyExerciseScore;
                    weeklyScore = userProfile.weeklyExerciseScore;
                    //wellnessScoreBar.setProgress(wellnessScore);
                    Log.d(TAG, "Exercise retrieved: " + dailyScore);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddExerciseActivity.this, "Something Wrong Happened", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.exerciseSubmit:
                addExercise();
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.cancelExercise:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }

    }
}