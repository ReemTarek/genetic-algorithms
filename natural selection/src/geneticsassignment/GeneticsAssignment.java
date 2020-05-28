package geneticsassignment;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Reem Tarek
 */
class Chromosomes {

    int size;
    boolean feasible = true;
    int[] arr = new int[size];
    int fitness = 0;

    public Chromosomes() {
    }

    public Chromosomes(int s) {
        this.size = s;
        this.arr = new int[this.size];
    }
}

class knapsack {

    int weight;
    int val;
}

public class GeneticsAssignment {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File file1 = new File("input.txt");
        File file2 = new File("output.txt");
        if (!file1.exists()) {
            file1.createNewFile();
        }
        if (!file2.exists()) {
            file2.createNewFile();
        }

        BufferedReader br = new BufferedReader(new FileReader("input.txt"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"));

        int testCasesNum = Integer.parseInt(br.readLine());

        for (int caseNum = 1; caseNum <= testCasesNum; caseNum++) {
            int numItems = 0;
            int maxWeight = 0;
            ArrayList<knapsack> knap = new ArrayList();
            ArrayList<Chromosomes> Chromo = new ArrayList();

            String line = br.readLine();

            if (line.isEmpty()) {
                numItems = Integer.parseInt(br.readLine());
                maxWeight = Integer.parseInt(br.readLine());

                for (int i = 0; i < numItems; i++) {
                    knapsack knapObj = new knapsack();
                    String str = br.readLine();
                    String[] parts = str.split(" ");
                    knapObj.weight = Integer.parseInt(parts[0]);
                    knapObj.val = Integer.parseInt(parts[1]);
                    knap.add(knapObj);
                }
            }
            Random rand = new Random();
            int popsize = rand.nextInt(30);
            popsize += 2;

            for (int i = 0; i < popsize; i++) {
                int curw = 0;
                int curfit = 0;
                Chromosomes cobj = new Chromosomes(numItems);
                for (int j = 0; j < numItems; j++) {
                    double one = Math.random();
                    if (one >= 0.5) {
                        cobj.arr[j] = 0;
                    } else {
                        cobj.arr[j] = 1;
                    }
                    curw += knap.get(j).weight * cobj.arr[j];
                    curfit += knap.get(j).val * cobj.arr[j];

                    if (curw > maxWeight) {
                        cobj.feasible = false;
                        j -= 1;
                        if (j <= 1) {
                            j = 0;
                        }
                        curw = 0;
                        curfit = 0;
                        continue;
                    } else {
                        cobj.feasible = true;
                        cobj.fitness = curfit;
                    }
                }
                Chromo.add(cobj);
            }

            int min = maxWeight - 2;
            int optRange = rand.nextInt((maxWeight - min) - 1) + min;
            int optW = 0, optV = 0;
            while (optW < optRange) {
                ArrayList<Chromosomes> selectedChromo = new ArrayList();
                selectedChromo = Selection(Chromo);

                ArrayList<Chromosomes> crossoverChromo = new ArrayList();
                crossoverChromo = Crossover(selectedChromo, knap, maxWeight);
                CheckFeasible(crossoverChromo, knap, maxWeight);
                ArrayList<Chromosomes> replaceChromo = new ArrayList();
                replaceChromo = Replacement(crossoverChromo, Chromo, knap);
                int val = 0;
                int weight = 0;
                for (int i = 0; i < knap.size(); i++) {
                    val += replaceChromo.get(0).arr[i] * knap.get(i).val;
                    weight += replaceChromo.get(0).arr[i] * knap.get(i).weight;
                }
                optW = weight;
                optV = val;
            }
            System.out.println("Case : " + caseNum);
            System.out.println("Optimum Solution = " + optV);
            bw.write("case : " + caseNum);
            bw.newLine();
            bw.write("Optimal Solution = " + optV);
            bw.newLine();
        }
        br.close();
        bw.close();
    }

    static ArrayList<Chromosomes> Selection(ArrayList<Chromosomes> popArr) {
        ArrayList<Chromosomes> selected = new ArrayList();
        int sum = 0;
        for (int i = 0; i < popArr.size(); i++) {
            sum += popArr.get(i).fitness;
        }

        Random rand = new Random();
        boolean flag = false;
        if (sum == 0) {
            selected.add(popArr.get(0));
            selected.add(popArr.get(1));
        } else {
            for (int i = 0; i < 2; i++) {
                int r = rand.nextInt((sum - 1) + 1) + 1;
                int nestedSum = 0;
                for (int j = 0; j < popArr.size(); j++) {
                    nestedSum += popArr.get(j).fitness;
                    if (r <= nestedSum) {
                        selected.add(popArr.get(j));
                        break;
                    }
                }
                if (i == 2 && selected.size() != 2) {
                    i--;
                }
            }
        }
        return selected;
    }

    static ArrayList<Chromosomes> Crossover(ArrayList<Chromosomes> SelectedChromo, ArrayList<knapsack> knap, int maxw) 
    {
        ArrayList<Chromosomes> crossoverChromo = new ArrayList();
        float Pc = (float) 0.5;
        
        Random rand = new Random();
        int r1 = rand.nextInt(((SelectedChromo.get(0).size - 1) - 1) + 1) + 1;
        float r2 = rand.nextFloat() * 1;
        
        if (r2 <= Pc) {
            Chromosomes obj1 = new Chromosomes(SelectedChromo.get(0).size);
            Chromosomes obj2 = new Chromosomes(SelectedChromo.get(0).size);
        
            for (int i = 0; i < r1; i++) {
                obj1.arr[i] = SelectedChromo.get(0).arr[i];
                obj2.arr[i] = SelectedChromo.get(1).arr[i];
            }
            for (int i = r1; i < SelectedChromo.get(0).size; i++) {
                obj1.arr[i] = SelectedChromo.get(1).arr[i];
                obj2.arr[i] = SelectedChromo.get(0).arr[i];
            }
            
            crossoverChromo.add(obj1);
            crossoverChromo.add(obj2);
        } 
        else {
            for (int i = 0; i < SelectedChromo.size(); i++) {
                crossoverChromo.add(SelectedChromo.get(i));
            }
        }
        float Pm = (float) 0.001;
        for (int i = 0; i < crossoverChromo.size(); i++) {
            for (int j = 0; j < crossoverChromo.get(i).size; j++) {
                float r = rand.nextFloat() * 1;
                if (r <= Pm) {
                    if (crossoverChromo.get(i).arr[j] == 0) {
                        crossoverChromo.get(i).arr[j] = 1;
                    } else {
                        crossoverChromo.get(i).arr[j] = 0;
                    }
                    CheckFeasible(crossoverChromo, knap, maxw);
                    if (crossoverChromo.get(i).feasible == false) {
                        j--;
                        if (j <= 1) {
                            j = 0;
                        }
                    }
                }
            }
        }
        return crossoverChromo;
    }

    static ArrayList<Chromosomes> Replacement(ArrayList<Chromosomes> offSpring, ArrayList<Chromosomes> popArr, ArrayList<knapsack> knap) 
    {
        ArrayList<Chromosomes> replaceChromo = new ArrayList();
        for (int i = 0; i < popArr.size(); i++) {
            replaceChromo.add(popArr.get(i));
        }
        replaceChromo.add(offSpring.get(0));
        replaceChromo.add(offSpring.get(1));

        CalcFitness(replaceChromo, knap);

        SortPopulation(replaceChromo);
        replaceChromo.remove(replaceChromo.size() - 1);
        replaceChromo.remove(replaceChromo.size() - 2);
        return replaceChromo;
    }

    static void CheckFeasible(ArrayList<Chromosomes> Chromo, ArrayList<knapsack> knap, int maxw) {
        for (int i = 0; i < Chromo.size(); i++) {
            int curw = 0;
            int curfit = 0;
            for (int j = 0; j < knap.size(); j++) {

                curw += knap.get(j).weight * Chromo.get(i).arr[j];
                curfit += knap.get(j).val * Chromo.get(i).arr[j];

                if (curw > maxw) {
                    Chromo.get(i).feasible = false;
                }
            }
        }
    }

    static void CalcFitness(ArrayList<Chromosomes> popArr, ArrayList<knapsack> knap) {
        for (int i = 0; i < popArr.size(); i++) {
            int curfit = 0;
            for (int j = 0; j < popArr.get(0).size; j++) {
               // double one = Math.random();
                curfit += knap.get(j).val * popArr.get(i).arr[j];
            }
            popArr.get(i).fitness = curfit;
        }
    }

    static void SortPopulation(ArrayList<Chromosomes> popArr) {
        for (int i = 0; i < popArr.size(); i++) {
            for (int j = i + 1; j < popArr.size(); j++) {
                Chromosomes temp = new Chromosomes();
                if (popArr.get(i).fitness < popArr.get(j).fitness) {
                    temp = popArr.get(i);
                    popArr.set(i, popArr.get(j));
                    popArr.set(j, temp);
                }
            }
        }
    }
}
