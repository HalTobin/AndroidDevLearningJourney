package com.example.maru.utils;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;

import com.example.maru.R;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RecyclerViewItemDateAssertion implements ViewAssertion {
    private final int expectedYear;
    private final int expectedMonth;
    private final int expectedDay;

    public RecyclerViewItemDateAssertion(int expectedYear, int expectedMonth, int expectedDay) {
        this.expectedYear = expectedYear;
        this.expectedMonth = expectedMonth;
        this.expectedDay = expectedDay;
    }

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        RecyclerView recyclerView = (RecyclerView) view;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        assertThat(adapter.getItemCount(), is(expectedYear));
        assertThat(adapter.getItemCount(), is(expectedMonth));
        assertThat(adapter.getItemCount(), is(expectedDay));

        adapter.getItemId(R.id.item_list_meeting_txt_name);
    }
}