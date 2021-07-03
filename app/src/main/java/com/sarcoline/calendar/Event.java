package com.sarcoline.calendar;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Event {
    @SerializedName("title")
    public String title;
    @SerializedName("group-id")
    public int groupId;
    @SerializedName("date")
    public Date Date;
    @SerializedName("address")
    public String address;

}
