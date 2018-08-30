package com.rms.mocket.common;

import android.content.Context;

import com.rms.mocket.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class DictionaryUtils {
    final static int LIMIT = 50;
    final static boolean APPLY_LIMIT = false;

    public static ArrayList<HashMap<String, String>> getTermList(Context context, String given_term){

        ArrayList<HashMap<String, String>> all_terms = new ArrayList<>();

        try {

            if(given_term.isEmpty()
                    || (!Character.isLetter(given_term.charAt(0)))) return all_terms;
            given_term = given_term.toLowerCase();

            InputStream inputStream = context.getResources().openRawResource(findDictResID(given_term));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;


            int count = 0;
            while ((line = reader.readLine()) != null) {
//                Log.d("Mocket","line: "+line);
                if(count >= LIMIT && APPLY_LIMIT) break;

                if(line.equals("")) continue;

                if(line.charAt(0) == '\"' && line.charAt(line.length()-1) == '\"'){
                    line = line.substring(1, line.length()-1);
                }

                if(!line.contains("(")) continue;

                String term = line.substring(0, line.indexOf("(")).trim();
                String definition = line.substring(line.indexOf("("), line.length()).trim();

                if(term.toLowerCase().startsWith(given_term.toLowerCase())
                        || term.toLowerCase().equals(given_term.toLowerCase())) {
                    HashMap<String, String> converted_term = new HashMap<>();
                    converted_term.put("term", term);
                    converted_term.put("definition", definition);
                    all_terms.add(converted_term);
                    count++;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return all_terms;
    }




    /* Levenshtein Distance */
    private static int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    public static int computeLevenshteinDistance(CharSequence lhs, CharSequence rhs) {
        int[][] distance = new int[lhs.length() + 1][rhs.length() + 1];

        for (int i = 0; i <= lhs.length(); i++)
            distance[i][0] = i;
        for (int j = 1; j <= rhs.length(); j++)
            distance[0][j] = j;

        for (int i = 1; i <= lhs.length(); i++)
            for (int j = 1; j <= rhs.length(); j++)
                distance[i][j] = minimum(
                        distance[i - 1][j] + 1,
                        distance[i][j - 1] + 1,
                        distance[i - 1][j - 1] + ((lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1));

        return distance[lhs.length()][rhs.length()];
    }

    private static int findDictResID(String term){
        int result = 0;
        if(term.length() == 0) System.exit(1);
        char firstLetter = term.charAt(0);

        switch(firstLetter){
            case 'a': result = R.raw.a; break;
            case 'b': result = R.raw.b; break;
            case 'c': result = R.raw.c; break;
            case 'd': result = R.raw.d; break;
            case 'e': result = R.raw.e; break;
            case 'f': result = R.raw.f; break;
            case 'g': result = R.raw.g; break;
            case 'h': result = R.raw.h; break;
            case 'i': result = R.raw.i; break;
            case 'j': result = R.raw.j; break;
            case 'k': result = R.raw.k; break;
            case 'l': result = R.raw.l; break;
            case 'm': result = R.raw.m; break;
            case 'n': result = R.raw.n; break;
            case 'o': result = R.raw.o; break;
            case 'p': result = R.raw.p; break;
            case 'q': result = R.raw.q; break;
            case 'r': result = R.raw.r; break;
            case 's': result = R.raw.s; break;
            case 't': result = R.raw.t; break;
            case 'u': result = R.raw.u; break;
            case 'v': result = R.raw.v; break;
            case 'w': result = R.raw.w; break;
            case 'x': result = R.raw.x; break;
            case 'y': result = R.raw.y; break;
            case 'z': result = R.raw.z; break;
            default:
                System.exit(1);
        }
        return result;
    }
}
