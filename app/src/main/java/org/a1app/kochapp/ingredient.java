package org.a1app.kochapp;

import java.io.Serializable;

/**
 * Created by siebencorgie on 11/9/17.
 */

public class ingredient implements Serializable{

    public String name;
    public String ammount;

    //generates this ingredient
    ingredient(String name, String ammount){
        this.name = name;
        this.ammount = ammount;
    }
    //uninit ingredient
    ingredient(){
        this.name = "NO NAME";
        this.ammount = "NO AMMOUNT";
    }

    //returns the ammount
    public String ammount(){
        return this.ammount;
    }

    //returns the name
    public String name(){
        return this.name;
    }
    //to make it Serializable
    public String toString() {
        return "I am a class";
    }
}
