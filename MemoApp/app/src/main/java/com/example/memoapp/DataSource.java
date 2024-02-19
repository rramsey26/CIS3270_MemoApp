package com.example.memoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class DataSource {
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public DataSource(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertMemo(Memo m) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("subject", m.getSubject());
            initialValues.put("details", m.getDetails());
            initialValues.put("priority", m.getPriority());
            initialValues.put("priority_level", m.getPriority_level());
            initialValues.put("date", String.valueOf(m.getDate().getTimeInMillis()));

            didSucceed = database.insert("memo", null, initialValues) > 0;
        }
        catch (Exception e) {
            //Do nothing - will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean updateMemo(Memo m) {
        boolean didSucceed = false;
        try {
            Long rowID = (long) m.getMemoID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("subject", m.getSubject());
            updateValues.put("details", m.getDetails());
            updateValues.put("priority", m.getPriority());
            updateValues.put("priority_level", m.getPriority_level());
            updateValues.put("date", String.valueOf(m.getDate().getTimeInMillis()));

            didSucceed = database.update("memo", updateValues, "_id=" + rowID, null) > 0;
        }
        catch (Exception e) {
            //Do nothing - will return false if there is an exception
        }
        return didSucceed;
    }

    public int getLastMemoID() {
        int lastID;
        try {
            String query = "Select MAX(_id) from memo";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastID = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e) {
            lastID = -1;
        }
        return lastID;
    }

    public ArrayList<String> getMemoSubject() {
        ArrayList<String> memoSubjects = new ArrayList<>();
        try{
            String query = "Select subject from memo";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                memoSubjects.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            memoSubjects = new ArrayList<String>();
        }
        return memoSubjects;
    }

    public ArrayList<Memo> getMemos(String sortField, String sortOrder) {
        ArrayList<Memo> memos = new ArrayList<Memo>();
        try {
            String query = "SELECT * FROM memo ORDER BY " + sortField + " " + sortOrder;
            Cursor cursor = database.rawQuery(query, null);

            Memo newMemo;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newMemo = new Memo();
                newMemo.setMemoID(cursor.getInt(0));
                newMemo.setSubject(cursor.getString(1));
                newMemo.setDetails(cursor.getString(2));
                newMemo.setPriority(cursor.getString(3));
                newMemo.setPriority_level(cursor.getInt(4));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(5)));
                newMemo.setDate(calendar);

                memos.add(newMemo);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            memos = new ArrayList<Memo>();
        }
        return memos;
    }

    public Memo getSpecificMemo(int memoID) {
        Memo memo = new Memo();
        String query = "SELECT * FROM memo WHERE _id =" + memoID;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            memo.setMemoID(cursor.getInt(0));
            memo.setSubject(cursor.getString(1));
            memo.setDetails(cursor.getString(2));
            memo.setPriority(cursor.getString(3));
            memo.setPriority_level(cursor.getInt(4));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(cursor.getString(5)));
            memo.setDate(calendar);

            cursor.close();
        }
        return memo;
    }

    public  boolean deleteMemo(int memoID) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("memo", "_id=" + memoID, null) > 0;
        }
        catch (Exception e) {
            // Do nothing -return value already set to false
        }
        return didDelete;
    }
}
