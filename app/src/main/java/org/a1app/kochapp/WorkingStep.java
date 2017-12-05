package org.a1app.kochapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class WorkingStep extends AppCompatActivity {

    private WorkingRecipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_step);
        //Get the stuff
        this.recipe = (WorkingRecipe) getIntent().getExtras().getSerializable(MainActivity.RECIPEWORKDER);

        //Setup the text views
        TextView todo = findViewById(R.id.WS_ed_todo);
        TextView step = findViewById(R.id.WS_step);
        /*
        todo.setText(recipe.getSteps().get(0).should_be_done);
        step.setText(recipe.getSteps().get(0).to_do);
        */

    }

}
