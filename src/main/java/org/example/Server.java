package org.example;
import java.io.*;
import java.net.*;

//5.29.18.80
public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("השרת מוכן ומאזין לפורט 5000...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("לקוח התחבר: " + clientSocket.getInetAddress());

                // קבלת הודעה מהלקוח
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String clientMessage = in.readLine();
                System.out.println("הודעה מהלקוח: " + clientMessage);

                // שליחת תגובה ללקוח
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println("התקבל: " + clientMessage);

                // סגירת החיבור ללקוח
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
