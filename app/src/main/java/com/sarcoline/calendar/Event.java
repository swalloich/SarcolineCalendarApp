package com.sarcoline.calendar;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Event {
    @SerializedName("title")
    String title;
    @SerializedName("group-id")
    int groupId;
    @SerializedName("date")
    Date Date;
    @SerializedName("address")
    String address;

}
