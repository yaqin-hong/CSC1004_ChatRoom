package org.example;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Socket s = new Socket("127.0.0.1",8888);
            System.out.println("Starting client...");
            InputStream is = s.getInputStream();
            DataInputStream din = new DataInputStream(is);
            OutputStream os = s.getOutputStream();
            DataOutputStream dout = new DataOutputStream(os);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(dout));

            // turn internet input into a thread
            Thread readerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(din));
                        String message;
                        while (s.isConnected()) {
                            message = br.readLine();
                            if (message != null) {
                                System.out.println(message);
                            }
                        }
                    } catch (IOException e) {
                        // e.printStackTrace();
                    }
                }
            });
            readerThread.start();

            // let keyboard input as usual
            BufferedReader kr = new BufferedReader(new InputStreamReader(System.in, "GBK"));
            Scanner sc = new Scanner(System.in);
            String key = "";
            key = sc.nextLine();
            while (!key.equals("stop")) {
                if (key != null) {
                    bw.write(key + "\r\n");
                    bw.flush();
                }
                key = sc.nextLine();
            }
            readerThread.interrupt();
            kr.close();
            dout.close();
            os.close();
            din.close();
            is.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}