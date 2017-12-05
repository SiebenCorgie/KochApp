package org.a1app.kochapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class AddIngredients extends AppCompatActivity {
    //the displayed list
    public ArrayList<ingredient> ing_list;
    //the current xml file
    public XmlWriter xml_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);

        //init the list
        //TODO duno if needed
        this.ing_list = new ArrayList<ingredient>();

        //Populate the view with some dumys
        String[] values = { "Empty!", "Add", "Something"};

        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.simple_list, values);
        ListView listView = (ListView) findViewById(R.id.Ing_Current_list);
        listView.setAdapter(adapter);

        //get the xml file name from the intent
        Intent intent = getIntent();
        Bundle message = intent.getExtras();
        XmlWriter xml_send = (XmlWriter) message.getSerializable(StartRecipe.EXTRA_XML_FILE);
        //save it to the global one
        xml_file = xml_send;

    }

    //Just cleans the text when a text edit is pressed
    public void onTextClicked(View v){
        this.clean_texts();
    }

    //cleans the text views
    public void clean_texts(){
        EditText ed_name = (EditText) findViewById(R.id.edit_add_what);
        EditText ed_val = (EditText) findViewById(R.id.Ing_ammount);

        ed_name.setText("");
        ed_val.setText("");
    }

    //Cancels the view
    public void cancel(View v){
        finish();
    }

    //adds the item to list then updates teh list view.
    public void add_to_list(View v){

        EditText ed_name = (EditText) findViewById(R.id.edit_add_what);
        EditText ed_val = (EditText) findViewById(R.id.Ing_ammount);



        String name = ed_name.getText().toString();
        String val = ed_val.getText().toString();

        ingredient new_ing = new ingredient(name, val);

        this.ing_list.add(new_ing);
        //now update the view
        this.updateList();
        //now clean for a possible next entry
        clean_texts();
    }

    //update the ingredient list
    public void updateList(){
        //Create the name list
        String[] values = new String[this.ing_list.size()];
        for (int i=0; i<this.ing_list.size(); i++){
            values[i] = this.ing_list.get(i).name();
        }

        //TODO can be done more nicely
        //recreate the adapter
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.simple_list, values);
        ListView listView = (ListView) findViewById(R.id.Ing_Current_list);
        listView.setAdapter(adapter);
    }

    //collects the incrediens and sends them to the Utilities activity
    public void next(View v){
        /*TODO
        At this point the program should append the text for each ingredient.
        TODO*/


        //now, for each ingredients, add it to the xml
        for (int i=0; i<this.ing_list.size(); i++){
            this.xml_file.addingredient(this.ing_list.get(i));
        }



        Intent intent = new Intent(this, AddUtilities.class);
        intent.putExtra(StartRecipe.EXTRA_XML_FILE, xml_file);
        startActivity(intent);
        finish();
        //Toast.makeText(getApplicationContext(), "Got: " + xml_name, Toast.LENGTH_LONG).show();
    }

}
