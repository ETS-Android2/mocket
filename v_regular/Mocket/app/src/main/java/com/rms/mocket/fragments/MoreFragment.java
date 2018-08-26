package com.rms.mocket.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rms.mocket.R;
import com.rms.mocket.activities.EditProfileActivity;
import com.rms.mocket.database.DatabaseHandlerUser;

import java.util.HashMap;


public class MoreFragment extends Fragment {

    DatabaseHandlerUser db_user;

    View rootView;

    ImageView imageView_editProfile;
    ImageView imageView_profile;

    TextView textView_name;
    TextView textView_email;

    Button button_save;

    Spinner spinner_notification;
    Spinner spinner_game;
    Spinner spinner_gesture;
    Spinner spinner_vibration;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_more, container, false);

        imageView_editProfile = (ImageView) rootView.findViewById(R.id.MORE_imageView_editProfile);
        imageView_profile = (ImageView) rootView.findViewById(R.id.MORE_imageView_profile);

        textView_name = (TextView) rootView.findViewById(R.id.MORE_textView_name);
        textView_email = (TextView) rootView.findViewById(R.id.MORE_textView_email);

        button_save = (Button) rootView.findViewById(R.id.MORE_button_save);

        spinner_notification = (Spinner) rootView.findViewById(R.id.MORE_spinner_notification);
        spinner_game = (Spinner) rootView.findViewById(R.id.MORE_spinner_game);
        spinner_gesture = (Spinner) rootView.findViewById(R.id.MORE_spinner_gesture);
        spinner_vibration = (Spinner) rootView.findViewById(R.id.MORE_spinner_vibration);

        db_user = new DatabaseHandlerUser(rootView.getContext());
        db_user.printDatabase();



        this.setSaveButtonListener();


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.initialSetting();
    }

    private void initialSetting(){
        /* Get user setting from database */
        HashMap<String, String> user_data =  db_user.getUserData();


        /* Set profile image */
        byte[] byte_profile_image = db_user.getUserProfileImage();
        Bitmap bmp = BitmapFactory.decodeByteArray(byte_profile_image, 0, byte_profile_image.length);
        imageView_profile.setImageBitmap(bmp);


        /* Initialize name and email */
        textView_name.setText(
                user_data.get(DatabaseHandlerUser.COLUMN_FIRST_NAME) + " " +
                user_data.get(DatabaseHandlerUser.COLUMN_LAST_NAME));
        textView_email.setText(user_data.get(DatabaseHandlerUser.COLUMN_EMAIL));


        /* Edit profile button */
        imageView_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(rootView.getContext(), EditProfileActivity.class);
                startActivity(i);
            }
        });
        Glide.with(rootView.getContext())
                .load(R.drawable.edit_icon)
                .into(imageView_editProfile);


        /* Setting spinners */
        ArrayAdapter<CharSequence> arrayAdapter_notification = ArrayAdapter.createFromResource(rootView.getContext(), R.array.notification_duration, R.layout.support_simple_spinner_dropdown_item);
        spinner_notification.setAdapter(arrayAdapter_notification);
        for(int i = 0; i < arrayAdapter_notification.getCount();i++){
            String temp_item = (String) arrayAdapter_notification.getItem(i);
            if(temp_item.equals(user_data.get(DatabaseHandlerUser.COLUMN_SETTING_NOTIFICATION))){
                spinner_notification.setSelection(i);
            }
        }

        ArrayAdapter<CharSequence> arrayAdapter_gesture = ArrayAdapter.createFromResource(rootView.getContext(), R.array.gesture_option, R.layout.support_simple_spinner_dropdown_item);
        spinner_gesture.setAdapter(arrayAdapter_gesture);
        for(int i = 0; i < arrayAdapter_gesture.getCount();i++){
            String temp_item = (String) arrayAdapter_gesture.getItem(i);
            if(temp_item.equals(user_data.get(DatabaseHandlerUser.COLUMN_SETTING_GESTURE))){
                spinner_gesture.setSelection(i);
            }
        }

        ArrayAdapter<CharSequence> arrayAdapter_vibration = ArrayAdapter.createFromResource(rootView.getContext(), R.array.vibration, R.layout.support_simple_spinner_dropdown_item);
        spinner_vibration.setAdapter(arrayAdapter_vibration);
        for(int i = 0; i < arrayAdapter_vibration.getCount();i++){
            String temp_item = (String) arrayAdapter_vibration.getItem(i);
            if(temp_item.equals(user_data.get(DatabaseHandlerUser.COLUMN_SETTING_VIBRATION))){
                spinner_vibration.setSelection(i);
            }
        }

        ArrayAdapter<CharSequence> arrayAdapter_game = ArrayAdapter.createFromResource(rootView.getContext(), R.array.game_daily, R.layout.support_simple_spinner_dropdown_item);
        spinner_game.setAdapter(arrayAdapter_game);
        for(int i = 0; i < arrayAdapter_game.getCount();i++){
            String temp_item = (String) arrayAdapter_game.getItem(i);
            if(temp_item.equals(user_data.get(DatabaseHandlerUser.COLUMN_SETTING_GAME))){
                spinner_game.setSelection(i);
            }
        }


    }

    public void setSaveButtonListener(){

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String notification = (String) spinner_notification.getSelectedItem();
                String game = (String) spinner_game.getSelectedItem();
                String gesture = (String) spinner_gesture.getSelectedItem();
                String vibration = (String) spinner_vibration.getSelectedItem();

                db_user.saveSettings(notification, game, gesture, vibration);

                String message = "Settings have been saved.";
                Toast.makeText(rootView.getContext(), message, Toast.LENGTH_LONG).show();
            }
        });

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
