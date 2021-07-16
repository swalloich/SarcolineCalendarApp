package com.sarcoline.calendar;

import com.google.gson.annotations.SerializedName;


import java.util.Calendar;
import java.util.Date;

public class Event {
    @SerializedName("title")
    public String title;
    @SerializedName("group-id")
    public int groupId;
    @SerializedName("date")
    public Date date;
    @SerializedName("address")
    public String address;

    public Event()
    {
        title = "Event";
        groupId = 1;
        date = Calendar.getInstance().getTime();
        address = "Not set";
    }

}
