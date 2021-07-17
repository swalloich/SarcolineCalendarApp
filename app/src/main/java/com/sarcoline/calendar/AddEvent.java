package com.sarcoline.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddEvent extends AppCompatActivity {

    private Event event;
    private LocalDateTime currentDate;
    private LocalDateTime givenDate;
    private Intent intent;

    /****************************************************************
     * Remove Whitespace
     * Description: Removes whitespaces from a string.
     * @param string The string that to remove spaces from.
     * @return The string with spaces removed.
     ***************************************************************/
    private String removeWhitespace(String string)
    {
        String newString = new String();
        for (int i = 0; i < string.length(); i++)
        {
            if (Character.isWhitespace(string.charAt(i)))
            {
                newString += '_';
            }
            else
            {
                newString += string.charAt(i);
            }
        }
        return newString;
    }

    /****************************************************************
     * Populate Date
     * Description: Fills the date field in with the date that was
     *      selected when the view was started.
     * @param dateTime A LocalDateTime object containing the date to
     *                 populate the event_date_field.
     ***************************************************************/
    private void populateDate(LocalDateTime dateTime)
    {
        EditText dateField = (EditText) this.findViewById(R.id.event_date_field);
        dateField.setText(dateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    }

    /****************************************************************
     * Populate Time
     * Description: fills the time field with the closest 5 minute
     *      increment to the current time.
     ****************************************************************/
    private void populateTime()
    {
        EditText timeField = (EditText) this.findViewById(R.id.event_time_field);
        timeField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("H:mm")));
    }

    /****************************************************************
     * Authenticate Event
     * Description: Grabs the values of the title, address, date, and
     *      time and ensures they are valid.
     * @return returns an event containing the contents of the fields
     *      if authentication passes. If it doesn't pass, it returns
     *      null.
     ***************************************************************/
    private Event authenticateEvent()
    {
        EditText titleField = (EditText) findViewById(R.id.event_title_field);
        EditText addressField = (EditText) findViewById(R.id.event_address_field);
        EditText dateField = (EditText) findViewById(R.id.event_date_field);
        EditText timeField = (EditText) findViewById(R.id.event_time_field);

        if (titleField.getText().toString() != "")
        {
            event.title = titleField.getText().toString();
            System.out.println("The event title is: " + event.title);
            System.out.println("The title variable in the event object is: " + event.title);
        }

        if (addressField.getText().toString() != "")
            event.address = titleField.getText().toString();

        try
        {

            Date eventDate = new SimpleDateFormat("MM/dd/yyyy HH:mm").parse(dateField.getText().toString() + ' ' + timeField.getText().toString());
            event.date = eventDate;
        }
        catch (ParseException e)
        {
            System.out.println("Error parsing values in date field: " + e.getLocalizedMessage());
        }

        return event;
    }

    public AddEvent()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        this.intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null)
        {
            this.givenDate = LocalDateTime.of(b.getInt("year"), b.getInt("month"), b.getInt("dayOfMonth"), 0,0);
            this.currentDate = LocalDateTime.now();
            if (!givenDate.toLocalDate().equals(currentDate.toLocalDate()))
                populateDate(givenDate);
            else
                populateDate(currentDate);
        }

        event = new Event();

        populateTime();
    }

    /****************************************************************
     * Create Event
     * Description: Takes the values stored in the event variable and
     *      serialized them as a json file.
     ***************************************************************/
    public void createEvent(View view)
    {
        final String fileSeparator = System.getProperty("file.separator");
        String directory = fileSeparator + getFilesDir() + fileSeparator + givenDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

        if (authenticateEvent() != null)
        {
            System.out.println("authenticated");
            File path = new File(directory);
            Gson gson = new Gson();
            String eventJson = gson.toJson(event);
            try
            {
                int nameIteration = 1;

                if (!path.exists())
                    path.mkdir();

                String file = fileSeparator + removeWhitespace(event.title) + ".json";
                path = new File(directory + file);

                while (path.exists())
                {
                    file = fileSeparator + removeWhitespace(event.title) + ++nameIteration + ".json";
                    path = new File(directory + file);
                }

                FileWriter writer = new FileWriter(path);
                writer.write(eventJson);
                writer.close();
                System.out.println("file written to: " + path.toString());
                finish();
            } catch (IOException e)
            {
                System.out.println("File could not be written. Error: " + e.getMessage());
            }
        }
    }
}