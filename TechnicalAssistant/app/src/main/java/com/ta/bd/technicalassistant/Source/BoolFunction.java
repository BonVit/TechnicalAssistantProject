package com.ta.bd.technicalassistant.Source;

/**
 * Created by Vitaliy on 4/28/2015.
 */
class BoolFunction {
    private String[] truth_table;

    public String[] getTruthTable() {
        return truth_table;
    }



    BoolFunction(String function) {
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
    }
}
