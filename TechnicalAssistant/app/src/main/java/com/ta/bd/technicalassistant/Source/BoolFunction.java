package com.ta.bd.technicalassistant.Source;

/**
 * Created by Vitaliy on 4/28/2015.
 */
class BoolFunction {
    private String[] truthTable;

    public String[] getTruthTable() {
        return truthTable;
    }



    BoolFunction(String function) {
        truthTable = new String [function.length()];
        for(int i = 0; i < truthTable.length; i++)
            truthTable[i] = "";
        for(int i = 0, divider = function.length() / 2; i < Math.log(function.length()) / Math.log(2); i++, divider /= 2)
            for(int j = 0; j < function.length(); j++)
                if(((j / divider) & 1) == 0)
                    truthTable[j] += '0';
                else
                    truthTable[j] += '1';
        for(int i = 0; i < function.length(); i++)
            truthTable[i] += function.charAt(i);
    }
}
