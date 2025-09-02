package Opgave4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

public class UDPServerNonBlock {
    public static void main(String[] args) {
        int port = 5004;

        try {
            // Opret en UDP DatagramChannel (NIO-versionen af DatagramSocket)
            DatagramChannel channel = DatagramChannel.open();

            // Bind den til port 5004 så klienter kan sende til os
            channel.bind(new InetSocketAddress(port));

            // Skift til non-blocking mode
            channel.configureBlocking(false);

            // Brug Selector til at "lytte" efter events (fx data klar til at blive læst)
            Selector selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);

            System.out.println("Non-blocking UDP server kører på port " + port);

            // Server-løkke (kører for evigt)
            while (true) {
                // Venter på at der er data klar (blocking indtil noget sker)
                selector.select();

                // Henter alle keys (events) som er klar
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}