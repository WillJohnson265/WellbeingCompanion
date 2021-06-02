package com.example.fypwellbeingcompanion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ShapeAdapter extends ArrayAdapter<Activities> {


    public ShapeAdapter(@NonNull Context context, int resource, List<Activities> activityList) {
        super(context, resource, activityList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Activities activity = getItem(position);

        if(convertView == null){
            convertView = (LayoutInflater.from(getContext()).inflate(R.layout.row, parent, false));
        }

        TextView title = (TextView) convertView.findViewById(R.id.titleText);
        TextView subTitle = (TextView) convertView.findViewById(R.id.subTitleText);
        TextView points = (TextView) convertView.findViewById(R.id.pointsText);
        
        title.setText(activity.getTitle());
        subTitle.setText(activity.getDescription());
        points.setText(activity.getPoints());


        return convertView;
    }
}
