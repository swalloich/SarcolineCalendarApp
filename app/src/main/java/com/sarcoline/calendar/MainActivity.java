package com.sarcoline.calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.PopupMenu;

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

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    protected LocalDateTime CURRENT_DATE;
    private Bundle calendarBundle;
    private RecyclerView eventRecycler;
    private RecyclerView.Adapter eRecyclerAdapter;
    private RecyclerView.LayoutManager eRecyclerManager;
    private File dateDirectory;
    private LocalDateTime selectedDate;

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
        return sortEvents(events);
    }

    private ArrayList<Event> sortEvents(ArrayList<Event> events)
    {
        ArrayList<Event> sortedEvents = new ArrayList<>();
        Event earliestEvent;
        while (events.size() > 0)
        {
            earliestEvent = events.get(0);
            for (Event currentEvent : events)
            {
                if (!earliestEvent.equals(currentEvent) && currentEvent.isBefore(earliestEvent))
                    earliestEvent = currentEvent;
            }
            sortedEvents.add(earliestEvent);
            events.remove(earliestEvent);
        }
        return sortedEvents;
    }

    private void updateRecycler(File newDir)
    {
        eRecyclerAdapter = new EventAdapter(getEvents(newDir));
        eventRecycler.setAdapter(eRecyclerAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CURRENT_DATE = LocalDateTime.now();
        selectedDate = CURRENT_DATE;

        final String fileSeparator = System.getProperty("file.separator");
        dateDirectory = new File(fileSeparator + getFilesDir()
                + fileSeparator + CURRENT_DATE.format(DateTimeFormatter.ISO_LOCAL_DATE)
                + fileSeparator);

        eventRecycler = (RecyclerView) findViewById(R.id.events_recycler);
        eventRecycler.setHasFixedSize(true);
        eRecyclerManager = new LinearLayoutManager(this);
        eventRecycler.setLayoutManager(eRecyclerManager);
        updateRecycler(dateDirectory);

        CalendarView cal = (CalendarView) findViewById(R.id.monthCal);
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                final String fileSeparator = System.getProperty("file.separator");
                calendarBundle.putInt("year", year);
                calendarBundle.putInt("month", month + 1);
                calendarBundle.putInt("dayOfMonth", dayOfMonth);

                //for debugging
                selectedDate = LocalDateTime.of(year,month,dayOfMonth,0,0);
                System.out.println(selectedDate.format(DateTimeFormatter.ofPattern("LLL dd uuuu")));

                LocalDateTime selectedDate = LocalDateTime.of(year,month + 1,dayOfMonth,0,0);
                dateDirectory = new File(fileSeparator + getFilesDir()
                        + fileSeparator + selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
                        + fileSeparator);
                updateRecycler(dateDirectory);
            }
        });
    }

    public void addButton(View view)
    {
        PopupMenu addMenu = new PopupMenu(this, view);
        MenuInflater inflater = addMenu.getMenuInflater();
        inflater.inflate(R.menu.add_menu, addMenu.getMenu());
        addMenu.setOnMenuItemClickListener(this);
        addMenu.show();
    }

    private void createEvent()
    {
        Intent intent = new Intent(this, AddEvent.class);
        verifyBundle();
        intent.putExtras(calendarBundle);
        startActivity(intent);
    }

    private void createGroup()
    {
        Intent intent = new Intent(this, CreateGroup.class);
        startActivity(intent);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        updateRecycler(dateDirectory);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.new_event_option:
                createEvent();
                return true;
            case R.id.new_group_option:
                createGroup();
                return true;
            default:
                return false;
        }
    }
}