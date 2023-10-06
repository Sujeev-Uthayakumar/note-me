package com.sujeevuthayakumar.noteme;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String COLUMN_NOTE = "NOTE";
    public static final String NOTE_TABLE = COLUMN_NOTE + "_TABLE";
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
                + COLUMN_NOTE_COLOR + " TEXT)";

        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
