package ga_assignemnt.pkg3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Double.NaN;
import java.util.ArrayList;

/**
 *
 * @author Om-mostafa El-Hariry
 */
public class GA_Assignemnt3 {

    /**
     * @param args the command line arguments
     */
    static class FuzzySet {

        String name;
        String type;
        ArrayList<Integer> points = new ArrayList();
    }

    static class FuzzyVariable {

        String variableName;
        int variableValue;
        ArrayList<FuzzySet> fuzzySets = new ArrayList();
    }

    static class FuzzyResult {

        String resultName;
        ArrayList<FuzzySet> resultSets = new ArrayList();
        ArrayList<Evaluation> resultMembership = new ArrayList();
    }

    static class Fuzzy {

        int variablesNum;
        ArrayList<FuzzyVariable> fuzzyVariables = new ArrayList();
        FuzzyResult fuzzyResult = new FuzzyResult();
    }

    static class Rule {

        int varNumPerRule;
        String variableName[];
        String variableValue[];
        String operator[];
        String resultName;
        String resultValue;

        Rule(int var) {
            varNumPerRule = var;
            variableName = new String[var];
            variableValue = new String[var];
            operator = new String[var - 1];
        }
    }

    public static void main(String[] args) throws IOException 
    {
        File inputFile = new File("input.txt");
        if (!inputFile.exists()) 
            inputFile.createNewFile();
        
        BufferedReader br = new BufferedReader(new FileReader("input.txt"));
        Fuzzy fuzzyData = new Fuzzy();

        fuzzyData.variablesNum = Integer.parseInt(br.readLine());
        for (int i = 0; i < fuzzyData.variablesNum; i++) 
        {
            FuzzyVariable varData = new FuzzyVariable();
            String line = br.readLine();
            String arr[] = line.split(" ");

            varData.variableName = arr[0];
            varData.variableValue = Integer.parseInt(arr[1]);

            int setsNum = Integer.parseInt(br.readLine());
            for (int j = 0; j < setsNum; j++) 
            {
                FuzzySet setData = ReadSetFromFile(br, setsNum);
                varData.fuzzySets.add(setData);
            }
            fuzzyData.fuzzyVariables.add(varData);
        }
        
        FuzzyResult resData = new FuzzyResult();
        resData.resultName = br.readLine();
        int resultSetsNum = Integer.parseInt(br.readLine());
        for (int i = 0; i < resultSetsNum; i++) 
        {
            FuzzySet resultSetData = ReadSetFromFile(br, resultSetsNum);
            resData.resultSets.add(resultSetData);
        }
        fuzzyData.fuzzyResult = resData;

        int rulesNum = Integer.parseInt(br.readLine());
        ArrayList<Rule> rules = new ArrayList();
        for (int i = 0; i < rulesNum; i++) 
        {
            Rule rule = ReadRuleFromFile(br, rulesNum);
            rules.add(rule);
        }

        ArrayList<FuzzificationData> fuzzification = new ArrayList();
            
        for(int i=0; i<fuzzyData.variablesNum; i++)
        {
            FuzzificationData fData = Fuzzification(fuzzyData.fuzzyVariables.get(i));
            fuzzification.add(fData);
        }
        for(int i=0; i<fuzzification.size(); i++)
        {
            System.out.println("varName " + fuzzification.get(i).varName);
            for(int j=0; j<fuzzification.get(i).resultSet.size(); j++)
            {
                System.out.println("\tsetName " + fuzzification.get(i).resultSet.get(j).setName);
                System.out.println("\tmembership " + fuzzification.get(i).resultSet.get(j).MemberShipResult);
            }
        }
        for(int i=0; i<rules.size(); i++)
        {
            Evaluation result = EvaluationRule(rules.get(i), fuzzification);
            resData.resultMembership.add(result);
        }
        for(int i=0; i<rules.size(); i++)
            System.out.println("Rule " + i + " = " + resData.resultMembership.get(i).fuzzyResult);
        double deFuzzy = DeFuzzification(resData, rules);
        System.out.println("Final Result : " + deFuzzy);
        br.close();
    }

    static FuzzySet ReadSetFromFile(BufferedReader br, int setsNum) throws IOException 
    {
        FuzzySet setData = new FuzzySet();
        String line = br.readLine();
        String arr1[] = line.split(" ");
        setData.name = arr1[0];
        setData.type = arr1[1];
        String p = br.readLine();
        String arr2[] = p.split(" ");
        for(int i = 0; i < arr2.length; i++) 
        {
            setData.points.add(Integer.parseInt(arr2[i]));
        }
        return setData;
    }

    static Rule ReadRuleFromFile(BufferedReader br, int rulesNum) throws IOException 
    {
        String ruleStr = br.readLine();
        String arr[] = ruleStr.split(" ");
        Rule rule = new Rule(Integer.parseInt(arr[0]));
        int varNum = 0, optNum = 0, len = 1;
        while (varNum < rule.varNumPerRule || optNum < rule.varNumPerRule - 1 || len < arr.length) {
            rule.variableName[varNum] = arr[len];
            len += 2;
            rule.variableValue[varNum] = arr[len];
            len++;
            if (arr[len].equals("then")) {
                len++;
                rule.resultName = arr[len];
                len += 2;
                rule.resultValue = arr[len];
                break;
            }
            rule.operator[optNum] = arr[len];
            len++;
            varNum++;
            optNum++;
        }
        return rule;
    }

    static class Point
    {
        int x;
        int y;
        Point(){}
        Point(int valX, int valY)
        {
            x = valX;
            y = valY;
        }
    }
    static ArrayList<Point> GetShapePoints(ArrayList<Integer> fuzzyPoints)
    {
        ArrayList<Point> points = new ArrayList();
        Point p = new Point(fuzzyPoints.get(0), 0);
        points.add(p);
        p = new Point(fuzzyPoints.get(1), 1);
        points.add(p);
        
        if(fuzzyPoints.size() == 3)
        {
            p = new Point(fuzzyPoints.get(2), 0);
            points.add(p);
        }
        else if(fuzzyPoints.size() == 4)
        {
            p = new Point(fuzzyPoints.get(2), 1);
            points.add(p);
            p = new Point(fuzzyPoints.get(3), 0);
            points.add(p);
        }
        return points;
    }
    static double GetSlope(Point p1, Point p2)
    {
        double slope = (double)(p2.y - p1.y) / (double)(p2.x - p1.x);
        return slope;
    }
    static double Get_b(Point p, double slope)
    {
        double b = p.y - (slope * p.x);
        return b;
    }
    static double GetMembershipResult(int x, double slope, double b)
    {
        double y = (slope * x) + b;
        return y;
    }

    static class FuzzificationSet
    {
        String setName;
        double MemberShipResult;
    }
    static class FuzzificationData
    {
        String varName;
        ArrayList<FuzzificationSet> resultSet = new ArrayList();
    }
    static FuzzificationData Fuzzification(FuzzyVariable varData)
    {
        FuzzificationData fData = new FuzzificationData();
        ArrayList<FuzzificationSet> allSets = new ArrayList();
        for(int n=0; n<varData.fuzzySets.size(); n++)
        {
            ArrayList<Point> points = GetShapePoints(varData.fuzzySets.get(n).points);
            
            Point p1 = new Point();
            Point p2 = new Point();
            for(int i=1; i<points.size(); i++)
            {
                if(varData.variableValue >= points.get(i - 1).x && varData.variableValue <= points.get(i).x)
                {
                    p1 = points.get(i-1);
                    p2 = points.get(i);
                    break;
                }
            }
            double slope = GetSlope(p1, p2);
            double b = Get_b(p1, slope);
            double membershipValue = GetMembershipResult(varData.variableValue, slope, b);
            fData.varName = varData.variableName;
            FuzzificationSet set = new FuzzificationSet();
            set.setName = varData.fuzzySets.get(n).name;
            set.MemberShipResult = membershipValue;
            allSets.add(set);
        }
        fData.resultSet = allSets;
        return fData;
    }

    static class Evaluation
    {
        String resultValue;
        double fuzzyResult;
    }
    static Evaluation EvaluationRule(Rule rule, ArrayList<FuzzificationData> fuzzification)
    {
        ArrayList<Double> memberShip = new ArrayList();
        int i = 0;
        while(i < rule.varNumPerRule)
        {
            if(rule.variableName[i].equals(fuzzification.get(i).varName))
            {
                for(int j=0; j<fuzzification.get(i).resultSet.size(); j++)
                {
                    if(rule.variableValue[i].equals(fuzzification.get(i).resultSet.get(j).setName))
                    {
                        memberShip.add(fuzzification.get(i).resultSet.get(j).MemberShipResult);
                    }
                }
            }
            i++;
        }
        Evaluation result = new Evaluation();
        result.resultValue = rule.resultValue;
    
        int count = 0;
        for(int j=0; j<memberShip.size(); j++)
        {
            if(Double.isNaN(memberShip.get(j)))
                memberShip.set(j, 0.0);
        }
            System.out.println(memberShip);
    
        if(rule.operator[0].equals("OR"))
        {
            result.fuzzyResult = GetMax(memberShip);
        }
        else if(rule.operator[0].equals("AND"))
        {
            result.fuzzyResult = GetMin(memberShip);
        }
        return result;
    }
    static double GetMax(ArrayList<Double> arr)
    {
        double max = -100000000;
        for(int i=0; i<arr.size(); i++)
        {
            if(max < arr.get(i))
                max = arr.get(i);
        }
        return max;
    }
    static double GetMin(ArrayList<Double> arr)
    {
        double min = 1000000000;
        for(int i=0; i<arr.size(); i++)
        {
            if(min > arr.get(i))
                min = arr.get(i);
        }
        return min;
    }

    static double GetAverage(ArrayList<Integer> points)
    {
        double average = 0.0;
        for(int i=0; i<points.size(); i++)
        {
            average += points.get(i);
        }
        average /= points.size();
        return average;
    }
    static double GetSum(ArrayList<Double> arr)
    {
        double sum = 0.0;
        for(int i=0; i<arr.size(); i++)
        {
            sum += arr.get(i);
        }
        return sum;
    }
    static double DeFuzzification(FuzzyResult fRes, ArrayList<Rule> rules)
    {
        ArrayList<Double> average = new ArrayList();
        ArrayList<Double> numenator = new ArrayList();
        double sumDenominator = 0.0;
        for(int i=0; i<rules.size(); i++)
        {
            double ave = 0.0;
            for(int j=0; j<fRes.resultSets.size(); j++)
            {
                if(fRes.resultSets.get(j).name.equals(rules.get(i).resultValue))
                {
                    ave = GetAverage(fRes.resultSets.get(j).points);
                    break;
                }
            }
            average.add(ave);
            double num = average.get(i) * fRes.resultMembership.get(i).fuzzyResult;
            numenator.add(num);
            sumDenominator += fRes.resultMembership.get(i).fuzzyResult;
        }
        double sumNumenator = GetSum(numenator);
        double deFuzzy = sumNumenator / sumDenominator;
        return deFuzzy;
    }
}
