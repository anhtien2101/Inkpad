package com.example.activity.inkpad;

/**
 * Created by Tien on 03-Mar-16.
 */
public class Note {
    // name of table in database
    public static final String TABLE_NOTE = "Notes";
    // name of column in table Notes
    public static final String NOTE_NAME = "name";
    public static final String NOTE_CONTENT = "content";
    public static final String NOTE_TIME = "time";
    public static final String ID = "id";

    public String name;
    public String content;
    public String time;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
