package org.a1app.kochapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class StepWizard extends AppCompatActivity {

    //the xml file
    XmlHandler xml_file;
    //an empty step
    private CookingStep step = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_wizard);

        //get the xml file name from the intent
        Intent intent = getIntent();
        Bundle message = intent.getExtras();
        XmlHandler xml_send = (XmlHandler) message.getSerializable(StartRecipe.EXTRA_XML_FILE);
        //save it to the global one
        xml_file = xml_send;

    }

    /** add step to xml **/
    public void addStep(){
        EditText ed_to_be = (EditText) findViewById(R.id.Wiz_ToBe);
        EditText ed_time = (EditText) findViewById(R.id.Wiz_Time);
        EditText ed_to_do = (EditText) findViewById(R.id.Wiz_ToDo);

        //Test that every thing is filled with something
        if (
                ed_to_be.getText().toString() != "" &&
                ed_time.getText().toString() != "" &&
                ed_to_do.getText().toString() != ""
                ){
            //try to convert the time to an int

            int time = 0;
            try{

                time = Integer.parseInt(ed_time.getText().toString());

                this.step = new CookingStep(
                        ed_to_be.getText().toString(),
                        time,
                        ed_to_do.getText().toString()
                );
            }catch (NumberFormatException nfe){
                //print the problem and null this.step
                Toast.makeText(
                        getApplicationContext(), "Insert a valid time!", Toast.LENGTH_LONG
                ).show();

                this.step = null;
            }
        }

    }


    /** Going to a next step **/
    public void onNext(View v){


        this.addStep();

        //add the step
        if (this.step != null){
            xml_file.addStep(this.step);
            //is successful, open a new wizard
            Intent intent = new Intent(this, StepWizard.class);
            intent.putExtra(StartRecipe.EXTRA_XML_FILE, xml_file);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(
                    getApplicationContext() ,
                    "Don't you want to add anything to the step?",
                    Toast.LENGTH_LONG
            ).show();
        }

    }

    /** finializing the xml and ending the activity **/
    public void onFinish(View v){

        this.addStep();

        if (this.step != null){
            xml_file.addStep(this.step);
        }

        xml_file.endXml();
        finish();
    }
}
