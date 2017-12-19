package org.a1app.kochapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class WorkingStep extends AppCompatActivity {

    private WorkingRecipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_step);


        //Get the stuff
        System.out.println("Getting recipe ...");
        this.recipe = (WorkingRecipe) getIntent().getExtras().getSerializable(MainActivity.RECIPEWORKDER);


        Toast.makeText(getApplicationContext(), "Found " + recipe.getDevices().size() + " Devices", Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "Found " + recipe.getSteps().size() + " Steps", Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "Found " + recipe.getIngredients().size() + " Ingredients", Toast.LENGTH_LONG).show();

        //Setup the step view with dummy steps
        //Populate the list view with some dummys
        ingredient_device_adapter ing_dev_adapter = new ingredient_device_adapter(this, recipe.getIngredients(), recipe.getDevices());
        ListView device_and_ingredients_list = (ListView) findViewById(R.id.IngredientsAndDevices);
        device_and_ingredients_list.setAdapter(ing_dev_adapter);
        //The devices/ingredients don't need a listener, but the steps do



        //Now also setup the list for the expendable one
        StepAdapter step_adapter = new StepAdapter(this, recipe.getSteps());
        ListView StepView = (ListView) findViewById(R.id.CookingSteps);
        StepView.setAdapter(step_adapter);

        StepView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO: Get The time needed, start the "Should be done" message, if stated "OK"
                //TODO: Start the countdown thread. When finished, throw a message to the home screen
                //TODO and make a noise. Also Update the duration thingy in the adapter and cross
                //TODO out thing which are done.
            }
        });

    }

}
