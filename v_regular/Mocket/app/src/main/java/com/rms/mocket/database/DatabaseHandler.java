package com.rms.mocket.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Mocket.db";
    public static final String TABLE_WORDS = "words_table";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        SQLiteDatabase database = this.getWritableDatabase();
    }

    /* Checks if the password is correct */
    public static boolean checkEmailAndPassword(String email, String password) {
        //TODO: Check if the password is correct from Database
        return true;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
