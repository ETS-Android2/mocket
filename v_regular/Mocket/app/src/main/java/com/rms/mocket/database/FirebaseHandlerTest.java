package com.rms.mocket.database;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rms.mocket.common.Utils;

public class FirebaseHandlerTest {

    public final static String REFERENCE_TEST = "test";

    public static void increaseCount(String date){
        final String converted_date = date.replaceAll("/","");
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mTestDatabase =
                FirebaseDatabase.getInstance().getReference(REFERENCE_TEST);

        mTestDatabase.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (FirebaseAuth.getInstance().getCurrentUser() == null) return;

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for(DataSnapshot child: children) {
                    Utils.log(child.getKey());
                    if(child.getKey().equals(converted_date)){
                        int child_count =Integer.parseInt(child.getValue(String.class));
                        child_count++;
                        mTestDatabase.child(user_id).child(converted_date).setValue(Integer.toString(child_count));
                        return;
                    }

                }
                mTestDatabase.child(user_id).child(converted_date).setValue(Integer.toString(0));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
