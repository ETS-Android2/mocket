package com.rms.mocket.common;

import android.content.Context;
import android.os.Vibrator;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rms.mocket.object.User;

import static android.content.Context.VIBRATOR_SERVICE;

public class VibratorUtils {

    public static void vibrateCorrect(Context context){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


        mDatabase.child(User.REFERENCE_USERS).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(mAuth.getCurrentUser() == null) return;

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                String setting_game = "";
                String setting_notification = "";
                String setting_vibration = "";
                String setting_gesture = "";


                for(DataSnapshot child: children) {
                    String key = child.getKey();
                    String value = child.getValue(String.class);
                    switch (key) {
                        case "setting_game":
                            setting_game = value;
                            break;

                        case "setting_gesture":
                            setting_gesture = value;
                            break;

                        case "setting_notification":
                            setting_notification = value;
                            break;

                        case "setting_vibration":
                            setting_vibration = value;
                            break;

                    }
                }

                if(setting_vibration.equals("OFF")) return;

                Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
                try {
                    vibrator.vibrate(50);
                    Thread.sleep(150);
                    vibrator.vibrate(50);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public static void vibrateIncorrect(Context context){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


        mDatabase.child(User.REFERENCE_USERS).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(mAuth.getCurrentUser() == null) return;

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                String setting_game = "";
                String setting_notification = "";
                String setting_vibration = "";
                String setting_gesture = "";


                for(DataSnapshot child: children) {
                    String key = child.getKey();
                    String value = child.getValue(String.class);
                    switch (key) {
                        case "setting_game":
                            setting_game = value;
                            break;

                        case "setting_gesture":
                            setting_gesture = value;
                            break;

                        case "setting_notification":
                            setting_notification = value;
                            break;

                        case "setting_vibration":
                            setting_vibration = value;
                            break;

                    }
                }

                if(setting_vibration.equals("OFF")) return;

                Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(200);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public static void vibrateAlert(Context context){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


        mDatabase.child(User.REFERENCE_USERS).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(mAuth.getCurrentUser() == null) return;

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                String setting_game = "";
                String setting_notification = "";
                String setting_vibration = "";
                String setting_gesture = "";


                for(DataSnapshot child: children) {
                    String key = child.getKey();
                    String value = child.getValue(String.class);
                    switch (key) {
                        case "setting_game":
                            setting_game = value;
                            break;

                        case "setting_gesture":
                            setting_gesture = value;
                            break;

                        case "setting_notification":
                            setting_notification = value;
                            break;

                        case "setting_vibration":
                            setting_vibration = value;
                            break;

                    }
                }

                if(setting_vibration.equals("OFF")) return;

                Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(50);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}
