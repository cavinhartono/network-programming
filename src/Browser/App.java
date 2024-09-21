package Browser;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class App {
  public static void main(String[] args) {
    try {
      HttpServer server = HttpServer.create(new InetSocketAddress(8002), 0);
      server.createContext("/", new MyHandle());
      server.setExecutor(null);
      server.start();
      System.out.println("Server on running... (http://localhost:8002)");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

class MyHandle implements HttpHandler {
  @Override
  public void handle(HttpExchange t) {
    try {
      String HtmlTemplate = new String(Files.readAllBytes(Paths.get("src\\Browser\\index.html")));

      String name = "Cavin Hartono";
      String response = HtmlTemplate.replace("{{ name }}", name)
          .replace("{{ isTest }}", "Success");

      t.sendResponseHeaders(200, response.length());

      OutputStream out = t.getResponseBody();
      out.write(response.getBytes());
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}