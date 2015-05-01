package com.ta.bd.technicalassistant.Source;

import java.util.Vector;

/**
 * Created by Vitaliy on 4/28/2015.
 */

class BoolFunction
{
    private String[] truth_table;
    private Vector<String> dnf;
    private Vector<String> cnf;


    public String[] getTruthTable()
    {
        return truth_table;
    }

    public Vector<String> getDnf()
    {
        return dnf;
    }

    public Vector<String> getCnf()
    {
        return cnf;
    }

    /*private static Vector<String> minimize(Vector<String> terms){


        return terms;
    }*/

    BoolFunction(String function)
    {
        //Create truth table
        truth_table = new String [function.length()];
        for(int i = 0; i < truth_table.length; i++)
            truth_table[i] = "";
        for(int i = 0, divider = function.length() / 2; i < Math.log(function.length()) / Math.log(2); i++, divider /= 2)
            for(int j = 0; j < function.length(); j++)
                if(((j / divider) & 1) == 0)
                    truth_table[j] += '0';
                else
                    truth_table[j] += '1';
        for(int i = 0; i < function.length(); i++)
            truth_table[i] += function.charAt(i);

        //DNF and CNF
        dnf = new Vector<String>();
        cnf = new Vector<String>();
        for(String tmp : truth_table)
            if(tmp.charAt(tmp.length() - 1) == '1')
                dnf.addElement(tmp.substring(0, tmp.length() - 1));
            else
                cnf.addElement(tmp.substring(0, tmp.length() - 1));
    }
}
