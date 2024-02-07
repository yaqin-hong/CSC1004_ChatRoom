package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {
    private static Socket one_s;
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(8888);
            System.out.println("start serverer....");
            this.one_s = ss.accept();
            this.outputString("welcome please your id:\n");
            System.out.println("kehu:"+one_s.getInetAddress().getHostAddress()+"connected");

            BufferedReader br = new BufferedReader(new InputStreamReader(one_s.getInputStream()));
            String id = br.readLine();
            this.outputString("welcome:" + id + "\n");
            while (this.one_s.isConnected()) {
                String mess = br.readLine();
                SimpleDateFormat sdf = new SimpleDateFormat();
                sdf.applyPattern("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                mess = sdf.format(date) + ":" + mess;
                System.out.println("kehu:" + mess);
                this.outputString(mess + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void outputString(String outstr) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(one_s.getOutputStream()));
            bw.write(outstr + "\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
