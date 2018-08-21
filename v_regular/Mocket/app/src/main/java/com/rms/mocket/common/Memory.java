package com.rms.mocket.common;

import com.rms.mocket.database.DatabaseHandler;

import java.util.HashMap;

public class Memory {



    public static String getNextDateToMemorize(String date_latest, String memory_level){

        try {

            switch (Integer.parseInt(memory_level)) {
                case 1:
                    return DateUtils.getDateDaysLater(date_latest, 1);

                case 2:
                    return DateUtils.getDateDaysLater(date_latest, 1);

                case 3:
                    return DateUtils.getDateDaysLater(date_latest, 1);

                case 4:
                    return DateUtils.getDateDaysLater(date_latest, 3);

                case 5:
                    return DateUtils.getDateDaysLater(date_latest, 7);

                case 6:
                    return DateUtils.getDateDaysLater(date_latest, 15);

                case 7:
                    return DateUtils.getDateDaysLater(date_latest, 30);

                default:
                    return null;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static HashMap<String, String> updateMemoryLevel(HashMap<String, String> term){
        String temp_memory_level = term.get(DatabaseHandler.COLUMN_MEMORY_LEVEL);

        String date_to_memorize = Memory.getNextDateToMemorize(
                term.get(DatabaseHandler.COLUMN_DATE_LATEST),
                term.get(DatabaseHandler.COLUMN_MEMORY_LEVEL));

        if(DateUtils.getDateToday().equals(date_to_memorize)){
            String new_temp_memory_level;
            if(temp_memory_level.equals("7")){
                new_temp_memory_level = "0";
            }else{
                int int_temp_memory_level = Integer.parseInt(temp_memory_level);
                int_temp_memory_level++;
                new_temp_memory_level = Integer.toString(int_temp_memory_level);
            }
            term.put(DatabaseHandler.COLUMN_MEMORY_LEVEL, new_temp_memory_level);
        }

        // Update last memorized date to today.
        term.put(DatabaseHandler.COLUMN_DATE_LATEST, DateUtils.getDateToday());

        return term;

    }
}
