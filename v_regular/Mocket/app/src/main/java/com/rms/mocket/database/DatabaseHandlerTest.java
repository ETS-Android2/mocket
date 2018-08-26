package com.rms.mocket.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class DatabaseHandlerTest extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mocket.db";

    /* Names for TEST table */
    public static final String TABLE_TEST = "test";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_COUNT = "count";

    /* Indexes for TEST table */
    public static final int INDEX_ID = 0;
    public static final int INDEX_DATE = 1;
    public static final int INDEX_COUNT = 2;


    public static final String TABLE_USERS = "users";

    public DatabaseHandlerTest(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+DatabaseHandlerTerms.TABLE_TERMS+ " ("
                + DatabaseHandlerTerms.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                +", " + DatabaseHandlerTerms.COLUMN_TERM + " TEXT"
                +", " + DatabaseHandlerTerms.COLUMN_DEFINITION + " TEXT"
                +", " + DatabaseHandlerTerms.COLUMN_DATE_ADD + " TEXT"
                +", " + DatabaseHandlerTerms.COLUMN_DATE_LATEST + " TEXT"
                +", " + DatabaseHandlerTerms.COLUMN_MEMORY_LEVEL + " INTEGER)");

        sqLiteDatabase.execSQL("create table "+DatabaseHandlerTest.TABLE_TEST+ " ("
                + DatabaseHandlerTest.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                +", " + DatabaseHandlerTest.COLUMN_DATE + " TEXT"
                +", " + DatabaseHandlerTest.COLUMN_COUNT + " INTEGER)");

        sqLiteDatabase.execSQL("create table "+DatabaseHandlerGame.TABLE_GAME+ " ("
                + DatabaseHandlerGame.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                +", " + DatabaseHandlerGame.COLUMN_DATE + " TEXT"
                +", " + DatabaseHandlerGame.COLUMN_CORRECT + " INTEGER"
                +", " + DatabaseHandlerGame.COLUMN_INCORRECT + " INTEGER)");

        sqLiteDatabase.execSQL("create table "+DatabaseHandlerUser.TABLE_USER+ " ("
                + DatabaseHandlerUser.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                +", " + DatabaseHandlerUser.COLUMN_EMAIL + " TEXT"
                +", " + DatabaseHandlerUser.COLUMN_PASSWORD + " TEXT"
                +", " + DatabaseHandlerUser.COLUMN_PROFILE + " blob"
                +", " + DatabaseHandlerUser.COLUMN_FIRST_NAME + " TEXT"
                +", " + DatabaseHandlerUser.COLUMN_LAST_NAME + " TEXT"
                +", " + DatabaseHandlerUser.COLUMN_SETTING_NOTIFICATION + " TEXT"
                +", " + DatabaseHandlerUser.COLUMN_SETTING_GAME + " TEXT"
                +", " + DatabaseHandlerUser.COLUMN_SETTING_GESTURE + " TEXT"
                +", " + DatabaseHandlerUser.COLUMN_SETTING_VIBRATION + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_TERMS);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_USERS);
    }

    public int getCount(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_TEST, null);

        int count = 0;
        boolean found = false;

        while(cursor.moveToNext()){
            String temp_date = cursor.getString(INDEX_DATE);
            String temp_count = cursor.getString(INDEX_COUNT);

            if (temp_date.equals(date)){
                count = Integer.parseInt(temp_count);
                found = true;
                break;
            }
        }

        // Create if not exist.
        if(!found){
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_DATE, date);
            contentValues.put(COLUMN_COUNT, 0);
            long result = db.insert(TABLE_TEST, null, contentValues);

            if(result == -1) System.exit(1);
        }
        cursor.close();
        return count;
    }

    public HashMap<String, Integer> getTestCount(){

        HashMap<String, Integer> all_test = new HashMap<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_TEST, null);

        while(cursor.moveToNext()){
            String temp_date = cursor.getString(INDEX_DATE);
            String temp_count = cursor.getString(INDEX_COUNT);

            all_test.put(temp_date, Integer.parseInt(temp_count));

        }

        return all_test;
    }

    public boolean setCount(String date, int count){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_COUNT, count);

        int index = this.getCountIndex(date);
        if (index == -1){
            long result = db.insert(TABLE_TEST, null, contentValues);
            if(result == -1) return false;
            else return true;

        } else {
            long result = db.update(TABLE_TEST, contentValues,"id = ?",
                    new String[]{Integer.toString(index)} );

            if(result == -1) return false;
            else return true;

        }
    }

    public int getCountIndex(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_TEST, null);

        int id = -1;

        while(cursor.moveToNext()){
            String temp_id = cursor.getString(INDEX_ID);
            String temp_date = cursor.getString(INDEX_DATE);
            if (temp_date.equals(date)){
                cursor.close();
                return Integer.parseInt(temp_id);
            }
        }
        cursor.close();
        return id;
    }

}
