package com.sai.weatherio.recycler_view_helpers;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by sai on 2/4/18.
 */

public class RecyclerViewItemCountAssertion implements ViewAssertion {

    private final int countToVerify;

    public RecyclerViewItemCountAssertion(int countToVerify) {
        this.countToVerify = countToVerify;
    }

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        RecyclerView recyclerView = (RecyclerView) view;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        assertThat(adapter.getItemCount(), is(countToVerify));
    }
}
