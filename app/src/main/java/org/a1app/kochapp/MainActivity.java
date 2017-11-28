package org.a1app.kochapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Setup the main window
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Populate the view with some dumys
        String[] values = this.getLocalRecipes();


        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.simple_list, values);
        ListView listView = (ListView) findViewById(R.id.MainList);
        listView.setAdapter(adapter);

    }

    public void addRecipe (View v){
        //open the adding dialog
        Intent intent = new Intent(this, StartRecipe.class);
        startActivity(intent);

    }

    /**
     * Gets the current recipes in the ~/Documents/Recepies folder
     */

    private String[] getLocalRecipes(){
         //first of all get the documents location

        // Get the directory for the user's public Documents directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), XmlHandler.recipe_dir_name);
        if (!file.mkdirs()) {
            Log.e("TEDDY!", "Directory not created");
        }

        //now list all available recipes
        return file.list();



    }

}
