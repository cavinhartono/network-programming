package Kelompok.Client;

import java.io.*;
import java.net.*;

class App {
  public static void main(String[] args) {
    try {
      Socket socket = new Socket("IP_SERVER", 5000);

      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

      System.out.println("Masukan command untuk eksekusi di remote laptop B:");
      String command = consoleInput.readLine();
      out.println(command);

      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      String response;
      while ((response = in.readLine()) != null) {
        System.out.println(response);
      }

      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
