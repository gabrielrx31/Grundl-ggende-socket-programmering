import java.io.*;
import java.net.*;

public class TCPClient {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 5001;

        try (Socket socket = new Socket(hostname, port);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            writer.println("Hello Server");
            String response = reader.readLine();
            System.out.println("Server response: " + response);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}