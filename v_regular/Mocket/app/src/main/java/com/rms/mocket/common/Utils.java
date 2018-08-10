package com.rms.mocket.common;

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
    }



    /* Generates six digits verification code. */
    public static int generateVerificationCode() {
        Random rnd = new Random();
        return 100000 + rnd.nextInt(900000);
    }

    public static void sendEmail(String email, String message) {

    }

}
