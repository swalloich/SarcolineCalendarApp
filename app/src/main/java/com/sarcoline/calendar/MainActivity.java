package com.sarcoline.calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    protected LocalDateTime CURRENT_DATE;
    private Bundle calendarBundle;

    public MainActivity()
    {
        CURRENT_DATE = LocalDateTime.now();
        calendarBundle = new Bundle();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        Menu m1 = navView.getMenu();
        m1.add(R.id.navGroup1, 1, 100, "Month");
        m1.add(R.id.navGroup1, 2, 200, "Week");

        CalendarView cal = (CalendarView) findViewById(R.id.monthCal);
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendarBundle.putInt("year", year);
                calendarBundle.putInt("month", month);
                calendarBundle.putInt("dayOfMonth", dayOfMonth);
            }
        });
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

    public void createEvent(View view)
    {
        Intent intent = new Intent(this, AddEvent.class);
        verifyBundle();
        intent.putExtras(calendarBundle);
        startActivity(intent);
    }


}