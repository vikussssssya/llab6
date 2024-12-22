package com.example.myapplication6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public class MyDbHelper extends SQLiteOpenHelper {

    public MyDbHelper(@Nullable Context context) {
        super(context, MyConst.DATABASE_NAME, null, MyConst.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MyConst.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MyConst.SQL_DROP_TABLE);
        onCreate(db);
    }

    // Вставка напоминания
    public void insertReminder(Reminder reminder) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(MyConst.TITLE, reminder.getTitle());
            values.put(MyConst.DESCRIPTION, reminder.getDescription());
            values.put(MyConst.DATE, reminder.getDatetime());
            db.insert(MyConst.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Получение всех напоминаний
    public List<Reminder> getAllReminders() {
        List<Reminder> reminderList = new ArrayList<>();
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT " + MyConst.ID + ", " + MyConst.TITLE + ", " +
                     MyConst.DESCRIPTION + ", " + MyConst.DATE + " FROM " + MyConst.TABLE_NAME, null)) {
            if (cursor.moveToFirst()) {
                do {
                    reminderList.add(new Reminder(
                            cursor.getInt(cursor.getColumnIndex(MyConst.ID)),
                            cursor.getString(cursor.getColumnIndex(MyConst.TITLE)),
                            cursor.getString(cursor.getColumnIndex(MyConst.DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndex(MyConst.DATE))
                    ));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reminderList;
    }

    // Удаление напоминания
    public void deleteReminder(int id) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.delete(MyConst.TABLE_NAME, MyConst.ID + " = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
