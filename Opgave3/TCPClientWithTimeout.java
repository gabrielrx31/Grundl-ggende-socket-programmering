package Opgave3;

import java.io.*;
import java.net.*;

public class TCPClientWithTimeout {
    public static void main(String[] args) {
        //Serverens adresse og porten
        String hostname = "localhost";
        int port = 5001;

        //Opretter en TCP forbindelse til serveren på den angivne adresse og port
        try (Socket socket = new Socket(hostname, port)){
            //Sætter timeout til 5 sekunder (5000 millisekunder) (hvor længe klienten venter for at kunne forbinde)
            socket.connect(new InetSocketAddress(hostname, port), 5000);
            //Hvor længe klienten venter på at læse data fra serveren
            socket.setSoTimeout(5000);
            //Writer bruges til at sende tekst til serveren
            //socket.getOutputStream() = strømmen ud fra klient til server
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             //Læs tekst fra serveren
             //socket.getInputStream() = strømmen ind fra server til klient
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
            //Sender en besked til serveren
            writer.println("Hello Server");
            //Læser én linje svar fra serveren 
            String response = reader.readLine();
            //Skriver svaret fra serveren på skærmen
            System.out.println("Server response: " + response);
        } catch (SocketTimeoutException ex) {
            //Hvis connect() eller readLine() overskrider timeout værdien, fanges undtagelsen her
            System.out.println("Operation timed out.");
            //Fanger andre fejl her
        } catch(IOException ex) {
            //Hvis der sker en fejl, print stakken
            ex.printStackTrace();
        }
    }
}