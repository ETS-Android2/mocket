package com.rms.mocket.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rms.mocket.common.DateUtils;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mocket.db";

    /* Names for TERMS table */
    public static final String TABLE_TERMS = "terms";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TERM = "term";
    public static final String COLUMN_DEFINITION = "definition";
    public static final String COLUMN_DATE_ADD = "date_add";
    public static final String COLUMN_DATE_LATEST = "date_latest";
    public static final String COLUMN_MEMORY_LEVEL = "memory_level";


    public static final String TABLE_USERS = "users";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /* Checks if the password is correct */
    public static boolean checkEmailAndPassword(String email, String password) {
        //TODO: Check if the password is correct from Database
        return true;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+TABLE_TERMS+ " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                +", " + COLUMN_TERM + " TEXT"
                +", " + COLUMN_DEFINITION + " TEXT"
                +", " + COLUMN_DATE_ADD + " TEXT"
                +", " + COLUMN_DATE_LATEST + " TEXT"
                +", " + COLUMN_MEMORY_LEVEL + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_TERMS);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_USERS);
    }

    public boolean addTerm(String term, String definition){

        String date_today = DateUtils.getDateToday();
        int memory_level = 1;

        SQLiteDatabase term_db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TERM, term);
        contentValues.put(COLUMN_DEFINITION, definition);
        contentValues.put(COLUMN_DATE_ADD, date_today);
        contentValues.put(COLUMN_DATE_LATEST, date_today);
        contentValues.put(COLUMN_MEMORY_LEVEL, memory_level);
        long result = term_db.insert(TABLE_TERMS, null, contentValues);

        if(result == -1) return false;
        else return true;
    }

    public Cursor getAllTerms(){
        SQLiteDatabase term_db = this.getWritableDatabase();
        Cursor terms = term_db.rawQuery("select * from "+TABLE_TERMS, null);
        return terms;
    }
}
