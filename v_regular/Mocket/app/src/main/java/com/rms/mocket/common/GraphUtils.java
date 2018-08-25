package com.rms.mocket.common;

import android.content.Context;

import com.rms.mocket.database.DatabaseHandlerGame;
import com.rms.mocket.database.DatabaseHandlerTerms;
import com.rms.mocket.database.DatabaseHandlerTest;
import com.rms.mocket.fragments.GraphFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class GraphUtils {

    public static ArrayList<Integer> getTermData(Context context, String time_type){

        ArrayList<Integer> term_data = new ArrayList<>();

        DatabaseHandlerTerms db_term = new DatabaseHandlerTerms(context);
        HashMap<String, Integer> term_count = db_term.getTermCount();
//        Log.d("Mocket", term_count.toString());

        switch(time_type){
            case GraphFragment.TYPE_WEEK:
                String[] day_order = DateUtils.orderDayNumber(DateUtils.getDateToday());
                for(int i=0; i<day_order.length; i++){
                    String day = day_order[i];
                    int count = 0;
                    for(String date: term_count.keySet()) {
                        if(date.equals(day)){
                            count += term_count.get(date);
                        }
                    }
                    term_data.add(count);
                }
//                Log.d("Mocket", term_data.toString());
                break;

            case GraphFragment.TYPE_MONTH:
                String[] date_order = DateUtils.orderDateNumber(DateUtils.getDateToday());
                for(int i=0; i<date_order.length; i++){
                    String day = date_order[i];
                    int count = 0;
                    for(String date: term_count.keySet()) {
                        if(date.equals(day)){
                            count += term_count.get(date);
                        }
                    }
                    term_data.add(count);
                }
//                Log.d("Mocket", term_data.toString());
                break;

            case GraphFragment.TYPE_YEAR:

                String[] month_order = DateUtils.orderMonthNumber(DateUtils.getDateToday());
                for(int i=0; i<month_order.length; i++){
                    String month = month_order[i];
                    int count = 0;
                    for(String date: term_count.keySet()) {
                        if(date.startsWith(month)){
                            count += term_count.get(date);
                        }
                    }
                    term_data.add(count);
                }
//                Log.d("Mocket", term_data.toString());
                break;
        }

        return term_data;


    }


    public static ArrayList<Integer> getTestData(Context context, String time_type){

        ArrayList<Integer> test_data = new ArrayList<>();

        DatabaseHandlerTest db_test = new DatabaseHandlerTest(context);
        HashMap<String, Integer> test_count = db_test.getTestCount();
//        Log.d("Mocket", test_count.toString());

        switch(time_type){
            case GraphFragment.TYPE_WEEK:
                String[] day_order = DateUtils.orderDayNumber(DateUtils.getDateToday());
                for(int i=0; i<day_order.length; i++){
                    String day = day_order[i];
                    int count = 0;
                    for(String date: test_count.keySet()) {
                        if(date.equals(day)){
                            count += test_count.get(date);
                        }
                    }
                    test_data.add(count);
                }
//                Log.d("Mocket", test_data.toString());
                break;

            case GraphFragment.TYPE_MONTH:
                String[] date_order = DateUtils.orderDateNumber(DateUtils.getDateToday());
                for(int i=0; i<date_order.length; i++){
                    String day = date_order[i];
                    int count = 0;
                    for(String date: test_count.keySet()) {
                        if(date.equals(day)){
                            count += test_count.get(date);
                        }
                    }
                    test_data.add(count);
                }
//                Log.d("Mocket", test_data.toString());
                break;

            case GraphFragment.TYPE_YEAR:

                String[] month_order = DateUtils.orderMonthNumber(DateUtils.getDateToday());
                for(int i=0; i<month_order.length; i++){
                    String month = month_order[i];
                    int count = 0;
                    for(String date: test_count.keySet()) {
                        if(date.startsWith(month)){
                            count += test_count.get(date);
                        }
                    }
                    test_data.add(count);
                }
//                Log.d("Mocket", test_data.toString());
                break;
        }

        return test_data;


    }

    public static ArrayList<Integer> getGameDataCorrect(Context context, String time_type){

        ArrayList<Integer> game_data_correct = new ArrayList<>();

        DatabaseHandlerGame db_game = new DatabaseHandlerGame(context);
        HashMap<String, Integer> game_count_correct = db_game.getAllCorrectCount();
//        Log.d("Mocket", game_count_correct.toString());

        switch(time_type){
            case GraphFragment.TYPE_WEEK:
                String[] day_order = DateUtils.orderDayNumber(DateUtils.getDateToday());
                for(int i=0; i<day_order.length; i++){
                    String day = day_order[i];
                    int count = 0;
                    for(String date: game_count_correct.keySet()) {
                        if(date.equals(day)){
                            count += game_count_correct.get(date);
                        }
                    }
                    game_data_correct.add(count);
                }
//                Log.d("Mocket", game_data_correct.toString());
                break;

            case GraphFragment.TYPE_MONTH:
                String[] date_order = DateUtils.orderDateNumber(DateUtils.getDateToday());
                for(int i=0; i<date_order.length; i++){
                    String day = date_order[i];
                    int count = 0;
                    for(String date: game_count_correct.keySet()) {
                        if(date.equals(day)){
                            count += game_count_correct.get(date);
                        }
                    }
                    game_data_correct.add(count);
                }
//                Log.d("Mocket", game_data_correct.toString());
                break;

            case GraphFragment.TYPE_YEAR:

                String[] month_order = DateUtils.orderMonthNumber(DateUtils.getDateToday());
                for(int i=0; i<month_order.length; i++){
                    String month = month_order[i];
                    int count = 0;
                    for(String date: game_count_correct.keySet()) {
                        if(date.startsWith(month)){
                            count += game_count_correct.get(date);
                        }
                    }
                    game_data_correct.add(count);
                }
//                Log.d("Mocket", game_data_correct.toString());
                break;
        }

        return game_data_correct;
    }

    public static ArrayList<Integer> getGameDataIncorrect(Context context, String time_type){

        ArrayList<Integer> game_data_incorrect = new ArrayList<>();

        DatabaseHandlerGame db_game = new DatabaseHandlerGame(context);
        HashMap<String, Integer> game_count_incorrect = db_game.getAllIncorrectCount();
//        Log.d("Mocket", game_count_incorrect.toString());

        switch(time_type){
            case GraphFragment.TYPE_WEEK:
                String[] day_order = DateUtils.orderDayNumber(DateUtils.getDateToday());
                for(int i=0; i<day_order.length; i++){
                    String day = day_order[i];
                    int count = 0;
                    for(String date: game_count_incorrect.keySet()) {
                        if(date.equals(day)){
                            count += game_count_incorrect.get(date);
                        }
                    }
                    game_data_incorrect.add(count);
                }
//                Log.d("Mocket", game_data_incorrect.toString());
                break;

            case GraphFragment.TYPE_MONTH:
                String[] date_order = DateUtils.orderDateNumber(DateUtils.getDateToday());
                for(int i=0; i<date_order.length; i++){
                    String day = date_order[i];
                    int count = 0;
                    for(String date: game_count_incorrect.keySet()) {
                        if(date.equals(day)){
                            count += game_count_incorrect.get(date);
                        }
                    }
                    game_data_incorrect.add(count);
                }
//                Log.d("Mocket", game_data_incorrect.toString());
                break;

            case GraphFragment.TYPE_YEAR:

                String[] month_order = DateUtils.orderMonthNumber(DateUtils.getDateToday());
                for(int i=0; i<month_order.length; i++){
                    String month = month_order[i];
                    int count = 0;
                    for(String date: game_count_incorrect.keySet()) {
                        if(date.startsWith(month)){
                            count += game_count_incorrect.get(date);
                        }
                    }
                    game_data_incorrect.add(count);
                }
//                Log.d("Mocket", game_data_incorrect.toString());
                break;
        }

        return game_data_incorrect;
    }
}
