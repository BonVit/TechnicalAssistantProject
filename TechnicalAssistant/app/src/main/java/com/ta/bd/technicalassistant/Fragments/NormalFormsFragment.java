package com.ta.bd.technicalassistant.Fragments;

import android.support.v4.app.Fragment;
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
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ta.bd.technicalassistant.R;
import com.ta.bd.technicalassistant.Source.BoolFunction;

/**
 * Created by Vitaliy on 6/24/2015.
 */
public class NormalFormsFragment extends Fragment
{
    private View view;

    private TextView textView_hint;
    private TextView textView_hint_text;
    private EditText editText_input;
    private TextView textView_input_count;
    private TableLayout table_layout;
    private Button button;
    private TextView textView_truth_table;
    private EditText editText_result;

    private BoolFunction bool_function;

    private void createTable(TableLayout table_layout, String[][] data)
    {
        for(int i = 0; i < data.length; i++)
        {
            TableRow new_table_row = new TableRow(table_layout.getContext());


            for(int j = 0; j < data[i].length; j++)
            {
                TextView new_text_view = new TextView(new_table_row.getContext());
                new_text_view.setText(data[i][j]);
                new_text_view.setBackground(getResources().getDrawable(R.drawable.text_view_table_cell));
                new_text_view.setMinWidth(10);
                new_text_view.setMinHeight(10);
                new_text_view.setTextSize(14);
                new_table_row.addView(new_text_view);
            }

            table_layout.addView(new_table_row);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.normal_forms_fragment, container, false);

        textView_hint = (TextView) view.findViewById(R.id.textView_normal_forms_hint);
        textView_hint_text = (TextView) view.findViewById(R.id.textView_normal_forms_hint_text);
        textView_hint_text.setMovementMethod(LinkMovementMethod.getInstance());
        editText_input = (EditText) view.findViewById(R.id.editText_normal_forms_input);
        textView_input_count = (TextView) view.findViewById(R.id.textView_normal_forms_input_count);
        textView_input_count.setText(R.string.symbols_entered);
        textView_input_count.append("0");
        textView_input_count.setTextColor(getResources().getColor(R.color.red));
        table_layout = (TableLayout) view.findViewById(R.id.tableLayout_truth_table);
        button = (Button) view.findViewById(R.id.button_normal_forms);
        textView_truth_table = (TextView) view.findViewById(R.id.textView_normal_forms_truth_table);
        editText_result = (EditText) view.findViewById(R.id.editText_normal_forms_result);


        editText_input.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                //Minimization button enable control and count entered symbols
                double check_length = Math.log(editText_input.getText().length()) / Math.log(2);
                if (check_length == (int) check_length && editText_input.getText().length() > 1)
                {
                    textView_input_count.setTextColor(getResources().getColor(R.color.green));
                    button.setEnabled(true);
                }
                else
                {
                    textView_input_count.setTextColor(getResources().getColor(R.color.red));
                    button.setEnabled(false);
                }
                textView_input_count.setText(R.string.symbols_entered);
                textView_input_count.append(((Integer) editText_input.getText().length()).toString());
            }
        });

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
                textView_truth_table.setVisibility(View.VISIBLE);
                bool_function = new BoolFunction(editText_input.getText().toString(), BoolFunction.DONT_MINIMIZE);
                String[][] data = new String[bool_function.getTruthTable().length + 1][bool_function.getTruthTable()[0].length()];

                for (int i = 0; i < data[0].length - 1; i++)
                    data[0][i] = "X" + (data[0].length - 1 - i - 1);
                data[0][data[0].length - 1] = "f";

                for (int i = 1; i < data.length; i++)
                    for (int j = 0; j < data[i].length; j++)
                    {
                        data[i][j] = "";
                        data[i][j] += bool_function.getTruthTable()[i - 1].charAt(j);
                    }

                table_layout.removeAllViews();
                createTable(table_layout, data);

                editText_result.setVisibility(View.VISIBLE);
                String set_text = new String(getString(R.string.result) + "\n" + getString(R.string.dnf) + " = ");
                for (int i = 0; i < bool_function.getDnf().size(); i++)
                {
                    if (i != 0)
                        set_text += " + ";
                    set_text += bool_function.getDnf().elementAt(i);
                }
                set_text += "\n" + getString(R.string.cnf) + " = ";
                for (int i = 0; i < bool_function.getCnf().size(); i++)
                {
                    if (i != 0)
                        set_text += " * ";
                    for (int j = 0; j < bool_function.getCnf().elementAt(i).length(); j++)
                    {
                        if(j == 0)
                            set_text += "(";
                        else
                            set_text += " + ";
                        set_text += bool_function.getCnf().elementAt(i).charAt(j);
                    }
                    set_text += ")";
                }
                editText_result.setText(set_text);
            }
        });

        return view;
    }
}
