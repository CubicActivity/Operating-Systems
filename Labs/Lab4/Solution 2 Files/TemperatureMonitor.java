import java.io.*;
import java.util.Scanner;

public class TemperatureMonitor {
    public static void main(String[] args) {
        while (true) {
            try {
                File file = new File("/data/temperature.txt");
                if (file.exists()) {
                    Scanner scanner = new Scanner(file);
                    int sum = 0;
                    int count = 0;
                    
                    while (scanner.hasNextInt()) {
                        sum += scanner.nextInt();
                        count++;
                    }
                    scanner.close();
                    
                    if (count > 0) {
                        double average = (double) sum / count;
                        String level = "High";
                        if (average < 19) level = "Low";
                        else if (average < 35) level = "Medium";
                        
                        try (FileWriter fw = new FileWriter("/data/temperaturelevel.txt")) {
                            fw.write(level + "\n");
                            System.out.println("Current level: " + level);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            try {
                Thread.sleep(60000); // 60 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}