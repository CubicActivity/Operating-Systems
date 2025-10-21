import java.io.*;
import java.util.Random;

public class TemperatureSensor {
    public static void main(String[] args) {
        Random random = new Random();
        while (true) {
            try (FileWriter fw = new FileWriter("/data/temperature.txt", true)) {
                for (int i = 0; i < 5; i++) {
                    int temp = 5 + random.nextInt(46); // 5-50 range
                    fw.write(temp + " ");
                }
                fw.write("\n");
                System.out.println("Logged 5 new temperatures");
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            try {
                Thread.sleep(30000); // 30 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}