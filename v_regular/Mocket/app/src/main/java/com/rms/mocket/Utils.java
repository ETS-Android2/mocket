package com.rms.mocket;

import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Random;



public class Utils {

    /* Resize ListView dynamically */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        final int UNBOUNDED = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);


        int grossElementHeight = 0;
        for (int i = 0; i < listView.getCount(); i++) {
            View childView = listAdapter.getView(i, null, listView);
            childView.measure(UNBOUNDED, UNBOUNDED);
            grossElementHeight += childView.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = grossElementHeight;
        listView.setLayoutParams(params);
        listView.requestLayout();

/*
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }


        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()-1 ));
        Log.d("Mocket","totalHeight: "+totalHeight);
        Log.d("Mocket","listView.getDividerHeight(): "+listView.getDividerHeight());
        Log.d("Mocket","listAdapter.getCount(): "+listAdapter.getCount());
        Log.d("Mocket","params.height: "+params.height);
        listView.setLayoutParams(params);
        listView.requestLayout();

*/
    }

    /* Generates six digits verification code. */
    public static int generateVerificationCode(){
        Random rnd = new Random();
        return 100000 + rnd.nextInt(900000);
    }

    public static void sendEmail(String email, String message){

    }

}
