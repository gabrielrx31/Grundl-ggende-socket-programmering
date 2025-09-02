package Opgave3;

import java.io.*;
import java.net.*;

public class TCPServerWithTimeout {
    public static void main(String[] args) {
        //Opretter en server socket der lytter på port 5001
        try (ServerSocket serverSocket = new ServerSocket(5001)) {
            //Sætter en timeout på 5 sekunder (5000 millisekunder) for hvor længe serveren venter på at acceptere en forbindelse
            serverSocket.setSoTimeout (5000);
            System.out.println("Server is listening on port 5001");

            //Venter på en klient. Hvis ingen klient forbinder inden 5sek -> SocketTimeoutException
            Socket socket = serverSocket.accept();
            System.out.println("New client connected");

            //Opret BufferedReader til at læse fra klienten
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //Opret printWriter til at sende tekstlinjer tilbage til klienten
            // true = autoflush (tøm bufferet hver gang der skrives en linje)
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            //Læs én linje tekst fra klienten
            String message = reader.readLine();

            //Samme tekst sendes tilbage til klienten med "Echo: " foran
            System.out.println("Received: " + message);
            writer.println("Echo: " + message);

            //Når try-blokken afsluttes, lukkes både socket og streams automatisk
        } catch (SocketTimeoutException ex) {
            //Hvis accept() overskrider timeout værdien, fanges undtagelsen her
            System.out.println("No client connected within the timeout period.");
            //Fanger andre fejl her
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}