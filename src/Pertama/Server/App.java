package Pertama.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class App {
  public static void main(String[] args) {
    try {
      ServerSocket serverSocket = new ServerSocket(8002);

      while (true) {
        Socket socket = serverSocket.accept();

        Thread thread = new ClientHandlerThread(socket);
        thread.start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

class ClientHandlerThread extends Thread {
  private Socket socket;

  ClientHandlerThread(Socket socket) {
    this.socket = socket;
  }

  public void run() {
    try {
      InputStream ipStream = socket.getInputStream();
      InputStreamReader reader = new InputStreamReader(ipStream);
      char[] buffer = new char[1024];
      int bytesRead = reader.read(buffer);

      System.out.println("Received from Client: " + new String(buffer, 0, bytesRead));

      OutputStream outputStream = socket.getOutputStream();
      OutputStreamWriter writer = new OutputStreamWriter(outputStream);
      writer.write("Hello, Client!");
      writer.flush();

      socket.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}