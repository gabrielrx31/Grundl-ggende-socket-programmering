package Opgave4;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import java.util.Iterator;

public class UDPClientNonBlock {
    public static void main(String[] args) {
        //Adresse og port på serveren vi vil connecte til
        String hostname = "localhost";
        int port = 5004;

        try{
            //Opret en UDP datagram channel 
            DatagramChannel channel = DatagramChannel.open();
            //Bind den til port 5004 så klienten kan send til os
            channel.bind(new InetSocketAddress(5004));
            //Skift til non-blocking mode
            channel.configureBlocking(false);

            //Brug en selector til at lytte til events (fx data der er klar til at blive læst)
            Selector selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);
            
            //Uendelig løkke
            while (true) {
                //Venter indtil der er data klar til at blive læst (blocking indtil der sker noget)
                selector.select();
                //Henter alle keys (events) der er klar
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove(); // fjerner den, så vi ikke behandler den igen

                    if (key.isReadable()) {
                        // Vi har modtaget en besked
                        DatagramChannel dc = (DatagramChannel) key.channel();

                        ByteBuffer buffer = ByteBuffer.allocate(512);

                        // receive() er non-blocking her
                        SocketAddress clientAddr = dc.receive(buffer);

                        if (clientAddr != null) {
                            buffer.flip(); // gør buffer klar til læsning
                            byte[] data = new byte[buffer.remaining()];
                            buffer.get(data);

                            String message = new String(data);
                            System.out.println("Received: " + message);

                            // Lav svar (ligesom i din blocking-version)
                            String response = "Echo: " + message;
                            ByteBuffer responseBuffer = ByteBuffer.wrap(response.getBytes());

                            // send() sender svaret tilbage til klienten
                            dc.send(responseBuffer, clientAddr);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            //Hvis der sker en fejl print stakken
            ex.printStackTrace();
        }
    }
}