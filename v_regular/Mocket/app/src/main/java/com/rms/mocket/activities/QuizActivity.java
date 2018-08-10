package com.rms.mocket.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rms.mocket.R;
import com.rms.mocket.common.DateUtils;
import com.rms.mocket.database.DatabaseHandler;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class QuizActivity extends AppCompatActivity {


    EasyFlipView cardFlipView;
    LinearLayout card_front;
    TextView card_back;
    TextView textView_term;
    Button button_next;
    LinearLayout back_header;
    LinearLayout front_header;

    boolean thumbSelected = false;
    boolean thumbUp = false;
    boolean cardFront = true;
    String quiz_type;
    DatabaseHandler db;
    ArrayList<HashMap<String, String>> terms = new ArrayList<>();

    String currentTermId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        cardFlipView = (EasyFlipView) findViewById(R.id.QUIZGAME_easyFlipView_card);
        card_front = (LinearLayout) findViewById(R.id.QUIZGAME_linearLayout_card_front);
        card_back = (TextView) findViewById(R.id.QUIZGAME_textView_definition);
        textView_term = (TextView) findViewById(R.id.QUIZGAME_textView_term);
        button_next = (Button) findViewById(R.id.QUIZGAME_button_next);

        front_header = (LinearLayout) findViewById(R.id.QUIZGAME_linearLayout_front_header);
        back_header = (LinearLayout) findViewById(R.id.QUIZGAME_linearLayout_back_header);


        cardFlipView.setFlipDuration(500);
        db = new DatabaseHandler(this.getBaseContext());
        Intent intent = getIntent();
        quiz_type = intent.getExtras().getString("type");

        if(quiz_type.equals("quiz")){
            terms = getTodayTerms();
        }
        else terms = getDailyTest();

        this.showNextTerm();
        this.setNextButtonListener();


        card_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(terms.size()==0) return;

                if (quiz_type.equals("quiz")){
                    back_header.setVisibility(View.INVISIBLE);
                }else{
                    back_header.setVisibility(View.VISIBLE);
                }
                front_header.setVisibility(View.INVISIBLE);
                textView_term.setVisibility(View.INVISIBLE);
                card_back.setVisibility(View.VISIBLE);
                cardFront = false;
                cardFlipView.flipTheView();
                card_front.setClickable(false);
            }
        });


        card_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                front_header.setVisibility(View.VISIBLE);
                textView_term.setVisibility(View.VISIBLE);

                back_header.setVisibility(View.INVISIBLE);
                card_back.setVisibility(View.INVISIBLE);


                cardFront = true;
                cardFlipView.flipTheView();
                card_front.setClickable(true);

            }
        });

    }


    public void goPreviousActivity(View v) {
        this.finish();
    }


    public void thumbsUp(View v) {
        /* Show Next button */
        Button button_next = (Button) findViewById(R.id.QUIZGAME_button_next);
        button_next.setVisibility(View.VISIBLE);

        if (thumbSelected) {
            ImageView imageView_thumbDown = (ImageView) findViewById(R.id.QUIZGAME_imageView_thumbsDown);
            imageView_thumbDown.setImageResource(R.drawable.unhappy);
        }
        ((ImageView) v).setImageResource(R.drawable.happy_checked);
        thumbSelected = true;
        thumbUp = true;
    }


    public void thumbsDown(View v) {
        /* Show Next button */
        final Button button_next = (Button) findViewById(R.id.QUIZGAME_button_next);
        button_next.setVisibility(View.VISIBLE);

        if (thumbSelected) {
            ImageView imageView_thumbUp = (ImageView) findViewById(R.id.QUIZGAME_imageView_thumbsUp);
            imageView_thumbUp.setImageResource(R.drawable.happy);
        }
        ((ImageView) v).setImageResource(R.drawable.unhappy_checked);
        thumbSelected = true;
        thumbUp = false;
    }


    public ArrayList<HashMap<String, String>> getTodayTerms(){
        Cursor cursor_terms = db.getAllTerms();

        ArrayList<HashMap<String, String>> terms = new ArrayList<>();
        while(cursor_terms.moveToNext()){
            String id = cursor_terms.getString(0);
            String term = cursor_terms.getString(1);
            String definition = cursor_terms.getString(2);
            String date_add = cursor_terms.getString(3);

            if(!date_add.equals(DateUtils.getDateToday())) continue;

            HashMap<String, String> temp_hash = new HashMap<>();
            temp_hash.put(DatabaseHandler.COLUMN_ID, id);
            temp_hash.put(DatabaseHandler.COLUMN_TERM, term);
            temp_hash.put(DatabaseHandler.COLUMN_DEFINITION, definition);

            terms.add(temp_hash);
        }
        return terms;
    }


    public ArrayList<HashMap<String, String>> getDailyTest(){
        ArrayList<HashMap<String, String>> terms = new ArrayList<>();
        return terms;
    }


    public void showNextTerm(){

        if(!cardFront) {
            cardFlipView.flipTheView();
            cardFront = true;
        }

        /* When there is no term to be tested*/
        if(terms.size() == 0){
            card_front.setClickable(false);
            cardFlipView.setFlipEnabled(false);
            front_header.setVisibility(View.INVISIBLE);

            textView_term.setText("");
            textView_term.setBackgroundResource(R.drawable.quiz_completed);

        }else {

            int randomNum = ThreadLocalRandom.current().nextInt(0, terms.size());
            String term = terms.get(randomNum).get(DatabaseHandler.COLUMN_TERM);
            String definition = terms.get(randomNum).get(DatabaseHandler.COLUMN_DEFINITION);
            String id = terms.get(randomNum).get(DatabaseHandler.COLUMN_ID);
            currentTermId = id;

            TextView textView_leftTerm = (TextView) findViewById(R.id.QUIZGAME_textView_total);
            textView_leftTerm.setText(Integer.toString(terms.size()));


            /* Set term and definition */
            textView_term.setText(term);
            card_back.setText(definition);

            front_header.setVisibility(View.VISIBLE);

        }


        ImageView imageView_thumbUp = (ImageView) findViewById(R.id.QUIZGAME_imageView_thumbsUp);
        ImageView imageView_thumbDown = (ImageView) findViewById(R.id.QUIZGAME_imageView_thumbsDown);


        imageView_thumbUp.setImageResource(R.drawable.happy);
        imageView_thumbDown.setImageResource(R.drawable.unhappy);


        card_front.setClickable(true);
        button_next.setVisibility(View.GONE);
        textView_term.setVisibility(View.VISIBLE);
        card_back.setVisibility(View.INVISIBLE);
        thumbUp = false;
        thumbSelected = false;

    }

    public void setNextButtonListener() {
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(thumbUp){
                    for (int i=0; i<terms.size(); i++){
                        String id = terms.get(i).get(DatabaseHandler.COLUMN_ID);

                        if (id.equals(currentTermId)){
                            terms.remove(i);
                            break;
                        }
                    }

                    if(!quiz_type.equals("quiz")){
                        //TODO: Update Daily test on database (server and local).
                    }

                }
                showNextTerm();
            }
        });
    }


}
