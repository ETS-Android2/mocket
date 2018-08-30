package com.rms.mocket.fragments;

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
import com.rms.mocket.activities.EditProfileActivity;
import com.rms.mocket.activities.LoginActivity;
import com.rms.mocket.common.Utils;
import com.rms.mocket.object.User;


public class MoreFragment extends Fragment {
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

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    User user;
    String user_id;
    String email;



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

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        this.setSaveButtonListener();
        this.initialSetting();

        Utils.log("More.onCreate()");

        return rootView;
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
        Utils.log("More.onResume()");

    }

    private void initialSetting(){

        user_id = mAuth.getCurrentUser().getUid();


        mDatabase.child(User.REFERENCE_USERS).child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(mAuth.getCurrentUser() == null) return;

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                String full_name = "";
                String first_name = "";
                String last_name = "";
                String profile_image = "";


                for(DataSnapshot child: children) {
                    String key = child.getKey();
                    String value = child.getValue(String.class);
                    switch (key) {
                        case "first_name":
                            first_name = value;
                            full_name = value +" "+ full_name;
                            break;
                        case "last_name":
                            last_name = value;
                            full_name = full_name + value;
                            break;
                        case "profile_image":
                            /* Set profile image */
                            profile_image = value;
                            Utils.setEncodedImage(imageView_profile, value);

                            break;

                        case "setting_game":
                            ArrayAdapter<CharSequence> arrayAdapter_game = ArrayAdapter.createFromResource(rootView.getContext(), R.array.game_daily, R.layout.support_simple_spinner_dropdown_item);
                            spinner_game.setAdapter(arrayAdapter_game);
                            for(int i = 0; i < arrayAdapter_game.getCount();i++){
                                String temp_item = (String) arrayAdapter_game.getItem(i);
                                if(temp_item.equals(value)){
                                    spinner_game.setSelection(i);
                                }
                            }
                            break;

                        case "setting_gesture":
                            ArrayAdapter<CharSequence> arrayAdapter_gesture = ArrayAdapter.createFromResource(rootView.getContext(), R.array.gesture_option, R.layout.support_simple_spinner_dropdown_item);
                            spinner_gesture.setAdapter(arrayAdapter_gesture);
                            for(int i = 0; i < arrayAdapter_gesture.getCount();i++){
                                String temp_item = (String) arrayAdapter_gesture.getItem(i);
                                if(temp_item.equals(value)){
                                    spinner_gesture.setSelection(i);
                                }
                            }
                            break;

                        case "setting_notification":
                            /* Setting spinners */
                            ArrayAdapter<CharSequence> arrayAdapter_notification = ArrayAdapter.createFromResource(rootView.getContext(), R.array.notification_duration, R.layout.support_simple_spinner_dropdown_item);
                            spinner_notification.setAdapter(arrayAdapter_notification);
                            for(int i = 0; i < arrayAdapter_notification.getCount();i++){
                                String temp_item = (String) arrayAdapter_notification.getItem(i);
                                if(temp_item.equals(value)){
                                    spinner_notification.setSelection(i);
                                }
                            }
                            break;

                        case "setting_vibration":
                            ArrayAdapter<CharSequence> arrayAdapter_vibration = ArrayAdapter.createFromResource(rootView.getContext(), R.array.vibration, R.layout.support_simple_spinner_dropdown_item);
                            spinner_vibration.setAdapter(arrayAdapter_vibration);
                            for(int i = 0; i < arrayAdapter_vibration.getCount();i++){
                                String temp_item = (String) arrayAdapter_vibration.getItem(i);
                                if(temp_item.equals(value)){
                                    spinner_vibration.setSelection(i);
                                }
                            }
                            break;

                    }
                }

                user = new User(first_name, last_name, profile_image);

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


                /* Initialize name and email */
                textView_name.setText(full_name);
                email = mAuth.getCurrentUser().getEmail();
                textView_email.setText(email);

                return;

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }



    public void setSaveButtonListener(){

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String notification = (String) spinner_notification.getSelectedItem();
                String game = (String) spinner_game.getSelectedItem();
                String gesture = (String) spinner_gesture.getSelectedItem();
                String vibration = (String) spinner_vibration.getSelectedItem();

                user.setting_gesture = gesture;
                user.setting_vibration = vibration;
                user.setting_game = game;
                user.setting_notification = notification;

                mDatabase.child(User.REFERENCE_USERS).child(user_id).setValue(user);

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
