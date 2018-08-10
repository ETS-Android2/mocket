package com.rms.mocket.common;

import java.text.ParseException;

public class Memory {



    public static String getNextDateToMemorize(String date_latest, int memory_level) throws ParseException {

        switch(memory_level){
            case 1: return DateUtils.getDateDaysLater(date_latest,1);

            case 2: return DateUtils.getDateDaysLater(date_latest,1);

            case 3: return DateUtils.getDateDaysLater(date_latest,1);

            case 4: return DateUtils.getDateDaysLater(date_latest,3);

            case 5: return DateUtils.getDateDaysLater(date_latest,7);

            case 6: return DateUtils.getDateDaysLater(date_latest,15);

            case 7: return DateUtils.getDateDaysLater(date_latest,30);

            default: return null;
        }
    }
}
