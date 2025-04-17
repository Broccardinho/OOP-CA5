package org.example.main;

import org.example.server.F1Server;

public class ServerLauncher {
    public static void main(String[] args) {
        F1Server server = new F1Server();
        server.start();
    }
}