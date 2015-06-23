package com.ta.bd.technicalassistant.Source;

import java.util.Vector;

/**
 * Created by Vitaliy on 4/28/2015.
 */

public class BoolFunction
{
    private String function;
    private String[] truth_table;
    private Vector<String> dnf;
    private Vector<String> cnf;
    private Vector<String> mdnf;
    private String[][] mdnf_log_table;
    private Vector<String> mdnf_log;

    public String[][] getMdnfLogTable()
    {
        return mdnf_log_table;
    }

    public Vector<String> getMdnfLog()
    {
        return mdnf_log;
    }

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

    public static Vector<String> minimize(Vector<String> terms, Vector<String> log, Vector<String[][]> table)
    {
        log.clear();
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
        log.removeElementAt(log.size() - 1);
        log.removeElementAt(log.size() - 1);

        tmp = null;

        //Table
        String[][] table_tmp = new String[pre_result.size() + 1][terms.size() + 1];
        table_tmp[0][0] = "";
        for(int i = 1; i < pre_result.size() + 1; i++)
            table_tmp[i][0] = pre_result.get(i - 1);
        for(int i = 1; i < terms.size() + 1; i++)
            table_tmp[0][i] = terms.get(i - 1);
        int index = -1;
        for(int i = 1; i < terms.size() + 1; i++)
        {
            index = -1;
            for(int j = 1; j < pre_result.size() + 1; j++)
            {
                boolean is_sub_term = true;
                table_tmp[j][i] = "";
                for(int k = 0; k < terms.firstElement().length(); k++)
                    if(!((table_tmp[j][0].charAt(k) == '-') || (table_tmp[j][0].charAt(k) == table_tmp[0][i].charAt(k))))
                        is_sub_term = false;
                if(is_sub_term)
                    if(index == -1)
                    {
                        index = j;
                        table_tmp[j][i] = "+";
                    }
                    else
                    {
                        table_tmp[j][i] = "#";
                        table_tmp[index][i] = "#";
                    }
            }
        }
        table.addElement(table_tmp);

        //Different mdnf from table
        Vector<String> result = new Vector<String>();
        Vector<Vector<String>> terms_not_in_core = new Vector<Vector<String>>();
        String core = new String("");
        boolean is_colomn_in_core[] = new boolean[table_tmp[0].length - 1];
        for(int i = 1; i < table_tmp.length; i++)
            for(int j = 1; j < table_tmp[i].length; j++)
            {
                if(table_tmp[i][j] == "+")
                    if (!core.contains(table_tmp[i][0]))
                    {
                        if(!core.isEmpty())
                            core += " + ";
                        core += table_tmp[i][0];
                    }
            }
        for(int i = 1; i < table_tmp.length; i++)
            if(core.contains(table_tmp[i][0]))
                for(int j = 1; j < table_tmp[i].length; j++)
                    if(table_tmp[i][j] == "+" || table_tmp[i][j] == "#")
                        is_colomn_in_core[j - 1] = true;
        for(int i = 0; i < is_colomn_in_core.length; i++)
            if(!is_colomn_in_core[i])
            {
                terms_not_in_core.addElement(new Vector<String>());
                for(int j = 1; j < table_tmp.length; j++)
                    if (table_tmp[j][i + 1] == "#")
                        terms_not_in_core.lastElement().addElement(table_tmp[j][0]);
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

    public Vector<String> getMdnf()
    {
        return mdnf;
    }

    public String getFunction() { return function; }

    public BoolFunction(String func)
    {
        function = new String(func);
        //Create truth table
        truth_table = new String [func.length()];
        for(int i = 0; i < truth_table.length; i++)
            truth_table[i] = "";
        for(int i = 0, divider = func.length() / 2; i < Math.log(func.length()) / Math.log(2); i++, divider /= 2)
            for(int j = 0; j < func.length(); j++)
                if(((j / divider) & 1) == 0)
                    truth_table[j] += '0';
                else
                    truth_table[j] += '1';
        for(int i = 0; i < func.length(); i++)
            truth_table[i] += func.charAt(i);

        //DNF and CNF
        dnf = new Vector<String>();
        cnf = new Vector<String>();
        for(String tmp : truth_table)
            if(tmp.charAt(tmp.length() - 1) == '1')
                dnf.addElement(tmp.substring(0, tmp.length() - 1));
            else
                cnf.addElement(tmp.substring(0, tmp.length() - 1));

        //MDNF
        mdnf_log = new Vector<String>();
        mdnf_log_table = null;
        Vector<String[][]> buffer = new Vector<String[][]>();
        mdnf = new Vector<String>(minimize(dnf, mdnf_log, buffer));
        mdnf_log_table = buffer.firstElement();
    }
}
