package com.rms.mocket;

import android.app.Activity;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class MemoryFragment extends Fragment {

    boolean visibility_termList= false;
    ArrayList<HashMap<String,String>> exampleTerm = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_memory, container, false);

        /* Add to memory section */
        final EditText editText_term = (EditText) rootView.findViewById(R.id.MEMORY_editText_term);
        final EditText editText_definition = (EditText) rootView.findViewById(R.id.MEMORY_editText_definition);
        final ListView listView_termList = (ListView) rootView.findViewById(R.id.MEMORY_listView_termList);


        ////////////////////////////////////////////////////////
        /* This is a temporary code. */

//        LayoutInflater temp_inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.term_item, container,false);
//
        HashMap<String, String> exampleHash1 = new HashMap<>();
        exampleHash1.put("term","vigorous");
        exampleHash1.put("definition","adj: strong, healthy, and full of energy.");

        HashMap<String, String> exampleHash2 = new HashMap<>();
        exampleHash2.put("term","doctor");
        exampleHash2.put("definition","a person who is skilled in the science of medicine");

        exampleTerm.add(exampleHash1);
        exampleTerm.add(exampleHash2);


//        TermListAdapter adapter = new TermListAdapter(getActivity(), R.layout.term_item, exampleTerm);
//        listView_termList.setAdapter(adapter);

        ////////////////////////////////////////////////////////

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

        final ImageView imageView_expand = (ImageView) rootView.findViewById(R.id.MEMORY_imageView_expand);
        imageView_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(visibility_termList){
                    imageView_expand.setImageResource(R.drawable.expand_button);
                    listView_termList.setVisibility(View.GONE);
                    visibility_termList = false;
                }else{
                    imageView_expand.setImageResource(R.drawable.shrink_button);
                    listView_termList.setVisibility(View.VISIBLE);
                    visibility_termList = true;
                }

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

    class TermListAdapter extends ArrayAdapter {
        Context context;
        int layoutResourceId;
        ArrayList<HashMap<String,String>> data = null;

        public TermListAdapter(Context context, int layoutResourceId, ArrayList<HashMap<String,String>> data) {
            super(context, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.data = data;
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
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(layoutResourceId, viewGroup, false);
//
//            TextView textView_term = (TextView) view.findViewById(R.id.TERMITEM_term);
//            TextView textView_definition = (TextView) view.findViewById(R.id.TERMITEM_definition);
//            textView_term.setText(exampleTerm.get(i).get("term"));
//            textView_definition.setText(exampleTerm.get(i).get("definition"));


            return view;
        }
    }



}
