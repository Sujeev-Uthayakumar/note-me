package com.sujeevuthayakumar.noteme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String COLUMN_NOTE = "NOTE";
    public static final String NOTE_TABLE = COLUMN_NOTE + "_TABLE";
    public static final String COLUMN_NOTE_IMAGE = COLUMN_NOTE + "_IMAGE";
    public static final String COLUMN_NOTE_TITLE = COLUMN_NOTE + "_TITLE";
    public static final String COLUMN_NOTE_SUBTITLE = COLUMN_NOTE + "_SUBTITLE";
    public static final String COLUMN_NOTE_COLOR = "NOTE_COLOR";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "note.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + NOTE_TABLE
                + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NOTE_TITLE + " TEXT, "
                + COLUMN_NOTE_SUBTITLE + " TEXT, "
                + COLUMN_NOTE + " TEXT, "
                + COLUMN_NOTE_IMAGE + " BLOB, "
                + COLUMN_NOTE_COLOR + " TEXT)";

        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(NoteModel noteModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NOTE_TITLE, noteModel.getTitle());
        cv.put(COLUMN_NOTE_SUBTITLE, noteModel.getSubTitle());
        cv.put(COLUMN_NOTE, noteModel.getNote());
        cv.put(COLUMN_NOTE_COLOR, noteModel.getNoteColor());

        if (noteModel.getImage() != null) {
            cv.put(COLUMN_NOTE_IMAGE, noteModel.getImage());
        }

        long insert = db.insert(NOTE_TABLE, null, cv);

        return insert != -1;
    }

    public boolean updateOne(NoteModel noteModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NOTE_TITLE, noteModel.getTitle());
        cv.put(COLUMN_NOTE_SUBTITLE, noteModel.getSubTitle());
        cv.put(COLUMN_NOTE, noteModel.getNote());
        cv.put(COLUMN_NOTE_COLOR, noteModel.getNoteColor());

        if (noteModel.getImage() != null) {
            cv.put(COLUMN_NOTE_IMAGE, noteModel.getImage());
        }

        int update = db.update(NOTE_TABLE, cv, "id=?", new String[]{Integer.toString(noteModel.getId())});
        return update != -1;
    }

    public boolean deleteOne(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int delete = db.delete(NOTE_TABLE, "id=?", new String[]{Integer.toString(id)});
        return delete != -1;
    }

    public List<NoteModel> getEveryone() {
        List<NoteModel> returnList = new ArrayList<>();

        // get data from the database

        String queryString = "SELECT * FROM " + NOTE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int noteID = cursor.getInt(0);
                String noteTitle = cursor.getString(1);
                String noteSubtitle = cursor.getString(2);
                String note = cursor.getString(3);
                byte[] noteImage = cursor.getBlob(4);
                String noteColor = cursor.getString(5);

                NoteModel newNote = new NoteModel(noteID, noteTitle, noteSubtitle, note, noteColor, noteImage);
                returnList.add(newNote);
            } while (cursor.moveToNext());
        } else {

        }

        cursor.close();
        db.close();
        return returnList;
    }
}
