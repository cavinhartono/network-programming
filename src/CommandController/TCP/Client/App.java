package CommandController.TCP.Client;

import java.io.*;
import java.net.*;

class App {
  private static int PORT = 5000;
  private static String IP_ADDRESS = "192.000.00.00";

  public static void main(String[] args) {
    try {
      Socket socket = new Socket(IP_ADDRESS, PORT);

      if (socket.isConnected()) {
        System.out.println("Successfully connected to server at " + socket.getInetAddress());
      }

      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

      System.out.println("Enter command to execute on the remote machine:");
      String command = consoleInput.readLine();
      out.println(command);

      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      String response;
      while ((response = in.readLine()) != null) {
        System.out.println(response);
      }

      socket.close();
    } catch (IOException e) {
      System.out.println("Could not connect to the server.");
      e.printStackTrace();
    }
  }
}
