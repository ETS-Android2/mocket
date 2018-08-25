package com.rms.mocket.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
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
import com.rms.mocket.R;
import com.rms.mocket.common.DateUtils;
import com.rms.mocket.common.GraphUtils;
import com.rms.mocket.common.KeyboardUtils;
import com.rms.mocket.common.TermUtils;
import com.rms.mocket.common.VibratorUtils;
import com.rms.mocket.database.DatabaseHandlerTerms;

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
    private ArrayList<HashMap<String, String>> exampleTerm = new ArrayList<>();
    private DatabaseHandlerTerms db;
    private TextView textView_allTermTitle;
    private String currentFilter = "";
    private View rootView;
    private LinearLayout linearLayout_graph;
    private LinearLayout linearLayout_all_term;
    private LinearLayout linearLayout_searchView;

    String current_graph_type;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_graph, container, false);

        db = new DatabaseHandlerTerms(rootView.getContext());

        textView_allTermTitle =
                (TextView) rootView.findViewById(R.id.GRAPH_textView_allTermsTitle);
        linearLayout_graph = (LinearLayout) rootView.findViewById(R.id.GRAPH_linearLayout_graph);
        linearLayout_all_term = (LinearLayout) rootView.findViewById(R.id.GRAPH_linearLayout_allTerms);
        linearLayout_searchView = (LinearLayout) rootView.findViewById(R.id.GRAPH_linearLayout_searchView);

        int all_term_count = db.getAllTerms().getCount();
        textView_allTermTitle.setText("All Terms in Memory (" + Integer.toString(all_term_count) + ")");

        this.setGraph(TYPE_WEEK);
        this.setTimeButtonListener();
        this.setExpandButtonListener();
        this.setSearchViewListener();

        return rootView;
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

        LineData mlineData = new LineData(generateTermData(graph_type));
        mlineData.addDataSet((new LineData(generateTestData(graph_type))).getDataSetByIndex(0));
        data.setData(mlineData);

        data.setData(generateBarData(graph_type));

        Typeface mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");
        data.setValueTypeface(mTfLight);

        xAxis.setAxisMaximum(data.getXMax() + 0.25f);

        mChart.setData(data);
        mChart.animateXY(1000, 1000);
        mChart.invalidate();


    }

    private LineDataSet generateTermData(String time_type) {

        ArrayList<Entry> entries = new ArrayList<Entry>();

        LineDataSet set=null;

        switch(time_type){
            case TYPE_YEAR:

                ArrayList<Integer> term_data_year = GraphUtils.getTermData(getContext(),TYPE_YEAR);
                for (int index = 0; index < term_data_year.size(); index++)
                    entries.add(new Entry(index , term_data_year.get(index)));
                set = new LineDataSet(entries, CATEGORY_TERM);
                break;

            case TYPE_MONTH:
                ArrayList<Integer> term_data_month = GraphUtils.getTermData(getContext(),TYPE_MONTH);
                for (int index = 0; index < term_data_month.size(); index++)
                    entries.add(new Entry(index , term_data_month.get(index)));
                set = new LineDataSet(entries, CATEGORY_TERM);
                break;

            case TYPE_WEEK:
                ArrayList<Integer> term_data_week = GraphUtils.getTermData(getContext(),TYPE_WEEK);
                for (int index = 0; index < term_data_week.size(); index++)
                    entries.add(new Entry(index , term_data_week.get(index)));
                set = new LineDataSet(entries, CATEGORY_TERM);
                break;
        }

        set.setColor(Color.rgb(240,238,7));
        set.setLineWidth(3f);
        set.setCircleColor(Color.rgb(240,238,7));
        set.setCircleSize(4f);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(0f);
        set.setValueTextColor(Color.rgb(240,238,7));
        set.setHighLightColor(Color.MAGENTA);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        return set;
    }

    private LineDataSet generateTestData(String time_type) {

        ArrayList<Entry> entries = new ArrayList<Entry>();

        LineDataSet set=null;

        switch(time_type){
            case TYPE_YEAR:
                ArrayList<Integer> test_data_year = GraphUtils.getTestData(getContext(),TYPE_YEAR);
                for (int index = 0; index < test_data_year.size(); index++)
                    entries.add(new Entry(index , test_data_year.get(index)));
                set = new LineDataSet(entries, CATEGORY_TEST);
                break;

            case TYPE_MONTH:
                ArrayList<Integer> test_data_month = GraphUtils.getTestData(getContext(),TYPE_MONTH);
                for (int index = 0; index < test_data_month.size(); index++)
                    entries.add(new Entry(index , test_data_month.get(index)));
                set = new LineDataSet(entries, CATEGORY_TEST);
                break;

            case TYPE_WEEK:
                ArrayList<Integer> test_data_week = GraphUtils.getTestData(getContext(),TYPE_WEEK);
                for (int index = 0; index < test_data_week.size(); index++)
                    entries.add(new Entry(index , test_data_week.get(index)));
                set = new LineDataSet(entries, CATEGORY_TEST);
                break;
        }

        set.setColor(Color.rgb(162,234,242));
        set.setLineWidth(2f);
        set.setCircleColor(Color.rgb(162,234,242));
        set.setCircleSize(3f);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(0f);
        set.setValueTextColor(Color.rgb(162,234,242));
        set.setHighLightColor(Color.MAGENTA);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        return set;
    }

    private BarData generateBarData(String time_type) {
        ArrayList<BarEntry> entry = new ArrayList<BarEntry>();

        switch(time_type){
            case TYPE_YEAR:
                ArrayList<Integer> game_data_correct_year = GraphUtils.getGameDataCorrect(getContext(),TYPE_YEAR);
                ArrayList<Integer> game_data_incorrect_year = GraphUtils.getGameDataIncorrect(getContext(),TYPE_YEAR);
                for (int index = 0; index < game_data_correct_year.size(); index++) {
                    // stacked
                    entry.add(new BarEntry(index, new float[]{
                            game_data_correct_year.get(index), game_data_incorrect_year.get(index)}));
                }
                break;
            case TYPE_MONTH:
                ArrayList<Integer> game_data_correct_month = GraphUtils.getGameDataCorrect(getContext(),TYPE_MONTH);
                ArrayList<Integer> game_data_incorrect_month = GraphUtils.getGameDataIncorrect(getContext(),TYPE_MONTH);
                for (int index = 0; index < game_data_correct_month.size(); index++) {
                    // stacked
                    entry.add(new BarEntry(index, new float[]{
                            game_data_correct_month.get(index), game_data_incorrect_month.get(index)}));
                }
                break;
            case TYPE_WEEK:
                ArrayList<Integer> game_data_correct_week = GraphUtils.getGameDataCorrect(getContext(),TYPE_WEEK);
                ArrayList<Integer> game_data_incorrect_week = GraphUtils.getGameDataIncorrect(getContext(),TYPE_WEEK);
                for (int index = 0; index < game_data_correct_week.size(); index++) {
                    // stacked
                    entry.add(new BarEntry(index, new float[]{
                            game_data_correct_week.get(index), game_data_incorrect_week.get(index)}));
                }
                break;
        }
        BarDataSet set = new BarDataSet(entry, "- Game");
        set.setStackLabels(new String[]{"Correct", "Incorrect"});
        set.setColors(new int[]{Color.rgb(211,233,211),Color.rgb(246,214,214)});
        set.setValueTextSize(0f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarData data = new BarData(set);

        return data;

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
                TermListAdapter adapter = new TermListAdapter(getActivity(), R.layout.term_item, exampleTerm, rootView);
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

        Cursor cursor_terms = db.getAllTerms();
        if(cursor_terms.getCount() == 0){
            textView_allTermTitle.setText("All Terms in Memory (0)");
            return;
        }

        ArrayList<HashMap<String, String>> terms = new ArrayList<>();
        int all_term_total = 0;
        while(cursor_terms.moveToNext()){
            String id = cursor_terms.getString(DatabaseHandlerTerms.INDEX_ID);
            String term = cursor_terms.getString(DatabaseHandlerTerms.INDEX_TERM);
            String definition = cursor_terms.getString(DatabaseHandlerTerms.INDEX_DEFINITION);
            String date_add = cursor_terms.getString(DatabaseHandlerTerms.INDEX_DATE_ADD);
            String date_latest = cursor_terms.getString(DatabaseHandlerTerms.INDEX_DATE_LATEST);
            String memory_level = cursor_terms.getString(DatabaseHandlerTerms.INDEX_MEMORY_LEVEL);

            all_term_total += 1;
            if(!term.toLowerCase().contains(currentFilter.toLowerCase())) continue;

            HashMap<String, String> temp_hash = new HashMap<>();
            temp_hash.put(DatabaseHandlerTerms.COLUMN_ID, id);
            temp_hash.put(DatabaseHandlerTerms.COLUMN_TERM, term);
            temp_hash.put(DatabaseHandlerTerms.COLUMN_DEFINITION, definition);
            temp_hash.put(DatabaseHandlerTerms.COLUMN_DATE_ADD,date_add);
            temp_hash.put(DatabaseHandlerTerms.COLUMN_DATE_LATEST,date_latest);
            temp_hash.put(DatabaseHandlerTerms.COLUMN_MEMORY_LEVEL,memory_level);

            terms.add(temp_hash);
        }


        ArrayList<HashMap<String, String>> sorted_terms = TermUtils.sortTerms(terms);

        /* Update the title name */
        textView_allTermTitle.setText("All Terms in Memory (" + all_term_total + ")");

        /* Display Today's terms */
        ListView listView_termList = (ListView) rootView.findViewById(R.id.GRAPH_listView_termList);
        ListAdapter adapter = new GraphFragment.TermListAdapter(getActivity(), R.layout.term_item, sorted_terms, rootView);
        listView_termList.setAdapter(adapter);
    }


    /**
     * This ArrayAdapter is for handling term ListView dynamically.
     */
    class TermListAdapter extends ArrayAdapter {
        Context context;
        int layoutResourceId;
        ArrayList<HashMap<String, String>> data = null;
        View rootView;

        public TermListAdapter(Context context,
                               int layoutResourceId,
                               ArrayList<HashMap<String, String>> data, View rootView) {
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
            textView_term.setText(data.get(i).get(DatabaseHandlerTerms.COLUMN_TERM));
            textView_definition.setText(data.get(i).get(DatabaseHandlerTerms.COLUMN_DEFINITION));

            String term_id = data.get(i).get(DatabaseHandlerTerms.COLUMN_ID);
            String memory_level = data.get(i).get(DatabaseHandlerTerms.COLUMN_MEMORY_LEVEL);

            ImageView imageView_edit = (ImageView) view.findViewById(R.id.TERMITEM_editButton);
            ImageView imageView_crown = (ImageView) view.findViewById(R.id.TERMITEM_imageView_crown);

            if(memory_level.equals("0")){
                imageView_crown.setVisibility(View.VISIBLE);
            }

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

                    HashMap<String, String> term = db.getTermAt(term_id);

                    /* Initialize the blanks. */
                    EditText editText_term = (EditText) mView.findViewById(R.id.EDITTERM_editText_term);
                    EditText editText_definition = (EditText) mView.findViewById(R.id.EDITTERM_editText_definition);
                    TextView textView_addedDate = (TextView) mView.findViewById(R.id.EDITTERM_textView_addedDate);
                    TextView textView_lastMemorizedDate = (TextView) mView.findViewById(R.id.EDITTERM_textView_lastMemorizedDate);
                    editText_term.setText(term.get(DatabaseHandlerTerms.COLUMN_TERM));
                    editText_definition.setText(term.get(DatabaseHandlerTerms.COLUMN_DEFINITION));
                    textView_addedDate.setText(term.get(DatabaseHandlerTerms.COLUMN_DATE_ADD));
                    textView_lastMemorizedDate.setText(term.get(DatabaseHandlerTerms.COLUMN_DATE_LATEST));

                    Button button_save = (Button) mView.findViewById(R.id.EDITTERM_button_save);
                    Button button_cancel = (Button) mView.findViewById(R.id.EDITTERM_button_cancel);
                    Button button_delete = (Button) mView.findViewById(R.id.EDITTERM_button_delete);

                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.create();

                    button_save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            /* When one of the blanks is empty. */
                            if(editText_term.getText().toString().isEmpty()
                                    || editText_definition.getText().toString().isEmpty()){
                                Toast.makeText(getContext(), "Some blank is empty.", Toast.LENGTH_LONG).show();
                            }else{
                                term.put(DatabaseHandlerTerms.COLUMN_TERM, editText_term.getText().toString());
                                term.put(DatabaseHandlerTerms.COLUMN_DEFINITION, editText_definition.getText().toString());

                                db.updateTerm(term);
                                db.updateToServer();

                                dialog.dismiss();

                                VibratorUtils.vibrateAlert(rootView.getContext());
                                updateTermList();
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
                            if(button_delete.getText().toString().equals("Delete")){
                                button_delete.setText("Confirm");
                            }else {

                                db.deleteTerm(term_id);
                                db.updateToServer();

                                KeyboardUtils.hideKeyboard(getActivity());
                                dialog.dismiss();
                                VibratorUtils.vibrateAlert(rootView.getContext());
                                updateTermList();
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
