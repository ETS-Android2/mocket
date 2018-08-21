package com.rms.mocket.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.rms.mocket.R;
import com.rms.mocket.common.KeyboardUtils;
import com.rms.mocket.common.TermUtils;
import com.rms.mocket.common.VibratorUtils;
import com.rms.mocket.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.HashMap;


public class GraphFragment extends Fragment {

    boolean visibility_termList = false;
    ArrayList<HashMap<String, String>> exampleTerm = new ArrayList<>();
    DatabaseHandler db;
    TextView textView_allTermTitle;
    String currentFilter = "";
    View rootView;
    LinearLayout linearLayout_graph;
    LinearLayout linearLayout_all_term;
    LinearLayout linearLayout_searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ////////////////////////////////////////////////////////
        /* This is a temporary code. */

        HashMap<String, String> exampleHash1 = new HashMap<>();
        exampleHash1.put("term", "vigorous");
        exampleHash1.put("definition", "adj: strong, healthy, and full of energy.");

        HashMap<String, String> exampleHash2 = new HashMap<>();
        exampleHash2.put("term", "doctor");
        exampleHash2.put("definition", "a person who is skilled in the science of medicine");

        HashMap<String, String> exampleHash3 = new HashMap<>();
        exampleHash3.put("term", "deep learning");
        exampleHash3.put("definition", "Deep learning is part of a broader family of machine learning methods based on learning data representations, as opposed to task-specific algorithms.");

        HashMap<String, String> exampleHash4 = new HashMap<>();
        exampleHash4.put("term", "blockchain");
        exampleHash4.put("definition", "continuously growing list of records, called blocks, which are linked and secured using cryptography");

        exampleTerm.add(exampleHash1);
        exampleTerm.add(exampleHash2);
        exampleTerm.add(exampleHash3);
        exampleTerm.add(exampleHash4);

        ////////////////////////////////////////////////////////


        rootView = inflater.inflate(R.layout.fragment_graph, container, false);
        db = new DatabaseHandler(rootView.getContext());

        textView_allTermTitle =
                (TextView) rootView.findViewById(R.id.GRAPH_textView_allTermsTitle);
        linearLayout_graph = (LinearLayout) rootView.findViewById(R.id.GRAPH_linearLayout_graph);
        linearLayout_all_term = (LinearLayout) rootView.findViewById(R.id.GRAPH_linearLayout_allTerms);
        linearLayout_searchView = (LinearLayout) rootView.findViewById(R.id.GRAPH_linearLayout_searchView);


        /**
         * Statistics graph.
         *
         */

        /////////////////////////////////////////////////////////////////////////////////
        /* This is sample data */

        GraphView graph = (GraphView) rootView.findViewById(R.id.GRAPH_graphView_graph);
        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(0.5, 4),
                new DataPoint(1, 5),
                new DataPoint(1.5, 2),
                new DataPoint(2, 3)
        });
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 3),
                new DataPoint(0.5, 1),
                new DataPoint(1, 4),
                new DataPoint(1.5, 3),
                new DataPoint(2, 2)
        });
        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 6),
                new DataPoint(0.5, 5),
                new DataPoint(1, 3),
                new DataPoint(1.5, 2),
                new DataPoint(2, 4)
        });
        /////////////////////////////////////////////////////////////////////////////////
        series1.setTitle("Memorized");
        series2.setTitle("Quiz");
        series3.setTitle("Game");

        series1.setColor(Color.GREEN);
        series2.setColor(Color.RED);
        series3.setColor(Color.BLUE);

        series1.setThickness(12);
        series2.setThickness(12);
        series3.setThickness(12);

        graph.addSeries(series1);
        graph.addSeries(series2);
        graph.addSeries(series3);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getLegendRenderer().setTextSize(40);

        int all_term_count = db.getAllTerms().getCount();
        textView_allTermTitle.setText("All Terms in Memory (" + Integer.toString(all_term_count) + ")");

        this.setExpandButtonListener();
        this.setSearchViewListener();


        return rootView;
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
                    /* Reset the terms */
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
            String id = cursor_terms.getString(DatabaseHandler.INDEX_ID);
            String term = cursor_terms.getString(DatabaseHandler.INDEX_TERM);
            String definition = cursor_terms.getString(DatabaseHandler.INDEX_DEFINITION);
            String date_add = cursor_terms.getString(DatabaseHandler.INDEX_DATE_ADD);
            String date_latest = cursor_terms.getString(DatabaseHandler.INDEX_DATE_LATEST);
            String memory_level = cursor_terms.getString(DatabaseHandler.INDEX_MEMORY_LEVEL);

            all_term_total += 1;
            if(!term.toLowerCase().contains(currentFilter.toLowerCase())) continue;

            HashMap<String, String> temp_hash = new HashMap<>();
            temp_hash.put(DatabaseHandler.COLUMN_ID, id);
            temp_hash.put(DatabaseHandler.COLUMN_TERM, term);
            temp_hash.put(DatabaseHandler.COLUMN_DEFINITION, definition);
            temp_hash.put(DatabaseHandler.COLUMN_DATE_ADD,date_add);
            temp_hash.put(DatabaseHandler.COLUMN_DATE_LATEST,date_latest);
            temp_hash.put(DatabaseHandler.COLUMN_MEMORY_LEVEL,memory_level);

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
            textView_term.setText(data.get(i).get(DatabaseHandler.COLUMN_TERM));
            textView_definition.setText(data.get(i).get(DatabaseHandler.COLUMN_DEFINITION));

            String term_id = data.get(i).get(DatabaseHandler.COLUMN_ID);
            String memory_level = data.get(i).get(DatabaseHandler.COLUMN_MEMORY_LEVEL);

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
                    editText_term.setText(term.get(DatabaseHandler.COLUMN_TERM));
                    editText_definition.setText(term.get(DatabaseHandler.COLUMN_DEFINITION));
                    textView_addedDate.setText(term.get(DatabaseHandler.COLUMN_DATE_ADD));
                    textView_lastMemorizedDate.setText(term.get(DatabaseHandler.COLUMN_DATE_LATEST));

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
                                term.put(DatabaseHandler.COLUMN_TERM, editText_term.getText().toString());
                                term.put(DatabaseHandler.COLUMN_DEFINITION, editText_definition.getText().toString());

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
