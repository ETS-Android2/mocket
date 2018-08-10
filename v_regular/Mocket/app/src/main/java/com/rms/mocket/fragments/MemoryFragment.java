package com.rms.mocket.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.rms.mocket.R;
import com.rms.mocket.activities.QuizActivity;
import com.rms.mocket.common.DateUtils;
import com.rms.mocket.common.TermUtils;
import com.rms.mocket.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.VIBRATOR_SERVICE;


public class MemoryFragment extends Fragment {

    boolean visibility_termList = false;
    DatabaseHandler db;

    EditText editText_term;
    EditText editText_definition;
    TextView textView_todaysMemoryTitle;
    View rootView;
    LinearLayout linearLayout_addMemory;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_memory, container, false);
        db = new DatabaseHandler(rootView.getContext());

        editText_term = (EditText) rootView.findViewById(R.id.MEMORY_editText_term);
        editText_definition = (EditText) rootView.findViewById(R.id.MEMORY_editText_definition);
        textView_todaysMemoryTitle =
                (TextView) rootView.findViewById(R.id.MEMORY_textView_todaysMemoryTitle);
        linearLayout_addMemory = (LinearLayout) rootView.findViewById(R.id.MEMORY_linearLayout_addToMemory);

        this.setClearButtonListener();
        this.setLookUpButtonListener();
        this.setMemorizeButtonListener();
        this.setQuizButtonListener();

        this.updateTermList("");
        this.setSearchViewListener();

        return rootView;
    }

    public void setClearButtonListener(){
        TextView textView_clear = (TextView) rootView.findViewById(R.id.MEMORY_textView_clear);
        textView_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearTexts();
            }
        });
    }

    public void setLookUpButtonListener(){
        Button button_lookup = (Button) rootView.findViewById(R.id.MEMORY_button_lookUp);
        button_lookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String term = editText_term.getText().toString();

                //TODO: look up the term from dictionary.


            }
        });
    }

    public void setMemorizeButtonListener(){
        Button button_memorize = (Button) rootView.findViewById(R.id.MEMORY_button_memorize);
        button_memorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( editText_term.getText().toString().isEmpty()
                    || editText_definition.getText().toString().isEmpty()){

                    Toast.makeText(getContext(), "Some blank is empty.", Toast.LENGTH_LONG).show();
                    return;
                }

                /* Add it to database */
                String term = editText_term.getText().toString();
                String definition = editText_definition.getText().toString();
                if (!db.addTerm(term, definition))
                    Toast.makeText(getContext(), "Failed to add into database.", Toast.LENGTH_LONG).show();
                else {
                    Vibrator vibrator = (Vibrator) rootView.getContext().getSystemService(VIBRATOR_SERVICE);
                    try {
                        vibrator.vibrate(50);
                        Thread.sleep(150);
                        vibrator.vibrate(50);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                clearTexts();
                updateTermList("");
            }
        });
    }

    public void setQuizButtonListener(){
        final ImageView imageView_quizButton = (ImageView) rootView.findViewById(R.id.MEMORY_imageView_quizButton);
        imageView_quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(rootView.getContext(), QuizActivity.class);
                i.putExtra("type", "quiz");
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                Log.d("Mocket","before startActivity");
                startActivity(i);
            }
        });
    }

    public void setSearchViewListener(){
        SearchView searchView = (SearchView) rootView.findViewById(R.id.MEMORY_searchView_term);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                updateTermList(s);
                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    linearLayout_addMemory.setVisibility(View.GONE);
                }else{
                    linearLayout_addMemory.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void clearTexts(){
        editText_term.setText("");
        editText_term.clearFocus();
        editText_definition.setText("");
        editText_definition.clearFocus();
    }

    public void updateTermList(String filter){

        Cursor cursor_terms = db.getAllTerms();
        if(cursor_terms.getCount() == 0){
            textView_todaysMemoryTitle.setText("Today's Memory (0)");
            return;
        }

        ArrayList<HashMap<String, String>> terms = new ArrayList<>();
        while(cursor_terms.moveToNext()){
            String id = cursor_terms.getString(0);
            String term = cursor_terms.getString(1);
            String definition = cursor_terms.getString(2);
            String date_add = cursor_terms.getString(3);

            if(!date_add.equals(DateUtils.getDateToday())) continue;
            if(!term.toLowerCase().contains(filter.toLowerCase())) continue;

            HashMap<String, String> temp_hash = new HashMap<>();
            temp_hash.put(DatabaseHandler.COLUMN_ID, id);
            temp_hash.put(DatabaseHandler.COLUMN_TERM, term);
            temp_hash.put(DatabaseHandler.COLUMN_DEFINITION, definition);

            terms.add(temp_hash);
        }

        ArrayList<HashMap<String, String>> sorted_terms = TermUtils.sortTerms(terms);

        /* Update the title name */
        textView_todaysMemoryTitle.setText("Today's Memory (" + sorted_terms.size() + ")");

        /* Display Today's terms */
        ListView listView_termList = (ListView) rootView.findViewById(R.id.MEMORY_listView_termList);
        ListAdapter adapter = new TermListAdapter(getActivity(), R.layout.term_item, sorted_terms, rootView);
        listView_termList.setAdapter(adapter);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

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

            ImageView imageView_edit = (ImageView) view.findViewById(R.id.TERMITEM_editButton);

            /* Image optimizer */
            Glide.with(rootView.getContext())  // Activity or Fragment
                    .load(R.drawable.edit_icon)
                    .into(imageView_edit);

            RelativeLayout layout_edit = (RelativeLayout) view.findViewById(R.id.TERMITEM_layout_editButton);
            layout_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "Edit button clicked.", Toast.LENGTH_LONG).show();
                }
            });

            return view;
        }
    }
}
