package com.rms.mocket.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class DatabaseHandlerGame extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mocket.db";

    /* Names for GAME table */
    public static final String TABLE_GAME = "game";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_CORRECT = "correct";
    public static final String COLUMN_INCORRECT = "incorrect";

    /* Indexes for GAME table */
    public static final int INDEX_ID = 0;
    public static final int INDEX_DATE = 1;
    public static final int INDEX_CORRECT = 2;
    public static final int INDEX_INCORRECT = 3;


    public static final String TABLE_USERS = "users";

    public DatabaseHandlerGame(Context context) {
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_TERMS);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_USERS);
    }

    public int getCorrectCount(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_GAME, null);
        int count = 0;
        boolean found = false;

        while(cursor.moveToNext()){
            String temp_date = cursor.getString(INDEX_DATE);
            String temp_correct_count = cursor.getString(INDEX_CORRECT);

            if (temp_date.equals(date)){
                count = Integer.parseInt(temp_correct_count);
                found = true;
                break;

            }
        }

        // Create if not exist.
        if(!found){
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_DATE, date);
            contentValues.put(COLUMN_CORRECT, 0);
            contentValues.put(COLUMN_INCORRECT, 0);
            long result = db.insert(TABLE_GAME, null, contentValues);

            if(result == -1) System.exit(1);
        }

        cursor.close();
        return count;
    }

    public int getIncorrectCount(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_GAME, null);

        int count = 0;
        boolean found = false;

        while(cursor.moveToNext()){
            String temp_date = cursor.getString(INDEX_DATE);
            String temp_incorrect_count = cursor.getString(INDEX_INCORRECT);

            if (temp_date.equals(date)){
                count = Integer.parseInt(temp_incorrect_count);
                found = true;
                break;
            }
        }

        // Create if not exist.
        if(!found){
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_DATE, date);
            contentValues.put(COLUMN_CORRECT, 0);
            contentValues.put(COLUMN_INCORRECT, 0);
            long result = db.insert(TABLE_GAME, null, contentValues);

            if(result == -1) System.exit(1);
        }
        cursor.close();
        return count;
    }


    public boolean setCount(String date, int correct, int incorrect){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_CORRECT, correct);
        contentValues.put(COLUMN_INCORRECT, incorrect);

        int index = this.getCountIndex(date);
        if (index == -1){
            long result = db.insert(TABLE_GAME, null, contentValues);
            if(result == -1) return false;
            else return true;

        } else {
            long result = db.update(TABLE_GAME, contentValues,"id = ?",
                    new String[]{Integer.toString(index)} );

            if(result == -1) return false;
            else return true;

        }
    }

    public int getCountIndex(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_GAME, null);

        int id = -1;

        while(cursor.moveToNext()){
            String temp_id = cursor.getString(INDEX_ID);
            String temp_date = cursor.getString(INDEX_DATE);
            if (temp_date.equals(date)){
                return Integer.parseInt(temp_id);
            }
        }
        cursor.close();
        return id;
    }

    public HashMap<String, Integer> getAllCorrectCount(){
        HashMap<String, Integer> all_correct_count = new HashMap<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_GAME, null);

        while(cursor.moveToNext()){
            String temp_date = cursor.getString(INDEX_DATE);
            String temp_correct_count = cursor.getString(INDEX_CORRECT);

            all_correct_count.put(temp_date,Integer.parseInt(temp_correct_count));
        }

        cursor.close();
        return all_correct_count;
    }

    public HashMap<String, Integer> getAllIncorrectCount(){
        HashMap<String, Integer> all_incorrect_count = new HashMap<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_GAME, null);

        while(cursor.moveToNext()){
            String temp_date = cursor.getString(INDEX_DATE);
            String temp_correct_count = cursor.getString(INDEX_INCORRECT);

            all_incorrect_count.put(temp_date,Integer.parseInt(temp_correct_count));
        }

        cursor.close();
        return all_incorrect_count;
    }
}
