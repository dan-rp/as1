package com.example.risticpe.risticpe_fueltrack;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

/**
 * Created by dan on 31/01/16.
 */
/*
    A tester for the model class (LogEntry). The only methods in LogEntry that do any work
    are getDate() and getCost(). The rest are just direct getters and setters for class variables;
    As such, there wasn't a huge amount of stuff to test on the model.
 */


public class LogEntryTester extends ActivityInstrumentationTestCase2<FuelTrackActivity>
{
    public LogEntryTester()
    {
        super(FuelTrackActivity.class);
    }

    public void testSetCalendar()
    {
        LogEntry logEntry = new LogEntry();

        logEntry.setCalendar(1999, 6, 1);

        /* The expected format of the return string is "1999-6-1" */

        String[] dateComponents = logEntry.getDate().split("-");

        assertEquals("1999", dateComponents[0]);
        assertEquals("6", dateComponents[1]);
        assertEquals("1", dateComponents[2]);
    }

    public void testCostCalculation()
    {
        LogEntry logEntry = new LogEntry();

        float cents_per_litre = 71.9f;
        float litres = 148.453f;

        logEntry.setCentsPerLitre(cents_per_litre);
        logEntry.setLitresOfFuel(litres);

        assertEquals((cents_per_litre/100)*litres, logEntry.getCost(), 0);
    }
}
