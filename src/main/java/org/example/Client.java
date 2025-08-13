package org.example;
import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 5000)) {
            System.out.println("התחברת לשרת!");

            // שליחת הודעה לשרת
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("שלום מהלקוח!");

            // קבלת תגובה מהשרת
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String serverResponse = in.readLine();
            System.out.println("תגובה מהשרת: " + serverResponse);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

