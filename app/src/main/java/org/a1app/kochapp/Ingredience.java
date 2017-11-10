package org.a1app.kochapp;

import java.io.Serializable;

/**
 * Created by siebencorgie on 11/9/17.
 */

public class Ingredience implements Serializable{

    private String name;
    private String ammount;

    //generates this ingredience
    Ingredience(String name, String ammount){
        this.name = name;
        this.ammount = ammount;
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
