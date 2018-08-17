package com.rms.mocket.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
import com.rms.mocket.common.DictionaryUtils;
import com.rms.mocket.common.KeyboardUtils;
import com.rms.mocket.common.TermUtils;
import com.rms.mocket.common.VibratorUtils;
import com.rms.mocket.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.HashMap;


public class MemoryFragment extends Fragment {

    boolean visibility_termList = false;
    DatabaseHandler db;

    EditText editText_term;
    EditText editText_definition;
    TextView textView_todaysMemoryTitle;
    View rootView;
    LinearLayout linearLayout_addMemory;

    String currentFilter = "";
    ArrayList<HashMap<String, String>> dictItems = new ArrayList<>();
    int page_num=0;

    AlertDialog lookUp_dialog;



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

        this.updateTermList();
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

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(rootView.getContext());
                View mView = getLayoutInflater().inflate(R.layout.lookup_dialogue, null);


                /* Initialize the blanks. */
                EditText editText_term = (EditText) mView.findViewById(R.id.LOOKUP_editText_term);
                editText_term.setText(term);

                TextView textView_prev = (TextView) mView.findViewById(R.id.LOOKUP_textView_prev);
                textView_prev.setTextColor(getResources().getColor(R.color.mocket_hint));
                TextView textView_next = (TextView) mView.findViewById(R.id.LOOKUP_textView_next);
                textView_next.setTextColor(getResources().getColor(R.color.mocket_hint));

                Button button_cancel = (Button) mView.findViewById(R.id.LOOKUP_button_cancel);
                Button button_lookup = (Button) mView.findViewById(R.id.LOOKUP_button_lookUp);

                dictItems = DictionaryUtils.getTermList(rootView.getContext(), term);
                if(dictItems.size() > 10) textView_next.setTextColor(getResources().getColor(R.color.mocket_font_light));

                ArrayList<HashMap<String, String>> temp_dictItems = new ArrayList<>();
                for(int i=0; i < dictItems.size() && i < 10; i++){
                    temp_dictItems.add(dictItems.get(i));
                }


                ListView listView_lookUpList = (ListView) mView.findViewById(R.id.LOOKUP_listView_termList);
                ListAdapter adapter = new LookupListAdapter(getActivity(), R.layout.dict_item, temp_dictItems, mView);
                listView_lookUpList.setAdapter(adapter);

                mBuilder.setView(mView);
                lookUp_dialog = mBuilder.create();

                button_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lookUp_dialog.dismiss();
                        page_num = 0;
                    }
                });

                button_lookup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        textView_prev.setTextColor(getResources().getColor(R.color.mocket_hint));
                        textView_next.setTextColor(getResources().getColor(R.color.mocket_hint));
                        dictItems = DictionaryUtils.getTermList(rootView.getContext(), editText_term.getText().toString());
                        if(dictItems.size() > 10) textView_next.setTextColor(getResources().getColor(R.color.mocket_font_light));

                        ArrayList<HashMap<String, String>> temp_dictItems = new ArrayList<>();
                        for(int i=0; i < dictItems.size() && i < 10; i++){
                            temp_dictItems.add(dictItems.get(i));
                        }

                        ListAdapter adapter = new LookupListAdapter(getActivity(), R.layout.dict_item, temp_dictItems, mView);
                        listView_lookUpList.setAdapter(adapter);

                        page_num = 0;
                    }
                });


                textView_prev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(page_num==0){
                            return;
                        }
                        else{
                            textView_next.setTextColor(getResources().getColor(R.color.mocket_font_light));
                            page_num--;
                            ArrayList<HashMap<String, String>> temp_dictItems = new ArrayList<>();
                            int count = 0;
                            for(int i=page_num*10; i < dictItems.size() && count < 10; i++){
                                temp_dictItems.add(dictItems.get(i));
                                count++;
                            }

                            ListAdapter adapter = new LookupListAdapter(getActivity(), R.layout.dict_item, temp_dictItems, mView);
                            listView_lookUpList.setAdapter(adapter);

                            if(page_num == 0) textView_prev.setTextColor(getResources().getColor(R.color.mocket_hint));

                        }

                    }
                });


                textView_next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(page_num*10+10 >= dictItems.size()){
                            return;
                        }else{
                            textView_prev.setTextColor(getResources().getColor(R.color.mocket_font_light));
                            page_num++;
                            ArrayList<HashMap<String, String>> temp_dictItems = new ArrayList<>();
                            int count = 0;
                            for(int i=page_num*10; i < dictItems.size() && count <10; i++){
                                temp_dictItems.add(dictItems.get(i));
                                count++;
                            }

                            ListAdapter adapter = new LookupListAdapter(getActivity(), R.layout.dict_item, temp_dictItems, mView);
                            listView_lookUpList.setAdapter(adapter);

                            if(page_num*10+10 >= dictItems.size()) textView_next.setTextColor(getResources().getColor(R.color.mocket_hint));
                        }
                    }
                });

                lookUp_dialog.setCanceledOnTouchOutside(false);
                lookUp_dialog.show();

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
                    VibratorUtils.vibrateAlert(rootView.getContext());
                }

                clearTexts();
                updateTermList();
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
                currentFilter = s;
                updateTermList();
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

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                currentFilter="";
                return false;
            }
        });
    }

    public void clearTexts(){
        editText_term.setText("");
        editText_term.clearFocus();
        editText_definition.setText("");
        editText_definition.clearFocus();
    }

    public void updateTermList(){

        Cursor cursor_terms = db.getAllTerms();
        if(cursor_terms.getCount() == 0){
            textView_todaysMemoryTitle.setText("Today's Memory (0)");
            return;
        }

        ArrayList<HashMap<String, String>> terms = new ArrayList<>();
        int today_term_total = 0;
        while(cursor_terms.moveToNext()){
            String id = cursor_terms.getString(0);
            String term = cursor_terms.getString(1);
            String definition = cursor_terms.getString(2);
            String date_add = cursor_terms.getString(3);

            if(!date_add.equals(DateUtils.getDateToday())) continue;
            today_term_total += 1;
            if(!term.toLowerCase().contains(currentFilter.toLowerCase())) continue;

            HashMap<String, String> temp_hash = new HashMap<>();
            temp_hash.put(DatabaseHandler.COLUMN_ID, id);
            temp_hash.put(DatabaseHandler.COLUMN_TERM, term);
            temp_hash.put(DatabaseHandler.COLUMN_DEFINITION, definition);

            terms.add(temp_hash);
        }


        ArrayList<HashMap<String, String>> sorted_terms = TermUtils.sortTerms(terms);

        /* Update the title name */
        textView_todaysMemoryTitle.setText("Today's Memory (" + today_term_total + ")");

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

            String term_id = data.get(i).get(DatabaseHandler.COLUMN_ID);

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

    class LookupListAdapter extends ArrayAdapter {
        Context context;
        int layoutResourceId;
        ArrayList<HashMap<String, String>> data = null;
        View rootView;

        public LookupListAdapter(Context context,
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

            String dictItem_term = data.get(i).get(DatabaseHandler.COLUMN_TERM);
            String dictItem_definition = data.get(i).get(DatabaseHandler.COLUMN_DEFINITION);

            TextView textView_term = (TextView) view.findViewById(R.id.DICTITEM_term);
            TextView textView_definition = (TextView) view.findViewById(R.id.DICTITEM_definition);
            textView_term.setText(dictItem_term);
            textView_definition.setText(dictItem_definition);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editText_term.setText(dictItem_term);
                    editText_definition.setText(dictItem_definition);
                    lookUp_dialog.dismiss();
                }
            });

            return view;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
