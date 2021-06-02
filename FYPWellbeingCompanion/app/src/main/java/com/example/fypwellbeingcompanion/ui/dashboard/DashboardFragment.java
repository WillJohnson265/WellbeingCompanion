package com.example.fypwellbeingcompanion.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.fypwellbeingcompanion.R;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private int dailyGoal;
    private TextView dailyGoalCustom;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        Spinner pointsSpinner = root.findViewById(R.id.pointsSpinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.points));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dailyGoalCustom = (TextView) root.findViewById(R.id.customDailyGoal);
        dailyGoalCustom.setVisibility(root.INVISIBLE);

        pointsSpinner.setAdapter(myAdapter);

        pointsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        dailyGoal = 50;
                        dailyGoalCustom.setVisibility(root.INVISIBLE);
                        break;
                    case 1:
                        dailyGoal = 75;
                        dailyGoalCustom.setVisibility(root.INVISIBLE);
                        break;
                    case 2:
                        dailyGoal = 100;
                        dailyGoalCustom.setVisibility(root.INVISIBLE);
                        break;
                    case 3:
                        dailyGoal = 125;
                        dailyGoalCustom.setVisibility(root.INVISIBLE);
                        break;
                    case 4:
                        dailyGoalCustom.setVisibility(root.VISIBLE);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return root;
    }
}