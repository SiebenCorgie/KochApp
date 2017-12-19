package org.a1app.kochapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.a1app.kochapp.R;

import java.util.ArrayList;

/**
 * Created by siebencorgie on 12/19/17.
 */

public class ingredient_device_adapter extends ArrayAdapter {

    private final Context context;
    private final ArrayList<ingredient> indredients;
    private final ArrayList<String> devices;

    public ingredient_device_adapter(Context context, ArrayList<ingredient> ingredients, ArrayList<String> devices) {
        super(context, -1, ingredients);
        this.context = context;
        this.indredients = ingredients;
        this.devices = devices;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.cooking_ingredient_device_list, parent, false);

        TextView IngredientsView = (TextView) rowView.findViewById(R.id.TVIngredient);
        TextView DeviceView = (TextView) rowView.findViewById(R.id.TV_Device);

        //Set the ingredients text
        if (position <= this.indredients.size() - 1){
            IngredientsView.setText(this.indredients.get(position).ammount + "   " + this.indredients.get(position).name);
        }else{
            IngredientsView.setText("");
        }

        //Set the device text
        if (position <= this.devices.size() - 1){
            DeviceView.setText(this.devices.get(position));
        }else{
            DeviceView.setText("");
        }


        return rowView;
    }
}
