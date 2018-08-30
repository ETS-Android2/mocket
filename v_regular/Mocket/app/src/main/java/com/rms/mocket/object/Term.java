package com.rms.mocket.object;

import com.rms.mocket.common.DateUtils;

public class Term {

    public static final String REFERENCE_TERMS = "terms";

    public String id;
    public String term;
    public String definition;
    public String date_add;
    public String date_latest;
    public String memory_level;

    public Term(){

    }
    public Term(String id, String term, String definition){
        this.id = id;
        this.term = term;
        this.definition = definition;
        this.date_add = DateUtils.getDateToday();
        this.date_latest = DateUtils.getDateToday();
        this.memory_level = "1";
    }


}
