package it.sevenbits.http.server;

import it.sevenbits.http.server.core.ServerListenerThread;

import java.io.BufferedReader;
import java.io.IOException;


/**
 * class Main
 */
final class Main {

    /**
     * main function
     *
     * @param args - console arguments
     */
    public static void main(final String[] args) {
        try {
            ServerListenerThread serverListenerThread = new ServerListenerThread(8081);
            serverListenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
