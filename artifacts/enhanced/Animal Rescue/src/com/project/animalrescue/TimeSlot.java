package com.project.animalrescue;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeSlot {
    private Date startTime;
    private Date endTime;
    private boolean isAvailable;

    public TimeSlot(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = true;
    }

    public TimeSlot() {
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        return "TimeSlot [startTime=" + sdf.format(startTime) + ", endTime=" + sdf.format(endTime) + ", isAvailable=" + isAvailable + "]";
    }
}
