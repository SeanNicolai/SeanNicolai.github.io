package com.project.animalrescue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleManager {
    private final List<TimeSlot> timeSlots;
    private final int dailyQuota;

    public ScheduleManager(int dailyQuota) {
        this.dailyQuota = dailyQuota;
        this.timeSlots = new ArrayList<>();
        initializeTimeSlots();
    }

    private void initializeTimeSlots() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9); // Start at 9 AM
        calendar.set(Calendar.MINUTE, 0);
        for (int i = 0; i < dailyQuota; i++) {
            Date startTime = calendar.getTime();
            calendar.add(Calendar.MINUTE, 30); // Each slot is 30 minutes
            Date endTime = calendar.getTime();
            timeSlots.add(new TimeSlot(startTime, endTime));
        }
    }

    public TimeSlot getNextAvailableSlot() {
        for (TimeSlot slot : timeSlots) {
            if (slot.isAvailable()) {
                slot.setAvailable(false);
                return slot;
            }
        }
        return null;
    }

    public void resetTimeSlots() {
        for (TimeSlot slot : timeSlots) {
            slot.setAvailable(true);
        }
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }
}
