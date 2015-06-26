package com.ta.bd.technicalassistant.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.ta.bd.technicalassistant.R;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Created by Vitaliy on 6/26/2015.
 */
public class GraphFragment extends Fragment
{
    private View view;

    private GraphView graphView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.graph_fragment, container, false);

        graphView = (GraphView) view.findViewById(R.id.graph);

        Expression expression = new ExpressionBuilder("x + 3 * y")
                .variables("x", "y")
                .build()
                .setVariable("x", 2)
                .setVariable("y", 1);
        double result = expression.evaluate();

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(1, 1),
                new DataPoint(1, 2),
                new DataPoint(3, result)
        });
        graphView.addSeries(series);


        return view;
    }
}
