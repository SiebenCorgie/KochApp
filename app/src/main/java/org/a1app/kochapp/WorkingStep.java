package org.a1app.kochapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.awt.font.TextAttribute;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class WorkingStep extends AppCompatActivity {

    //Holds the used recipe
    private WorkingRecipe recipe;
    //Holds a list of already "done" steps.
    private ArrayList<Integer> blackList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_step);


        //Get the stuff
        System.out.println("Getting recipe ...");
        this.recipe = (WorkingRecipe) getIntent().getExtras().getSerializable(MainActivity.RECIPEWORKDER);

        //Setup the step view with dummy steps
        //Populate the list view with some dummys
        ingredient_device_adapter ing_dev_adapter = new ingredient_device_adapter(this, recipe.getIngredients(), recipe.getDevices());
        ListView device_and_ingredients_list = (ListView) findViewById(R.id.IngredientsAndDevices);
        device_and_ingredients_list.setAdapter(ing_dev_adapter);
        //The devices/ingredients don't need a listener, but the steps do



        //Now also setup the list for the expendable one
        final StepAdapter step_adapter = new StepAdapter(this, recipe.getSteps());
        ListView StepView = findViewById(R.id.CookingSteps);
        StepView.setAdapter(step_adapter);

        StepView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, final int i, long l) {

                //Test for blacklist if we are in, throw a toast and return early
                for (int f=0; f<blackList.size(); f++){
                    if (blackList.get(f) == i){
                        //error text
                        String error_text = getResources().getString(R.string.ErrorStepAlreadyUsed);
                        Toast.makeText(getApplicationContext(), error_text, Toast.LENGTH_SHORT).show();
                        //early return
                        return;
                    }
                }

                //Get The time needed, start the "Should be done" message, if stated "OK"
                // Start the countdown thread. When finished, throw a message
                // and make a noise. Also Update the duration thingy in the adapter and cross
                // out thing which are done.

                //Start the "Should be done" dialog and check the message
                AlertDialog.Builder alDia = new AlertDialog.Builder(WorkingStep.this);
                alDia.setTitle(R.string.CookingAlertTitle).setMessage(recipe.getSteps().get(i).should_be_done);

                //Set the ok and cancle button
                alDia.setNegativeButton(R.string.Cancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int n) {
                        //Do things on cancel
                        Toast.makeText(getApplicationContext(), "cancel!", Toast.LENGTH_SHORT).show();
                    }
                });

                //When pressed okay, calls this
                //NOTE: if pressed "OK" we add this step to the blacklist (by id) this prevents
                // the user from unknowingly doing things twice.
                alDia.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int n) {
                        //pressed ok, add the current id to the blacklist
                        blackList.add(i);

                        //Starting the worker thread which is counting down, advancing the progress
                        //bar and finally throwing the alarm
                        //start the worker thread
                        new Thread(new Runnable() {
                            public void run() {

                                //FIrst of all reset the eye candy 100% progress bar to 0%
                                //now publish the thingy
                                ProgressBar bar = view.findViewById(R.id.ProgressBar);
                                bar.setProgress(0);

                                //Get the time we have for this step
                                int time = recipe.getSteps().get(i).time_for_step * 60 * 1000; //in millisecs

                                //now we calculate the time to wait to update it per second
                                int time_step = time / 100;

                                //now we loop 100 times and wait a 1/100th each iteration. I choose
                                // this way because we only can advance from 0-100 in whole numbers :)
                                for (int n=0; n<100; n++) {
                                    //sleep
                                    try {
                                        Thread.sleep(time_step);
                                    } catch (InterruptedException inter) {
                                        System.out.println("Something went wrong!");
                                        break;
                                    }
                                    //now update the bar
                                    bar.setProgress(n);
                                }


                                //Finished the Waiting, time to throw all of dem bells
                                //We do this by doing two things.... first, throw another dialog,
                                //second play a sound while not pressed "Ok". This has to be done
                                // in the UI thread because of reasons only google and java know.
                                // not really but I am to lazy to explain ^^

                                WorkingStep.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Start the sound and dialog

                                        //First of all get the text needed for the dialog from the
                                        // string resources
                                        String finished_title = WorkingStep.this.getResources().getString(R.string.CookingFinshedAlertTitle);
                                        String finished_message = WorkingStep.this.getResources().getString(R.string.CookingFinishedMessage);
                                        //now start the sound
                                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                                        final Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                        r.play();
                                        //and Start the dialog
                                        AlertDialog.Builder OkDia = new AlertDialog.Builder(WorkingStep.this);
                                        OkDia.setTitle(finished_title)
                                                .setMessage(finished_message + " \"" + recipe.getSteps().get(i).to_do + "\"");
                                        //Setup the Ok Callback to stop the alarm sound
                                        OkDia.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                r.stop();
                                            }
                                        });
                                        //show the dialog while playing, waiting for the ok
                                        OkDia.show();
                                    }
                                });

                            }
                        }).start();
                    }
                });

                //Create the dialog... this is actually BEFORE the OnClick"Ok" ...
                alDia.show();
            }
        });

    }

}
