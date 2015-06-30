package com.ta.bd.technicalassistant.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ta.bd.technicalassistant.R;

/**
 * Created by Vitaliy on 6/30/2015.
 */
public class EquationFragment extends Fragment
{
    private View view;

    private TextView textView_hint;
    private TextView textView_hint_text;
    private Button button;
    private EditText editText_input;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.equation_fragment, container, false);

        textView_hint = (TextView) view.findViewById(R.id.textView_equation_hint);
        textView_hint_text = (TextView) view.findViewById(R.id.textView_equation_hint_text);
        textView_hint_text.setMovementMethod(LinkMovementMethod.getInstance());
        button = (Button) view.findViewById(R.id.button_equation);
        editText_input = (EditText) view.findViewById(R.id.editText_equation);

        textView_hint.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (textView_hint_text.getMaxLines() == 0)
                {
                    textView_hint_text.setMaxLines(Integer.MAX_VALUE);
                    textView_hint_text.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
                    textView_hint.setText(getString(R.string.hint));
                    textView_hint.setBackground(getResources().getDrawable(R.drawable.spoiler));
                } else
                {
                    textView_hint_text.setMaxLines(0);
                    SpannableString spanString = new SpannableString(getString(R.string.hint));
                    spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
                    textView_hint.setText(spanString);
                    textView_hint.setBackground(getResources().getDrawable(R.color.application_background_color));
                }
            }
        });

        button.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (editText_input.getText().toString().length() == 0)
                {
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.input_equation) + "!", Toast.LENGTH_SHORT)
                            .show();
                    return false;
                }
                int event_action = event.getAction();
                switch (event_action)
                {
                    case MotionEvent.ACTION_DOWN:
                        button.setBackground(getResources().getDrawable(R.drawable.button_on_down));
                        button.callOnClick();
                        return true;
                    case MotionEvent.ACTION_UP:
                        button.setBackground(getResources().getDrawable(R.drawable.button));
                        return false;
                }
                return false;
            }
        });

        return view;
    }
}
