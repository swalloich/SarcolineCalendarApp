package com.sarcoline.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

public class AddEvent extends AppCompatActivity {

    private Event event;
    private LocalDateTime date;
    private Intent intent;

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
            //Date date = new Date (b.getLong("date"));
            //System.out.println("Date contains: " + date);
            this.date = LocalDateTime.of(b.getInt("year"), b.getInt("month"), b.getInt("dayOfMonth"), 0,0);
        }

        populateDate();
        populateTime();
    }

    /****************************************************************
     * Create Event
     * Description: Takes the values stored in the event variable and
     *      serialized them as a json file.
     ***************************************************************/
    private void createEvent(View view)
    {
        if (authenticateEvent())
            System.out.println("You're not supposed to be here!");
    }

    /****************************************************************
     * Populate Date
     * Description: Fills the date field in with the date that was
     *      selected when the view was started.
     ***************************************************************/
    private void populateDate()
    {
        EditText editText = (EditText) this.findViewById(R.id.event_date_field);
        editText.setText(date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    }

    /****************************************************************
     * Populate Time
     * Description: fills the time field with the closest 5 minute
     *      increment to the current time.
     ****************************************************************/
    private void populateTime()
    {
        EditText timeField = (EditText) this.findViewById(R.id.event_time_field);
        timeField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("h:mm a")));
    }

    /****************************************************************
     * Authenticate Event
     * Description: Grabs the values of the title, address, date, and
     *      time and ensures they are valid.
     * @return returns true or false depending on whether or not the
     *      values in the checked fields were valid.
     ***************************************************************/
    private boolean authenticateEvent()
    {
        return false;
    }
}