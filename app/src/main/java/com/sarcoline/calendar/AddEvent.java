package com.sarcoline.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class AddEvent extends AppCompatActivity {

    private Event event;
    private LocalDateTime currentDate;
    private LocalDateTime givenDate;
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
            this.givenDate = LocalDateTime.of(b.getInt("year"), b.getInt("month"), b.getInt("dayOfMonth"), 0,0);
            this.currentDate = LocalDateTime.ofInstant(new Date(b.getLong("date")).toInstant(), ZoneId.systemDefault());
            if (!givenDate.toLocalDate().equals(currentDate.toLocalDate()))
                populateDate(givenDate);
            else
                populateDate(currentDate);
        }

        populateTime();
    }

    /****************************************************************
     * Create Event
     * Description: Takes the values stored in the event variable and
     *      serialized them as a json file.
     ***************************************************************/
    private void createEvent(View view)
    {
        if (authenticateEvent() != null)
            System.out.println("You're not supposed to be here!");
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
        EditText dateField = (EditText) findViewById(R.id.event_date_field);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String[] newDate = format.format(dateField.getText()).split("\\/");
        int[] dateInts = new int[3];

        if (newDate.length == 3) {
            for (int i = 0; i < 3; i++) {
                dateInts[i] = Integer.parseInt(newDate[i]);
                System.out.println(dateInts[i]);
            }
        }
        else if (newDate.length > 3 || newDate.length < 3)
            return null;

        return new Event();
    }
}