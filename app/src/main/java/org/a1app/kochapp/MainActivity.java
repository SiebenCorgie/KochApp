package org.a1app.kochapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Setup the main window
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Populate the view with some dumys
        String[] values = {"Android","IPhone","WindowsMobile","Blackberry",
                "WebOS","Ubuntu","Windows7","Max OS X", "Teddy",
                "Was", "Here", "As", "Well", "And", "Did", "A", "Thing"};


        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.simple_list, values);
        ListView listView = (ListView) findViewById(R.id.MainList);
        listView.setAdapter(adapter);

    }

    public void addRecipe(View v){
        //open the adding dialog
        Intent intent = new Intent(this, StartRecipe.class);
        startActivity(intent);
    }

}
