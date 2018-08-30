package com.rms.mocket.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rms.mocket.R;
import com.rms.mocket.activities.LoginActivity;
import com.rms.mocket.activities.QuizActivity;
import com.rms.mocket.common.DateUtils;
import com.rms.mocket.common.DictionaryUtils;
import com.rms.mocket.common.KeyboardUtils;
import com.rms.mocket.common.TermUtils;
import com.rms.mocket.common.Utils;
import com.rms.mocket.common.VibratorUtils;

import com.rms.mocket.database.FirebaseHandlerTerm;
import com.rms.mocket.object.Term;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


public class MemoryFragment extends Fragment{

    public final static String TYPE_QUIZ = "quiz";
    private TextToSpeech tts;

    DatabaseReference mTermDatabase;

    EditText editText_term;
    EditText editText_definition;
    TextView textView_todaysMemoryTitle;
    ImageView imageView_speak;
    View rootView;
    LinearLayout linearLayout_addMemory;

    String currentFilter = "";
    ArrayList<HashMap<String, String>> dictItems = new ArrayList<>();
    int page_num=0;

    AlertDialog lookUp_dialog;

    String user_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_memory, container, false);

        editText_term = (EditText) rootView.findViewById(R.id.MEMORY_editText_term);
        editText_definition = (EditText) rootView.findViewById(R.id.MEMORY_editText_definition);
        textView_todaysMemoryTitle =
                (TextView) rootView.findViewById(R.id.MEMORY_textView_todaysMemoryTitle);
        linearLayout_addMemory = (LinearLayout) rootView.findViewById(R.id.MEMORY_linearLayout_addToMemory);

        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mTermDatabase = FirebaseDatabase.getInstance().getReference(Term.REFERENCE_TERMS);


        this.setClearButtonListener();
        this.setLookUpButtonListener();
        this.setMemorizeButtonListener();
        this.setQuizButtonListener();
        this.setSpeakButtonListener();

        this.setSearchViewListener();

        return rootView;
    }




    public void setSpeakButtonListener(){
        imageView_speak = (ImageView) rootView.findViewById(R.id.MEMORY_imageView_speak);
        imageView_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_to_speak = editText_term.getText().toString();
                tts=new TextToSpeech(rootView.getContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        // TODO Auto-generated method stub
                        if(status == TextToSpeech.SUCCESS){
                            int result=tts.setLanguage(Locale.US);
                            if(result==TextToSpeech.LANG_MISSING_DATA ||
                                    result==TextToSpeech.LANG_NOT_SUPPORTED){
                                Log.e("error", "This Language is not supported");
                            }
                            else{
                                if(text_to_speak==null||"".equals(text_to_speak))
                                {
                                    Log.d("Mocket","TextToSpeech: Content not available.");
                                }else
                                    tts.speak(text_to_speak, TextToSpeech.QUEUE_FLUSH, null);                    }
                        }
                        else
                            Log.e("error", "Initilization Failed!");
                    }
                });
            }
        });
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

                FirebaseHandlerTerm.addTerm(term, definition);

                updateTermList();

                clearTexts();
            }
        });
    }

    public void setQuizButtonListener(){
        final ImageView imageView_quizButton = (ImageView) rootView.findViewById(R.id.MEMORY_imageView_quizButton);
        imageView_quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(rootView.getContext(), QuizActivity.class);
                i.putExtra(QuizActivity.TYPE, TYPE_QUIZ);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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

        mTermDatabase.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (FirebaseAuth.getInstance().getCurrentUser() == null) return;

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                ArrayList<Term> terms = new ArrayList<>();
                for(DataSnapshot child: children) {
                    Term term = child.getValue(Term.class);

                    if(!term.term.toLowerCase().contains(currentFilter.toLowerCase())) continue;

                    if(term.date_add.equals(DateUtils.getDateToday())){
                        terms.add(term);
                    }
                }

                ArrayList<Term> sorted_terms = TermUtils.sortTerms(terms);
                textView_todaysMemoryTitle.setText("Today's Memory (" + sorted_terms.size() + ")");

                /* Display Today's terms */
                ListView listView_termList = (ListView) rootView.findViewById(R.id.MEMORY_listView_termList);
                ListAdapter adapter = new TermListAdapter(getActivity(), R.layout.term_item, sorted_terms, rootView);
                listView_termList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ConvertTextToSpeech() {
        // TODO Auto-generated method stub

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

            String dictItem_term = data.get(i).get("term");
            String dictItem_definition = data.get(i).get("definition");

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

        this.updateTermList();

    }

    @Override
    public void onPause(){
        if(tts !=null){
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }
}
