package com.ta.bd.technicalassistant.Source;

import java.util.Vector;

/**
 * Created by Vitaliy on 4/28/2015.
 */

public class BoolFunction
{
    public static final int DONT_MINIMIZE = 0;
    public static final int MDNF = 1;
    public static final int MKNF = 2;
    public static final int FIND_ALL = 3;

    private String function;
    private String[] truth_table;
    private Vector<String> dnf;
    private Vector<String> cnf;
    private Vector<String> mdnf;
    private String[][] mdnf_log_table;
    private Vector<String> mdnf_log;
    private Vector<String> mknf;
    private String[][] mknf_log_table;
    private Vector<String> mknf_log;

    public String[][] getMdnfLogTable()
    {
        return mdnf_log_table;
    }

    public Vector<String> getMdnfLog()
    {
        return mdnf_log;
    }

    public Vector<String> getMknf()
    {
        return mknf;
    }

    public String[][] getMknfLogTable()
    {
        return mknf_log_table;
    }

    public Vector<String> getMknfLog()
    {
        return mknf_log;
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
            {
                end_of_minimization = true;
                for (int i = 0; i < tmp.size(); i++)
                    if (tmp.elementAt(i) != pre_result.elementAt(i))
                    {
                        end_of_minimization = false;
                        break;
                    }
            }
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

        //Core
        String core = new String("");
        Vector<Integer> colomns_not_in_core = new Vector<Integer>();
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

        //Different mdnf from table
        Vector<String> result = new Vector<String>();
        Vector<Vector<Term>> terms_not_in_core = new Vector<>();
        for(int i = 1; i < table_tmp.length; i++)
        {
            if (core.contains(table_tmp[i][0]))
                for (int j = 1; j < table_tmp[i].length; j++)
                    if (table_tmp[i][j] == "+" || table_tmp[i][j] == "#")
                        is_colomn_in_core[j - 1] = true;
        }
        for(int i = 0; i < is_colomn_in_core.length; i++)
            if(!is_colomn_in_core[i])
            {
                colomns_not_in_core.addElement(i + 1);
                terms_not_in_core.addElement(new Vector<Term>());
                String temporary = new String("");

                for (int j = 1; j < table_tmp.length; j++)
                    if (table_tmp[j][i + 1] == "#")
                    {
                        Term new_term = new Term();
                        new_term.setValue(table_tmp[j][0]);
                        new_term.setNum(j);
                        Vector<Integer> new_term_nums_of_colomns = new Vector<Integer>();
                        for(int k = 1; k < table_tmp[j].length; k++)
                            if(table_tmp[j][k] == "+" || table_tmp[j][k] == "#")
                                new_term_nums_of_colomns.addElement(k);
                        new_term.setNumOfColomns(new_term_nums_of_colomns);

                        terms_not_in_core.lastElement().addElement(new_term);
                    }
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

                if(indexs[0] >= terms_not_in_core.firstElement().size())
                    break;

                String buff = new String(core);
                Vector<Integer> added_colomns = new Vector<Integer>();
                for(int i = 0; i < terms_not_in_core.size(); i++)
                {
                    if(!added_colomns.contains(colomns_not_in_core.elementAt(i)))
                    {
                        buff += " + " + terms_not_in_core.elementAt(i).elementAt(indexs[i]).getValue();
                        added_colomns.addAll(terms_not_in_core.elementAt(i).elementAt(indexs[i]).getNumOfColomns());
                    }
                }
                if(!result.contains(buff))
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

    private Vector<String> mdnf_to_mknf(Vector<String> mdnf)
    {
        Vector<String> result = new Vector<String>();

        for(int i = 0; i < mdnf.size(); i++)
        {
            boolean is_first = true;
            String new_string = new String("");
            for(int j = 0; j < mdnf.elementAt(i).length(); j++)
            {
                switch(mdnf.elementAt(i).charAt(j))
                {
                    case '0':
                        if(is_first)
                            new_string += "(0";
                        else
                            new_string += " + 0";
                        is_first = false;
                        break;
                    case '1':
                        if(is_first)
                            new_string += "(1";
                        else
                            new_string += " + 1";
                        is_first = false;
                        break;
                    case '-':
                        if(is_first)
                            new_string += "(-";
                        else
                            new_string += " + -";
                        is_first = false;
                        break;
                    case ' ':
                        new_string += ") * ";
                        j+=2;
                        is_first = true;
                        break;
                }
            }
            new_string += ")";
            result.addElement(new_string);
        }
        return result;
    }

    public BoolFunction(String func, int choose)
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
            {
                String buffer = new String("");
                for(int j = 0; j < tmp.length() - 1; j++)
                    if(tmp.charAt(j) == '1')
                        buffer += '0';
                    else
                        buffer += '1';
                cnf.addElement(buffer);
            }

        //MDNF
        switch(choose)
        {
            case DONT_MINIMIZE:
                mdnf_log = null;
                mdnf_log_table = null;
                mdnf = null;
                mknf = null;
                mknf_log = null;
                mknf_log_table = null;
                break;
            case 1:
                mdnf_log = new Vector<String>();
                mdnf_log_table = null;
                Vector<String[][]> mdnf_buffer = new Vector<String[][]>();
                mdnf = new Vector<String>(minimize(dnf, mdnf_log, mdnf_buffer));
                mdnf_log_table = mdnf_buffer.firstElement();
                break;
            case 2:
                mknf_log = new Vector<String>();
                mknf_log_table = null;
                Vector<String[][]> mknf_buffer = new Vector<String[][]>();
                mknf = new Vector<String>(minimize(cnf, mknf_log, mknf_buffer));
                mknf = mdnf_to_mknf(mknf);
                mknf_log_table = mknf_buffer.firstElement();
                break;
            default:
                mdnf_log = new Vector<String>();
                mdnf_log_table = null;
                Vector<String[][]> mdnf_buff = new Vector<String[][]>();
                mdnf = new Vector<String>(minimize(dnf, mdnf_log, mdnf_buff));
                mdnf_log_table = mdnf_buff.firstElement();
                mknf_log = new Vector<String>();
                mknf_log_table = null;
                Vector<String[][]> mknf_buff = new Vector<String[][]>();
                mknf = new Vector<String>(minimize(cnf, mknf_log, mknf_buff));
                mknf = mdnf_to_mknf(mknf);
                mknf_log_table = mknf_buff.firstElement();
        }
    }
}

class Term
{
    private String value;
    private int num;
    private Vector<Integer> num_of_colomns;

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public int getNum()
    {
        return num;
    }

    public void setNum(int num)
    {
        this.num = num;
    }

    public Vector<Integer> getNumOfColomns()
    {
        return num_of_colomns;
    }

    public void setNumOfColomns(Vector<Integer> num_of_colomns)
    {
        this.num_of_colomns = num_of_colomns;
    }
}
