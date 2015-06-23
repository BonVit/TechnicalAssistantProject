package com.ta.bd.technicalassistant.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ta.bd.technicalassistant.R;

/**
 * Created by Vitaliy on 6/23/2015.
 */
public class MainFragment extends Fragment
{
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.main_fragment, container, false);

        return view;
    }
}
