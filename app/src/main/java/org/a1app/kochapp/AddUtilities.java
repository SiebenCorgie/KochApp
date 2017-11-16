package org.a1app.kochapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddUtilities extends AppCompatActivity {
    //the displayed list
    public ArrayList<String> device_list;

    //the xml file
    XmlHandler xml_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_utilites);

        //get the xml file name from the intent
        Intent intent = getIntent();
        Bundle message = intent.getExtras();
        XmlHandler xml_send = (XmlHandler) message.getSerializable(StartRecipe.EXTRA_XML_FILE);
        //save it to the global one
        xml_file = xml_send;

        //init the array
        this.device_list = new ArrayList<String>();

        //now init the list with one item
        String[] values = {"Empty, please add the device needed", "please"};

        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.simple_list, values);
        ListView listView = (ListView) findViewById(R.id.Util_Current_list);
        listView.setAdapter(adapter);

    }

    //updates the device list
    public void update_list(){
        /*
        //Create the name list
        String[] values = new String[this.device_list.size()];
        for (int i=0; i<this.device_list.size(); i++){
            values[i] = this.device_list.get(i);
        }
    */
        //TODO can be done more nicely
        //recreate the adapter
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.simple_list, this.device_list);
        ListView listView = (ListView) findViewById(R.id.Util_Current_list);
        listView.setAdapter(adapter);
    }

    //adds a new item to the list
    public void onAdd(View v){
        EditText device_entry_edit = findViewById(R.id.Util_add_device);
        String device_entry = device_entry_edit.getText().toString();
        this.device_list.add(device_entry);

        this.update_list();

        //remove the old word
        device_entry_edit.setText("");
    }

    //closes the activity
    public void onBack(View v){
        finish();
    }

    //goes to the step wizard
    public void onNext(View v){
        /*TODO
        At this point the program should append the text for each device.
        TODO*/

        //now, for each device, add it to the xml
        for (int i=0; i< this.device_list.size(); i++){
            this.xml_file.addDevice(this.device_list.get(i));
        }


        /* TODO pass the xml further */
        Intent to_first_wiz = new Intent(this, StepWizard.class);
        to_first_wiz.putExtra(StartRecipe.EXTRA_XML_FILE, xml_file);
        startActivity(to_first_wiz);
        finish();

    }
}
