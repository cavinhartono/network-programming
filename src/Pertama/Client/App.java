package Pertama.Client;

import java.io.*;
import java.net.Socket;

public class App {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 8002);

            OutputStream outputStream = socket.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);

            outputStreamWriter.write("Hello, server!");
            outputStreamWriter.flush();

            InputStream ipStream = socket.getInputStream();
            InputStreamReader ipStreamReader = new InputStreamReader(ipStream);
            char[] buffer = new char[1024];
            int bytesRead = ipStreamReader.read(buffer);

            System.out.println("Server response: " + new String(buffer, 0, bytesRead));

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
