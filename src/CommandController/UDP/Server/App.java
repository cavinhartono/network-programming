package CommandController.UDP.Server;

import java.net.*;
import java.io.*;

class App {
  private static int PORT = 5000;

  public static void main(String[] args) {
    try {
      try (DatagramSocket serverSocket = new DatagramSocket(PORT)) {
        byte[] receiveBuffer = new byte[1024];
        System.out.println("Server is listening on port 5000...");

        while (true) {
          DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
          serverSocket.receive(receivePacket);
          String command = new String(receivePacket.getData(), 0, receivePacket.getLength());
          InetAddress clientAddress = receivePacket.getAddress();
          int clientPort = receivePacket.getPort();
          System.out.println("Command received from client at " + clientAddress + ": " + command);

          Runtime runtime = Runtime.getRuntime();
          Process process = runtime.exec(command);

          BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
          BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

          StringBuilder outputBuilder = new StringBuilder();
          String s;
          while ((s = stdInput.readLine()) != null) {
            outputBuilder.append(s).append("\n");
          }
          while ((s = stdError.readLine()) != null) {
            outputBuilder.append("Error: ").append(s).append("\n");
          }

          byte[] sendBuffer = outputBuilder.toString().getBytes();
          DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
          serverSocket.send(sendPacket);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
