package com.sarcoline.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    protected final Date SELECTED_DATE = Calendar.getInstance().getTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        Menu m1 = navView.getMenu();
        m1.add(R.id.navGroup1, 1, 100, "Month");
        m1.add(R.id.navGroup1, 2, 200, "Week");
    }

    public void createEvent(View view)
    {
        Intent intent = new Intent(this, AddEvent.class);
        startActivity(intent);
    }
}