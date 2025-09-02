package Opgave2;

import java.io.IOException;
import java.net.*;

public class UDPClient {
    public static void main(String[] args) {
        //Adresse og port på serveren vi vil connecte til
        String hostname = "localhost";
        int port = 5002;

        //Opretter en UDP socket, systemet vælger selv en ledig port
        try (DatagramSocket socket = new DatagramSocket()) {
            //Laver en byte array med beskeden vi vil sende
            //skal konverteres til bytes fordi UDP sender rå data
            byte[] buffer = "Hello Server".getBytes();
            //Finder IP-adressen til "localhost" så vi ved hvor beskeden skal sendes hen
            InetAddress address = InetAddress.getByName(hostname);
            //Opretter en packet med beskeden (bytes), længden, adressen og porten på serveren
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
            //Sender pakken til serveren
            socket.send(packet);

            //Forbereder en packet til at modtage svar fra serveren
            byte[] responseBuffer = new byte[512];
            //Laver en tomme packet hvor svaret fra serveren kan lægges
            DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
            //Venter indtil der kommer et svar fra serveren
            //Når det sker gemmes svaret i responsepacket
            socket.receive(responsePacket);
            //Konverter de bytes vi fik fra serveren til en String
            String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
            //Udskriver svaret på skærmen
            System.out.println("Server response: " + response);
        } catch (IOException ex) {
            //Hvis der sker en fejl print stakken
            ex.printStackTrace();
        }
    }
}
