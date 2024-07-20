package com.chapeaumoineau.musicrange.activity.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.chapeaumoineau.musicrange.activity.fragments.AlbumFragment;
import com.chapeaumoineau.musicrange.activity.fragments.PlayerFragment;
import com.chapeaumoineau.musicrange.activity.fragments.SetlistFragment;
import com.chapeaumoineau.musicrange.model.Album;
import com.chapeaumoineau.musicrange.model.Artist;

import java.util.ArrayList;
import java.util.List;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> myFragments = new ArrayList<>();

    public MainPagerAdapter(FragmentManager fm) { super(fm); }

    public void addFragment(Fragment fragment) {
        myFragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) { return myFragments.get(position); }

    @Override
    public int getCount() { return myFragments.size(); }
}
