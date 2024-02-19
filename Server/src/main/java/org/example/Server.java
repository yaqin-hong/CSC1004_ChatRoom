package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.io.DataInputStream.readUTF;

public class Server implements Runnable {
    String username;
    Socket csocket;
    static ArrayList<String> nameList;
    static ArrayList<Socket> socketList;

    Server(Socket s) {
        this.csocket = s;
        this.username = null;
    }

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8888);
        nameList = new ArrayList<String>();
        socketList = new ArrayList<Socket>();
        System.out.println("Start Server....");

        while (true) {
            Socket s = ss.accept();
            System.out.println("Start Connecting....");
            Server ser = new Server(s);
            Thread thr = new Thread(ser);
            thr.start();
        }
    }

    public void run() {
        try {
            InputStream is = this.csocket.getInputStream();
            DataInputStream din = new DataInputStream(is);
            // InputStreamReader r = new InputStreamReader(din);
            // BufferedReader br = new BufferedReader(r);
            String message = "";
            this.onceOut("Welcome to the chatroom, please input your username: ");

            do {
                message = din.readUTF();

                if (nameList.contains(message)) {
                    this.onceOut("This username already exists, please input another one: ");
                } else {
                    this.username = message;
                    message = "Welcome " + this.username + " to the chatroom";
                    nameList.add(this.username);
                    socketList.add(this.csocket);
                    broadcastMessage(this.username, message);
                    this.onceOut(message);
                    break;
                }
            } while (this.csocket.isConnected());

            while (this.csocket.isConnected()) {
                message = din.readUTF();
                if (message != null) {
                    System.out.println(this.username + ": " + message);
                    broadcastMessage(this.username, message);
                } else {
                    this.csocket.close();
                    break;
                }
            }
            message = "Client " + this.username + " exits the chatroom.";
            broadcastMessage(this.username, message);
            int index = nameList.indexOf(this.username);
            nameList.remove(index);
            socketList.remove(index);
            this.username = null;
            Thread.sleep(1000);
            this.csocket.shutdownInput();
            this.csocket.shutdownOutput();
            din.close();
            is.close();
            this.csocket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void broadcastMessage(String username, String out) {

        for (int i = 0; i < nameList.size(); i++) {
            if (username.equals(nameList.get(i))) {

            } else {
                SimpleDateFormat sdf = new SimpleDateFormat();
                sdf.applyPattern("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String mess = sdf.format(date) + " / " + username + " : " + out;

                try {
                    OutputStream os = socketList.get(i).getOutputStream();
                    DataOutputStream dout = new DataOutputStream(os);
//                    OutputStreamWriter w = new OutputStreamWriter(dout);
//                    BufferedWriter bw = new BufferedWriter(w);
                    dout.writeUTF(mess);
                    dout.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void onceOut(String oneOut) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String messa = sdf.format(date) + " / " + oneOut;
        System.out.println("Client " + messa);
        try {
            OutputStream os = this.csocket.getOutputStream();
            DataOutputStream dout = new DataOutputStream(os);
            // OutputStreamWriter w = new OutputStreamWriter(dout);
            // BufferedWriter bw = new BufferedWriter(w);
            dout.writeUTF(messa);
            dout.flush();
            // dout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}