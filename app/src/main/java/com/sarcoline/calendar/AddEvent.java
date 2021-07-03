package com.sarcoline.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AddEvent extends AppCompatActivity {

    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Intent intent = getIntent();
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
     * @param activity The main activity.
     ***************************************************************/
    private void populateDate(Activity activity)
    {

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