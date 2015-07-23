package com.ta.bd.technicalassistant.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ta.bd.technicalassistant.R;

/**
 * Created by Vitaliy on 6/23/2015.
 */
public class MainFragment extends Fragment
{
    private View view;
    private TextView textView_about_text;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.main_fragment, container, false);
        textView_about_text = (TextView) view.findViewById(R.id.textView_about_text);
        textView_about_text.setMovementMethod(LinkMovementMethod.getInstance());

        return view;
    }
}
