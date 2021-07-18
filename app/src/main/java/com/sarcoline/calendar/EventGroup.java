package com.sarcoline.calendar;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class EventGroup
{
    @SerializedName("group-name")
    private String name;
    @SerializedName("group-id")
    private int id;
    private Context context;

    public EventGroup(String name, int id, Context context)
    {
        setName(name);
        setId(id);
        this.context = context;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setId(int id)
    {
        this.id = checkId(id);
    }

    public int getID()
    {
        return this.id;
    }

    public ArrayList<EventGroup> getGroups(Context context)
    {
        File groupsFile = new File(System.getProperty("file.separator")
                + context.getFilesDir() + System.getProperty("file.separator") +
                "groups.json");
        String contents = new String();
        Gson gson = new Gson();
        ArrayList<EventGroup> groups = new ArrayList<>();
        try
        {
            Scanner groupScanner = new Scanner(groupsFile);
            while (groupScanner.hasNextLine())
                contents += groupScanner.next();
            groups = gson.fromJson(contents, new TypeToken<ArrayList<EventGroup>>(){}.getType());
        } catch (IOException e)
        {
            System.out.println("Error when searching for groups file: " +
                    e.getLocalizedMessage());
            return null;
        }
        return groups;
    }

    private int checkId(int id)
    {
        ArrayList<EventGroup> groups = getGroups(context);
        for (EventGroup group : groups)
        {
            if (group.getID() == id)
                id++;
        }
        return id;
    }
}
