package com.example.risticpe.risticpe_fueltrack;

import android.graphics.Color;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by dan on 24/01/16.
 */
/*
    The model class for log entries. Contains a field for each piece of data as well as
    a color for the UI and a Random for selecting the color randomly. Other than that
    it is all getters and setters. Uses a Calendar for keeping track of the date.
 */

public class LogEntry
{
    private Calendar calendar;
    private String station, fuel_grade;
    private float odometer_reading, litres_of_fuel, cents_per_litre;
    private int color;

    /* For some variety */
    private final static String[] colors =
            {
                    /* Colors courtesy of http://cloford.com/resources/colours/500col.htm */
                    "#00FF7F",
                    "#00EE00",
                    "#CDCD00",
                    "#EE9A00",
                    "#CD3700",
                    "#FF0000",
                    "#808080",
                    "#9370DB",
                    "#C67171",
                    "#FF6347",
                    "#FF6103",
                    "#FFD700",
                    "#9ACD32",
                    "#00EE76",
                    "#7FFF00",
                    "#836FFF",
                    "#4876FF",
                    "#7FFFD4",
                    "#FFFF00",
                    "#CDC8B1",
                    "#D6D6D6"
            };

    public LogEntry()
    {
        /* Randomly select a color for the listview element */
        Random random = new Random(System.currentTimeMillis());
        color = Color.parseColor(colors[random.nextInt(colors.length)]);
        this.calendar = Calendar.getInstance();
    }

    public int getColor()
    {
        return color;
    }

    public float getCost()
    {
        return litres_of_fuel * (cents_per_litre/100f);
    }

    public String getDate()
    {
        /* Returns a string for convenience rather than the Calendar itself. */
        return Integer.toString(calendar.get(Calendar.YEAR)) + "-" +
                Integer.toString(calendar.get(Calendar.MONTH)) + "-" +
                Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void setCalendar(int year, int month, int day)
    {
        calendar.set(year, month, day);
    }

    public String getStation()
    {
        return station;
    }

    public void setStation(String station)
    {
        this.station = station;
    }

    public String getFuelgrade()
    {
        return fuel_grade;
    }

    public void setFuelGrade(String fuel_grade)
    {
        this.fuel_grade = fuel_grade;
    }

    public float getOdometerReading()
    {
        return odometer_reading;
    }

    public void setOdometerReading(float odometer_reading)
    {
        this.odometer_reading = odometer_reading;
    }

    public float getLitresOfFuel()
    {
        return litres_of_fuel;
    }

    public void setLitresOfFuel(float litres_of_fuel)
    {
        this.litres_of_fuel = litres_of_fuel;
    }

    public float getCentsPerLitre()
    {
        return cents_per_litre;
    }

    public void setCentsPerLitre(float cents_per_litre)
    {
        this.cents_per_litre = cents_per_litre;
    }
}
