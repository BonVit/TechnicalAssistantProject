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
import com.ta.bd.technicalassistant.Source.SolveEquation;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.Vector;

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
    private EditText editText_result;
    private EditText editText_limit1;
    private EditText editText_limit2;
    private EditText editText_accuracy;

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
        editText_result = (EditText) view.findViewById(R.id.editText_equation_result);
        editText_limit1 = (EditText) view.findViewById(R.id.editText_equation_limit1);
        editText_limit2 = (EditText) view.findViewById(R.id.editText_equation_limit2);
        editText_accuracy = (EditText) view.findViewById(R.id.editText_equation_epsilon);

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
                int event_action = event.getAction();
                switch (event_action)
                {
                    case MotionEvent.ACTION_DOWN:
                        if (editText_input.getText().toString().length() == 0)
                        {
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.input_equation) + "!", Toast.LENGTH_SHORT)
                                    .show();
                            return false;
                        }
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

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (editText_input.getText().toString().length() == 0)
                    return;

                //Wrapper (another crunch)
                Vector<Expression> expression = new Vector<Expression>();
                try
                {
                    expression.addElement(new ExpressionBuilder(editText_input.getText().toString())
                            .variables("x")
                            .build());
                } catch (Exception e)
                {
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.invalid_equation), Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                editText_result.setVisibility(View.VISIBLE);
                String set_text = "";
                set_text += getString(R.string.result) + "\n";

                double limit1, limit2, eps;
                if (editText_limit1.getText().toString().length() == 0)
                    limit1 = Double.parseDouble(editText_limit1.getHint().toString());
                else
                    limit1 = Double.parseDouble(editText_limit1.getText().toString());
                if (editText_limit2.getText().toString().length() == 0)
                    limit2 = Double.parseDouble(editText_limit2.getHint().toString());
                else
                    limit2 = Double.parseDouble(editText_limit2.getText().toString());
                if (editText_accuracy.getText().toString().length() == 0)
                    eps = Double.parseDouble(editText_accuracy.getHint().toString());
                else
                    eps = Double.parseDouble(editText_accuracy.getText().toString());

                Vector<Double> result;
                //Solution
                result = SolveEquation.iterationMethod(expression.firstElement(), limit1, limit2, eps);

                for(int i = 0; i < result.size(); i++)
                {
                    if(i != 0)
                        set_text += "\n";
                    set_text += "X[" + (i + 1) + "] = " + result.elementAt(i);
                }

                editText_result.setText(set_text);
            }
        });

        return view;
    }
}
