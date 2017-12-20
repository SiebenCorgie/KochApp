package org.a1app.kochapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by siebencorgie on 12/19/17.
 */

public class StepAdapter extends ArrayAdapter {
    private final Context context;
    private final ArrayList<CookingStep> step;
    private int progress = 0;

    public StepAdapter(Context context, ArrayList<CookingStep> step) {
        super(context, -1, step);
        this.context = context;
        this.step = step;
        this.progress = 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.cooking_step_view, parent, false);

        TextView Time = (TextView) rowView.findViewById(R.id.TVTime);
        TextView Todo = (TextView) rowView.findViewById(R.id.TVStep);

        //Set the Time text

        String min_string = parent.getResources().getString(R.string.Short_Minutes);
        if (position <= this.step.size() - 1){
            Time.setText("" + this.step.get(position).time_for_step+ " " + min_string);
        }
        //Set the todoText
        if (position <= this.step.size() - 1){
            Todo.setText(this.step.get(position).to_do);
        }





        return rowView;
    }
}
