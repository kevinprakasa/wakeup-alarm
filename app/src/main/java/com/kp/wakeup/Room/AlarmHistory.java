package com.kp.wakeup.Room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alarmhistories")
public class AlarmHistory {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private long time;

    private String status;

    public AlarmHistory(long time, String status) {
        this.time = time;
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}
