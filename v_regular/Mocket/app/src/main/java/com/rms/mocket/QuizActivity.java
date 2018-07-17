package com.rms.mocket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wajahatkarim3.easyflipview.EasyFlipView;

public class QuizActivity extends AppCompatActivity {

    boolean thumbSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        final EasyFlipView cardFlipView = (EasyFlipView) findViewById(R.id.QUIZGAME_easyFlipView_card);
        cardFlipView.setFlipDuration(500);
        final LinearLayout card_front = (LinearLayout) findViewById(R.id.QUIZGAME_linearLayout_card_front);
        final TextView card_back = (TextView) findViewById(R.id.QUIZGAME_textView_definition);


        card_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardFlipView.flipTheView();
                card_front.setClickable(false);
            }
        });



        card_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardFlipView.flipTheView();
                card_front.setClickable(true);
            }
        });

    }



    public void goPreviousActivity(View v){
        this.finish();
    }

    public void thumbsUp(View v){
        /* Show Next button */
        Button button_next = (Button) findViewById(R.id.QUIZGAME_button_next);
        button_next.setVisibility(View.VISIBLE);

        if(thumbSelected){
            ImageView imageView_thumbDown = (ImageView) findViewById(R.id.QUIZGAME_imageView_thumbsDown);
            imageView_thumbDown.setImageResource(R.drawable.thumbs_down);
        }
        ((ImageView)v).setImageResource(R.drawable.thumbs_up_checked);
        thumbSelected  = true;
    }

    public void thumbsDown(View v){
        /* Show Next button */
        Button button_next = (Button) findViewById(R.id.QUIZGAME_button_next);
        button_next.setVisibility(View.VISIBLE);

        if(thumbSelected){
            ImageView imageView_thumbUp = (ImageView) findViewById(R.id.QUIZGAME_imageView_thumbsUp);
            imageView_thumbUp.setImageResource(R.drawable.thumbs_up);
        }
        ((ImageView)v).setImageResource(R.drawable.thumbs_down_checked);
        thumbSelected  = true;
    }
}
