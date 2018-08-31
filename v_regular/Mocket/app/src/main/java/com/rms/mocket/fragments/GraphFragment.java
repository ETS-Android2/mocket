package com.rms.mocket.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rms.mocket.R;
import com.rms.mocket.activities.LoginActivity;
import com.rms.mocket.common.DateUtils;
import com.rms.mocket.common.KeyboardUtils;
import com.rms.mocket.common.TermUtils;
import com.rms.mocket.common.Utils;
import com.rms.mocket.common.VibratorUtils;
import com.rms.mocket.database.FirebaseHandlerGame;
import com.rms.mocket.database.FirebaseHandlerTerm;
import com.rms.mocket.database.FirebaseHandlerTest;
import com.rms.mocket.object.Term;

import java.util.ArrayList;
import java.util.HashMap;


public class GraphFragment extends Fragment {

    private CombinedChart mChart;

    public static final String TYPE_MONTH = "month";
    public static final String TYPE_WEEK = "week";
    public static final String TYPE_YEAR = "year";

    public static final String CATEGORY_TERM = "Memory";
    public static final String CATEGORY_TEST = "Test";
    public static final String CATEGORY_GAME = "Game";

    private boolean visibility_termList = false;

    private TextView textView_allTermTitle;
    private String currentFilter = "";
    private View rootView;
    private LinearLayout linearLayout_graph;
    private LinearLayout linearLayout_all_term;
    private LinearLayout linearLayout_searchView;

    String current_graph_type;

    DatabaseReference mTermDatabase;
    DatabaseReference mTestDatabase;
    DatabaseReference mGameDatabase;
    String user_id;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_graph, container, false);

//        db = new DatabaseHandlerTerms(rootView.getContext());
        mTermDatabase = FirebaseDatabase.getInstance().getReference(Term.REFERENCE_TERMS);
        mTestDatabase = FirebaseDatabase.getInstance().getReference(FirebaseHandlerTest.REFERENCE_TEST);
        mGameDatabase = FirebaseDatabase.getInstance().getReference(FirebaseHandlerGame.REFERENCE_GAME);
        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        textView_allTermTitle =
                (TextView) rootView.findViewById(R.id.GRAPH_textView_allTermsTitle);
        linearLayout_graph = (LinearLayout) rootView.findViewById(R.id.GRAPH_linearLayout_graph);
        linearLayout_all_term = (LinearLayout) rootView.findViewById(R.id.GRAPH_linearLayout_allTerms);
        linearLayout_searchView = (LinearLayout) rootView.findViewById(R.id.GRAPH_linearLayout_searchView);


        /* Display total term count in database. */
        mTermDatabase.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (FirebaseAuth.getInstance().getCurrentUser() == null) return;
                int count = (int) dataSnapshot.getChildrenCount();
                textView_allTermTitle.setText("All Terms in Memory (" + Integer.toString(count) + ")");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        this.setTimeButtonListener();
        this.setExpandButtonListener();
        this.setSearchViewListener();
        this.setGraph(TYPE_WEEK);
        Utils.log("Graph.onCreate()");

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if(!user.isEmailVerified()){
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(rootView.getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        } else{
            Intent intent = new Intent(rootView.getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        Button button_week  = (Button) rootView.findViewById(R.id.GRAPH_button_week);
        Button button_month  = (Button) rootView.findViewById(R.id.GRAPH_button_month);
        Button button_year  = (Button) rootView.findViewById(R.id.GRAPH_button_year);
        button_week.setPaintFlags(0);
        button_month.setPaintFlags(0);
        button_year.setPaintFlags(0);
        button_week.setPaintFlags(button_week.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Utils.log("Graph.onResume()");

    }

    public void setGraph(String graph_type){

        current_graph_type = graph_type;

        mChart = rootView.findViewById(R.id.GRAPH_graphView_graph);
        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);

        // draw bars behind lines
        mChart.setDrawOrder(new DrawOrder[]{
                DrawOrder.BAR, DrawOrder.LINE
        });

        Legend l = mChart.getLegend();
        l.setFormSize(11);
        l.setTextSize(11);
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);


        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(true);
        rightAxis.setAxisMinimum(-0.9f);
        rightAxis.setTextSize(11);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMinimum(-0.9f);
        leftAxis.setTextSize(11);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTH_SIDED);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(10);


        if(graph_type.equals(TYPE_YEAR)){
            String[] mType= DateUtils.orderMonth(DateUtils.getDateToday());
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return mType[(int) value % mType.length];
                }
            });
        }else if(graph_type.equals(TYPE_MONTH)){
            String[] mType= DateUtils.orderDate(DateUtils.getDateToday());
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return mType[(int) value % mType.length];
                }
            });
        }else if(graph_type.equals(TYPE_WEEK)){
            String[] mType= DateUtils.orderDay(DateUtils.getDateToday());
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return mType[(int) value % mType.length];
                }
            });
        }


        CombinedData data = new CombinedData();
        mChart.animateXY(1000, 1000);


        /* Update Term data */
        mTermDatabase.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) return;

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                HashMap<String, Integer> term_count = new HashMap<>();

                for(DataSnapshot child: children) {
                    Term term = child.getValue(Term.class);
                    if(term_count.containsKey(term.date_add)){
                        term_count.put(term.date_add, term_count.get(term.date_add)+1);
                    }else{
                        term_count.put(term.date_add, 1);
                    }
                }

                String[] order=null;
                ArrayList<Integer> term_data = new ArrayList<>();
                switch(graph_type){
                    case GraphFragment.TYPE_WEEK:
                        order = DateUtils.orderDayNumber(DateUtils.getDateToday());
                        for(int i=0; i<order.length; i++){
                            String day = order[i];
                            int count = 0;
                            for(String date: term_count.keySet()) {
                                if(date.equals(day)){
                                    count += term_count.get(date);
                                }
                            }
                            term_data.add(count);
                        }
                        break;

                    case GraphFragment.TYPE_MONTH:
                        order = DateUtils.orderDateNumber(DateUtils.getDateToday());
                        for(int i=0; i<order.length; i++){
                            String day = order[i];
                            int count = 0;
                            for(String date: term_count.keySet()) {
                                if(date.equals(day)){
                                    count += term_count.get(date);
                                }
                            }
                            term_data.add(count);
                        }
                        break;

                    case GraphFragment.TYPE_YEAR:
                        order = DateUtils.orderMonthNumber(DateUtils.getDateToday());
                        for(int i=0; i<order.length; i++){
                            String month = order[i];
                            int count = 0;
                            for(String date: term_count.keySet()) {
                                if(date.startsWith(month)){
                                    count += term_count.get(date);
                                }
                            }
                            term_data.add(count);
                        }
                        break;
                }

                ArrayList<Entry> entries = new ArrayList<Entry>();
                for (int index = 0; index < term_data.size(); index++)
                    entries.add(new Entry(index , term_data.get(index)));
                LineDataSet term_set = new LineDataSet(entries, CATEGORY_TERM);


                term_set.setColor(Color.rgb(240,238,7));
                term_set.setLineWidth(3f);
                term_set.setCircleColor(Color.rgb(240,238,7));
                term_set.setCircleSize(4f);
                term_set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                term_set.setDrawValues(true);
                term_set.setValueTextSize(0f);
                term_set.setValueTextColor(Color.rgb(240,238,7));
                term_set.setHighLightColor(Color.MAGENTA);
                term_set.setAxisDependency(YAxis.AxisDependency.LEFT);

                LineData mlineData = new LineData(term_set);


                /* Update Test Data */
                mTestDatabase.child(user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;

                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                        HashMap<String, Integer> test_count = new HashMap<>();

                        for(DataSnapshot child: children) {
                            test_count.put(DateUtils.revertDate(child.getKey()), Integer.parseInt(child.getValue(String.class)));
                        }

                        String[] order=null;
                        ArrayList<Integer> test_data = new ArrayList<>();
                        switch(graph_type){
                            case GraphFragment.TYPE_WEEK:
                                order = DateUtils.orderDayNumber(DateUtils.getDateToday());
                                for(int i=0; i<order.length; i++){
                                    String day = order[i];
                                    int count = 0;
                                    for(String date: test_count.keySet()) {
                                        if(date.equals(day)){
                                            count += test_count.get(date);
                                        }
                                    }
                                    test_data.add(count);
                                }
                                break;

                            case GraphFragment.TYPE_MONTH:
                                order = DateUtils.orderDateNumber(DateUtils.getDateToday());
                                for(int i=0; i<order.length; i++){
                                    String day = order[i];
                                    int count = 0;
                                    for(String date: test_count.keySet()) {
                                        if(date.equals(day)){
                                            count += test_count.get(date);
                                        }
                                    }
                                    test_data.add(count);
                                }
                                break;

                            case GraphFragment.TYPE_YEAR:
                                order = DateUtils.orderMonthNumber(DateUtils.getDateToday());
                                for(int i=0; i<order.length; i++){
                                    String month = order[i];
                                    int count = 0;
                                    for(String date: test_count.keySet()) {
                                        if(date.startsWith(month)){
                                            count += test_count.get(date);
                                        }
                                    }
                                    test_data.add(count);
                                }
                                break;
                        }

                        ArrayList<Entry> entries = new ArrayList<Entry>();

                        for (int index = 0; index < test_data.size(); index++)
                            entries.add(new Entry(index , test_data.get(index)));
                        LineDataSet test_set = new LineDataSet(entries, CATEGORY_TEST);


                        test_set.setColor(Color.rgb(162,234,242));
                        test_set.setLineWidth(2f);
                        test_set.setCircleColor(Color.rgb(162,234,242));
                        test_set.setCircleSize(3f);
                        test_set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                        test_set.setDrawValues(true);
                        test_set.setValueTextSize(0f);
                        test_set.setValueTextColor(Color.rgb(162,234,242));
                        test_set.setHighLightColor(Color.MAGENTA);
                        test_set.setAxisDependency(YAxis.AxisDependency.LEFT);

                        mlineData.addDataSet((new LineData(test_set)).getDataSetByIndex(0));

                        /* Update Game Data */
                        mGameDatabase.child(user_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (FirebaseAuth.getInstance().getCurrentUser() == null) return;

                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                                HashMap<String, Integer> game_count_correct = new HashMap<>();
                                HashMap<String, Integer> game_count_incorrect = new HashMap<>();

                                for(DataSnapshot child: children) {
                                    game_count_correct.put(DateUtils.revertDate(child.getKey()),
                                            Integer.parseInt(child.child(FirebaseHandlerGame.CORRECT).getValue(String.class)));
                                    game_count_incorrect.put(DateUtils.revertDate(child.getKey()),
                                            Integer.parseInt(child.child(FirebaseHandlerGame.INCORRECT).getValue(String.class)));
                                }

                                ArrayList<Integer> game_data_correct = new ArrayList<>();
                                ArrayList<Integer> game_data_incorrect = new ArrayList<>();

                                String[] order = null;
                                switch(graph_type){
                                    case GraphFragment.TYPE_WEEK:
                                        order = DateUtils.orderDayNumber(DateUtils.getDateToday());
                                        for(int i=0; i<order.length; i++){
                                            String day = order[i];
                                            int correct_count = 0;
                                            int incorrect_count = 0;
                                            for(String date: game_count_correct.keySet()) {
                                                if(date.equals(day)){
                                                    correct_count += game_count_correct.get(date);
                                                    incorrect_count += game_count_incorrect.get(date);
                                                }
                                            }
                                            game_data_correct.add(correct_count);
                                            game_data_incorrect.add(incorrect_count);
                                        }
                                        break;

                                    case GraphFragment.TYPE_MONTH:
                                        order = DateUtils.orderDateNumber(DateUtils.getDateToday());
                                        for(int i=0; i<order.length; i++){
                                            String day = order[i];
                                            int correct_count = 0;
                                            int incorrect_count = 0;
                                            for(String date: game_count_correct.keySet()) {
                                                if(date.equals(day)){
                                                    correct_count += game_count_correct.get(date);
                                                    incorrect_count += game_count_incorrect.get(date);
                                                }
                                            }
                                            game_data_correct.add(correct_count);
                                            game_data_incorrect.add(incorrect_count);
                                        }
                                        break;

                                    case GraphFragment.TYPE_YEAR:
                                        order = DateUtils.orderMonthNumber(DateUtils.getDateToday());
                                        for(int i=0; i<order.length; i++){
                                            String month = order[i];
                                            int correct_count = 0;
                                            int incorrect_count = 0;
                                            for(String date: game_count_correct.keySet()) {
                                                if(date.startsWith(month)){
                                                    correct_count += game_count_correct.get(date);
                                                    incorrect_count += game_count_incorrect.get(date);
                                                }
                                            }
                                            game_data_correct.add(correct_count);
                                            game_data_incorrect.add(incorrect_count);
                                        }
                                        break;
                                }

                                ArrayList<BarEntry> entry = new ArrayList<BarEntry>();

                                for (int index = 0; index < game_data_correct.size(); index++) {
                                    // stacked
                                    entry.add(new BarEntry(index, new float[]{
                                            game_data_correct.get(index), game_data_incorrect.get(index)}));
                                }

                                BarDataSet game_set = new BarDataSet(entry, "- Game");
                                game_set.setStackLabels(new String[]{"Correct", "Incorrect"});
                                game_set.setColors(new int[]{Color.rgb(211,233,211),Color.rgb(246,214,214)});
                                game_set.setValueTextSize(0f);
                                game_set.setAxisDependency(YAxis.AxisDependency.LEFT);

                                BarData bar_data = new BarData(game_set);

                                data.setData(mlineData);
                                data.setData(bar_data);

                                Typeface mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");
                                data.setValueTypeface(mTfLight);

                                xAxis.setAxisMaximum(data.getXMax() + 0.25f);

                                mChart.setData(data);
                                mChart.invalidate();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void setTimeButtonListener(){

        Button week_button = (Button) rootView.findViewById(R.id.GRAPH_button_week);
        Button month_button = (Button) rootView.findViewById(R.id.GRAPH_button_month);
        Button year_button = (Button) rootView.findViewById(R.id.GRAPH_button_year);

        week_button.setPaintFlags(week_button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        week_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                week_button.setPaintFlags(0);
                month_button.setPaintFlags(0);
                year_button.setPaintFlags(0);

                setGraph(TYPE_WEEK);
                Button button = (Button) view;
                button.setPaintFlags(button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            }
        });


        month_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                week_button.setPaintFlags(0);
                month_button.setPaintFlags(0);
                year_button.setPaintFlags(0);

                setGraph(TYPE_MONTH);
                Button button = (Button) view;
                button.setPaintFlags(button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            }
        });


        year_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                week_button.setPaintFlags(0);
                month_button.setPaintFlags(0);
                year_button.setPaintFlags(0);

                setGraph(TYPE_YEAR);
                Button button = (Button) view;
                button.setPaintFlags(button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            }
        });

    }

    public void setExpandButtonListener(){
        /**
         * OnClick: expand arrow on [All Terms in Memory] section.
         *  @ Shrink:
         *  @ Expand: Show ListView of [All Terms in Memory].
         * */
        final ImageView imageView_expand = (ImageView) rootView.findViewById(R.id.GRAPH_imageView_expand);
        imageView_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ListView listView_termList = (ListView) rootView.findViewById(R.id.GRAPH_listView_termList);
                if (visibility_termList) {
                    /* Reset the terms */;

                    listView_termList.setAdapter(null);
                    imageView_expand.setImageResource(R.drawable.expand_button);
                    linearLayout_graph.setVisibility(View.VISIBLE);
                    listView_termList.setVisibility(View.GONE);
                    linearLayout_searchView.setVisibility(View.GONE);
                    visibility_termList = false;
                } else {
                    /* Display All terms */
                    updateTermList();

                    imageView_expand.setImageResource(R.drawable.shrink_button);
                    linearLayout_graph.setVisibility(View.GONE);
                    listView_termList.setVisibility(View.VISIBLE);
                    linearLayout_searchView.setVisibility(View.VISIBLE);
                    visibility_termList = true;

                }

            }
        });
    }


    public void setSearchViewListener(){
        SearchView searchView = (SearchView) rootView.findViewById(R.id.GRAPH_searchView_term);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                currentFilter = s;
                updateTermList();
                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    if(!visibility_termList){
                        ImageView imageView_expand = (ImageView) rootView.findViewById(R.id.GRAPH_imageView_expand);
                        imageView_expand.performClick();
                    }
                }
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                currentFilter="";
                return false;
            }
        });

    }

    public void updateTermList(){

        mTermDatabase.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (FirebaseAuth.getInstance().getCurrentUser() == null) return;

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                ArrayList<Term> terms = new ArrayList<>();

                for(DataSnapshot child: children) {
                    Term term = child.getValue(Term.class);

                    if(!term.term.toLowerCase().contains(currentFilter.toLowerCase())) continue;

                    terms.add(term);
                }

                ArrayList<Term> sorted_terms = TermUtils.sortTerms(terms);
                textView_allTermTitle.setText("All Terms in Memory (" + sorted_terms.size() + ")");

                /* Display Today's terms */
                ListView listView_termList = (ListView) rootView.findViewById(R.id.GRAPH_listView_termList);
                ListAdapter adapter = new GraphFragment.TermListAdapter(getActivity(), R.layout.term_item, sorted_terms, rootView);
                listView_termList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    /**
     * This ArrayAdapter is for handling term ListView dynamically.
     */
    class TermListAdapter extends ArrayAdapter {
        Context context;
        int layoutResourceId;
        ArrayList<Term> data = null;
        View rootView;

        public TermListAdapter(Context context,
                               int layoutResourceId,
                               ArrayList<Term> data, View rootView) {
            super(context, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.data = data;
            this.rootView = rootView;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(layoutResourceId, viewGroup, false);

            TextView textView_term = (TextView) view.findViewById(R.id.TERMITEM_term);
            TextView textView_definition = (TextView) view.findViewById(R.id.TERMITEM_definition);
            textView_term.setText(data.get(i).term);
            textView_definition.setText(data.get(i).definition);

            Term term = data.get(i);
            String term_id = data.get(i).id;

            ImageView imageView_edit = (ImageView) view.findViewById(R.id.TERMITEM_editButton);

            /* Image optimizer */
            Glide.with(rootView.getContext())  // Activity or Fragment
                    .load(R.drawable.edit_icon)
                    .into(imageView_edit);

            RelativeLayout layout_edit = (RelativeLayout) view.findViewById(R.id.TERMITEM_layout_editButton);
            layout_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(rootView.getContext());
                    View mView = getLayoutInflater().inflate(R.layout.edit_term_dialogue, null);

                    /* Initialize the blanks. */
                    EditText editText_term = (EditText) mView.findViewById(R.id.EDITTERM_editText_term);
                    EditText editText_definition = (EditText) mView.findViewById(R.id.EDITTERM_editText_definition);
                    TextView textView_addedDate = (TextView) mView.findViewById(R.id.EDITTERM_textView_addedDate);
                    TextView textView_lastMemorizedDate = (TextView) mView.findViewById(R.id.EDITTERM_textView_lastMemorizedDate);
                    editText_term.setText(term.term);
                    editText_definition.setText(term.definition);
                    textView_addedDate.setText(term.date_add);
                    textView_lastMemorizedDate.setText(term.date_latest);

                    Button button_save = (Button) mView.findViewById(R.id.EDITTERM_button_save);
                    Button button_cancel = (Button) mView.findViewById(R.id.EDITTERM_button_cancel);
                    Button button_delete = (Button) mView.findViewById(R.id.EDITTERM_button_delete);

                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.create();

                    button_save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            /* When one of the blanks is empty. */
                            if (editText_term.getText().toString().isEmpty()
                                    || editText_definition.getText().toString().isEmpty()) {
                                Toast.makeText(getContext(), "Some blank is empty.", Toast.LENGTH_LONG).show();
                            } else {
                                term.term = editText_term.getText().toString();
                                term.definition = editText_definition.getText().toString();

                                Utils.log("Before FirebaseHandlerTerm.updateTerm()");
                                FirebaseHandlerTerm.updateTerm(term_id, term);
                                Utils.log("After FirebaseHandlerTerm.updateTerm()");

                                dialog.dismiss();

                                updateTermList();

                                VibratorUtils.vibrateAlert(rootView.getContext());
                                Toast.makeText(getContext(), "Saved.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    button_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    button_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (button_delete.getText().toString().equals("Delete")) {
                                button_delete.setText("Confirm");
                            } else {

                                FirebaseHandlerTerm.deleteTerm(term_id);

                                KeyboardUtils.hideKeyboard(getActivity());
                                dialog.dismiss();
                                updateTermList();
                                VibratorUtils.vibrateAlert(rootView.getContext());
                                Toast.makeText(getContext(), "Deleted.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    dialog.show();
                }

            });

            return view;
        }
    }

}
