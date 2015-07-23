package com.ta.bd.technicalassistant.Source;

import net.objecthunter.exp4j.Expression;

import java.util.Vector;

/**
 * Created by Vitaliy on 7/2/2015.
 */

public class SolveEquation
{
    public static Vector<Double> iterationMethod(Expression expression, double limit1, double limit2, double eps)
    {
        //left and right values
        if(limit2 < limit1)
        {
            double temp = limit1;
            limit1 = limit2;
            limit2 = temp;
        }

        Vector<Double> result = new Vector<Double>();
        double yl = expression.setVariable("x", limit1).evaluate(), yr = yl;
        do
        {
            limit1 += eps;
            if(limit1 > limit2)
                break;

            double check1, check2;
            try
            {
                yr = expression.setVariable("x", limit1).evaluate();
                check1 = expression.setVariable("x", limit1 - 0.5 * eps).evaluate();
                check2 = expression.setVariable("x", limit1 + 0.5 * eps).evaluate();
            }
            catch (ArithmeticException e)
            {
                yl = yr;
                continue;
            }

            if((yl <= 0 && yr >= 0) || (yl >= 0 && yr <= 0)
                    || (yl >= 0 && yr >= 0 && check1 < yl && check2 > yr)
                    || (yl <= 0 && yr <= 0 && check1 > yl && check2 < yr))
                result.addElement(limit1 - eps / 2);

            yl = yr;
        }while(true);

        return  result;
    }
}