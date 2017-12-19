package org.a1app.kochapp;

import java.io.Serializable;

/**
 * Created by siebencorgie on 11/16/17.
 */

public class CookingStep implements Serializable {
    public String should_be_done;
    public int time_for_step;
    public String to_do;

    CookingStep(String should_be_done, int time_for_step, String to_do){
        this.should_be_done = should_be_done;
        this.time_for_step = time_for_step;
        this.to_do = to_do;
    }

    //uninit step
    CookingStep(){
        this.should_be_done = "NO CONTENT";
        this.time_for_step = 0;
        this.to_do = "NOTHING TO DO";
    }
}
