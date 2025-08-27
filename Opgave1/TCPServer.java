import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5001)) {
            System.out.println("Server is listening on port 5001");
            Socket socket = serverSocket.accept();
            System.out.println("New client connected");

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            String message = reader.readLine();
            System.out.println("Received: " + message);
            writer.println("Echo: " + message);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}