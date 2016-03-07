package com.example.activity.inkpad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tien on 03-Mar-16.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "note.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_table = "CREATE TABLE " + Note.TABLE_NOTE + "( " +
                Note.ID + " integer primary key, " +
                Note.NOTE_NAME + " text, " +
                Note.NOTE_CONTENT + " text, " +
                Note.NOTE_TIME + " text)";
        sqLiteDatabase.execSQL(create_table);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
