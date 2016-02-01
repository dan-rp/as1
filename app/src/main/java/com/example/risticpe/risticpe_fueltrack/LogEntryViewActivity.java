package com.example.risticpe.risticpe_fueltrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/*
    This is the view that the app goes to when someone clicks on an element in
    the listview. It displays the full information for the associated log entry
    (the listview only shows the station, fuel cost, and date).
    Also provides the option of editing the entry or deleting it.
 */

public class LogEntryViewActivity extends Activity
{
    /* Only needs to know these codes */
    private final static int edit_log_entry_code = 3;
    private final static int delete_log_entry_code = 5;

    private float litres, cents_per_litre, odo_reading;
    private int listposition;
    private String date, station, fuel_grade;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_entry_view);

        Intent intent = getIntent();

        /* Pull all the data needed from the intent */
        litres = intent.getFloatExtra("litres", 0);
        cents_per_litre = intent.getFloatExtra("cents", 0);
        odo_reading = intent.getFloatExtra("odo", 0);
        date = intent.getStringExtra("date");
        station = intent.getStringExtra("station");
        fuel_grade = intent.getStringExtra("grade");
        listposition = intent.getIntExtra("position", 0);
        float total = intent.getFloatExtra("cost", 0);

        /* Populate the view */
        ((TextView) findViewById(R.id.log_entry_view_date)).setText(date);
        ((TextView) findViewById(R.id.log_entry_view_station_name)).setText(station);
        ((TextView) findViewById(R.id.log_entry_view_fuel_grade)).setText(fuel_grade);
        ((TextView) findViewById(R.id.log_entry_view_litres)).setText(String.format("%.3f L", litres));
        ((TextView) findViewById(R.id.log_entry_view_cents)).setText(String.format("%.1f ¢/L", cents_per_litre));
        ((TextView) findViewById(R.id.log_entry_view_odo)).setText(String.format("%.1f km", odo_reading));
        ((TextView) findViewById(R.id.log_entry_view_cost)).setText(String.format("$%.2f", total));
    }

    public void startLogEditor(View view)
    {
        /*
            The edit button got pressed. The data for this entry will
            be sent to the log entry editing screen so that it is visible
            while changes are made.
        */
        Intent intent = new Intent(this, NewLogEntryActivity.class);

        intent.putExtra("date", date);
        intent.putExtra("station", station);
        intent.putExtra("grade", fuel_grade);
        intent.putExtra("odo", Float.toString(odo_reading));
        intent.putExtra("litres", Float.toString(litres));
        intent.putExtra("cents", Float.toString(cents_per_litre));

        intent.putExtra("position", listposition);
        intent.putExtra("request_type", edit_log_entry_code);

        /*
            This activity is going to die so that the edit screen
            returns directly to the main screen. I felt that this
            looked better than going back to this view.
        */
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);

        startActivity(intent);
        finish();
    }

    public void deleteEntry(View view)
    {
        /*
            The delete button got pressed. The corresponding code
            will be sent back to the main activity.
        */
        Intent returnedIntent = new Intent();

        returnedIntent.putExtra("request_type", delete_log_entry_code);
        returnedIntent.putExtra("position", listposition);

        setResult(Activity.RESULT_OK, returnedIntent);

        finish();
    }

    public void back(View view)
    {
        finish();
    }

}
