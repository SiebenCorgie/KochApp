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
    STEP, DEVICE, INGREIENCE
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
        //init the needed stuff
        ArrayList<CookingStep> allSteps = null;
        int eventType = parser.getEventType();
        CookingStep currentStep = null;
        ingredient currentIngredient = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = null;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name == "item") {
                        currentStep = new CookingStep();
                        //we are in a new step //could be done nice
                    } else if (name == "id") {
                        //currentStep.name = parser.nextText();
                    } else if (name == "name") {
                        this.name = parser.nextText();
                    } else if (name == "ingredient") {
                        currentIngredient.name = parser.nextText();
                    } else if (name == "ammount") {
                        currentIngredient.ammount = parser.nextText();
                        //ingredient finished, add it and gen new one
                        this.ingredients.add(currentIngredient);
                        currentIngredient = new ingredient();
                    } else if (name == "device") {
                        this.devices.add(parser.nextText());
                    } else if (name == "to_be_done") {
                        currentStep.should_be_done = parser.nextText();
                    } else if (name == "time") {
                        currentStep.time_for_step = Integer.parseInt(parser.nextText());
                    } else if (name == "to_do") {
                        currentStep.to_do = parser.nextText();
                        //done with this stepp, adding and creating a new blank one
                        this.steps.add(currentStep);
                        currentStep = new CookingStep();
                    }


                    break;
                case XmlPullParser.END_TAG:
                    //finished the file
                    break;
            }
            eventType = parser.next();
        }
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
