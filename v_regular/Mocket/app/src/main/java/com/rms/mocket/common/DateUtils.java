package com.rms.mocket.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static String changeDateFormatShort(String date){
        String[] splitted_date = date.split("/");

        String year = splitted_date[0].substring(2,4);
        String month = splitted_date[1];
        String day = splitted_date[2];
        return month + "/" + day + "/" +year;
    }

    public static boolean isNotPassed(String string_date){
        Calendar c = Calendar.getInstance();
        boolean result = false;
        try {
            result = DATE_FORMAT.parse(string_date).after(DATE_FORMAT.parse(DateUtils.getDateToday()));
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        return result;
    }

    public static String getMonth(String full_date){
        String month = full_date.substring(5,7);
        String result_month = "";
        switch(month){
            case "01":
                result_month = "Jan";
                break;
            case "02":
                result_month = "Feb";
                break;
            case "03":
                result_month = "Mar";
                break;
            case "04":
                result_month = "Apr";
                break;
            case "05":
                result_month = "May";
                break;
            case "06":
                result_month = "Jun";
                break;
            case "07":
                result_month = "Jul";
                break;
            case "08":
                result_month = "Aug";
                break;
            case "09":
                result_month = "Sep";
                break;
            case "10":
                result_month = "Oct";
                break;
            case "11":
                result_month = "Nov";
                break;
            case "12":
                result_month = "Dec";
                break;
        }
        return result_month;

    }

    public static String getMonthNumber(String full_date){
        String month = full_date.substring(5,7);
        return month;

    }

    public static String getYearNumber(String full_date){
        String year = full_date.substring(0,4);
        return year;

    }

    public static String[] orderMonth(String date){

        String starting_month = getMonth(date);
        String year = getYearNumber(date);
        String year_short = year.substring(2,4);
        ArrayList<String> mMonths = new ArrayList<String>(Arrays.asList(
                "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        ));

        int starting_index = mMonths.indexOf(starting_month);

        ArrayList<String> new_mMonths = new ArrayList<>();
        for(int i=starting_index+1; i<mMonths.size();i++){
            int new_year = Integer.parseInt(year) - 1;
            String new_year_short = Integer.toString(new_year).substring(2,4);
            new_mMonths.add(mMonths.get(i)+'`'+new_year_short);
        }
        for(int i = 0; i < starting_index+1;i++){
            new_mMonths.add(mMonths.get(i) + '`'+year_short);
        }

        String[] result_months = new String[new_mMonths.size()];
        for(int i=0; i < new_mMonths.size(); i++){
            result_months[i] = new_mMonths.get(i);
        }

        return result_months;
    }

    public static String[] orderMonthNumber(String date){

        String starting_month = getMonthNumber(date);
        String year = getYearNumber(date);
        ArrayList<String> mMonths = new ArrayList<String>(Arrays.asList(
                "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"
        ));

        int starting_index = mMonths.indexOf(starting_month);

        ArrayList<String> new_mMonths = new ArrayList<>();
        for(int i=starting_index+1; i<mMonths.size();i++){
            int new_year = Integer.parseInt(year) - 1;
            new_mMonths.add(Integer.toString(new_year) + "/"+ mMonths.get(i));
        }
        for(int i = 0; i < starting_index+1;i++){
            new_mMonths.add(year+"/"+mMonths.get(i));
        }

        String[] result_months = new String[new_mMonths.size()];
        for(int i=0; i < new_mMonths.size(); i++){
            result_months[i] = new_mMonths.get(i);
        }

        return result_months;
    }

    public static String[] orderDate(String date){

        String[] result_date = new String[30];
        try{
            int index = 0;
            for(int i=29; i >= 0;i--){
                Date form_date = DATE_FORMAT.parse(date);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(form_date);
                calendar.add(Calendar.DATE, 0-i);
                String new_date = DATE_FORMAT.format(calendar.getTime()).substring(5,10);
                result_date[index] = new_date;
                index++;
            }

        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        return result_date;

    }

    public static String[] orderDateNumber(String date){

        String[] result_date = new String[30];
        try{
            int index = 0;
            for(int i=29; i >= 0;i--){
                Date form_date = DATE_FORMAT.parse(date);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(form_date);
                calendar.add(Calendar.DATE, 0-i);
                String new_date = DATE_FORMAT.format(calendar.getTime());
                result_date[index] = new_date;
                index++;
            }

        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        return result_date;

    }

    public static String[] orderDay(String date){

        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of the week abbreviated
        String starting_day = "";
        try {
            starting_day = simpleDateformat.format(DATE_FORMAT.parse(date));
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        ArrayList<String> mDay = new ArrayList<String>(Arrays.asList(
                "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
        ));

        int starting_index = mDay.indexOf(starting_day);

        ArrayList<String> new_mDay = new ArrayList<>();
        for(int i=starting_index+1; i<mDay.size();i++){
            new_mDay.add(mDay.get(i));
        }
        for(int i = 0; i < starting_index+1;i++){
            new_mDay.add(mDay.get(i));
        }

        String[] result_days = new String[new_mDay.size()];
        for(int i=0; i < mDay.size(); i++){
            result_days[i] = new_mDay.get(i);
        }

        return result_days;
    }

    public static String[] orderDayNumber(String date){

        String[] result_date = new String[7];
        try{
            int index = 0;
            for(int i=6; i >= 0;i--){
                Date form_date = DATE_FORMAT.parse(date);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(form_date);
                calendar.add(Calendar.DATE, 0-i);
                String new_date = DATE_FORMAT.format(calendar.getTime());
                result_date[index] = new_date;
                index++;
            }

        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        return result_date;
    }

    public static String revertDate(String date){
        String year = date.substring(0,4);
        String month = date.substring(4,6);
        String day = date.substring(6,8);

        return year + '/' + month + '/' + day;
    }

}
