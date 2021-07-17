package com.sarcoline.calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CalendarView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    protected LocalDateTime CURRENT_DATE;
    private Bundle calendarBundle;
    private RecyclerView eventRecycler;
    private RecyclerView.Adapter eRecyclerAdapter;
    private RecyclerView.LayoutManager eRecyclerManager;

    public MainActivity()
    {
        calendarBundle = new Bundle();
    }

    private void verifyBundle()
    {
        calendarBundle.putLong("date", CURRENT_DATE.getLong(ChronoField.EPOCH_DAY));
        if (calendarBundle.getInt("year") == 0)
            calendarBundle.putInt("year", CURRENT_DATE.getYear());
        if (calendarBundle.getInt("month") == 0)
            calendarBundle.putInt("month", CURRENT_DATE.getMonthValue());
        if (calendarBundle.getInt("dayOfMonth") == 0)
            calendarBundle.putInt("dayOfMonth", CURRENT_DATE.getDayOfMonth());
    }

    /****************************************************************
     * Make Event
     * Description: created an event object from the file given.
     * @param file The file object containing the path to a .json
     *             file for the event.
     * @return an event object with it's values set to those found in
     *      the json file specified.
     ***************************************************************/
    private Event makeEvent(File file) throws InvalidParameterException
    {
        //ensure the file we're given is a valid file for this purpose.
        if (!file.toString().endsWith(".json"))
            throw new InvalidParameterException("File object passed to" +
                    "makeEvent method in MainActivity.java does not end in .json.");

        //get contents of the given file and store them as a string.
        String contents = new String();
        try
        {
            Scanner jsonScanner = new Scanner(file);
            while (jsonScanner != null && jsonScanner.hasNextLine())
            {
                contents += jsonScanner.nextLine();
            }
            jsonScanner.close();
        } catch (IOException e)
        {
            System.out.println("Error occurred while reading event file \""
                + file.toString() + "\":" + e.getLocalizedMessage());
            return null;
        }

        //populate the variables in this file with the ones taken
        //from the json file.
        Gson gson = new Gson();
        Event e = gson.fromJson(contents, Event.class);

        return e;
    }

    /****************************************************************
     * Get Events
     * Description: Fills an array list with event objects in the
     *      specified directory.
     * @param directory The directory containing the .json files for
     *                  the events.
     * @return An array list of event objects found in the directory.
     ***************************************************************/
    private ArrayList<Event> getEvents(File directory)
    {
        System.out.println("requested directory is: " + directory.toString());
        File [] files = directory.listFiles(new FileFilter()
        {
            @Override
            public boolean accept(File pathname)
            {
                System.out.println("DEBUG current file: " + pathname.toString());
                if (pathname.toString().endsWith(".json"))
                    return true;
                else
                    return false;
            }
        });

        ArrayList<Event> events = new ArrayList<>();
        if (files != null)
        {
            for (File file : files)
            {
                Event e = makeEvent(file);
                if (e != null)
                    events.add(makeEvent(file));
            }
        }
        return events;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CURRENT_DATE = LocalDateTime.now();

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        Menu m1 = navView.getMenu();
        m1.add(R.id.navGroup1, 1, 100, "Month");
        m1.add(R.id.navGroup1, 2, 200, "Week");

        final String fileSeparator = System.getProperty("file.separator");
        File todayDir = new File(fileSeparator + getFilesDir()
                + fileSeparator + CURRENT_DATE.format(DateTimeFormatter.ISO_LOCAL_DATE)
                + fileSeparator);

        eventRecycler = (RecyclerView) findViewById(R.id.events_recycler);
        eventRecycler.setHasFixedSize(true);
        eRecyclerManager = new LinearLayoutManager(this);
        eRecyclerAdapter = new EventAdapter(getEvents(todayDir));
        eventRecycler.setLayoutManager(eRecyclerManager);
        eventRecycler.setAdapter(eRecyclerAdapter);

        CalendarView cal = (CalendarView) findViewById(R.id.monthCal);
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                final String fileSeparator = System.getProperty("file.separator");
                calendarBundle.putInt("year", year);
                calendarBundle.putInt("month", month);
                calendarBundle.putInt("dayOfMonth", dayOfMonth);

                LocalDateTime selectedDate = LocalDateTime.of(year,month + 1,dayOfMonth,0,0);
                File currentDir = new File(fileSeparator + getFilesDir()
                        + fileSeparator + selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
                        + fileSeparator);
                System.out.println("selectedDate: " + selectedDate.format(DateTimeFormatter.ofPattern("LLL dd, yyyy")));
                System.out.println("checked directory has been updated to: " + currentDir.toString());
                eRecyclerAdapter = new EventAdapter(getEvents(currentDir));
                eventRecycler.setAdapter(eRecyclerAdapter);
            }
        });
    }

    public void createEvent(View view)
    {
        Intent intent = new Intent(this, AddEvent.class);
        verifyBundle();
        intent.putExtras(calendarBundle);
        startActivity(intent);
    }
}