package com.rms.mocket.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rms.mocket.R;
import com.rms.mocket.common.DateUtils;
import com.rms.mocket.common.VibratorUtils;
import com.rms.mocket.database.DatabaseHandlerGame;
import com.rms.mocket.database.DatabaseHandlerTerms;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class GameActivity extends AppCompatActivity {

    boolean answerPicked = false;

    DatabaseHandlerTerms db_term;
    DatabaseHandlerGame db_game;
    ArrayList<HashMap<String, String>> terms = new ArrayList<>();

    EasyFlipView easyFlipView1;
    EasyFlipView easyFlipView2;
    EasyFlipView easyFlipView3;
    EasyFlipView easyFlipView4;

    boolean easyFlipView1_isFront = true;
    boolean easyFlipView2_isFront = true;
    boolean easyFlipView3_isFront = true;
    boolean easyFlipView4_isFront = true;

    TextView textView_term;

    TextView textView_today_correct;
    TextView textView_today_incorrect;

    TextView answer1_front;
    TextView answer1_back;
    TextView answer2_front;
    TextView answer2_back;
    TextView answer3_front;
    TextView answer3_back;
    TextView answer4_front;
    TextView answer4_back;

    int answer_card = 0;
    int correct_count = 0;
    int incorrect_count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        db_term = new DatabaseHandlerTerms(this.getBaseContext());
        db_game = new DatabaseHandlerGame(this.getBaseContext());
        terms = getAllTerms();

        this.init();
        this.setAnswerClickListeners();
        this.showNextTerm();

    }


    public void init(){
        textView_term = (TextView) findViewById(R.id.GAME_textView_term);
        textView_today_correct = (TextView) findViewById(R.id.GAME_textView_todayCorrect);
        textView_today_incorrect = (TextView) findViewById(R.id.GAME_textView_todayIncorrect);

        easyFlipView1 = (EasyFlipView) findViewById(R.id.GAME_easyFlipView_answer1);
        easyFlipView1.setFlipDuration(500);
        easyFlipView1.setFlipEnabled(true);

        easyFlipView2 = (EasyFlipView) findViewById(R.id.GAME_easyFlipView_answer2);
        easyFlipView2.setFlipDuration(500);
        easyFlipView2.setFlipEnabled(true);

        easyFlipView3 = (EasyFlipView) findViewById(R.id.GAME_easyFlipView_answer3);
        easyFlipView3.setFlipDuration(500);
        easyFlipView3.setFlipEnabled(true);

        easyFlipView4 = (EasyFlipView) findViewById(R.id.GAME_easyFlipView_answer4);
        easyFlipView4.setFlipDuration(500);
        easyFlipView4.setFlipEnabled(true);

        answer1_front = (TextView) findViewById(R.id.GAME_textView_answer1_front);
        answer1_back = (TextView) findViewById(R.id.GAME_textView_answer1_back);

        answer2_front = (TextView) findViewById(R.id.GAME_textView_answer2_front);
        answer2_back = (TextView) findViewById(R.id.GAME_textView_answer2_back);

        answer3_front = (TextView) findViewById(R.id.GAME_textView_answer3_front);
        answer3_back = (TextView) findViewById(R.id.GAME_textView_answer3_back);

        answer4_front = (TextView) findViewById(R.id.GAME_textView_answer4_front);
        answer4_back = (TextView) findViewById(R.id.GAME_textView_answer4_back);
    }


    public ArrayList<HashMap<String, String>> getAllTerms(){
        Cursor cursor_terms = db_term.getAllTerms();
        ArrayList<HashMap<String, String>> terms = new ArrayList<>();

        while(cursor_terms.moveToNext()){
            String id = cursor_terms.getString(DatabaseHandlerTerms.INDEX_ID);
            String term = cursor_terms.getString(DatabaseHandlerTerms.INDEX_TERM);
            String definition = cursor_terms.getString(DatabaseHandlerTerms.INDEX_DEFINITION);
            String date_add = cursor_terms.getString(DatabaseHandlerTerms.INDEX_DATE_ADD);
            String date_latest = cursor_terms.getString(DatabaseHandlerTerms.INDEX_DATE_LATEST);
            String memory_level = cursor_terms.getString(DatabaseHandlerTerms.INDEX_MEMORY_LEVEL);

            HashMap<String, String> temp_hash = new HashMap<>();
            temp_hash.put(DatabaseHandlerTerms.COLUMN_ID, id);
            temp_hash.put(DatabaseHandlerTerms.COLUMN_TERM, term);
            temp_hash.put(DatabaseHandlerTerms.COLUMN_DEFINITION, definition);
            temp_hash.put(DatabaseHandlerTerms.COLUMN_DATE_ADD, date_add);
            temp_hash.put(DatabaseHandlerTerms.COLUMN_DATE_LATEST, date_latest);
            temp_hash.put(DatabaseHandlerTerms.COLUMN_MEMORY_LEVEL, memory_level);

            terms.add(temp_hash);
        }
        return terms;
    }


    public void setAnswerClickListeners(){

        /* Answer 1 FlipView */
        answer1_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easyFlipView1.flipTheView();
                if(answer_card == 1){
                    markCorrect(answer1_back);
                }else{
                    markIncorrect(answer1_back);
                }
                if (easyFlipView1_isFront) easyFlipView1_isFront = false;
                else easyFlipView1_isFront = true;

            }
        });


        /* Answer 2 FlipView */
        answer2_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easyFlipView2.flipTheView();
                if(answer_card == 2){
                    markCorrect(answer2_back);
                }else{
                    markIncorrect(answer2_back);
                }
                if (easyFlipView2_isFront) easyFlipView2_isFront = false;
                else easyFlipView2_isFront = true;


            }
        });


        /* Answer 3 FlipView */
        answer3_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easyFlipView3.flipTheView();
                if(answer_card == 3){
                    markCorrect(answer3_back);
                }else{
                    markIncorrect(answer3_back);
                }
                if (easyFlipView3_isFront) easyFlipView3_isFront = false;
                else easyFlipView3_isFront = true;


            }
        });



        /* Answer 4 FlipView */
        answer4_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easyFlipView4.flipTheView();
                if(answer_card == 4){
                    markCorrect(answer4_back);
                }else{
                    markIncorrect(answer4_back);
                }
                if (easyFlipView4_isFront) easyFlipView4_isFront = false;
                else easyFlipView4_isFront = true;

            }
        });

    }


    public void showNextTerm(){
        easyFlipView1.setVisibility(View.GONE);
        easyFlipView2.setVisibility(View.GONE);
        easyFlipView3.setVisibility(View.GONE);
        easyFlipView4.setVisibility(View.GONE);

        /* When there is no term to be tested*/
        if(terms.size() == 0){
            textView_term.setText("");
            textView_term.setBackgroundResource(R.drawable.quiz_completed);

        }else {

            int randomNum = ThreadLocalRandom.current().nextInt(0, terms.size());
            String term = terms.get(randomNum).get(DatabaseHandlerTerms.COLUMN_TERM);
            String real_definition = terms.get(randomNum).get(DatabaseHandlerTerms.COLUMN_DEFINITION);

            correct_count = db_game.getCorrectCount(DateUtils.getDateToday());
            incorrect_count = db_game.getIncorrectCount(DateUtils.getDateToday());

            textView_today_correct.setText(Integer.toString(correct_count));
            textView_today_incorrect.setText(Integer.toString(incorrect_count));
            textView_term.setText(term);

            /* Add real answer to the array list. */
            ArrayList<String> answers = new ArrayList<>();
            answers.add(real_definition);

            /* Randomly pick 3 definition from all terms. */
            ArrayList<Integer> indexes = new ArrayList<>();
            indexes.add(randomNum);
            while(answers.size() != 4 && indexes.size() != terms.size()){
                int temp_randomNum = ThreadLocalRandom.current().nextInt(0, terms.size());
                if(indexes.contains(temp_randomNum)) continue;

                String temp_definition = terms.get(temp_randomNum).get(DatabaseHandlerTerms.COLUMN_DEFINITION);
                answers.add(temp_definition);
                indexes.add(temp_randomNum);
            }

            Collections.shuffle(answers);

            for(int i=1; i<answers.size()+1; i++){
                String definition = answers.get(i-1);
                switch(i){
                    case 1:
                        answer1_front.setText(definition);
                        if(definition.equals(real_definition)) answer_card = 1;
                        easyFlipView1.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        answer2_front.setText(definition);
                        if(definition.equals(real_definition)) answer_card = 2;
                        easyFlipView2.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        answer3_front.setText(definition);
                        if(definition.equals(real_definition)) answer_card = 3;
                        easyFlipView3.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        answer4_front.setText(definition);
                        if(definition.equals(real_definition)) answer_card = 4;
                        easyFlipView4.setVisibility(View.VISIBLE);
                        break;

                }
            }

        }
    }


    public void markCorrect(TextView v) {

        if (!answerPicked) {
            ImageView imageView_correctIncorrect = (ImageView) findViewById(R.id.GAME_imageView_correct_incorrect);
            Glide.with(this)  // Activity or Fragment
                    .load(R.drawable.correct)
                    .into(imageView_correctIncorrect);
            imageView_correctIncorrect.setVisibility(View.VISIBLE);
            VibratorUtils.vibrateCorrect(this);
            int new_correct_count = db_game.getCorrectCount(DateUtils.getDateToday()) + 1;
            int new_incorrect_count = db_game.getIncorrectCount(DateUtils.getDateToday());
            db_game.setCount(DateUtils.getDateToday(), new_correct_count, new_incorrect_count);
            textView_today_correct.setText(Integer.toString(new_correct_count));
            //TODO: Update with database server.
        }

        answerPicked = true;
        v.setText("Correct");
        v.setTextSize(20);
        v.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.correct_green));
        Button button_next = (Button) findViewById(R.id.GAME_button_next);
        button_next.setVisibility(View.VISIBLE);

    }

    public void markIncorrect(TextView v) {

        if (!answerPicked) {
            ImageView imageView_correctIncorrect = (ImageView) findViewById(R.id.GAME_imageView_correct_incorrect);
            Glide.with(this)  // Activity or Fragment
                    .load(R.drawable.incorrect)
                    .into(imageView_correctIncorrect);
            imageView_correctIncorrect.setVisibility(View.VISIBLE);
            VibratorUtils.vibrateIncorrect(this);
            int new_correct_count = db_game.getCorrectCount(DateUtils.getDateToday());
            int new_incorrect_count = db_game.getIncorrectCount(DateUtils.getDateToday()) + 1;
            db_game.setCount(DateUtils.getDateToday(), new_correct_count, new_incorrect_count);
            textView_today_incorrect.setText(Integer.toString(new_incorrect_count));
            //TODO: Update with database server.
        }

        answerPicked = true;
        v.setText("Incorrect");
        v.setTextSize(20);
        v.setTextColor(getResources().getColor(R.color.mocket_red_light));
    }

    public void goPreviousActivity(View v) {
        this.finish();
    }

    public void clickNextButton(View v) {
        this.showNextTerm();
        answerPicked = false;
        ImageView imageView_correctIncorrect = (ImageView) findViewById(R.id.GAME_imageView_correct_incorrect);
        imageView_correctIncorrect.setVisibility(View.GONE);
        Button button_next = (Button) findViewById(R.id.GAME_button_next);
        button_next.setVisibility(View.GONE);


        /* Reset scrolls. */
        ScrollView scrollView_answer1 = (ScrollView) findViewById(R.id.GAME_scrollView_answer1);
        ScrollView scrollView_answer2 = (ScrollView) findViewById(R.id.GAME_scrollView_answer2);
        ScrollView scrollView_answer3 = (ScrollView) findViewById(R.id.GAME_scrollView_answer3);
        ScrollView scrollView_answer4 = (ScrollView) findViewById(R.id.GAME_scrollView_answer4);
        scrollView_answer1.scrollTo(0,0);
        scrollView_answer2.scrollTo(0,0);
        scrollView_answer3.scrollTo(0,0);
        scrollView_answer4.scrollTo(0,0);

        if(!easyFlipView1_isFront){
            easyFlipView1.flipTheView();
            easyFlipView1_isFront = true;
        }
        if(!easyFlipView2_isFront){
            easyFlipView2.flipTheView();
            easyFlipView2_isFront = true;
        }
        if(!easyFlipView3_isFront){
            easyFlipView3.flipTheView();
            easyFlipView3_isFront = true;
        }
        if(!easyFlipView4_isFront){
            easyFlipView4.flipTheView();
            easyFlipView4_isFront = true;
        }

    }
}
