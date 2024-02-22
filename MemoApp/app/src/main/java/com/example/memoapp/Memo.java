package com.example.memoapp;

import java.util.Calendar;

public class Memo {
    private int memoID;
    private String subject;
    private String details;
    private Calendar date;
    private String priority;
    private int priority_level;

    public Memo() {
        memoID = -1;
        date = Calendar.getInstance();
    }

    public int getMemoID() {
        return memoID;
    }

    public void setMemoID(int memoID) {
        this.memoID = memoID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getPriority_level() {
        return priority_level;
    }

    public void setPriority_level(int priority_level) {
        this.priority_level = priority_level;
    }
}
