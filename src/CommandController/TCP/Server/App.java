package CommandController.TCP.Server;

import java.io.*;
import java.net.*;

class App {
  private static int PORT = 5000;

  public static void main(String[] args) {
    try {
      try (ServerSocket serverSocket = new ServerSocket(PORT)) {
        System.out.println("Server is listening on port 5000...");

        while (true) {
          Socket socket = serverSocket.accept();
          System.out.println("Client connected from " + socket.getInetAddress());

          BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          String command = in.readLine();
          System.out.println("Command received: " + command);

          Runtime runtime = Runtime.getRuntime();
          Process process = runtime.exec(command);

          BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
          BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

          PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
          String s;
          while ((s = stdInput.readLine()) != null) {
            out.println(s);
          }
          while ((s = stdError.readLine()) != null) {
            out.println("Error: " + s);
          }

          socket.close();
          System.out.println("Client disconnected");
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
