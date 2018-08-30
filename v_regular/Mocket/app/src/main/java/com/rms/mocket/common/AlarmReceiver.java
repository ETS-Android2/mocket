package com.rms.mocket.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rms.mocket.R;
import com.rms.mocket.activities.MainActivity;
import com.rms.mocket.object.Term;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        DatabaseReference mTermDatabase = FirebaseDatabase.getInstance().getReference(Term.REFERENCE_TERMS);
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mTermDatabase.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {
                    Term term = child.getValue(Term.class);

                    String date_test = Memory.getNextDateToMemorize(term.date_latest, term.memory_level);
                    if(date_test.equals(null) || DateUtils.isNotPassed(date_test)) continue;

                    Intent start_intent = new Intent(context, MainActivity.class);
                    PendingIntent pIntent = PendingIntent.getActivity(context, 0, start_intent, 0);

                    Notification noti = new Notification.Builder(context)
                            .setContentTitle("Let's get the daily test done!").setSmallIcon(R.drawable.logo)
                            .setContentIntent(pIntent)
                            .build();
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    // hide the notification after its selected
                    noti.flags |= Notification.FLAG_AUTO_CANCEL;

                    notificationManager.notify(0, noti);
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}