package com.example.risticpe.risticpe_fueltrack;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.InputFilter;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

/*
    This is the view that handles creating new or editing existing log entries.
    It has fields for all the different bits of data, as well as a save and a cancel button.
 */

public class NewLogEntryActivity extends Activity
{
    private final static int edit_log_entry_code = 3;
    private final static int edit_canceled_code = 4;
    private int listposition;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_log_entry);

        /* These length filters keep the inputs to the required number of decimal places. */
        ((EditText) findViewById(R.id.odo_edit_left)).setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        ((EditText) findViewById(R.id.odo_edit_right)).setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});

        ((EditText) findViewById(R.id.litres_edit_left)).setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
        ((EditText) findViewById(R.id.litres_edit_right)).setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});

        ((EditText) findViewById(R.id.cents_edit_left)).setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
        ((EditText) findViewById(R.id.cents_edit_right)).setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});

        Intent intent = getIntent();


        /*
            If the user is coming from the LogEntryViewActivity, then it means they are editing
            an existing entry. The fields will be populated with the existing data, which they
            will then be able to see while modifying.
         */
        if(intent.getIntExtra("request_type", 0) == edit_log_entry_code)
        {
            /* Just extracting the data and stuffing it into the views here. */

            listposition = intent.getIntExtra("position", 0);
            String[] date_info = intent.getStringExtra("date").split("-");
            String[] odo = parseAndSplitFloat(intent.getStringExtra("odo"));
            String[] litres = parseAndSplitFloat(intent.getStringExtra("litres"));
            String[] cents = parseAndSplitFloat(intent.getStringExtra("cents"));

            ((DatePicker) findViewById(R.id.datePicker)).updateDate(
                    Integer.parseInt(date_info[0]), Integer.parseInt(date_info[1]), Integer.parseInt(date_info[2]));

            ((EditText) findViewById(R.id.station_name)).setText(intent.getStringExtra("station"));
            ((EditText) findViewById(R.id.fuel_grade)).setText(intent.getStringExtra("grade"));
            ((EditText) findViewById(R.id.odo_edit_left)).setText(odo[0]);
            ((EditText) findViewById(R.id.odo_edit_right)).setText(odo[1]);
            ((EditText) findViewById(R.id.litres_edit_left)).setText(litres[0]);
            ((EditText) findViewById(R.id.litres_edit_right)).setText(litres[1]);
            ((EditText) findViewById(R.id.cents_edit_left)).setText(cents[0]);
            ((EditText) findViewById(R.id.cents_edit_right)).setText(cents[1]);
        }
    }

    public void cancel(View view)
    {
        /* Cancel button was pressed. Nothing will be saved. */
        Intent returnedIntent = new Intent();

        returnedIntent.putExtra("request_type", edit_canceled_code);

        setResult(Activity.RESULT_OK, returnedIntent);

        finish();
    }

    public void save(View view)
    {
        /* This function consists mainly of string parsing from the fields and setting
         * default values for any that are left blank. */
        /******************************************************************************************/
        /* date, station, and fuel grade value parsing */

        int day = ((DatePicker) findViewById(R.id.datePicker)).getDayOfMonth();
        int month = ((DatePicker) findViewById(R.id.datePicker)).getMonth();
        int year = ((DatePicker) findViewById(R.id.datePicker)).getYear();

        String station = ((EditText) findViewById(R.id.station_name)).getText().toString();
        if(station.matches(""))
        {
            station = "some gas station";
        }
        String grade = ((EditText) findViewById(R.id.fuel_grade)).getText().toString();
        if(grade.matches(""))
        {
            grade = "regular";
        }

        /******************************************************************************************/
        /* odometer, litres purchased, and cents per litre value parsing */

        /* Retrieve odometer reading data and check for empty field */
        float odo;

        /* Integer part */
        if(((EditText) findViewById(R.id.odo_edit_left)).getText().toString().matches(""))
        {
            odo = 0;
        }
        else
        {
            odo = Float.parseFloat(((EditText) findViewById(R.id.odo_edit_left)).getText().toString());
        }

        /* Fractional part */
        if(!((EditText) findViewById(R.id.odo_edit_right)).getText().toString().matches(""))
        {
            odo += Float.parseFloat("0." + ((EditText) findViewById(R.id.odo_edit_right)).getText().toString());
        }

        /* Retrieve purchased litres data and check for empty field */
        float litres;

        if(((EditText) findViewById(R.id.litres_edit_left)).getText().toString().matches(""))
        {
            litres = 0;
        }
        else
        {
            litres = Float.parseFloat(((EditText) findViewById(R.id.litres_edit_left)).getText().toString());
        }

        if(!((EditText) findViewById(R.id.litres_edit_right)).getText().toString().matches(""))
        {
            litres += Float.parseFloat("0." + ((EditText) findViewById(R.id.litres_edit_right)).getText().toString());
        }

        /* Retrieve cents per litre data and check for empty field */
        float cents;

        if(((EditText) findViewById(R.id.cents_edit_left)).getText().toString().matches(""))
        {
            cents = 0;
        }
        else
        {
            cents = Float.parseFloat(((EditText) findViewById(R.id.cents_edit_left)).getText().toString());
        }

        if(!((EditText) findViewById(R.id.cents_edit_right)).getText().toString().matches(""))
        {
            cents += Float.parseFloat("0." + ((EditText) findViewById(R.id.cents_edit_right)).getText().toString());
        }
        /******************************************************************************************/

        Intent returnedIntent = new Intent();

        returnedIntent.putExtra("day", day);
        returnedIntent.putExtra("month", month);
        returnedIntent.putExtra("year", year);
        returnedIntent.putExtra("station", station);
        returnedIntent.putExtra("grade", grade);
        returnedIntent.putExtra("odo", odo);
        returnedIntent.putExtra("litres", litres);
        returnedIntent.putExtra("cents", cents);

        returnedIntent.putExtra("request_type", getIntent().getIntExtra("request_type", 0));

        if(getIntent().getIntExtra("request_type", 0) == edit_log_entry_code)
        {
            /* If the entry already existed, the data will be placed in it rather than a
             * new one. */
            returnedIntent.putExtra("position", listposition);
        }

        setResult(Activity.RESULT_OK, returnedIntent);

        finish();
    }

    private String[] parseAndSplitFloat(String s)
    {
        /* Just some parsing functionality. May not be entirely necessary. */
        String[] test = s.split("\\.");

        if(test.length == 0)
        {
            return new String[]{"0", "0"};
        }
        else if(test.length == 1)
        {
            return new String[]{test[0], "0"};
        }
        else
        {
            return new String[]{test[0], test[1]};
        }
    }

}
