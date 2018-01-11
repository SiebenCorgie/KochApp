package org.a1app.kochapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by siebencorgie on 11/16/17.
 * Is used to create new xml files, handle the storage location and
 * reading a recipe.
 */

public class XmlWriter implements Serializable {

    //the directory name for the xml
    public static String recipe_dir_name = "Recipes";
    private static String file_name = "No_Name.recipe";

    private static int id_counter;


    /** Contructs a new class by creating a new file with this name **/
    XmlWriter(String name, Context context){
        this.file_name = name + ".recipe"; //We append a .recipe file ending to indentify the correct files later when importing from other directorys
        //and init the counter
        this.id_counter = 0;


        this.createFile();
        //now add the xml header
        this.appendText("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        //now push the recipes tag
        this.appendText( "<Recipe>");

        //now add the name as well
        this.addName(name);

    }

    /** Creates a file with a name in a context */
    private void createFile(){
        //first create the file
        File rec_location = this.getDocumentLocation();
        File target_file = new File(rec_location, this.file_name);
        //now try to create it
        try {
            target_file.createNewFile();
        }catch (Exception e){
            Log.w("Exception: ", e);
        }
    }

    /** this will add the end tag for the recipes */
    public void endXml(){
        this.appendText("</Recipe>");
    }

    private void startItem(String name){
        String add_string = "<" + name + ">";
        add_string = ident(add_string, 1);
        this.appendText(add_string);
    }

    private void endItem(String name){
        String add_string = "</" + name + ">";
        add_string = ident(add_string, 1);
        this.appendText(add_string);
    }

    private void writeId(){
        String add_string = "<id>" + this.id_counter + "</id>";
        add_string = ident(add_string, 2);
        this.appendText(add_string);
        //ingrement the id
        this.id_counter += 1;
    }

    /** adds a name to the recipe **/
    private void addName(String name){
        this.startItem("RecipeName");
        this.writeId();

        this.appendText(ident("<name>" + name + "</name>", 2));

        this.endItem("RecipeName");
    }

    /** adds a new ingredient to the xml **/
    public void addingredient(ingredient ing){
        //start the item tag
        //item adder
        this.startItem("Ingredient");
        this.writeId();

        //name
        this.appendText(ident("<ingredient_name>" + ing.name() + "</ingredient_name>", 2));
        //amount
        this.appendText(ident("<ammount>" + ing.ammount() + "</ammount>", 2));

        this.endItem("Ingredient");
    }
    
    /** Adds a device to the xml **/
    public void addDevice(String device){
        this.startItem("Device");
        this.writeId();

        this.appendText(ident("<device_name>" + device + "</device_name>", 2));

        this.endItem("Device");
    }

    /** adds a step to the list */
    public void addStep(CookingStep step){
        this.startItem("Step");
        this.writeId();

        this.appendText(ident("<to_be_done>" + step.should_be_done + "</to_be_done>", 2));
        this.appendText(ident("<time>" + step.time_for_step + "</time>", 2));
        this.appendText(ident("<to_do>" + step.to_do + "</to_do>", 2));

        this.endItem("Step");
    }

    /** appends text to a file, then adds an page break **/
    private void appendText(String text){
        //first create the file
        File rec_location = this.getDocumentLocation();
        File target_file = new File(rec_location, this.file_name);
        FileOutputStream outputStream;

        //add a end line to the text
        String final_text = text + "\n";
        //now try to write to it
        try {
            outputStream = new FileOutputStream(target_file, true); //appending
            outputStream.write(final_text.getBytes());
            outputStream.close();
        }catch (Exception e){
            /* TODO find a nice way
            Toast.makeText(
                    this.file_context, "Something went wrong while writing to " + file_name, Toast.LENGTH_LONG
            ).show();
            */
        }
    }

    /** returning the **/
    public File getDocumentLocation() {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), this.recipe_dir_name);
        if (!file.mkdirs()) {
            Log.e("TEDDY!", "Directory not created");
        }
        return file;
    }

    private String ident(String text, int idents){
        String spaces = "";
        for (int i=0; i<  idents * 4; i++){ //4 space should be enough
            spaces += " ";
        }
        //add to text
        return spaces + text;
    }



}
