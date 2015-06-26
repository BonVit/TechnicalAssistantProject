package com.ta.bd.technicalassistant.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Selection;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ta.bd.technicalassistant.R;
import com.ta.bd.technicalassistant.Source.BoolFunction;

/**
 * Created by Vitaliy on 4/28/2015.
 */
public class MinimizeFragment extends Fragment
{
    private BoolFunction bool_function;

    private View view;
    private EditText editText_bool_function;
    private EditText editText_minimization_result;
    private Button button_mdnf;

    private TextView minimize_hint;
    private TextView minimize_hint_text;

    private TextView minimize_input_count;

    private TextView minimization_solution;
    private TextView minimization_solution_text;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.minimize_fragment, container, false);

        editText_bool_function = (EditText) view.findViewById(R.id.editText_input_bool_function);
        editText_minimization_result = (EditText) view.findViewById(R.id.editText_minimization_result);
        button_mdnf = (Button) view.findViewById(R.id.button_minimize);
        minimize_hint = (TextView) view.findViewById(R.id.textView_minimize_hint);
        minimize_hint_text = (TextView) view.findViewById(R.id.textView_minimize_hint_text);
        minimize_hint_text.setMovementMethod(LinkMovementMethod.getInstance());
        minimization_solution = (TextView) view.findViewById(R.id.textView_minimization_solution);
        minimization_solution_text = (TextView) view.findViewById(R.id.textView_minimization_solution_text);

        minimize_input_count = (TextView) view.findViewById(R.id.textView_minimize_input_count);
        minimize_input_count.setText(R.string.symbols_entered);
        minimize_input_count.append("0");
        minimize_input_count.setTextColor(getResources().getColor(R.color.red));

        editText_bool_function.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                //Type only 1 and 0
                editText_bool_function.setFocusable(true);
                if (editText_bool_function.getText().toString().length() == 0)
                    return;

                String temp = "";
                for (int i = 0; i < editText_bool_function.getText().toString().length(); i++)
                    if (editText_bool_function.getText().toString().charAt(i) == '1' ||
                            editText_bool_function.getText().toString().charAt(i) == '0')
                        temp += editText_bool_function.getText().toString().charAt(i);

                if (temp.length() != editText_bool_function.getText().toString().length())
                    editText_bool_function.setText(temp);
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                //Set cursor to the end
                Selection.setSelection((Editable) editText_bool_function.getText(), editText_bool_function.getText().length());

                //Minimization button enable control and count entered symbols
                double check_length = Math.log(editText_bool_function.getText().length()) / Math.log(2);
                if (check_length == (int) check_length && editText_bool_function.getText().length() > 1)
                {
                    minimize_input_count.setTextColor(getResources().getColor(R.color.green));
                    button_mdnf.setEnabled(true);
                } else
                {
                    minimize_input_count.setTextColor(getResources().getColor(R.color.red));
                    button_mdnf.setEnabled(false);
                }
                minimize_input_count.setText(R.string.symbols_entered);
                minimize_input_count.append(((Integer) editText_bool_function.getText().length()).toString());
            }
        });

        //MDNF button on click animation
        button_mdnf.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                int event_action = event.getAction();
                switch (event_action)
                {
                    case MotionEvent.ACTION_DOWN:
                        button_mdnf.setBackground(getResources().getDrawable(R.drawable.button_on_down));
                        button_mdnf.callOnClick();
                        return true;
                    case MotionEvent.ACTION_UP:
                        button_mdnf.setBackground(getResources().getDrawable(R.drawable.button));
                        return false;
                }
                return false;
            }
        });

        //MDNF button
        button_mdnf.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                editText_minimization_result.setVisibility(View.VISIBLE);
                editText_minimization_result.setText(R.string.result);
                bool_function = new BoolFunction(editText_bool_function.getText().toString());

                for (int i = 0; i < bool_function.getMdnf().size(); i++)
                    editText_minimization_result.setText(editText_minimization_result.getText().toString()
                            + "\n" + getString(R.string.mdnf) + "[" + (i + 1) + "] = " + bool_function.getMdnf().elementAt(i));

                minimization_solution.setVisibility(View.VISIBLE);

                //Log
                String set_text = new String("");
                set_text = getString(R.string.minimization_entered_function);
                set_text += bool_function.getFunction() + "\n";
                set_text += getString(R.string.minimization_truth_table);
                for (int i = 0; i < bool_function.getTruthTable()[0].length() - 1; i++)
                {
                    set_text += " x" + (bool_function.getTruthTable()[i].length() - i - 1) + " |";
                }
                set_text += " f  |\n";
                for (int i = 0; i < bool_function.getTruthTable().length; i++)
                {
                    int j;
                    for (j = 0; j < bool_function.getTruthTable()[i].length() - 1; j++)
                    {
                        set_text += " " + bool_function.getTruthTable()[i].charAt(j) + "   ";
                        set_text += "|";
                    }
                    set_text += " " + bool_function.getTruthTable()[i].charAt(j) + " |\n";
                }
                set_text += getString(R.string.dnf) + " = ";
                for (int i = 0; i < bool_function.getDnf().size(); i++)
                {
                    if (i != 0)
                        set_text += " + ";
                    set_text += bool_function.getDnf().elementAt(i);
                }
                set_text += "\n" + getString(R.string.minimization) + getString(R.string.minimization_terms);
                for (int i = 0, choose = 1, glue = 1; i < bool_function.getMdnfLog().size(); i++)
                {
                    if (bool_function.getMdnfLog().elementAt(i) == "\n")
                    {
                        if (choose == 0)
                        {
                            set_text += "\n" + getString(R.string.minimization_terms);
                            choose = 1;
                        } else
                        {
                            set_text += glue + getString(R.string.minimization_glue);
                            choose = 0;
                            glue++;
                        }
                    } else
                        set_text += bool_function.getMdnfLog().elementAt(i);
                }

                //Quine matrix
                set_text += getString(R.string.quaine_matrix);
                for (int i = 0, term_len = bool_function.getMdnfLogTable()[0][1].length(); i < bool_function.getMdnfLogTable().length; i++)
                {
                    if (i == 0)
                    {
                        for (int j = 0; j < term_len; j++)
                            set_text += "  ";
                        set_text += " |";

                        for (int j = 1; j < bool_function.getMdnfLogTable()[i].length; j++)
                            set_text += " " + bool_function.getMdnfLogTable()[i][j] + " |";
                        set_text += "\n";
                        continue;
                    }

                    for (int j = 0; j < bool_function.getMdnfLogTable()[i].length; j++)
                    {
                        set_text += " " + bool_function.getMdnfLogTable()[i][j] + " ";
                        if (j != 0)
                        {
                            for (int k = 0; k < term_len - 1; k++)
                                set_text += "  ";
                            if (j % 4 == 0)
                                set_text += " ";
                        }
                        set_text += " ";
                        set_text += "|";
                    }
                    set_text += "\n";
                }

                //Result
                set_text += "\n" + getString(R.string.result) + "\n";
                for (int i = 0; i < bool_function.getMdnf().size(); i++)
                    set_text += getString(R.string.mdnf) + "[" + (i + 1) + "] = " + bool_function.getMdnf().elementAt(i) + "\n";

                minimization_solution_text.setText(set_text);
            }
        });

        minimize_hint.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (minimize_hint_text.getMaxLines() == 0)
                {
                    minimize_hint_text.setMaxLines(Integer.MAX_VALUE);
                    minimize_hint_text.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
                    minimize_hint.setText(getString(R.string.hint));
                    minimize_hint.setBackground(getResources().getDrawable(R.drawable.spoiler));
                } else
                {
                    minimize_hint_text.setMaxLines(0);
                    SpannableString spanString = new SpannableString(getString(R.string.hint));
                    spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
                    minimize_hint.setText(spanString);
                    minimize_hint.setBackground(getResources().getDrawable(R.color.application_background_color));
                }
            }
        });

        minimization_solution.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (minimization_solution_text.getMaxLines() == 0)
                {
                    minimization_solution_text.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
                    minimization_solution_text.setMaxLines(Integer.MAX_VALUE);
                    minimization_solution.setText(getString(R.string.minimization_solution));
                    minimization_solution.setBackground(getResources().getDrawable(R.drawable.spoiler));
                }
                else
                {
                    minimization_solution_text.setMaxLines(0);
                    SpannableString spanString = new SpannableString(getString(R.string.minimization_solution));
                    spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
                    minimization_solution.setText(spanString);
                    minimization_solution.setBackground(getResources().getDrawable(R.color.application_background_color));
                }
            }
        });

        return view;
    }
}
