package com.example.fypwellbeingcompanion.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.fypwellbeingcompanion.Activities;
import com.example.fypwellbeingcompanion.Activity;
import com.example.fypwellbeingcompanion.AddExerciseActivity;
import com.example.fypwellbeingcompanion.AddSocialEventActivity;
import com.example.fypwellbeingcompanion.R;
import com.example.fypwellbeingcompanion.ShapeAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotificationsFragment extends Fragment implements View.OnClickListener {

    private NotificationsViewModel notificationsViewModel;
    float translationY = 100f;
    FloatingActionButton fabMain, fabExercise, fabSocial;
    Boolean isMenuOpen = false;
    OvershootInterpolator interpolator;
    private DatabaseReference activityReference;
    String userID, dateStr;
    FirebaseUser user;
    public ArrayList<Activities> activityList = new ArrayList<Activities>();
    //ListView listView;
    String TAG = "Activities";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);



        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        dateStr = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        activityReference = FirebaseDatabase.getInstance("https://fypwellbeingcompanion-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(userID).child("Activities").child(dateStr);
        //getActivities();
        setUpActivities();
        Log.d(TAG,"Activities List: " + activityList.get(1).getTitle());
        //setUpListView(root);
        initFabMenu(root);

        return root;
    }

    public void initFabMenu(View root){
        fabMain = root.findViewById(R.id.fabMain);
        fabExercise = root.findViewById(R.id.fabExercise);
        fabSocial = root.findViewById(R.id.fabSocial);
        //listView = (ListView) root.findViewById(R.id.listViewNotif);


        fabSocial.setTranslationY(translationY);
        fabExercise.setTranslationY(translationY);

        fabMain.setOnClickListener(this);
        fabSocial.setOnClickListener(this);
        fabExercise.setOnClickListener(this);


        closeMenu();
    }

    private void setUpListView(View root){
        //listView = (ListView) root.findViewById(R.id.listViewNotif);
        ShapeAdapter adapter = new ShapeAdapter(getContext(), 0, activityList);
        //listView.setAdapter(adapter);
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

    private void getActivities(){
        activityReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Activities activity = snapshot.getValue(Activities.class);
                    /*String title = activity.getTitle();
                    String description = activity.getDescription();
                    int points = activity.getPoints();

                    Activities event = new Activities(title, description, points);*/
                    //Log.d(TAG, "Adding to list: " + title);
                    activityList.add(activity);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setUpActivities(){
        Activities walk = new Activities("Walk", "Walk to the pub", 32);
        activityList.add(walk);

        Activities run = new Activities("Run", "Run to the river", 72);
        activityList.add(run);

        Activities coffee = new Activities("Coffee", "Coffee with Gerry", 20);
        activityList.add(coffee);
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

        }
    }
}