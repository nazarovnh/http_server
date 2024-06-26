package it.sevenbits.httpserver;

import it.sevenbits.httpserver.core.ServerListenerThread;

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
            ServerListenerThread serverListenerThread = new ServerListenerThread(Integer.parseInt(args[0]));
            serverListenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
