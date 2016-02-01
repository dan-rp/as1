package com.example.risticpe.risticpe_fueltrack;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dan on 28/01/16.
 */

/*
    Slightly modified arrayadapter allows better formatting of the fields in the listview.
    Also allows the different colors for listview elements to be set. This is the appropriate
    place for that because this is where the inflater service is called, and that is UI
    functionality as is the color thing.
 */


public class LogEntryAdapter extends ArrayAdapter<LogEntry>
{
    public LogEntryAdapter(Context context, ArrayList<LogEntry> logEntries)
    {
        super(context, 0, logEntries);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LogEntry logEntry = getItem(position);

        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.log_entry, parent, false);
        }

        /* Get references to the text fields */
        TextView fuelCostTV = (TextView) convertView.findViewById(R.id.fuel_cost_item);
        TextView stationNameTV = (TextView) convertView.findViewById(R.id.station_name_item);
        TextView dateTV = (TextView) convertView.findViewById(R.id.date_item);

        /* Populate the text fields */
        fuelCostTV.setText(String.format("$%.2f", logEntry.getCost()));
        stationNameTV.setText(logEntry.getStation());
        dateTV.setText(logEntry.getDate());


        /* Get a reference to the layer-list and set the color on the shape element */
        LayerDrawable bgLayers = (LayerDrawable) convertView.getBackground();
        GradientDrawable bgShape = (GradientDrawable) bgLayers.getDrawable(0);
        bgShape.setColor(logEntry.getColor());

        return convertView;
    }
}
