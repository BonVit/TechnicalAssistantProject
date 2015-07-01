package com.ta.bd.technicalassistant.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.ta.bd.technicalassistant.R;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.Vector;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Vitaliy on 6/26/2015.
 */

public class GraphFragment extends Fragment
{
    final double STEP = 0.2;
    final double MAX_Y = 100;
    final double MIN_Y = -99;

    private View view;

    private GraphView graphView;
    private TextView textView_hint;
    private TextView textView_hint_text;
    private Button button;
    private EditText editText_input;
    private EditText editText_limit1;
    private EditText editText_limit2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.graph_fragment, container, false);

        graphView = (GraphView) view.findViewById(R.id.graph);
        textView_hint = (TextView) view.findViewById(R.id.textView_graph_hint);
        textView_hint_text = (TextView) view.findViewById(R.id.textView_graph_hint_text);
        textView_hint_text.setMovementMethod(LinkMovementMethod.getInstance());
        button = (Button) view.findViewById(R.id.button_graph);
        editText_input = (EditText) view.findViewById(R.id.editText_graph);
        editText_limit1 = (EditText) view.findViewById(R.id.editText_graph_limit1);
        editText_limit2 = (EditText) view.findViewById(R.id.editText_graph_limit2);

        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setScrollable(true);
        graphView.getViewport().setScalable(true);
        graphView.getViewport().setMaxY(MAX_Y);
        graphView.getViewport().setMinY(MIN_Y);

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

                double limit1;
                double limit2;
                if (editText_limit1.getText().toString().length() == 0)
                    limit1 = Double.parseDouble(editText_limit1.getHint().toString());
                else
                    limit1 = Double.parseDouble(editText_limit1.getText().toString());
                if (editText_limit2.getText().toString().length() == 0)
                    limit2 = Double.parseDouble(editText_limit2.getHint().toString());
                else
                    limit2 = Double.parseDouble(editText_limit2.getText().toString());

                if (limit1 > limit2)
                {
                    double tmp = limit1;
                    limit1 = limit2;
                    limit2 = tmp;
                }

                //Wrapper (another crunch)
                Vector<Expression> expression = new Vector<Expression>();
                try
                {
                    expression.addElement(new ExpressionBuilder(editText_input.getText().toString())
                            .variables("x")
                            .build());
                }
                catch(Exception e)
                {
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.invalid_function), Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                addSeries(graphView, expression.firstElement(), limit1, limit2);

                graphView.getViewport().setMinX(limit1);
                graphView.getViewport().setMaxX(limit2);
            }
        });

        return view;
    }

    private void addSeries(GraphView graphView, Expression expression, double limit1, double limit2)
    {
        graphView.removeAllSeries();

        //int dots_number = (int) ((limit2 - limit1) / STEP);
        /*double minY = MAX_Y;
        double maxY = MIN_Y;*/

        Vector<Vector<DataPoint>> dataPoints = new Vector<Vector<DataPoint>>();
        dataPoints.addElement(new Vector<DataPoint>());
        for(/*int i = 0*/; limit1 < limit2;)
        {
            try
            {
                dataPoints.lastElement().addElement(new DataPoint(limit1, expression
                        .setVariable("x", limit1)
                        .evaluate()));
            }
            catch(ArithmeticException e)
            {
                dataPoints.addElement(new Vector<DataPoint>());
                limit1 += STEP;
                //i++;
                continue;
            }

            /*if(dataPoints.elementAt(i).lastElement().getY() < minY)
                minY = dataPoints.elementAt(i).lastElement().getY();
            if(dataPoints.elementAt(i).lastElement().getY() > maxY)
                maxY = dataPoints.elementAt(i).lastElement().getY();*/
            limit1 += STEP;
        }

        /*minY = Math.floor(minY);
        if(minY < MIN_Y)
            minY = MIN_Y;
        maxY = Math.ceil(maxY);
        if(maxY > MAX_Y)
            maxY = MAX_Y;
        graphView.getViewport().setMinY(minY);
        graphView.getViewport().setMaxY(maxY);*/

        for(int i = 0; i < dataPoints.size(); i++)
        {
            if(dataPoints.elementAt(i).size() == 0)
                continue;
            DataPoint[] dataPoint = new DataPoint[dataPoints.elementAt(i).size()];
            for(int j = 0; j < dataPoint.length; j++)
                dataPoint[j] = dataPoints.elementAt(i).elementAt(j);
            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoint);
            graphView.addSeries(series);
        }
    }

}
