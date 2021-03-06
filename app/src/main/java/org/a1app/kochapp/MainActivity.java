package org.a1app.kochapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import org.apache.commons.io.FileUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {
    public static final String RECIPEWORKDER = "com.example.myfirstapp.WORKGER";
    //The current list view of this app;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Setup the main window
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //First of all we handle any implicit intent. This can happen if the user opened the app
        // via a "alien" recipe stored somewhere. In this case we'll first copy this file to our
        // local recipes folder and then proceed as usual.
        handle_intent(getIntent());


        this.update_list();

        //create the listener
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
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
                    String entry = ((String) adapterView.getItemAtPosition(i)) + ".recipe"; //we append the ending again to get the file name in the filesystem
                    File recipe_xml = new File(RecipeLocation, entry);

                    FileInputStream fileInputStream = new FileInputStream(recipe_xml);
                    //InputStream in_s =
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(fileInputStream, null);

                    //Start the recipe from this parser
                    WorkingRecipe recipe = new WorkingRecipe(parser);
                    loaded_recipe = recipe;

                } catch (XmlPullParserException e) {
                    Toast.makeText(getApplicationContext(), "File loading failed (XmlPullParserException)", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "File loading failed (IO Exception)", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return;
                }


                if (loaded_recipe != null) {
                    //This happens if everything went all right, we don't want to throw anything in this event
                    //Toast.makeText(getApplicationContext(), "Name: " + loaded_recipe.getName(), Toast.LENGTH_SHORT).show();
                } else {
                    //This should not happen (Null for the loaded recipe), but it should be checked.
                    Toast.makeText(getApplicationContext(), "Could not load file :(", Toast.LENGTH_SHORT).show();
                    return;
                }

                //now send it as extra to the first cooking activity
                Intent intent = new Intent(getApplicationContext(), WorkingStep.class);
                intent.putExtra(RECIPEWORKDER, loaded_recipe);
                startActivity(intent);

            }
        });

        //Finished the on click listener to
        // now we also want to share stuff we we long click on a entry, this happens here
        this.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //we get the selected items name and create a "ShareActionProvider" from its file
                //path.

                File xml_path = null;
                try {
                    xml_path = new File(getRecipeDirectory(), getLocalRecipes()[i]);
                } catch (ArrayIndexOutOfBoundsException r) {
                    //It could be possible that we are using some strange index... should not happen
                    //but coming from Rust, its better to double check if the syntax doesn't.
                    Toast.makeText(getApplicationContext(), "Failed to send recipe", Toast.LENGTH_SHORT).show();
                    return true;
                }
                //If we got no exception the file should be good
                Uri xml_uri = Uri.fromFile(xml_path);


                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/xml");
                //put the xml into the share thingy
                share.putExtra(Intent.EXTRA_STREAM, xml_uri);

                //now start the sharing thing
                startActivity(Intent.createChooser(share, "Share the stuff"));


                return true;
            }
        });

    }

    public void addRecipe (View v){
        //open the adding dialog
        Intent intent = new Intent(this, StartRecipe.class);
        startActivity(intent);
        //After finishing the recipe, update the current list
        this.update_list();
    }

    /**
     * Updates the current list view
     */
    private void update_list(){
        //Populate the view with some dumys
        String[] values = this.getLocalRecipes();

        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].substring(0, values[i].length() - 7);
        }


        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.simple_list, values);
        this.listView = findViewById(R.id.MainList);
        this.listView.setAdapter(adapter);
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

    private void handle_intent(Intent intent){
        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();

        if (appLinkData != null){


            //Copying to local recipe directory, overwriting if necessary
            File source = new File(appLinkData.getPath());
            File dest = this.getRecipeDirectory();
            try{
                copy(source, dest);
            }catch (IOException e){
                Toast.makeText(this, "Failed to copy file!: " + e, Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(this, "Opened as usual...", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Copyies the file or src to dst via FileUtils. Relevant meme: https://twitter.com/rbanffy/status/950784408482648066
     * @param src
     * @param dst
     * @throws IOException
     */
    public void copy(File src, File dst) throws IOException {

        //Create the file in the dst
        String file_name = src.getName();
        Toast.makeText(this.getApplicationContext(), "Name: " + file_name, Toast.LENGTH_SHORT).show();
        File final_dest_file;
        final_dest_file = new File(dst, file_name);


        FileUtils.copyFile(src, final_dest_file);



    }

}
