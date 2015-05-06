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
    private Vector<String> mdnf;
    private String[][] mdnf_log_table;
    private Vector<String> mdnf_log;

    private static boolean equal(String term1, String term2)
    {
        if(term1.length() != term2.length())
            return false;
        int result = 0;
        for(int i = 0; i < term1.length(); i++)
        {
            if(term1.charAt(i) == '-' || term2.charAt(i) == '-')
                if(term1.charAt(i) != term2.charAt(i))
                    return false;
            if(term1.charAt(i) == term2.charAt(i))
                result++;
        }
        if(result == term1.length() - 1)
            return true;
        return false;
    }

    private static String glue(String term1, String term2)
    {
        String result = new String("");
        for(int i = 0; i < term1.length(); i++)
            if(term1.charAt(i) == term2.charAt(i))
                result += term1.charAt(i);
            else
                result += "-";
        return result;
    }

    public static Vector<String> minimize(Vector<String> terms, Vector<String> log, String[][] table)
    {
        log = new Vector<String>();
        Vector<String> tmp = new Vector<String>();
        Vector<String> pre_result;
        for(int i = 0; i < terms.size(); i++)
            tmp.addElement(new String(terms.get(i)));

        for(int i = 0; i < tmp.size(); i++)
            log.addElement((i + 1) + ") " + tmp.get(i) + "\n");
        log.addElement("\n");

        //Minimize
        boolean end_of_minimization = false;
        do
        {
            pre_result = new Vector<String>();
            boolean glued[] = new boolean [tmp.size()];
            for(int i = 0; i < tmp.size() - 1; i++)
            {
                for(int j = i + 1; j < tmp.size(); j++)
                {
                    if(equal(tmp.get(i), tmp.get(j)))
                    {
                        pre_result.addElement(glue(tmp.get(i), tmp.get(j)));
                        log.addElement((i + 1) + " - " + (j + 1) + " : " + pre_result.lastElement() + "\n");
                        glued[i] = true;
                        glued[j] = true;
                    }
                }
            }
            log.addElement("\n");

            //Terms that are not glued
            for(int i = 0; i < glued.length; i++)
                if(glued[i] == true)
                    glued[i] = false;
                else
                    pre_result.addElement(tmp.get(i));

            //End?
            end_of_minimization = false;
            if(tmp.size() == pre_result.size())
                for(int i = 0; i < tmp.size(); i++)
                    if(tmp.get(i) == pre_result.get(i))
                        end_of_minimization = true;
            if(end_of_minimization)
                break;

            //Restore
            tmp = new Vector<String>();
            for(int i = 0; i < pre_result.size(); i++)
            {
                if(!tmp.contains(pre_result.get(i)))
                    tmp.addElement(new String(pre_result.get(i)));
            }


            for(int i = 0; i < tmp.size(); i++)
                log.addElement((i + 1) + ") " + tmp.get(i) + "\n");
            log.addElement("\n");

        }while(!end_of_minimization);
        tmp = null;

        //Table
        table = new String[pre_result.size() + 1][terms.size() + 1];
        table[0][0] = "";
        for(int i = 1; i < pre_result.size() + 1; i++)
            table[i][0] = pre_result.get(i - 1);
        for(int i = 1; i < terms.size() + 1; i++)
            table[0][i] = terms.get(i - 1);
        int index = -1;
        for(int i = 1; i < terms.size() + 1; i++)
        {
            index = -1;
            for(int j = 1; j < pre_result.size() + 1; j++)
            {
                boolean is_sub_term = true;
                table[j][i] = "";
                for(int k = 0; k < terms.firstElement().length(); k++)
                    if(!((table[j][0].charAt(k) == '-') || (table[j][0].charAt(k) == table[0][i].charAt(k))))
                        is_sub_term = false;
                if(is_sub_term)
                    if(index == -1)
                    {
                        index = j;
                        table[j][i] = "+";
                    }
                    else
                    {
                        table[j][i] = "#";
                        table[index][i] = "#";
                    }
            }
        }

        for(int i = 0; i < table.length; i++)
        {
            for(int j = 0; j < table[i].length; j++)
            {
                System.out.print(table[i][j] + " | ");
            }
            System.out.println();
        }

        //Different mdnf from table
        Vector<String> result = new Vector<String>();
        Vector<Vector<String>> terms_not_in_core = new Vector<Vector<String>>();
        String core = new String("");
        boolean is_colomn_in_core[] = new boolean[table[0].length - 1];
        for(int i = 1; i < table.length; i++)
            for(int j = 1; j < table[i].length; j++)
            {
                if(table[i][j] == "+")
                    if (!core.contains(table[i][0]))
                    {
                        if(!core.isEmpty())
                            core += " + ";
                        core += table[i][0];
                    }
            }
        for(int i = 1; i < table.length; i++)
            if(core.contains(table[i][0]))
                for(int j = 1; j < table[i].length; j++)
                    if(table[i][j] == "+" || table[i][j] == "#")
                        is_colomn_in_core[j - 1] = true;
        for(int i = 0; i < is_colomn_in_core.length; i++)
            if(!is_colomn_in_core[i])
            {
                terms_not_in_core.addElement(new Vector<String>());
                for(int j = 1; j < table.length; j++)
                    if (table[j][i + 1] == "#")
                        terms_not_in_core.lastElement().addElement(table[j][0]);
            }
        int indexs[] = new int [terms_not_in_core.size()];
        if(indexs.length > 0)
        {
            while(indexs[0] < terms_not_in_core.firstElement().size())
            {
                int k = indexs.length - 1;
                while(indexs[k] == terms_not_in_core.elementAt(k).size() && k > 0)
                {
                    indexs[k] = 0;
                    indexs[k - 1]++;
                    k--;
                }
                String buff = new String(core);
                for(int i = 0; i < terms_not_in_core.size(); i++)
                    buff += " + " + terms_not_in_core.elementAt(i).elementAt(indexs[i]);
                result.addElement(buff);
                indexs[indexs.length - 1]++;
            }
        }

        if(result.isEmpty())
            result.addElement(core);
        return result;
    }

    public String[] getTruth_table()
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

    public Vector<String> getMdnf()
    {
        return mdnf;
    }

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

        //MDNF
        mdnf_log = null;
        mdnf_log_table = null;
        mdnf = new Vector<String>(minimize(dnf, mdnf_log, mdnf_log_table));
    }
}
