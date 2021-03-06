package org.a1app.kochapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class StartRecipe extends AppCompatActivity {

    //The xml to be sent as message to the next activity
    public static final String EXTRA_XML_FILE = "com.example.myfirstapp.XMLFILE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_recipe);

    }

    public void onNext(View v){
        EditText file_name_edit = findViewById(R.id.RecipeName);
        String file_name = file_name_edit.getText().toString();

        /*TODO
        At this point the xml file should be created and the first part, (the name) should be
        written inside
        TODO*/

        XmlWriter handler = new XmlWriter(file_name, getApplicationContext());

        Intent go_to_ingredients = new Intent(this, AddIngredients.class);
        go_to_ingredients.putExtra(EXTRA_XML_FILE, handler);
        startActivity(go_to_ingredients);
        finish();
    }

    public void onBack(View v){
        finish();
    }
}
