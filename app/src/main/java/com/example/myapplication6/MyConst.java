package com.example.myapplication6;

public class MyConst {
    // Основные настройки базы данных
    public static final String DATABASE_NAME = "Reminder.db";
    public static final int VERSION = 1;

    // Имя таблицы и столбцы
    public static final String TABLE_NAME = "reminders";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String DATE = "datetime";

    // SQL-запрос для создания таблицы
    public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TITLE + " TEXT NOT NULL, " +
            DESCRIPTION + " TEXT, " +
            DATE + " TEXT NOT NULL)";

    // SQL-запрос для удаления таблицы
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}

