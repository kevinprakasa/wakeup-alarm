package com.kp.wakeup.Room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alarms")
public class Alarm {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private long time;

    private int snooze;

    private String ringtone;

    private int day;

    private String label;

    public Alarm(long time, int snooze, String ringtone, int day, String label) {
        this.time = time;
        this.snooze = snooze;
        this.ringtone = ringtone;
        this.day = day;
        this.label = label;
    }

    public long getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    public int getSnooze() {
        return snooze;
    }

    public String getRingtone() {
        return ringtone;
    }

    public int getDay() {
        return day;
    }

    public String getLabel() {
        return label;
    }

    public void setId(int id) {
        this.id = id;
    }
}
