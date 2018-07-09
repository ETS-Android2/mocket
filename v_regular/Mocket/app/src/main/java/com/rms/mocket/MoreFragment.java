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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;


public class MoreFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_more, container, false);
        final ImageView imageView_editProfile = (ImageView) rootView.findViewById(R.id.MORE_imageView_editProfile);

        imageView_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(rootView.getContext(), EditProfileActivity.class);
                startActivity(i);
            }
        });

        /* Image optimizer */
        Glide.with(rootView.getContext())  // Activity or Fragment
                .load(R.drawable.edit_icon)
                .into(imageView_editProfile);

        final Spinner spinner_notification = (Spinner) rootView.findViewById(R.id.MORE_spinner_notification);
        final Spinner spinner_gesture = (Spinner) rootView.findViewById(R.id.MORE_spinner_gesture);

        ArrayAdapter<CharSequence> arrayAdapter_notification = ArrayAdapter.createFromResource(rootView.getContext(), R.array.notification_duration, R.layout.support_simple_spinner_dropdown_item);
        spinner_notification.setAdapter(arrayAdapter_notification);

        ArrayAdapter<CharSequence> arrayAdapter_gesture = ArrayAdapter.createFromResource(rootView.getContext(), R.array.gesture_option, R.layout.support_simple_spinner_dropdown_item);
        spinner_gesture.setAdapter(arrayAdapter_gesture);

        final Button button_save = (Button) rootView.findViewById(R.id.MORE_button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Save all settings selected.
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
