package org.example;
import java.io.*;
import java.net.Socket;
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
            Thread readerThread = new Thread(() -> {
                try {
                    String message;
                    while (s.isConnected()) {
                        message = din.readUTF();
                        if (message != null) {
                            System.out.println(message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            readerThread.start();

            BufferedReader kr = new BufferedReader(new InputStreamReader(System.in));
            String key = kr.readLine();
            while (!key.equals("stop")) {
                if (key != null) {
                    dout.writeUTF(key +" \r\n");
                    dout.flush();
                }
                key = kr.readLine();
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