package it.sevenbits.http.server;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.in;
import static java.lang.System.out;

public class GreetServer {

    private String method = null;
    private String uri = null;
    private Map<String, String> query = new HashMap();

    public void start(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        // on reste bloqué sur l'attente d'une demande client
        Socket clientSocket = serverSocket.accept();
        System.err.println("Nouveau client connecté");

        // on ouvre un flux de converation

        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        // chaque fois qu'une donnée est lue sur le réseau on la renvoi sur
        // le flux d'écriture.
        // la donnée lue est donc retournée exactement au même client.
        String s;
        parsingStartingLine(s = in.readLine());
        while ((s = in.readLine()) != null) {
            System.out.println(s);
            if (s.isEmpty()) {
                break;
            }
        }

        out.write("HTTP/1.0 200 OK\r\n");
        out.write("Date: Fri, 31 Dec 1999 23:59:59 GMT\r\n");
        out.write("Server: Apache/0.8.4\r\n");
        out.write("Content-Type: text/html\r\n");
        out.write("Content-Length: 59\r\n");
        out.write("Expires: Sat, 01 Jan 2000 00:59:59 GMT\r\n");
        out.write("Last-modified: Fri, 09 Aug 1996 14:21:40 GMT\r\n");
        out.write("\r\n");
        out.write("<P>HELLO "+ query.get("name") +"</P>");

        // on ferme les flux.
        System.err.println("Connexion avec le client terminée");
        out.close();
        in.close();
        clientSocket.close();
    }

    private void parsingStartingLine(String startingLine) {
        List<String> line = Arrays.asList(startingLine.split(" "));
        method = line.get(0);
        uri = line.get(1);
        String[] queries = uri.split("\\?")[1].split("=");
        // Add expection
        query.put(queries[0], queries[1]);
    }

    private void sendHeader(PrintWriter output, int statusCode, String statusText, String type, long lenght) {
        output.write("HTTP/1.0 200 OK\r\n");
//        output.write("Date: Fri, 31 Dec 1999 23:59:59 GMT\r\n");
//        output.write("Server: Apache/0.8.4\r\n");
//        output.write("Content-Type: text/html\r\n");
//        output.write("Content-Length: 59\r\n");
//        output.write("Expires: Sat, 01 Jan 2000 00:59:59 GMT\r\n");
//        output.write("Last-modified: Fri, 09 Aug 1996 14:21:40 GMT\r\n");
//        output.write("\r\n");
//        output.write("<TITLE>Exemple</TITLE>");
//        output.write("<P>Ceci est une page d'exemple.</P>");
    }

//    public void stop() {
//        in.close();
//        out.close();
//        clientSocket.close();
//        serverSocket.close();
//    }
}
