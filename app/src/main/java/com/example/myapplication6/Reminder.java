package com.example.myapplication6;

public class Reminder {
    private int id;
    private String title;
    private String description;
    private String  datetime;

    public Reminder(int id, String title, String description, String datetime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.datetime = datetime;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String  getDatetime() {
        return datetime;
    }
}

