package com.sarcoline.calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>
{
    private ArrayList<Event> eventList;
    private LocalDateTime localEventTime;

    public static class EventViewHolder extends RecyclerView.ViewHolder
    {
        public TextView timeView;
        public TextView titleView;
        public TextView addressView;

        public EventViewHolder(View itemView)
        {
            super(itemView);
            timeView = (TextView) itemView.findViewById(R.id.event_summary_time);
            titleView = (TextView) itemView.findViewById(R.id.event_summary_title);
            addressView = (TextView) itemView.findViewById(R.id.event_summary_address);
        }
    }

    public EventAdapter(ArrayList<Event> eventList)
    {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_summary, parent, false);
        EventViewHolder eventViewHolder = new EventViewHolder(v);
        return eventViewHolder;
    }

    @Override
    public void onBindViewHolder(EventAdapter.EventViewHolder holder, int position)
    {
        Event currentItem = eventList.get(position);
        holder.addressView.setText(currentItem.address);
        holder.titleView.setText(currentItem.title);

        localEventTime = LocalDateTime.ofInstant(currentItem.date.toInstant(), ZoneId.systemDefault());
        holder.timeView.setText(localEventTime.format(DateTimeFormatter.ofPattern("HH:mm")));
    }

    @Override
    public int getItemCount()
    {
        return eventList.size();
    }
}
