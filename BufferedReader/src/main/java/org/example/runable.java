package org.example;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

class runnable implements Runnable {
    private Thread t;
    private Socket s;

    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String message;
            while (s.isConnected() && (message = br.readLine()) != null) {
                System.out.println(message);
            }
        } catch (IOException e) {
            System.out.println("Error reading from Server: " + e.getMessage());
        }
    }

    public void start(Socket s) {
        this.s = s;
        if (t == null) {
            t = new Thread (this, "");
            t.start();
        }
    }
}
    
public class runable {
    public static void main(String[] args) throws IOException {
        try {
            Socket s = new Socket("127.0.0.1",8888);
            runnable R1 = new runnable();
            R1.start(s);

            InputStreamReader r = new InputStreamReader(System.in);
            BufferedReader kr = new BufferedReader(r);
            BufferedWriter bw = new BufferedWriter((new OutputStreamWriter(s.getOutputStream())));
            String key = "";
            while(!key.equals("stop")) {
                key = kr.readLine();
                bw.write(key + "\r\n");
                bw.flush();
                System.out.println("Enter: ");
            }
        }catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
