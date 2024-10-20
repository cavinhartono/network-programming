package CommandController.UDP.Client;

import java.net.*;
import java.io.*;

class App {
  public static void main(String[] args) {
    try {
      DatagramSocket clientSocket = new DatagramSocket();
      InetAddress serverAddress = InetAddress.getByName("192.000.00.00");
      byte[] sendBuffer;
      byte[] receiveBuffer = new byte[1024];

      BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("Masukan command untuk mengakses dalam server:");
      String command = consoleInput.readLine();
      sendBuffer = command.getBytes();

      DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, 5000);
      clientSocket.send(sendPacket);
      System.out.println("Command terkirim ke server " + serverAddress);

      DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
      clientSocket.receive(receivePacket);
      String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
      System.out.println("Response dari server:");
      System.out.println(response);

      clientSocket.close();
    } catch (Exception e) {
      System.out.println("Tidak dapat terhubung ke server.");
      e.printStackTrace();
    }
  }
}
