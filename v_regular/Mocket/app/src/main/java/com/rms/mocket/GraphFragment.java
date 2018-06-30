package com.rms.mocket;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.HashMap;



public class GraphFragment extends Fragment {

    boolean visibility_termList= false;
    ArrayList<HashMap<String,String>> exampleTerm = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ////////////////////////////////////////////////////////
        /* This is a temporary code. */

        HashMap<String, String> exampleHash1 = new HashMap<>();
        exampleHash1.put("term","vigorous");
        exampleHash1.put("definition","adj: strong, healthy, and full of energy.");

        HashMap<String, String> exampleHash2 = new HashMap<>();
        exampleHash2.put("term","doctor");
        exampleHash2.put("definition","a person who is skilled in the science of medicine");

        HashMap<String, String> exampleHash3 = new HashMap<>();
        exampleHash3.put("term","deep learning");
        exampleHash3.put("definition","Deep learning is part of a broader family of machine learning methods based on learning data representations, as opposed to task-specific algorithms.");

        HashMap<String, String> exampleHash4 = new HashMap<>();
        exampleHash4.put("term","blockchain");
        exampleHash4.put("definition","continuously growing list of records, called blocks, which are linked and secured using cryptography");

        exampleTerm.add(exampleHash1);
        exampleTerm.add(exampleHash2);
        exampleTerm.add(exampleHash3);
        exampleTerm.add(exampleHash4);

        ////////////////////////////////////////////////////////




        final View rootView = inflater.inflate(R.layout.fragment_graph, container, false);

        final TextView textView_allTermTitle =
                (TextView) rootView.findViewById(R.id.GRAPH_textView_allTermsTitle);
        textView_allTermTitle.setText("All Terms in Memory ("+exampleTerm.size()+")");

        /**
         * Statistics graph.
         *
         */

        /////////////////////////////////////////////////////////////////////////////////
        /* This is sample data */

        GraphView graph = (GraphView) rootView.findViewById(R.id.GRAPH_graphView_graph);
        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(0.5, 4),
                new DataPoint(1, 5),
                new DataPoint(1.5, 2),
                new DataPoint(2, 3)
        });
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 3),
                new DataPoint(0.5, 1),
                new DataPoint(1, 4),
                new DataPoint(1.5, 3),
                new DataPoint(2, 2)
        });
        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 6),
                new DataPoint(0.5, 5),
                new DataPoint(1, 3),
                new DataPoint(1.5, 2),
                new DataPoint(2, 4)
        });
        /////////////////////////////////////////////////////////////////////////////////
        series1.setTitle("Memorized");
        series1.setColor(Color.GREEN);
        series2.setTitle("Quiz");
        series2.setColor(Color.RED);
        series3.setTitle("Game");
        series3.setColor(Color.BLUE);
        graph.addSeries(series1);
        graph.addSeries(series2);
        graph.addSeries(series3);






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
                if(visibility_termList){
                    /* Reset the terms */
                    listView_termList.setAdapter(null);

                    imageView_expand.setImageResource(R.drawable.expand_button);
                    listView_termList.setVisibility(View.GONE);
                    visibility_termList = false;
                }else{
                    /* Display Today's terms */
                    listView_termList.setAdapter(adapter);

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






    /** This ArrayAdapter is for handling term ListView dynamically.
     *
     *
     */
    class TermListAdapter extends ArrayAdapter {
        Context context;
        int layoutResourceId;
        ArrayList<HashMap<String,String>> data = null;
        View rootView;

        public TermListAdapter(Context context,
                               int layoutResourceId,
                               ArrayList<HashMap<String,String>> data, View rootView) {
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
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(layoutResourceId, viewGroup, false);

            TextView textView_term = (TextView) view.findViewById(R.id.TERMITEM_term);
            TextView textView_definition = (TextView) view.findViewById(R.id.TERMITEM_definition);
            textView_term.setText(data.get(i).get("term"));
            textView_definition.setText(data.get(i).get("definition"));

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
