package KeamananSocket;

import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

class Server {
  private static int PORT = 5000;

  public static void main(String[] args) {
    startServer(PORT);
  }

  public static String generateHash(String data) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] encodedHash = digest.digest(data.getBytes());
    return Base64.getEncoder().encodeToString(encodedHash);
  }

  public static void startServer(int port) {
    try (ServerSocket serverSocket = new ServerSocket(port)) {
      System.out.println("Server is listening on port " + port + "...");

      while (true) {
        Socket socket = serverSocket.accept();
        handleClient(socket);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void handleClient(Socket socket) {
    try {
      System.out.println("Client connected from " + socket.getInetAddress());

      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      String receivedHash = in.readLine();
      System.out.println("Command dienkripsikan: " + receivedHash);

      String command = in.readLine();

      String calculatedHash = generateHash(command);
      if (receivedHash.equals(calculatedHash)) {
        executeCommand(command, socket);
      } else {
        System.out.println("Hash did not match. Possible data corruption.");
      }

      socket.close();
      System.out.println("Client disconnected");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void executeCommand(String command, Socket socket) throws IOException {
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
  }
}
