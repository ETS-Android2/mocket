package com.rms.mocket.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rms.mocket.common.DateUtils;

import java.util.HashMap;

public class DatabaseHandlerTerms extends SQLiteOpenHelper {

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

    public DatabaseHandlerTerms(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /* Checks if the password is correct */
    public static boolean checkEmailAndPassword(String email, String password) {
        //TODO: Check if the password is correct from Database
        return true;
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
                termHashMap.put(DatabaseHandlerTerms.COLUMN_ID, id);
                termHashMap.put(DatabaseHandlerTerms.COLUMN_TERM, term);
                termHashMap.put(DatabaseHandlerTerms.COLUMN_DEFINITION, definition);
                termHashMap.put(DatabaseHandlerTerms.COLUMN_DATE_ADD, date_added);
                termHashMap.put(DatabaseHandlerTerms.COLUMN_DATE_LATEST, date_lastMemorized);
                termHashMap.put(DatabaseHandlerTerms.COLUMN_MEMORY_LEVEL, memory_level);
                break;
            }
        }
        cursor_terms.close();
        return termHashMap;
    }

    public HashMap<String, Integer> getTermCount(){

        /*
        *   Key: Date
        *   Value: Count of terms.
        */
        HashMap<String, Integer> all_terms = new HashMap<>();

        SQLiteDatabase term_db = this.getWritableDatabase();
        Cursor cursor_terms = term_db.rawQuery("select * from "+TABLE_TERMS, null);

        while(cursor_terms.moveToNext()){
            String date_added = cursor_terms.getString(3);

            if(all_terms.containsKey(date_added)){
                int previous = all_terms.get(date_added);
                all_terms.put(date_added, previous+1);
            }else{
                all_terms.put(date_added,1);
            }

        }
        cursor_terms.close();
        return all_terms;
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
        contentValues.put(COLUMN_TERM, term.get(DatabaseHandlerTerms.COLUMN_TERM));
        contentValues.put(COLUMN_DEFINITION, term.get(DatabaseHandlerTerms.COLUMN_DEFINITION));
        contentValues.put(COLUMN_DATE_ADD, term.get(DatabaseHandlerTerms.COLUMN_DATE_ADD));
        contentValues.put(COLUMN_DATE_LATEST, term.get(DatabaseHandlerTerms.COLUMN_DATE_LATEST));
        contentValues.put(COLUMN_MEMORY_LEVEL, term.get(DatabaseHandlerTerms.COLUMN_MEMORY_LEVEL));
        long result = term_db.update(TABLE_TERMS, contentValues,"id = ?",
                new String[]{term.get(DatabaseHandlerTerms.COLUMN_ID)} );

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
