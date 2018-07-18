package com.rms.mocket.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rms.mocket.R;
import com.rms.mocket.activities.QuizActivity;

import java.util.ArrayList;
import java.util.HashMap;


public class MemoryFragment extends Fragment {

    boolean visibility_termList = false;
    ArrayList<HashMap<String, String>> exampleTerm = new ArrayList<>();

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

        final View rootView = inflater.inflate(R.layout.fragment_memory, container, false);

        final EditText editText_term = (EditText) rootView.findViewById(R.id.MEMORY_editText_term);
        final EditText editText_definition = (EditText) rootView.findViewById(R.id.MEMORY_editText_definition);

        final TextView textView_todaysMemoryTitle =
                (TextView) rootView.findViewById(R.id.MEMORY_textView_todaysMemoryTitle);
        textView_todaysMemoryTitle.setText("Today's Memory (" + exampleTerm.size() + ")");


        TextView textView_clear = (TextView) rootView.findViewById(R.id.MEMORY_textView_clear);
        textView_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editText_term.setText("");
                editText_term.clearFocus();
                editText_definition.setText("");
                editText_definition.clearFocus();
            }
        });

        Button button_lookup = (Button) rootView.findViewById(R.id.MEMORY_button_lookUp);
        button_lookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String term = editText_term.getText().toString();

                //TODO: look up the term from dictionary.


            }
        });

        Button button_memorize = (Button) rootView.findViewById(R.id.MEMORY_button_memorize);
        button_memorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Add to word lists and update database.
                Toast.makeText(getContext(), "Successfully Added to Memory.", Toast.LENGTH_LONG).show();
                editText_term.setText("");
                editText_term.clearFocus();
                editText_definition.setText("");
                editText_definition.clearFocus();
            }
        });


        /**
         * OnClick: expand arrow on [Today's Memory] section.
         *  @ Shrink:
         *  @ Expand: Show ListView of [Today's Memory].
         * */
        final ImageView imageView_expand = (ImageView) rootView.findViewById(R.id.MEMORY_imageView_expand);
        imageView_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ListView listView_termList = (ListView) rootView.findViewById(R.id.MEMORY_listView_termList);
                TermListAdapter adapter = new TermListAdapter(getActivity(), R.layout.term_item, exampleTerm, rootView);
                if (visibility_termList) {
                    /* Reset the terms */
                    listView_termList.setAdapter(null);

                    imageView_expand.setImageResource(R.drawable.expand_button);
                    listView_termList.setVisibility(View.GONE);
                    visibility_termList = false;
                } else {
                    /* Display Today's terms */
                    listView_termList.setAdapter(adapter);

                    imageView_expand.setImageResource(R.drawable.shrink_button);
                    listView_termList.setVisibility(View.VISIBLE);
                    visibility_termList = true;

                }

            }
        });

        final ImageView imageView_quizButton = (ImageView) rootView.findViewById(R.id.MEMORY_imageView_quizButton);
        imageView_quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: When Quiz Button is clicked, show quiz activity
                Intent i = new Intent(rootView.getContext(), QuizActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });

        return rootView;
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
            textView_term.setText(exampleTerm.get(i).get("term"));
            textView_definition.setText(exampleTerm.get(i).get("definition"));

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
