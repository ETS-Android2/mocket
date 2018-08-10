package com.rms.mocket.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    static final DateFormat DATE_FORMAT =new SimpleDateFormat("yyyy/MM/dd");

    public static String getDateToday(){
        /* Get current date */
        String date_today =DATE_FORMAT.format(Calendar.getInstance().getTime());

        return date_today;
    }

    public static String getDateDaysLater(String string_date, int days_later) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTime(DATE_FORMAT.parse(string_date));
        c.add(Calendar.DATE, days_later);

        return DATE_FORMAT.format(c.getTime());
    }

    public static int getDateDifference(String string_date1, String string_date2) throws ParseException {
        Date date1 = DATE_FORMAT.parse(string_date1);
        Date date2 = DATE_FORMAT.parse(string_date2);
        long diff = date2.getTime() - date1.getTime();

        if(diff < 0){
            throw new Error("Place earlier date first in the parameter.");
        }
        int diff_days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        return diff_days;
    }

}
