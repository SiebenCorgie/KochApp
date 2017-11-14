package org.a1app.kochapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class AddIngredients extends AppCompatActivity {
    //The xml to be sent as message to the next activity
    public static final String EXTRA_XML_FILE = "com.example.myfirstapp.ING_TO_UTIL";

    //the displayed list
    public ArrayList<Ingredience> ing_list;
    public String xml_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);

        //init the list
        //TODO duno if needed
        this.ing_list = new ArrayList<Ingredience>();

        //Populate the view with some dumys
        String[] values = { "Empty!", "Add", "Something"};

        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.simple_list, values);
        ListView listView = (ListView) findViewById(R.id.Ing_Current_list);
        listView.setAdapter(adapter);

        //get the xml file name from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(StartRecipe.EXTRA_XML_FILE);
        //save it to the global one
        xml_name = message;
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

        Ingredience new_ing = new Ingredience(name, val);

        this.ing_list.add(new_ing);
        //now update the view
        this.updateList();
        //now clean for a possible next entry
        clean_texts();
    }

    //update the ingredience list
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
        At this point the programm should append the text for each ingredient.
        TODO*/

        Intent intent = new Intent(this, AddUtilities.class);
        intent.putExtra(EXTRA_XML_FILE, xml_name);
        startActivity(intent);

        //Toast.makeText(getApplicationContext(), "Got: " + xml_name, Toast.LENGTH_LONG).show();
    }

}
