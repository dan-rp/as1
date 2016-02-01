package com.example.risticpe.risticpe_fueltrack;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

/*
    The main activity that starts the app.
    Displays a list of fuel log entries.
 */

public class FuelTrackActivity extends ListActivity
{
    private final static String logfile = "logs.sav";
    private ArrayList<LogEntry> logEntries;
    private ArrayAdapter<LogEntry> adapter;

    /* Codes for keeping track of which secondary activity is returning data
     * and what to do with the result */
    private final static int new_log_entry_code = 1;
    private final static int view_log_entry_code = 2;
    private final static int edit_log_entry_code = 3;
    private final static int edit_canceled_code = 4;
    private final static int delete_log_entry_code = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fuel_track);

        logEntries = new ArrayList<>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        /* Homemade image for floating button */
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.add_black_48px, options);

        fab.setImageBitmap(bitmap);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startLogEditor();
            }

        });
    }

    @Override
    protected void onStart() {
        // Taken from lonelyTwitter
        super.onStart();
        loadFromFile();
        adapter = new LogEntryAdapter(this, logEntries);
        setListAdapter(adapter);
        updateTotal(); //Except for this line
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        /*
            When the user clicks on an item in the list, they are taken to an activity
            that displays the full data for that item, as well as edit/delete options.
        */

        Intent intent = new Intent(this, LogEntryViewActivity.class);

        intent.putExtra("date", logEntries.get(position).getDate());
        intent.putExtra("station", logEntries.get(position).getStation());
        intent.putExtra("grade", logEntries.get(position).getFuelgrade());
        intent.putExtra("odo", logEntries.get(position).getOdometerReading());
        intent.putExtra("litres", logEntries.get(position).getLitresOfFuel());
        intent.putExtra("cents", logEntries.get(position).getCentsPerLitre());
        intent.putExtra("cost", logEntries.get(position).getCost());

        /* The log entry's position in the list will need to be retained if it is modified */
        intent.putExtra("position", position);

        startActivityForResult(intent, view_log_entry_code);
    }

    private void startLogEditor()
    {
        Intent intent = new Intent(this, NewLogEntryActivity.class);
        intent.putExtra("request_type", new_log_entry_code);
        startActivityForResult(intent, new_log_entry_code);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        /* Handle data returned by one of the secondary activities */
        switch(requestCode)
        {
            case new_log_entry_code:
            {
                /* The NewLogEntryActivity has returned */
                if(data != null && data.hasExtra("request_type")
                        && data.getIntExtra("request_type", 0) != edit_canceled_code)
                {
                    /* Generate a new LogEntry */
                    LogEntry logEntry = new LogEntry();

                    logEntry.setCalendar(data.getIntExtra("year", 0), data.getIntExtra("month", 0), data.getIntExtra("day", 0));
                    logEntry.setStation(data.getStringExtra("station"));
                    logEntry.setFuelGrade(data.getStringExtra("grade"));
                    logEntry.setOdometerReading(data.getFloatExtra("odo", 0));
                    logEntry.setLitresOfFuel(data.getFloatExtra("litres", 0));
                    logEntry.setCentsPerLitre(data.getFloatExtra("cents", 0));

                    logEntries.add(logEntry);
                    adapter.notifyDataSetChanged();
                    updateTotal();
                    saveInFile();
                }
                break;
            }
            case view_log_entry_code:
            {
                if (data != null && data.hasExtra("request_type"))
                {
                    /* The LogEntryViewActivity has returned via the NewLogEntryActivity */
                    if (data.getIntExtra("request_type", 0) == edit_log_entry_code)
                    {
                        /* The viewed entry was modified */
                        int position = data.getIntExtra("position", 0);

                        LogEntry logEntry = logEntries.get(position);

                        logEntry.setCalendar(data.getIntExtra("year", 0),
                                data.getIntExtra("month", 0), data.getIntExtra("day", 0));
                        logEntry.setStation(data.getStringExtra("station"));
                        logEntry.setFuelGrade(data.getStringExtra("grade"));
                        logEntry.setOdometerReading(data.getFloatExtra("odo", 0));
                        logEntry.setLitresOfFuel(data.getFloatExtra("litres", 0));
                        logEntry.setCentsPerLitre(data.getFloatExtra("cents", 0));

                        adapter.notifyDataSetChanged();
                        updateTotal();
                        saveInFile();
                    }
                    else if(data.getIntExtra("request_type", 0) == delete_log_entry_code)
                    {
                        /*
                            The LogEntryViewActivity has returned
                            and the delete button was pressed.
                        */
                        int position = data.getIntExtra("position", 0);

                        logEntries.remove(position);

                        adapter.notifyDataSetChanged();
                        updateTotal();
                        saveInFile();
                    }
                }
                break;
            }
        }
    }

    private void loadFromFile() {
        /* From lonelyTwitter */
        try {
            FileInputStream fis = openFileInput(logfile);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();

            // Took from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html 01-19 2016
            Type listType = new TypeToken<ArrayList<LogEntry>>() {}.getType();
            logEntries = gson.fromJson(in, listType);
        }
        catch (FileNotFoundException e)
        {
            /* Nothing to do */
        }
    }

    private void saveInFile() {
        /* From lonelyTwitter */
        try {
            FileOutputStream fos = openFileOutput(logfile, 0);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(logEntries, out);
            out.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    private void updateTotal()
    {
        /* Tally up all the fuel purchases */
        float sum = 0;

        for(int i = 0;i < logEntries.size();i++)
        {
            sum += logEntries.get(i).getCost();
        }

        String text = "Total Fuel Expenses: $" + String.format("%.2f", sum);

        ((TextView) findViewById(R.id.master_sum)).setText(text);
    }
}
