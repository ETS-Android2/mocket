package com.rms.mocket.common;

import com.rms.mocket.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class TermUtils {

    public static ArrayList<HashMap<String,String>> sortTerms(ArrayList<HashMap<String, String>> input_terms){
        ArrayList<HashMap<String,String>> sorted_terms = new ArrayList<>();
        while(!input_terms.isEmpty()){
            String lowest_term = null;
            int lowest_index =0;
            for(int i=0; i < input_terms.size(); i++){
                String temp_term = input_terms.get(i).get(DatabaseHandler.COLUMN_TERM);
                if(lowest_term == null){
                    lowest_term = temp_term;
                    lowest_index = i;
                }else {
                    if (lowest_term.toLowerCase().compareTo(temp_term.toLowerCase()) > 0) {
                        lowest_term = temp_term;
                        lowest_index = i;
                    }
                }
            }
            sorted_terms.add(input_terms.get(lowest_index));
            input_terms.remove(lowest_index);
        }

        return sorted_terms;
    }
}
