package com.rms.mocket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Fragment selectedFragment = new MemoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.MAIN_frameLayout_content,
                selectedFragment).commit();


    }



    /* OnClick: when a category is clicked. */
    public void categoryClicked(View v){
        ImageView imageView_memory= (ImageView) findViewById(R.id.MAIN_imageView_memory);
        ImageView imageView_quiz= (ImageView) findViewById(R.id.MAIN_imageView_quiz);
        ImageView imageView_graph= (ImageView) findViewById(R.id.MAIN_imageView_graph);
        ImageView imageView_more= (ImageView) findViewById(R.id.MAIN_imageView_more);

        imageView_memory.setImageResource(R.drawable.memory_icon);
        imageView_quiz.setImageResource(R.drawable.quiz_icon);
        imageView_graph.setImageResource(R.drawable.graph_icon);
        imageView_more.setImageResource(R.drawable.more_icon);

        RelativeLayout relativeLayout_memory = (RelativeLayout) findViewById(R.id.MAIN_relativeLayout_memory);
        RelativeLayout relativeLayout_quiz = (RelativeLayout) findViewById(R.id.MAIN_relativeLayout_quiz);
        RelativeLayout relativeLayout_graph = (RelativeLayout) findViewById(R.id.MAIN_relativeLayout_graph);
        RelativeLayout relativeLayout_more = (RelativeLayout) findViewById(R.id.MAIN_relativeLayout_more);

        relativeLayout_memory.setBackgroundResource(R.drawable.no_bottom_border);
        relativeLayout_quiz.setBackgroundResource(R.drawable.no_bottom_border);
        relativeLayout_graph.setBackgroundResource(R.drawable.no_bottom_border);
        relativeLayout_more.setBackgroundResource(R.drawable.no_bottom_border);

        v.setBackgroundResource(R.drawable.bottom_border);

        Fragment selectedFragment = null;

        switch(v.getId()){

            case R.id.MAIN_relativeLayout_memory:
                imageView_memory.setImageResource(R.drawable.clicked_memory_icon);
                selectedFragment = new MemoryFragment();

                break;

            case R.id.MAIN_relativeLayout_quiz:
                imageView_quiz.setImageResource(R.drawable.clicked_quiz_icon);
                selectedFragment = new QuizFragment();
                break;

            case R.id.MAIN_relativeLayout_graph:
                imageView_graph.setImageResource(R.drawable.clicked_graph_icon);
                selectedFragment = new GraphFragment();
                break;

            case R.id.MAIN_relativeLayout_more:
                imageView_more.setImageResource(R.drawable.clicked_more_icon);
                selectedFragment = new MoreFragment();
                break;


        }

        getSupportFragmentManager().beginTransaction().replace(R.id.MAIN_frameLayout_content,
                selectedFragment).commit();

    }


}
