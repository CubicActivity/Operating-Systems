package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    private static final String SERVER_ADDRESS = "194.149.135.49";
    private static final int PORT = 9753;
    private static final String INDEX = "236059";

    public static void main(String[] args) {
        while (true) {
            try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                // login with index
                sendMessage(out, "login:" + INDEX);

                String loginResponse = in.readLine();
                System.out.println("Login: " + loginResponse);

                if (!loginResponse.contains("logged in")) {
                    throw new IOException("Неуспешен Login: " + loginResponse);
                }else{
                    System.out.println("Logged in");
                }

                // index:hello
                sendMessage(out, INDEX + ":hello");

                String helloResponse = in.readLine();
                System.out.println("Hello: " + helloResponse);
                System.out.println("Hello response recieved");

                //two threads, one for receiving, one for sending messages
                new Thread(new Receiver(in)).start();
                new Thread(new Sender(out)).start();

                // keeps thread alive
                while (!socket.isClosed()) {
                    Thread.sleep(1000);
                }

            } catch (IOException | InterruptedException e) {
                System.err.println("Error: " + e.getMessage());
                try {
                    Thread.sleep(1000);  // Wait before reconnecting
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private static void sendMessage(BufferedWriter out, String message) throws IOException {
        out.write(message + "\n");
        out.flush();
    }

    static class Receiver implements Runnable {
        private final BufferedReader in;

        public Receiver(BufferedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received: " + message);
                }
            } catch (IOException e) {
                System.err.println("Receiver error: " + e.getMessage());
            }
        }
    }

    static class Sender implements Runnable {
        private final BufferedWriter out;
        private final Scanner scanner;

        public Sender(BufferedWriter out) {
            this.out = out;
            this.scanner = new Scanner(System.in);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String input = scanner.nextLine();
                    // Proveri dali tekstot ima format indeks:poraka
                    if (input.contains(":")) {
                        sendMessage(out, input);
                    } else {
                        System.out.println("Невалиден формат! Користи: Индекс:порака");
                    }
                }
            } catch (IOException e) {
                System.err.println("Грешка при праќање порака: " + e.getMessage());
            } finally {
                scanner.close();
            }
        }
    }
}