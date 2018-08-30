package com.rms.mocket.database;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseHandlerGame {

    public final static String REFERENCE_GAME = "game";

    public final static String CORRECT = "correct";
    public final static String INCORRECT = "incorrect";

    public static void increaseCorrectCount(String date){
        final String converted_date = date.replaceAll("/","");
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mGameDatabase =
                FirebaseDatabase.getInstance().getReference(REFERENCE_GAME);

        mGameDatabase.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(converted_date)){
                    mGameDatabase.child(user_id).child(converted_date).child(CORRECT).setValue(Integer.toString(1));
                    mGameDatabase.child(user_id).child(converted_date).child(INCORRECT).setValue(Integer.toString(0));
                }else{

                    Iterable<DataSnapshot> children = dataSnapshot.child(converted_date).getChildren();

                    for(DataSnapshot child: children) {
                        if(child.getKey().equals(CORRECT)){
                            int child_count =Integer.parseInt(child.getValue(String.class));
                            child_count++;
                            mGameDatabase.child(user_id).child(converted_date).child(CORRECT).setValue(Integer.toString(child_count));
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static void increaseIncorrectCount(String date){
        final String converted_date = date.replaceAll("/","");
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mGameDatabase =
                FirebaseDatabase.getInstance().getReference(REFERENCE_GAME);

        mGameDatabase.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(converted_date)){
                    mGameDatabase.child(user_id).child(converted_date).child(CORRECT).setValue(Integer.toString(0));
                    mGameDatabase.child(user_id).child(converted_date).child(INCORRECT).setValue(Integer.toString(1));
                }else{

                    Iterable<DataSnapshot> children = dataSnapshot.child(converted_date).getChildren();

                    for(DataSnapshot child: children) {
                        if(child.getKey().equals(INCORRECT)){
                            int child_count =Integer.parseInt(child.getValue(String.class));
                            child_count++;
                            mGameDatabase.child(user_id).child(converted_date).child(INCORRECT).setValue(Integer.toString(child_count));
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
