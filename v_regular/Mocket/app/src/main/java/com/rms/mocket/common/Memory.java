package com.rms.mocket.common;

import com.rms.mocket.object.Term;

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

    public static Term updateMemoryLevel(Term term){
        String temp_memory_level = term.memory_level;

        String date_to_memorize = Memory.getNextDateToMemorize(term.date_latest, term.memory_level);

        if(DateUtils.getDateToday().equals(date_to_memorize)){
            String new_temp_memory_level;
            if(temp_memory_level.equals("7")){
                new_temp_memory_level = "0";
            }else{
                int int_temp_memory_level = Integer.parseInt(temp_memory_level);
                int_temp_memory_level++;
                new_temp_memory_level = Integer.toString(int_temp_memory_level);
            }
            term.memory_level = new_temp_memory_level;
        }

        // Update last memorized date to today.
        term.date_latest = DateUtils.getDateToday();

        return term;

    }
}
