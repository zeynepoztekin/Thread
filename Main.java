import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    double [][] matrix;
    int threadCount;
    double sum;
    double max;
    class MyThread extends Thread {
        int startIndex;
        int endIndex;
        public MyThread(int baslangic, int bitis) {
            startIndex = baslangic;
            endIndex = bitis;
        }
        public void run() {
            double sumOfRow =0;
            for(int i = startIndex; i < endIndex;i++) {
                for(int j = 0; j < matrix.length; j++) {
                    sumOfRow += matrix[i][j];
                    if (max < matrix[i][j]){
                        max = matrix[i][j];
                    }
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (matrix) {
                sum += sumOfRow;
            }
        }
    }

    public Main(String fileName){
        try {
            Scanner reader = new Scanner(new FileReader(fileName));

            ArrayList<String> lines = new ArrayList<>();
            while (reader.hasNextLine()){
                lines.add(reader.nextLine());
            }
            matrix = new double[lines.size()][lines.get(0).strip().split("\\s+").length];
            String[] line;
            for (int i = 0; i < lines.size(); i++) {
                line = lines.get(i).strip().split("\\s+");
                for (int j = 0; j < matrix[0].length; j++) {
                    matrix[i][j] = Double.parseDouble(line[j]);
                }
            }
            threadCount = lines.size();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void calculate() {
        sum = 0;
        MyThread [] threads = new MyThread[Main.this.threadCount];
        for(int i = 0; i < threadCount; i++) {
            threads[i] = new MyThread(i, (i+1) );
            threads[i].start();
        }
        for(int i = 0; i < threadCount; i++){
            try {
                threads[i].join();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        String sumStr = "" + sum;
        sumStr = sumStr.indexOf(".") == sumStr.length()-2 && sumStr.contains(".0") ? (int)sum + "": sumStr;
        long factor = (long) Math.pow(10, 2);
        max = max * factor;
        long tmp = Math.round(max);
        max = (double) tmp / factor;
        String maxStr = "" + max;
        maxStr = maxStr.indexOf(".") == maxStr.length()-2 && maxStr.contains(".0") ? (int)max + "": maxStr;
        System.out.println("Toplam: " + sumStr + "\nMaksimum: " + maxStr);
    }

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        Main m = new Main(keyboard.next());
        m.calculate();
    }
}
