package genetic.assginment.pkg2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Math.pow;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author Om-mostafa El-Hariry
 */
public class GeneticAssginment2 {

    /**
     * @param args the command line arguments
     */
   static class MultipleDatasetDemo1 extends ApplicationFrame
    {
       private XYPlot plot;
   
    /** The index of the last dataset added. */
    private int datasetIndex = 0;
    
    /**
     * Constructs a new demonstration application.
     *
     * @param title  the frame title.
     */
    public MultipleDatasetDemo1(final String title) throws IOException {

        super(title);
        final XYSeriesCollection dataset1 = createdataset1("input");
           final JFreeChart chart = ChartFactory.createXYLineChart(
            "Curve fitting compare",
            "X", 
            "Y", 
            dataset1,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        chart.setBackgroundPaint(Color.white);
        
        this.plot = chart.getXYPlot();
        this.plot.setBackgroundPaint(Color.lightGray);
        this.plot.setDomainGridlinePaint(Color.white);
        this.plot.setRangeGridlinePaint(Color.white);
//        this.plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 4, 4, 4, 4));
        this.datasetIndex++;
                this.plot.setDataset(
                    this.datasetIndex, createdataset2("output")
                );
                this.plot.setRenderer(this.datasetIndex, new StandardXYItemRenderer());
        final ValueAxis axis = this.plot.getDomainAxis();
        axis.setAutoRange(true);

        final NumberAxis rangeAxis2 = new NumberAxis("Range Axis 2");
        rangeAxis2.setAutoRangeIncludesZero(false);
        
        final JPanel content = new JPanel(new BorderLayout());

        final ChartPanel chartPanel = new ChartPanel(chart);
        content.add(chartPanel);
        
        
       
        chartPanel.setPreferredSize(new java.awt.Dimension(700, 700));
        setContentPane(content);

    }

    /**
     * Creates a random dataset.
     * 
     * @param name  the series name.
     * 
     * @return The dataset.
     */
    private XYSeriesCollection createdataset1(final String name) throws FileNotFoundException, IOException {
        final XYSeries series = new XYSeries(name);
          File file1 = new File("input.txt");
     double []pointx = new double[70];
    double []pointy = new double[70];
     BufferedReader br;
     
          br = new BufferedReader(new FileReader("input-1.txt"));
          int i=0;
          String line = "";
          int ten=5;
          while((line=br.readLine() )!= null)
          {   String[] dline = line.split(" ");
              pointx[i]=Double.valueOf(dline[0]);
              pointy[i] = Double.parseDouble(dline[1]);
             // System.out.println(pointy[0]);
              
             series.add(pointx[i],pointy[i]);
             i++;
    //                      System.out.println(series.getDataItem(0));
          }
          br.close();
        return new XYSeriesCollection(series);
    }
    
  
  private XYSeriesCollection createdataset2(final String name) throws FileNotFoundException, IOException {
        final XYSeries series = new XYSeries(name);
          File file1 = new File("output-1.txt");
     double []pointx = new double[70];
    double []pointy = new double[70];
     BufferedReader br;
     
          br = new BufferedReader(new FileReader("output-1.txt"));
          int i=0;
          String line = "";
          int ten=5;
          while((line=br.readLine() )!= null)
          {   String[] dline = line.split(" ");
              pointx[i]=Double.valueOf(dline[0]);
              pointy[i] = Double.parseDouble(dline[1])+10;
             // System.out.println(pointy[0]);
              
             series.add(pointx[i],pointy[i]);
             i++;
                         // System.out.println(series.getDataItem(0));
          }
          br.close();
        return new XYSeriesCollection(series);
     
    }
    }
    static class Coefficient {

        int degree;
        double fitness;
        double[] arr;

        Coefficient(int dgr) {
            degree = dgr;
            arr = new double[degree + 1];
        }

        private Coefficient() {
        }
    }

    static class Point {

        double xPoint;
        double yPoint;

        Point() {
        }
    }

    public static void main(String[] args) throws IOException {
           File file1 = new File("input.txt");
        File file2 = new File("output.txt");
        File file3 = new File("input-1.txt");
        File file4 = new File("output-1.txt");
        if (!file1.exists()) {
            file1.createNewFile();
        }
        if (!file2.exists()) {
            file2.createNewFile();
        }

        BufferedReader br = new BufferedReader(new FileReader("input.txt"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"));
        int testCasesNum = Integer.parseInt(br.readLine());
        for (int caseNum = 1; caseNum <= testCasesNum; caseNum++) 
        {  
           
            String line = br.readLine();
            if(line == null)break;
            String []split = line.split(" ");
            int degree = Integer.parseInt(split[1]);
            int numPoints = Integer.parseInt(split[0]);
            ArrayList<Point> points = new ArrayList();
            Coefficient coff = new Coefficient(degree);
            int i=0;
            BufferedWriter bw1 = new BufferedWriter(new FileWriter("input-1.txt"));

            while(i<numPoints)
            {
                line = br.readLine();
                String []spoint = line.split(" ");
                Point point = new Point();
                point.xPoint = Double.valueOf(spoint[0]);
                point.yPoint = Double.valueOf(spoint[1]);
                bw1.write(point.xPoint+" "+point.yPoint);
                bw1.newLine();
                points.add(point);
                i++;
            }
        
            ArrayList<Coefficient> coefficient = GeneratePop(degree, points);
            CalcFitness(coefficient, points);
        int t = 0;
        int T = 100;
        int lower = -10;
        int upper = 10;
        double newFitness = 0;
        ArrayList<Point> parr = new ArrayList();
        double[] newArr = new double[degree + 1];
        while (t <= T) 
        {
            ArrayList<Coefficient> selectedCoff = new ArrayList();
            selectedCoff = Selection(coefficient);
            
            ArrayList<Coefficient> crossoverCoff = new ArrayList();
            crossoverCoff = Crossover(selectedCoff, degree, points);
            
            Mutation(crossoverCoff, points, t, T, lower, upper);
            
            ArrayList<Coefficient> replacedCoff = new ArrayList();
            replacedCoff = Replacement(crossoverCoff, coefficient, points);
            
            
            parr = CalculatePolynomial(replacedCoff,points);
            newFitness = replacedCoff.get(0).fitness;
            newArr = replacedCoff.get(0).arr;
            t++;
            BufferedWriter bw2 = new BufferedWriter(new FileWriter("output-1.txt"));
       for(int j=0;j<parr.size();j++)
       {//System.out.println("p obj "+parr.get(j).xPoint);
           bw2.write(parr.get(j).xPoint+" "+parr.get(j).yPoint);
           bw2.newLine();
       }
       bw2.close();
        }
        System.out.println("Fitness ---> " + newFitness);
       
       bw.write("case# " + caseNum);
       bw.newLine();
       for(int k=0; k<degree+1; k++)
       {
           bw.write(newArr[k] + "  ");
       }
       bw.newLine();
       bw1.close();
        final MultipleDatasetDemo1 demo = new MultipleDatasetDemo1("Case"+caseNum);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
              

        }
        br.close();
        bw.close();
  
    }

    static public ArrayList<Coefficient> GeneratePop(int degree, ArrayList<Point> points) {
        Random rand = new Random();
        int numPop = rand.nextInt((31 - 15) + 1) + 15;
        System.out.println("Pop Size" + numPop);
        ArrayList<Coefficient> coefficient = new ArrayList();
        for (int i = 0; i < numPop; i++) {
            Coefficient objCoff = new Coefficient(degree);
            for (int j = 0; j <= degree; j++) {
                objCoff.arr[j] = (-10) + Math.random() * (10 - (-10));
            }
            coefficient.add(objCoff);
        }
        return coefficient;
    }
static public ArrayList<Point> CalculatePolynomial(ArrayList<Coefficient> coefficient,ArrayList<Point> points)
{ArrayList<Point> newpoint = new ArrayList();
     
        
            for (int j = 0; j < points.size(); j++) {
                 Point pobj = new Point();
                int power = 0;
                double yCalc = 0;
                double sumcof=0;
                while (power <= coefficient.get(0).degree) {
                    sumcof += coefficient.get(0).arr[power] * (Math.pow(points.get(j).xPoint, power));

                    power++;
                }
                yCalc = sumcof;
                pobj.xPoint = points.get(j).xPoint;
                pobj.yPoint = yCalc;
                //System.out.println("p obj "+pobj.xPoint);
            newpoint.add(pobj);    
            }
           
           
        
     return newpoint;
}
    static public void CalcFitness(ArrayList<Coefficient> coefficient, ArrayList<Point> points) {
        for (int i = 0; i < coefficient.size(); i++) {
            double MeanDiff = 0;
            for (int j = 0; j < points.size(); j++) {
                int power = 0;
                double yCalc = 0;
                double sumcof=0;
                while (power <= coefficient.get(0).degree) {
                    sumcof += coefficient.get(i).arr[power] * (Math.pow(points.get(j).xPoint, power));

                    power++;
                }
                yCalc = sumcof;
                MeanDiff += Math.pow((yCalc - (double) points.get(j).yPoint), 2);
            }
            coefficient.get(i).fitness = MeanDiff / points.size();
        }
    }

    static ArrayList<Coefficient> Selection(ArrayList<Coefficient> coefficient) {
        ArrayList<Coefficient> selectedCoff = new ArrayList();
        int sum = 0;
        for (int i = 0; i < coefficient.size(); i++) {
            sum += coefficient.get(i).fitness;
        }

        Random rand = new Random();
        for (int i = 0; i < 2; i++) {
            int r = rand.nextInt((sum - 1) + 1) + 1;
            int nestedSum = 0;
            for (int j = 0; j < coefficient.size(); j++) {
                nestedSum += coefficient.get(j).fitness;
                if (r <= nestedSum) {
                    selectedCoff.add(coefficient.get(j));
                    break;
                }
            }
            if (i == 2 && selectedCoff.size() != 2) {
                i--;
            }
        }
        return selectedCoff;
    }

    static ArrayList<Coefficient> Crossover(ArrayList<Coefficient> SelectedCoff, int degree, ArrayList<Point> points) {
        ArrayList<Coefficient> crossoverCoff = new ArrayList();
        float Pc = (float) 0.5;

        Random rand = new Random();
        int r1 = rand.nextInt((degree - 1) + 1) + 1;
        float r2 = rand.nextFloat() * 1;

        if (r2 <= Pc) {
            Coefficient obj1 = new Coefficient(degree);
            Coefficient obj2 = new Coefficient(degree);

            for (int i = 0; i < r1; i++) {
                obj1.arr[i] = SelectedCoff.get(0).arr[i];
                obj2.arr[i] = SelectedCoff.get(1).arr[i];
            }
            for (int i = r1; i < degree; i++) {
                obj1.arr[i] = SelectedCoff.get(1).arr[i];
                obj2.arr[i] = SelectedCoff.get(0).arr[i];
            }
            crossoverCoff.add(obj1);
            crossoverCoff.add(obj2);
        } else {
            for (int i = 0; i < SelectedCoff.size(); i++) {
                crossoverCoff.add(SelectedCoff.get(i));
            }
        }
        CalcFitness(crossoverCoff, points);
        return crossoverCoff;
    }

    static public void Mutation(ArrayList<Coefficient> crossoverCoff, ArrayList<Point> points, int t, int T, int lower, int upper) {
        //ArrayList<Coefficient> mutatedCoff = new ArrayList();
        for (int i = 0; i < crossoverCoff.size(); i++) {
            for (int j = 0; j < crossoverCoff.get(i).arr.length; j++) {
                double dlower = crossoverCoff.get(i).arr[j] - lower;
                double dupper = upper - crossoverCoff.get(i).arr[j];
                double y = 0.0;
                int b = 1;
                Random rand = new Random();
                float r1 = rand.nextFloat() * 1;
                if (r1 <= 0.5) {
                    y = dlower;
                } else {
                    y = dupper;
                }
                float r = rand.nextFloat() * 1;

                double delta = y * (1 - pow(r, pow(1 - (t / T), b)));
                if (y == dlower) {

                    crossoverCoff.get(i).arr[j] -= delta;
                } else {
                    crossoverCoff.get(i).arr[j] += delta;
                }
            }
        }
        CalcFitness(crossoverCoff, points);

    }

    static ArrayList<Coefficient> Replacement(ArrayList<Coefficient> offSpring, ArrayList<Coefficient> coefficient, ArrayList<Point> points) {
        ArrayList<Coefficient> replaceCoff = new ArrayList();
        for (int i = 0; i < coefficient.size(); i++) {
            replaceCoff.add(coefficient.get(i));
        }
        replaceCoff.add(offSpring.get(0));
        replaceCoff.add(offSpring.get(1));

        CalcFitness(replaceCoff, points);
        SortCoff(replaceCoff);
        replaceCoff.remove(replaceCoff.size() - 1);
        replaceCoff.remove(replaceCoff.size() - 2);
        return replaceCoff;
    }

    static void SortCoff(ArrayList<Coefficient> coefficient) {
        for (int i = 0; i < coefficient.size(); i++) {
            for (int j = i + 1; j < coefficient.size(); j++) {
                if (coefficient.get(i).fitness > coefficient.get(j).fitness) {
                
                    Coefficient temp = new Coefficient();
                    temp = coefficient.get(i);
                    coefficient.set(i, coefficient.get(j));
                    coefficient.set(j, temp);
                }
            }
        }
  }
}
