package org.example;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Multithread implements Runnable {
    private static Socket csocket;
    Multithread(Socket csocket) {
        this.csocket = csocket;
    }

    public static void main(String[] args) throws Exception {
        ServerSocket ssock = new ServerSocket(1234);
        System.out.println("Listening");
        while (true) {
            Socket sock = ssock.accept();
            System.out.println("Connected");
            new Thread(new Multithread(sock)).start();
        }
    }
    public void run() {
        try {
            PrintStream pstream = new PrintStream
                    (csocket.getOutputStream());

            pstream.close();
            csocket.close();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
}

