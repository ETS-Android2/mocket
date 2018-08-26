package com.rms.mocket.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rms.mocket.R;
import com.rms.mocket.common.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DatabaseHandlerUser extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mocket.db";

    /* Names for USER table */
    public static final String TABLE_USER = "user";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_PROFILE = "profile";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_SETTING_NOTIFICATION = "setting_test";
    public static final String COLUMN_SETTING_GAME = "setting_game";
    public static final String COLUMN_SETTING_GESTURE = "setting_gesture";
    public static final String COLUMN_SETTING_VIBRATION = "setting_vibration";


    /* Indexes for USER table */
    public static final int INDEX_ID = 0;
    public static final int INDEX_EMAIL = 1;
    public static final int INDEX_PASSWORD = 2;
    public static final int INDEX_PROFILE = 3;
    public static final int INDEX_FIRST_NAME = 4;
    public static final int INDEX_LAST_NAME = 5;
    public static final int INDEX_SETTING_NOTIFICATION = 6;
    public static final int INDEX_SETTING_GAME = 7;
    public static final int INDEX_SETTING_GESTURE = 8;
    public static final int INDEX_SETTING_VIBRATION = 9;

    public DatabaseHandlerUser(Context context) {
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


    public void initializeUserData(Context context,
            String email, String password, byte[] profile, String firstName, String lastName){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from "+TABLE_USER, null);

        if(!cursor.moveToNext()){

            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_EMAIL, email);
            contentValues.put(COLUMN_PASSWORD, password);
            contentValues.put(COLUMN_FIRST_NAME, firstName);
            contentValues.put(COLUMN_LAST_NAME, lastName);

            List<String> setting_notification = Arrays.asList(context.getResources().getStringArray(R.array.notification_duration));
            List<String> setting_game = Arrays.asList(context.getResources().getStringArray(R.array.game_daily));
            List<String> setting_gesture = Arrays.asList(context.getResources().getStringArray(R.array.gesture_option));
            List<String> setting_vibration = Arrays.asList(context.getResources().getStringArray(R.array.vibration));

            contentValues.put(COLUMN_SETTING_NOTIFICATION, setting_notification.get(0));
            contentValues.put(COLUMN_SETTING_GAME, setting_game.get(0));
            contentValues.put(COLUMN_SETTING_GESTURE, setting_gesture.get(0));
            contentValues.put(COLUMN_SETTING_VIBRATION, setting_vibration.get(0));

            contentValues.put(COLUMN_PROFILE, profile);


            long result = db.insert(TABLE_USER, null, contentValues);
            cursor.close();

        }else{

            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_EMAIL, email);
            contentValues.put(COLUMN_PASSWORD, password);
            contentValues.put(COLUMN_FIRST_NAME, firstName);
            contentValues.put(COLUMN_LAST_NAME, lastName);
            contentValues.put(COLUMN_SETTING_NOTIFICATION, cursor.getString(INDEX_SETTING_NOTIFICATION));
            contentValues.put(COLUMN_SETTING_GAME, cursor.getString(INDEX_SETTING_GAME));
            contentValues.put(COLUMN_SETTING_GESTURE, cursor.getString(INDEX_SETTING_GESTURE));
            contentValues.put(COLUMN_SETTING_VIBRATION, cursor.getString(INDEX_SETTING_VIBRATION));
            contentValues.put(COLUMN_PROFILE, profile);

            long result = db.update(TABLE_USER, contentValues,"id = ?",
                    new String[]{cursor.getString(INDEX_ID)});
            cursor.close();

        }

    }


    public int getUserDataIndex(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_USER, null);

        int index = -1;

        if(cursor.moveToFirst()){
            String temp_id = cursor.getString(INDEX_ID);
            index = Integer.parseInt(temp_id);
        }
        cursor.close();
        return index;
    }


    public HashMap<String, String> getUserData(){
        HashMap<String, String> user_data = new HashMap<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_USER, null);

        if(cursor.moveToNext()){
            user_data.put(COLUMN_EMAIL, cursor.getString(INDEX_EMAIL));
            user_data.put(COLUMN_PASSWORD, cursor.getString(INDEX_PASSWORD));
            user_data.put(COLUMN_FIRST_NAME, cursor.getString(INDEX_FIRST_NAME));
            user_data.put(COLUMN_LAST_NAME, cursor.getString(INDEX_LAST_NAME));
            user_data.put(COLUMN_SETTING_NOTIFICATION, cursor.getString(INDEX_SETTING_NOTIFICATION));
            user_data.put(COLUMN_SETTING_GAME, cursor.getString(INDEX_SETTING_GAME));
            user_data.put(COLUMN_SETTING_GESTURE, cursor.getString(INDEX_SETTING_GESTURE));
            user_data.put(COLUMN_SETTING_VIBRATION, cursor.getString(INDEX_SETTING_VIBRATION));
        }
        cursor.close();

        return user_data;
    }

    public byte[] getUserProfileImage(){
        byte[] profile_image = null;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_USER, null);

        if(cursor.moveToNext()){
            profile_image = cursor.getBlob(INDEX_PROFILE);
        }
        cursor.close();

        return profile_image;
    }




    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_TERMS);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_USERS);
    }

    public void printDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_USER, null);

        Log.d(Utils.TAG, "[ Database ] USER ("+Integer.toString(cursor.getCount())+")");

        while(cursor.moveToNext()){
            Log.d(Utils.TAG, " ID: "+cursor.getString(INDEX_ID));
            Log.d(Utils.TAG, " - Email: "+cursor.getString(INDEX_EMAIL));
            Log.d(Utils.TAG, " - Password: "+cursor.getString(INDEX_PASSWORD));
            Log.d(Utils.TAG, " - First name: "+cursor.getString(INDEX_FIRST_NAME));
            Log.d(Utils.TAG, " - Last name: "+cursor.getString(INDEX_LAST_NAME));
            Log.d(Utils.TAG, " - Setting_notification: "+cursor.getString(INDEX_SETTING_NOTIFICATION));
            Log.d(Utils.TAG, " - Setting_game: "+cursor.getString(INDEX_SETTING_GAME));
            Log.d(Utils.TAG, " - Setting_gesture: "+cursor.getString(INDEX_SETTING_GESTURE));
            Log.d(Utils.TAG, " - Setting_vibration: "+cursor.getString(INDEX_SETTING_VIBRATION));
        }
        cursor.close();
    }


    public boolean saveSettings(
            String setting_notification, String setting_game,
            String setting_gesture, String setting_vibration){

        SQLiteDatabase db = this.getWritableDatabase();

        int user_index = getUserDataIndex();

        Cursor cursor = db.rawQuery("select * from "+TABLE_USER, null);
        while(cursor.moveToNext()){
            String id = cursor.getString(INDEX_ID);

            if (Integer.parseInt(id) == user_index){
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_EMAIL, cursor.getString(INDEX_EMAIL));
                contentValues.put(COLUMN_PASSWORD, cursor.getString(INDEX_PASSWORD));
                contentValues.put(COLUMN_FIRST_NAME, cursor.getString(INDEX_FIRST_NAME));
                contentValues.put(COLUMN_LAST_NAME, cursor.getString(INDEX_LAST_NAME));
                contentValues.put(COLUMN_SETTING_NOTIFICATION, setting_notification);
                contentValues.put(COLUMN_SETTING_GAME, setting_game);
                contentValues.put(COLUMN_SETTING_GESTURE, setting_gesture);
                contentValues.put(COLUMN_SETTING_VIBRATION, setting_vibration);
                contentValues.put(COLUMN_PROFILE, cursor.getBlob(INDEX_PROFILE));

                long result = db.update(TABLE_USER, contentValues,"id = ?",
                        new String[]{Integer.toString(user_index)} );

                cursor.close();
                if(result == -1) return false;
                else return true;
            }
        }
        return false;
    }

    public boolean saveUserData(String firstName, String lastName){

        SQLiteDatabase db = this.getWritableDatabase();

        int user_index = getUserDataIndex();

        Cursor cursor = db.rawQuery("select * from "+TABLE_USER, null);
        while(cursor.moveToNext()){
            String id = cursor.getString(INDEX_ID);

            if (Integer.parseInt(id) == user_index){
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_EMAIL, cursor.getString(INDEX_EMAIL));
                contentValues.put(COLUMN_PASSWORD, cursor.getString(INDEX_PASSWORD));
                contentValues.put(COLUMN_FIRST_NAME, firstName);
                contentValues.put(COLUMN_LAST_NAME, lastName);
                contentValues.put(COLUMN_SETTING_NOTIFICATION, cursor.getString(INDEX_SETTING_NOTIFICATION));
                contentValues.put(COLUMN_SETTING_GAME, cursor.getString(INDEX_SETTING_GAME));
                contentValues.put(COLUMN_SETTING_GESTURE, cursor.getString(INDEX_SETTING_GESTURE));
                contentValues.put(COLUMN_SETTING_VIBRATION, cursor.getString(INDEX_SETTING_VIBRATION));
                contentValues.put(COLUMN_PROFILE, cursor.getBlob(INDEX_PROFILE));


                long result = db.update(TABLE_USER, contentValues,"id = ?",
                        new String[]{Integer.toString(user_index)} );

                cursor.close();
                if(result == -1) return false;
                else return true;
            }
        }
        return false;
    }

    public boolean saveProfileImage(byte[] profile_image){

        SQLiteDatabase db = this.getWritableDatabase();

        int user_index = getUserDataIndex();

        Cursor cursor = db.rawQuery("select * from "+TABLE_USER, null);
        while(cursor.moveToNext()){
            String id = cursor.getString(INDEX_ID);

            if (Integer.parseInt(id) == user_index){
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_EMAIL, cursor.getString(INDEX_EMAIL));
                contentValues.put(COLUMN_PASSWORD, cursor.getString(INDEX_PASSWORD));
                contentValues.put(COLUMN_FIRST_NAME, cursor.getString(INDEX_FIRST_NAME));
                contentValues.put(COLUMN_LAST_NAME, cursor.getString(INDEX_LAST_NAME));
                contentValues.put(COLUMN_SETTING_NOTIFICATION, cursor.getString(INDEX_SETTING_NOTIFICATION));
                contentValues.put(COLUMN_SETTING_GAME, cursor.getString(INDEX_SETTING_GAME));
                contentValues.put(COLUMN_SETTING_GESTURE, cursor.getString(INDEX_SETTING_GESTURE));
                contentValues.put(COLUMN_SETTING_VIBRATION, cursor.getString(INDEX_SETTING_VIBRATION));
                contentValues.put(COLUMN_PROFILE, profile_image);

                long result = db.update(TABLE_USER, contentValues,"id = ?",
                        new String[]{Integer.toString(user_index)} );

                cursor.close();
                if(result == -1) return false;
                else return true;
            }
        }
        return false;
    }

    public void savePassword(String password){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from "+TABLE_USER, null);
        if(cursor.moveToNext()){
            String id = cursor.getString(INDEX_ID);
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_EMAIL, cursor.getString(INDEX_EMAIL));
            contentValues.put(COLUMN_PASSWORD, password);
            contentValues.put(COLUMN_FIRST_NAME, cursor.getString(INDEX_FIRST_NAME));
            contentValues.put(COLUMN_LAST_NAME, cursor.getString(INDEX_LAST_NAME));
            contentValues.put(COLUMN_SETTING_NOTIFICATION, cursor.getString(INDEX_SETTING_NOTIFICATION));
            contentValues.put(COLUMN_SETTING_GAME, cursor.getString(INDEX_SETTING_GAME));
            contentValues.put(COLUMN_SETTING_GESTURE, cursor.getString(INDEX_SETTING_GESTURE));
            contentValues.put(COLUMN_SETTING_VIBRATION, cursor.getString(INDEX_SETTING_VIBRATION));
            contentValues.put(COLUMN_PROFILE, cursor.getBlob(INDEX_PROFILE));

            long result = db.update(TABLE_USER, contentValues,"id = ?",
                    new String[]{id} );

            cursor.close();
        }

    }

    public String getPassword(){

        SQLiteDatabase db = this.getWritableDatabase();

        int user_index = getUserDataIndex();
        Cursor cursor = db.rawQuery("select * from "+TABLE_USER, null);

        String password = "";
        while(cursor.moveToNext()){
            String id = cursor.getString(INDEX_ID);

            if (Integer.parseInt(id) == user_index){
                password = cursor.getString(INDEX_PASSWORD);

            }
        }
        return password;
    }
}
