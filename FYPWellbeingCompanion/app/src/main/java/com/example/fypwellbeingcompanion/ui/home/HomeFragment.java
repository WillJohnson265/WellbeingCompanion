package com.example.fypwellbeingcompanion.ui.home;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.fypwellbeingcompanion.AddExerciseActivity;
import com.example.fypwellbeingcompanion.AddSocialEventActivity;
import com.example.fypwellbeingcompanion.R;
import com.example.fypwellbeingcompanion.RegisterActivity;
import com.example.fypwellbeingcompanion.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class HomeFragment extends Fragment implements View.OnClickListener{

    private HomeViewModel homeViewModel;
    private static final String TAG = "Fragment Activity";
    private ProgressBar wellnessScoreCircle, exerciseScoreCircle;
    private TextView wellnessScoreText, exerciseScoreText, wellnessTips;
    private int profileWellnessScore, profileExerciseScore, weeklySocials, weeklyExercise;
    private FirebaseUser user;
    private DatabaseReference profileReference;
    private String userID;
    float translationY = 100f;
    FloatingActionButton fabMain, fabExercise, fabSocial;
    Boolean isMenuOpen = false;
    OvershootInterpolator interpolator;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        wellnessScoreCircle = (ProgressBar) root.findViewById(R.id.wellnessScoreCircle);
        exerciseScoreCircle = (ProgressBar) root.findViewById(R.id.exerciseScoreCircle);
        wellnessScoreText = (TextView) root.findViewById(R.id.wellnessScore);
        exerciseScoreText = (TextView) root.findViewById(R.id.exerciseScore);
        wellnessTips = (TextView) root.findViewById(R.id.wellnessTipsText);

        initFabMenu(root);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        //dateStr = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        profileReference = FirebaseDatabase.getInstance("https://fypwellbeingcompanion-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(userID);

        getAndSetScores();
        //handleTipTexts();

        Log.d(TAG, "social retrieved: " + weeklySocials);
        Log.d(TAG, "ecxercise retrieved" + weeklyExercise);
        Log.d(TAG, "wellnessscore retrieved: " + profileWellnessScore);



        return root;
    }

    private void getAndSetScores() {
        profileReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    profileWellnessScore = userProfile.wellnessScore;
                    if (profileWellnessScore >= 100){
                        profileWellnessScore = 100;
                    }
                    profileExerciseScore = userProfile.dailyExerciseScore;
                    if(profileExerciseScore >= 100){
                        profileExerciseScore = 100;
                    }
                    weeklySocials = userProfile.socialInteractions;
                    weeklyExercise = userProfile.weeklyExerciseScore;
                    wellnessScoreCircle.setProgress(profileWellnessScore);
                    exerciseScoreCircle.setProgress(profileExerciseScore);
                    String strWellness = String.valueOf(profileWellnessScore);
                    String strExercise = String.valueOf(profileExerciseScore);
                    wellnessScoreText.setText(strWellness);
                    exerciseScoreText.setText(strExercise);

                    Log.d(TAG, "social retrieved: " + weeklySocials);
                    Log.d(TAG, "ecxercise retrieved" + weeklyExercise);
                    Log.d(TAG, "wellnessscore retrieved: " + profileWellnessScore);
                    handleTipTexts();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }



    private void handleTipTexts() {
        if (weeklySocials < 1){
            wellnessTips.setText("Have you tried to arrange meeting a friend this week?");
        }
        else if (weeklyExercise < 100){
            wellnessTips.setText("Have you tried scheduling regular exercise?");
        }
        else if (weeklyExercise > 150 && weeklySocials > 2){
            wellnessTips.setText("You have done great this week, you should feel better for it!");
        }
        else{
            wellnessTips.setText("There is nothing to suggest for now!");
        }
    }

    public void initFabMenu(View root){
        fabMain = root.findViewById(R.id.fabMain);
        fabExercise = root.findViewById(R.id.fabExercise);
        fabSocial = root.findViewById(R.id.fabSocial);

        fabSocial.setTranslationY(translationY);
        fabExercise.setTranslationY(translationY);

        fabMain.setOnClickListener(this);
        fabSocial.setOnClickListener(this);
        fabExercise.setOnClickListener(this);

        closeMenu();
    }

    private void openMenu(){
        isMenuOpen = !isMenuOpen;

        fabMain.animate().setInterpolator(interpolator).rotation(45f).setDuration(300).start();
        fabExercise.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabSocial.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();


    }

    private void closeMenu(){
        isMenuOpen = !isMenuOpen;

        fabMain.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();
        fabExercise.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabSocial.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void handleExercise(){
        startActivity(new Intent(getContext(), AddExerciseActivity.class));
    }

    private void handleSocial(){
        startActivity(new Intent(getContext(), AddSocialEventActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabMain:
                if (isMenuOpen){
                    closeMenu();
                } else {
                    openMenu();
                }
                break;
            case R.id.fabExercise:
                handleExercise();
                break;
            case R.id.fabSocial:
                handleSocial();
                break;

    }}
}