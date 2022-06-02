package it.sevenbits.http.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * main.java.it.sevenbits.practice.Main application entry point
 */
final  class Main {

    /**
     * main.java.it.sevenbits.practice.Main function for app
     *
     * @param args - console arguments
     */
    public static void main(final String[] args) throws IOException {
        GreetServer server=new GreetServer();
        while(true) {
            server.start(8081);
        }
    }
}
