package com.ta.bd.technicalassistant.Fragments;

//import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.TextView;

import com.ta.bd.technicalassistant.R;
import com.ta.bd.technicalassistant.Source.BoolFunction;

import java.util.Vector;

/**
 * Created by Vitaliy on 4/28/2015.
 */
public class MinimizeFragment extends Fragment
{
    private BoolFunction bool_function;

    private View view;
    private TextView string_bool_function;
    private TextView string_minimization_result;
    private Button button_minimize;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.minimize_layout, container, false);

        string_bool_function = (TextView) view.findViewById(R.id.editText_input_bool_fucntion);
        string_minimization_result = (TextView) view.findViewById(R.id.editText_minimization_result);
        button_minimize = (Button) view.findViewById(R.id.button_minimize);

        button_minimize.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                bool_function = new BoolFunction(string_bool_function.getText().toString());
                Vector<String> a = bool_function.getMdnf();
                string_minimization_result.setText(bool_function.getMdnf().elementAt(0));
            }
        });

        return view;
    }
}
