package Opgave2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {
    public static void main(String[] args) {
        //Opretter en UDP socket på port 5002
        //Socket = hvor vi modtager og sender data
        try (DatagramSocket socket = new DatagramSocket(5002)) {
            //Opretter en buffer på 512 bytes til at modtage data
            byte[] buffer = new byte[512];

            //Uendelig løkke, server kører hele tiden og venter på beskeder
            while (true) {
                //Opretter en packet hvor vi kan lægge modtagne data
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                //Venter på at modtage en packet
                socket.receive(packet);
                //Laver Bytes fra packet om til en String
                String message = new String(packet.getData(), 0, packet.getLength());
                //Printer beskeden på serverens skærm
                System.out.println("Received: " + message);

                //Laver en svarbesked
                String response = "Echi: " + message;
                //Laver svarbeskeden om til bytes så den kan sendes over net værket
                byte[] responseBytes = response.getBytes();
                //Pakker svaret ned i en packet med selve beskeden (bytes), længden, adressen på klienten og porten klienten brugte
                DatagramPacket responsePacket = new DatagramPacket(responseBytes, responseBytes.length, packet.getAddress(), packet.getPort());
                //Sender svar pakken tilbage til klienten
                socket.send(responsePacket);
            }
        } catch (IOException ex) {
            //Hvis der er en fejl, print stakken
            ex.printStackTrace();
        }
    }
}
