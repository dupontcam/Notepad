package br.com.sistematizacao.notepad.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.sistematizacao.notepad.model.Note;

public class NoteDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "notedb";
    private static final String DATABASE_TABLE = "notestable";

    // columns name for database table
    private static final String KEY_ID = "ID";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "description";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";

    public NoteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // create table nametable(ID INT PRIMARY KEY, title TEXT, content TEXT, date TEXT, time TEXT);
        String query = "CREATE TABLE " + DATABASE_TABLE + " (" +
                KEY_ID + " LONG PRIMARY KEY AUTOINCREMENT, "+
                KEY_TITLE + " TEXT, " +
                KEY_CONTENT + " TEXT, " +
                KEY_DATE + " TEXT, " +
                KEY_TIME + " TEXT)";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion >= newVersion)
            return;
        String query = "DROP TABLE IF EXISTS " + DATABASE_TABLE;
        db.execSQL(query);
        onCreate(db);
    }

    public long addNote(Note note){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(KEY_TITLE, note.getTitle());
        c.put(KEY_CONTENT, note.getDescription());
        c.put(KEY_DATE, note.getDate());
        c.put(KEY_TIME, note.getTime());

        long ID = db.insert(DATABASE_TABLE, null, c);
        Log.d("database", "ID: " + ID);
        return ID;
    }

    public List<Note> getNotes(){

        SQLiteDatabase db = this.getReadableDatabase();
        List<Note> allNotes = new ArrayList<>();

        // select * from databaseName
        String query = "SELECT * FROM " + DATABASE_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do{
                Note note = new Note();
                note.setID(cursor.getLong(0));
                note.setTitle(cursor.getString(1));
                note.setDescription(cursor.getString(2));
                note.setDate(cursor.getString(3));
                note.setTime(cursor.getString(4));
                allNotes.add(note);
            } while (cursor.moveToNext());

        }
        return allNotes;
    }

    public void updateNote(Note note) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_CONTENT, note.getDescription());
        values.put(KEY_DATE, note.getDate());
        values.put(KEY_TIME, note.getTime());
        db.update(DATABASE_TABLE, values, KEY_ID + " = ?", new String[]{String.valueOf(note.getID())});
    }

    public void deleteNote(Long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}

