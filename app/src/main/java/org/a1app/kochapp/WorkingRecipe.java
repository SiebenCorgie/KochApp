package org.a1app.kochapp;

import android.widget.Toast;

import org.a1app.kochapp.CookingStep;
import org.a1app.kochapp.WorkingStep;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


enum ItemType{

    RECIPE_NAME, STEP, DEVICE, DEVICE_NAME, INGREDIENT,
    INGREDIENT_NAME, NAME, AMMOUNT, TOBEDONE, TIME, TODO, ID,
    //used to determin when not to use the found text
    ENDTAG
}

/**
 * Created by siebencorgie on 12/5/17.
 * Holds all CookingSteps in a vector and can return the next one.
 * Can only be created form a pull parser.
 */

public class WorkingRecipe implements Serializable {

    //the name
    private String name = "NoName";
    //a list of devices
    private ArrayList<String> devices = new ArrayList();
    //a list of ingredients
    private ArrayList<ingredient> ingredients = new ArrayList();
    //a list of steps to do
    private ArrayList<CookingStep> steps = new ArrayList();


    //This can fail and will be handled by the app
    WorkingRecipe(XmlPullParser parser) throws XmlPullParserException, IOException {
        //System.out.println("Started Parser");
        //init the needed stuff
        int eventType = parser.getEventType();
        //an empty dummy step
        CookingStep currentStep = new CookingStep();
        //Am em,üty dummy ingredient
        ingredient currentIngredient = new ingredient();

        ItemType current_stage = ItemType.STEP;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            //System.out.println("Started event");

            switch (eventType) {
                //Sort what we are reading, there are 5 types: Start and end of the document/file
                // start and end of the tag, and the text itself. We use the tag text to setup the
                // "current_stage" enum and save the text depending on the stag to "this"

                //Starting the doc. Don't do anything
                case XmlPullParser.START_DOCUMENT:
                    //System.out.println("Got DocStart");
                    break;

                //Found a start tag, sort out which stage we are in
                case XmlPullParser.START_TAG:
                    //System.out.println("GotTagStart");
                    name = parser.getName();
                    //We do not use the id currently, but we need one.
                    if (name.equals("id")){
                        //System.out.println("Is " + name);
                        current_stage = ItemType.ID;
                    //The values
                    }else if (name.equals("name")){
                        //System.out.println("Is " + name);

                        current_stage = ItemType.NAME;
                    }else if (name.equals("ingredient_name")){
                        //System.out.println("Is " + name);

                        current_stage = ItemType.INGREDIENT_NAME;
                    }else if (name.equals("ammount")){
                        //System.out.println("Is " + name);

                        current_stage = ItemType.AMMOUNT;
                    }else if (name.equals("device_name")){
                        //System.out.println("Is " + name);

                        current_stage = ItemType.DEVICE_NAME;
                    }else if (name.equals("to_be_done")){
                        //System.out.println("Is " + name);

                        current_stage = ItemType.TOBEDONE;
                    }else if (name.equals("time")){
                        //System.out.println("Is " + name);

                        current_stage = ItemType.TIME;
                    }else if (name.equals("to_do")){
                        //System.out.println("Is " + name);

                        current_stage = ItemType.TODO;
                    ///The different item types
                    }else if (name.equals("RecipeName")){
                        //System.out.println("Is " + name);

                        current_stage = ItemType.RECIPE_NAME;
                    }else if (name.equals("Ingredient")){
                        //System.out.println("Is " + name);

                        current_stage = ItemType.INGREDIENT;
                    }else if (name.equals("Device")){
                        //System.out.println("Is " + name);

                        current_stage = ItemType.DEVICE;
                    }else if (name.equals("Step")){
                        //System.out.println("Is " + name);

                        current_stage = ItemType.STEP;
                    }else{
                        //System.out.println("Is *" + name + "* ... Not in scope");
                    }
                    break;

                //Add text based on current stage
                case XmlPullParser.TEXT:
                    //System.out.println("GotText: ");
                    //System.out.println("+" + parser.getText() + "+");
                    /*
                    if (parser.getText().charAt(0) == '\n'){
                        //System.out.println("UsedTheDirtyTrick");
                        break;
                    }
                    */
                    switch(current_stage){
                        case ID:
                            //do nothing
                            break;
                        case NAME:
                            this.name = parser.getText();
                            //System.out.println("Set Name");
                            break;

                        case INGREDIENT_NAME:
                            currentIngredient.name = parser.getText();
                            //System.out.println("Set Ingredient");
                            break;

                        case AMMOUNT:
                            currentIngredient.ammount = parser.getText();
                            //System.out.println("Set Ammount");
                            break;

                        case DEVICE_NAME:
                            this.devices.add(parser.getText());
                            //System.out.println("Set Device");
                            break;

                        case TOBEDONE:
                            currentStep.should_be_done = parser.getText();
                            //System.out.println("Set ToBeDone");
                            break;

                        case TIME:
                            //System.out.println("Parsing Time integer!");
                            //System.out.println("Text Is: " + parser.getText());
                            currentStep.time_for_step = Integer.parseInt(parser.getText());
                            //System.out.println("Set Time");
                            break;

                        case TODO:
                            currentStep.to_do = parser.getText();
                            //System.out.println("Set ToDo");
                            break;

                        case ENDTAG:
                            //System.out.println("USING THE DIRTY TRICK!");

                    }
                    break;

                //Check if we for instance need to push the current step item.
                case XmlPullParser.END_TAG:
                    //System.out.println("Got Tag End with name: " + parser.getName());
                    name = parser.getName();
                        ///The different item types
                    if (name.equals("Ingredient")) {
                        //System.out.println("Added Ingredient");
                        //we have to push this ingredient and start a new one
                        this.ingredients.add(currentIngredient);
                        currentIngredient = new ingredient();

                    }else if (name.equals("Step")){
                        //System.out.println("Added Step");
                       //have to push this step
                        this.steps.add(currentStep);
                        currentStep = new CookingStep();
                    }

                    //Now set to end tag
                    current_stage = ItemType.ENDTAG;

                    break;

                case XmlPullParser.END_DOCUMENT:
                    //System.out.println("Got Doc End");
                    break;
            }
            eventType = parser.next();
        }

        //System.out.println("Finished parsing XML!");
    }

    /**
     * Returns the name of the current recipe
     */
    public String getName(){
        return this.name;
    }

    /**
     * Returns the ingredients needed for it
     */
    public ArrayList<ingredient> getIngredients(){
        return this.ingredients;
    }
    /**
     * Returns the steps
     */
    public ArrayList<CookingStep> getSteps(){
        return this.steps;
    }
    /**
     * Returns the devices needed
     */
    public ArrayList<String> getDevices(){
        return this.devices;
    }

}
