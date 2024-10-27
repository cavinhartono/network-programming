package KeamananSocket;

import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

class Server {
  private static int PORT = 5000;

  /*
   * Fungsi utama -> Memanggil `startServer()` dengan `PORT` untuk memulai server
   */
  public static void main(String[] args) {
    startServer(PORT);
  }

  public static String generateHash(String data) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] encodedHash = digest.digest(data.getBytes());
    return Base64.getEncoder().encodeToString(encodedHash);
  }

  /*
   * Fungsi `startServer(int port)` -> Membuka `ServerSocket` pada port ditentukan
   */
  public static void startServer(int port) {
    /*
     * `ServerSocket` -> Socket khusus yang digunakan untuk menghubungkan permintaan koneksi dari client
     */
    try (ServerSocket serverSocket = new ServerSocket(port)) {
      System.out.println("Server is listening on port " + port + "...");

      /*
       * Server berjalan dalam loop tak terbatas untuk terus-menerus menghubungkan koneksi dari client 
       * dengan menggunakan `serverSocket.accept()`
       */
      while (true) {
        Socket socket = serverSocket.accept();
        handleClient(socket); // Saat client terhubung, sistem akan memanggil fungsi handleClient() untuk menangani koneksi tersebut
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*
   * `handleClient()` -> menangani koneksi dari client yang baru terhubung
   */
  public static void handleClient(Socket socket) {
    try {
      System.out.println("Client connected from " + socket.getInetAddress()); // digunakan untuk mendapatkan alamat IP dari client yang terhubung

      /*
       * Perintah yang dikirimkan dari client dibaca menggunakan `BufferedReader` melalui `socket.getInputStream()`
       */
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      String receivedHash = in.readLine();
      System.out.println("Command dienkripsikan: " + receivedHash);

      String command = in.readLine();

      String calculatedHash = generateHash(command);
      if (receivedHash.equals(calculatedHash)) {
        executeCommand(command, socket); // kemudian dieksekusi dengan memanggil `executeCommand()`
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
    /* 
     * untuk menjalankan perintah yang dikirim oleh client
     */ 
    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(command);

    /*
     * Output dari perintah dibaca menggunakan `BufferedReader`, satu untuk `InputStream` (output normal) 
     * dan satu lagi untuk `ErrorStream` (output error)
     */
    BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
    BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // kemudian dikirim kembali ke client 

    String s;
    while ((s = stdInput.readLine()) != null) {
      out.println(s);
    }
    while ((s = stdError.readLine()) != null) {
      out.println("Error: " + s);
    }
  }
}
