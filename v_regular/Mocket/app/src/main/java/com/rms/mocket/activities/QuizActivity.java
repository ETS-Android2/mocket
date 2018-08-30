package com.rms.mocket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rms.mocket.R;
import com.rms.mocket.common.DateUtils;
import com.rms.mocket.common.Memory;
import com.rms.mocket.database.FirebaseHandlerTerm;
import com.rms.mocket.database.FirebaseHandlerTest;
import com.rms.mocket.fragments.MemoryFragment;
import com.rms.mocket.fragments.QuizFragment;
import com.rms.mocket.object.Term;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class QuizActivity extends AppCompatActivity {

    public final static String TYPE = "type";

    EasyFlipView cardFlipView;
    LinearLayout card_front;
    TextView card_back;
    TextView textView_term;
    Button button_next;
    LinearLayout back_header;
    LinearLayout front_header;
    TextView textView_added;
    TextView textView_latest;

    boolean thumbSelected = false;
    boolean thumbUp = false;
    boolean cardFront = true;
    String quiz_type;


    DatabaseReference mTermDatabase;
    String user_id;

    String currentTermId;

    ArrayList<Term> terms = new ArrayList<>();

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

        textView_added = (TextView) findViewById(R.id.QUIZGAME_textView_added);
        textView_latest = (TextView) findViewById(R.id.QUIZGAME_textView_lastMemory);

        mTermDatabase = FirebaseDatabase.getInstance().getReference(Term.REFERENCE_TERMS);
        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        cardFlipView.setFlipDuration(500);

        this.setCardLisneners();

        Intent intent = getIntent();
        quiz_type = intent.getExtras().getString(TYPE);




        if(quiz_type.equals(MemoryFragment.TYPE_QUIZ)){
            this.getTodayTerms();

        }
        else this.getDailyTest();

        this.setNextButtonListener();
    }

    public void setCardLisneners(){

        card_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(terms.size()==0) return;

                if (quiz_type.equals(MemoryFragment.TYPE_QUIZ)){
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
            Glide.with(this)  // Activity or Fragment
                    .load(R.drawable.confused_emoticon)
                    .into(imageView_thumbDown);
        }
        Glide.with(this)  // Activity or Fragment
                .load(R.drawable.smile_emoticon_checked)
                .into((ImageView) v);
        thumbSelected = true;
        thumbUp = true;
    }


    public void thumbsDown(View v) {
        /* Show Next button */
        final Button button_next = (Button) findViewById(R.id.QUIZGAME_button_next);
        button_next.setVisibility(View.VISIBLE);

        if (thumbSelected) {
            ImageView imageView_thumbUp = (ImageView) findViewById(R.id.QUIZGAME_imageView_thumbsUp);
            Glide.with(this)  // Activity or Fragment
                    .load(R.drawable.smile_emoticon)
                    .into(imageView_thumbUp);
        }
        Glide.with(this)  // Activity or Fragment
                .load(R.drawable.confused_emoticon_checked)
                .into((ImageView) v);
        thumbSelected = true;
        thumbUp = false;
    }


    public void getTodayTerms(){
        mTermDatabase.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (FirebaseAuth.getInstance().getCurrentUser() == null) return;

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {

                    Term term = child.getValue(Term.class);
                    if (term.date_add.equals(DateUtils.getDateToday())) {
                        terms.add(term);
                    }
                }

                showNextTerm();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void getDailyTest(){
        mTermDatabase.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {
                    Term term = child.getValue(Term.class);

                    String date_test = Memory.getNextDateToMemorize(term.date_latest, term.memory_level);
                    if(date_test.equals(null) || DateUtils.isNotPassed(date_test)) continue;

                    terms.add(term);

                }

                showNextTerm();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
            back_header.setVisibility(View.INVISIBLE);

            textView_term.setText("");
            textView_term.setBackgroundResource(R.drawable.quiz_completed);

        }else {

            int randomNum = ThreadLocalRandom.current().nextInt(0, terms.size());
            String term = terms.get(randomNum).term;
            String definition = terms.get(randomNum).definition;
            String id = terms.get(randomNum).id;
            String added = terms.get(randomNum).date_add;
            String latest = terms.get(randomNum).date_latest;
            currentTermId = id;


            TextView textView_leftTerm = (TextView) findViewById(R.id.QUIZGAME_textView_total);
            textView_leftTerm.setText(Integer.toString(terms.size()));


            /* Set term and definition */
            textView_term.setText(term);
            card_back.setText(definition);

            textView_added.setText(DateUtils.changeDateFormatShort(added));
            textView_latest.setText(DateUtils.changeDateFormatShort(latest));

            front_header.setVisibility(View.VISIBLE);
        }


        ImageView imageView_thumbUp = (ImageView) findViewById(R.id.QUIZGAME_imageView_thumbsUp);
        ImageView imageView_thumbDown = (ImageView) findViewById(R.id.QUIZGAME_imageView_thumbsDown);

        Glide.with(this)  // Activity or Fragment
                .load(R.drawable.smile_emoticon)
                .into(imageView_thumbUp);
        Glide.with(this)  // Activity or Fragment
                .load(R.drawable.confused_emoticon)
                .into(imageView_thumbDown);


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
                    Term currentTerm = null;

                    for (int i=0; i<terms.size(); i++){
                        String id = terms.get(i).id;

                        if (id.equals(currentTermId)){
                            currentTerm = terms.get(i);
                            terms.remove(i);
                            break;
                        }
                    }

                    if(quiz_type.equals(QuizFragment.TYPE_TEST)){

                        Term updated_term = Memory.updateMemoryLevel(currentTerm);

                        FirebaseHandlerTerm.updateTerm(currentTermId, updated_term);

                        FirebaseHandlerTest.increaseCount(DateUtils.getDateToday());

                    }
                }
                showNextTerm();
            }
        });
    }


}
