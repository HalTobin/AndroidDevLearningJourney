package com.chapeaumoineau.musicrange.activity.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.chapeaumoineau.musicrange.R;

public class NullFragment extends Fragment {

    public static NullFragment newInstance() {
        NullFragment fragment = new NullFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_null, container, false);
        return view;
    }
}
