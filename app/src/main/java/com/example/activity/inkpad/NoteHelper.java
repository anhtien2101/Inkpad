package com.example.activity.inkpad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tien on 04-Mar-16.
 */
public class NoteHelper {
    DBHelper dbHelper;

    // constructor, create a dbhelper
    NoteHelper(Context context) {
        dbHelper = new DBHelper(context);
    }
//method insert the note into table
    public long insert (Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Note.NOTE_NAME, note.getName());
        contentValues.put(Note.NOTE_CONTENT, note.getContent());
        contentValues.put(Note.NOTE_TIME, note.getTime());

        long id = db.insert(Note.TABLE_NOTE, null, contentValues);
        return id;
    }

    // get a note in a line database by cursor
    public Note fetch(Cursor cursor) {
        // create new note and set its value
        Note note = new Note();
        note.setName(cursor.getString(cursor.getColumnIndex(Note.NOTE_NAME)));
        note.setContent(cursor.getString(cursor.getColumnIndex(Note.NOTE_CONTENT)));
        note.setTime(cursor.getString(cursor.getColumnIndex(Note.NOTE_TIME)));
        note.setId(cursor.getInt(cursor.getColumnIndex(Note.ID)));
        return note;
    }

    // get all element note in database
    public List<Note> getAll() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(Note.TABLE_NOTE, null, null, null, null, null, null);
        // create new list of Note
        List<Note> notes = new ArrayList<>();
        // check if cursor != null
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    // add a note get in a line into list of note
                    notes.add(fetch(cursor));
                } while (cursor.moveToNext());
            }
        }
        return notes; // return list note
    }
 // delete a note in databse by id
    public void delete(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Note.TABLE_NOTE, Note.ID + "=" + id, null);
        db.close();
    }

    public int update(Note n) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Note.ID, n.getId());
        contentValues.put(Note.NOTE_NAME, n.getName());
        contentValues.put(Note.NOTE_CONTENT, n.getContent());
        contentValues.put(Note.NOTE_TIME, n.getTime());
        int i = db.update(Note.TABLE_NOTE, contentValues, Note.ID + "=" + n.id, null);
        return i;
    }
}
