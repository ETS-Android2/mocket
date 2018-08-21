package com.rms.mocket.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rms.mocket.common.DateUtils;

import java.util.HashMap;

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

    /* Indexes for TERMS table */
    public static final int INDEX_ID = 0;
    public static final int INDEX_TERM = 1;
    public static final int INDEX_DEFINITION = 2;
    public static final int INDEX_DATE_ADD = 3;
    public static final int INDEX_DATE_LATEST = 4;
    public static final int INDEX_MEMORY_LEVEL = 5;


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


    public HashMap<String, String> getTermAt(String index){
        HashMap<String, String> termHashMap = new HashMap<>();

        SQLiteDatabase term_db = this.getWritableDatabase();
        Cursor cursor_terms = term_db.rawQuery("select * from "+TABLE_TERMS, null);

        while(cursor_terms.moveToNext()){
            String id = cursor_terms.getString(0);
            String term = cursor_terms.getString(1);
            String definition = cursor_terms.getString(2);
            String date_added = cursor_terms.getString(3);
            String date_lastMemorized = cursor_terms.getString(4);
            String memory_level = cursor_terms.getString(5);

            if (id.equals(index)){
                termHashMap.put(DatabaseHandler.COLUMN_ID, id);
                termHashMap.put(DatabaseHandler.COLUMN_TERM, term);
                termHashMap.put(DatabaseHandler.COLUMN_DEFINITION, definition);
                termHashMap.put(DatabaseHandler.COLUMN_DATE_ADD, date_added);
                termHashMap.put(DatabaseHandler.COLUMN_DATE_LATEST, date_lastMemorized);
                termHashMap.put(DatabaseHandler.COLUMN_MEMORY_LEVEL, memory_level);
                break;
            }
        }
        return termHashMap;
    }

    public boolean updateTerm(String id, String term, String definition, String date_add,
                              String date_latest, String memory_level){

        SQLiteDatabase term_db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TERM, term);
        contentValues.put(COLUMN_DEFINITION, definition);
        contentValues.put(COLUMN_DATE_ADD, date_add);
        contentValues.put(COLUMN_DATE_LATEST, date_latest);
        contentValues.put(COLUMN_MEMORY_LEVEL, memory_level);
        long result = term_db.update(TABLE_TERMS, contentValues,"id = ?",
                new String[]{id} );

        if(result == -1) return false;
        else return true;

    }

    public boolean updateTerm(HashMap<String, String> term){

        SQLiteDatabase term_db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TERM, term.get(DatabaseHandler.COLUMN_TERM));
        contentValues.put(COLUMN_DEFINITION, term.get(DatabaseHandler.COLUMN_DEFINITION));
        contentValues.put(COLUMN_DATE_ADD, term.get(DatabaseHandler.COLUMN_DATE_ADD));
        contentValues.put(COLUMN_DATE_LATEST, term.get(DatabaseHandler.COLUMN_DATE_LATEST));
        contentValues.put(COLUMN_MEMORY_LEVEL, term.get(DatabaseHandler.COLUMN_MEMORY_LEVEL));
        long result = term_db.update(TABLE_TERMS, contentValues,"id = ?",
                new String[]{term.get(DatabaseHandler.COLUMN_ID)} );

        if(result == -1) return false;
        else return true;
    }

    public int deleteTerm(String id){
        SQLiteDatabase term_db = this.getWritableDatabase();
        return term_db.delete(TABLE_TERMS,"id = ?", new String[]{id} );
    }

    public void updateToServer(){

    }
}
