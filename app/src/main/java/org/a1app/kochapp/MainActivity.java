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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {
    public static final String RECIPEWORKDER = "com.example.myfirstapp.WORKGER";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Setup the main window
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Populate the view with some dumys
        String[] values = this.getLocalRecipes();


        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.simple_list, values);
        ListView listView = findViewById(R.id.MainList);
        listView.setAdapter(adapter);

        //create the listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(), "Pressed " + i, Toast.LENGTH_SHORT).show();
                //load the recipe from the name
                //start the coking step with the first step

                WorkingRecipe loaded_recipe = null;

                XmlPullParserFactory pullParserFactory;
                try {
                    pullParserFactory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = pullParserFactory.newPullParser();
                    //Get the location where the file com from
                    File RecipeLocation = getRecipeDirectory();
                    //Get the file at this location and finally load it
                    String entry = (String) adapterView.getItemAtPosition(i);
                    File recipe_xml = new File(RecipeLocation, entry);

                    FileInputStream fileInputStream = new FileInputStream(recipe_xml);
                    //InputStream in_s =
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(fileInputStream, null);

                    //Start the recipe from this parser
                    WorkingRecipe recipe = new WorkingRecipe(parser);
                    loaded_recipe = recipe;

                } catch (XmlPullParserException e) {
                    Toast.makeText(getApplicationContext(), "Pullparser failed", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "File loading failed", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


                if (loaded_recipe != null){
                    Toast.makeText(getApplicationContext(), "Name: " + loaded_recipe.getName(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Could not load file :(", Toast.LENGTH_SHORT).show();
                }

                //now send it as extra to the first cooking activity
                Intent intent = new Intent(getApplicationContext(), WorkingStep.class);
                intent.putExtra(RECIPEWORKDER, loaded_recipe);
                startActivity(intent);

            }
        });
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
                Environment.DIRECTORY_DOCUMENTS), XmlWriter.recipe_dir_name);
        if (!file.mkdirs()) {
            Log.e("TEDDY!", "Directory not created");
        }

        //now list all available recipes
        return file.list();
    }

    /**
     * Returns the recipes directory
     */
    public File getRecipeDirectory(){
        // Get the directory for the user's public Documents directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), XmlWriter.recipe_dir_name);
        if (!file.mkdirs()) {
            Log.e("TEDDY!", "Directory not created");
        }

        return file;
    }

}
