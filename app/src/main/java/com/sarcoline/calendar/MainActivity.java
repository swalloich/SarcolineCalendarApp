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

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    protected Date SELECTED_DATE = Calendar.getInstance().getTime();
    private Bundle calendarBundle;

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
                SELECTED_DATE = new Date(view.getDate());
                calendarBundle = new Bundle();
                calendarBundle.putInt("year", year);
                calendarBundle.putInt("month", month);
                calendarBundle.putInt("dayOfMonth", dayOfMonth);
            }
        });
    }

    public void createEvent(View view)
    {
        CalendarView calendarView = (CalendarView) findViewById(R.id.monthCal);
        calendarBundle.putLong("date", calendarView.getDate());
        Intent intent = new Intent(this, AddEvent.class);
        intent.putExtras(calendarBundle);
        startActivity(intent);
    }


}