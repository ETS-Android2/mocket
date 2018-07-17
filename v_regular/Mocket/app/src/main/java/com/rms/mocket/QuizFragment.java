package com.rms.mocket;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class QuizFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_quiz, container, false);

        final ImageView imageView_start = (ImageView) rootView.findViewById(R.id.QUIZ_imageView_start);
        final ImageView imageView_playnow = (ImageView) rootView.findViewById(R.id.QUIZ_imageView_playnow);
        final TextView textView_leftTotal = (TextView) rootView.findViewById(R.id.QUIZ_textView_leftTotal);

        // TODO: Dynamically cahnge the left/total count.
        // textView_leftTotal.setText();


        imageView_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: When start QUIZ button is clicked!
                Intent intent = new Intent(rootView.getContext(), QuizActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        imageView_playnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), GameActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);

                // TODO: When play GAME button is clicked!
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
}
