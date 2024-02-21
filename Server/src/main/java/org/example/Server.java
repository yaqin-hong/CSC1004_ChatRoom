package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.io.DataInputStream.readUTF;

public class Server implements Runnable {
    String username;
    Socket csocket;
    int number = 0;
    static ArrayList<String> nameList;
    static ArrayList<Socket> socketList;
    static sqlite sqlite = null;
    Server(Socket s) {
        this.csocket = s;
        this.username = null;
    }

    public static void main(String[] args) throws IOException {
        sqlite = new sqlite();
        System.out.println("Open database successfully.");
        ServerSocket ss = new ServerSocket(8888);
        nameList = new ArrayList<>();
        socketList = new ArrayList<>();
        System.out.println("Start Server....");

        while (!ss.isClosed()) {
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
            InputStreamReader r = new InputStreamReader(din);
            BufferedReader br = new BufferedReader(r);
            String message = "";
            this.onceOut("Welcome to the chatroom, please input your username: ");

            do {
                message = br.readLine();
                if (nameList.contains(message)) {
                    this.onceOut("This username already exists, please input another one: ");
                } else {
                    this.username = message;
                    message = "Welcome " + this.username + " to the chatroom";
                    nameList.add(this.username);
                    socketList.add(this.csocket);
                    broadcastMessage(this.username, number, message);
                    this.onceOut(message);
                    break;
                }
            } while (this.csocket.isConnected());

            while (this.csocket.isConnected()) {
                message = br.readLine();
                if (message != null) {
                    number += 1;
                    if (message.indexOf("searchid:") == 0) {
                        message = sqlite.searchName(message.substring(9));
                        this.onceOut(message);
                    } else if (message.indexOf("searchchat:") == 0) {
                        message = sqlite.searchKey(message.substring(11));
                        this.onceOut(message);
                    } else {
                        System.out.println(this.username + "|" + number + ": " + message);
                        broadcastMessage(this.username, number, message);
                    }
                } else {
                    break;
                }
            }

            message = "Client " + this.username + " quits the chatroom.";
            broadcastMessage(this.username, number, message);
            System.out.println(message);
            int index = nameList.indexOf(this.username);
            nameList.remove(index);
            socketList.remove(index);
            this.username = null;
            this.csocket.shutdownInput();
            this.csocket.shutdownOutput();
            din.close();
            is.close();
            this.csocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void broadcastMessage(String username, int number, String out) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        Lock lock = new ReentrantLock();
        lock.lock();
        for (int i = 0; i < nameList.size(); i++) {
            if (username.equals(nameList.get(i))) {
                sqlite.addRecord(sdf.format(date), username, out);
            } else {
                String mess = sdf.format(date) + "/ " + username + "|" + number + " : " + out;
                try {
                    OutputStream os = socketList.get(i).getOutputStream();
                    DataOutputStream dout = new DataOutputStream(os);
                    OutputStreamWriter w = new OutputStreamWriter(dout);
                    BufferedWriter bw = new BufferedWriter(w);
                    bw.write(mess + "\r\n");
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        lock.unlock();
    }

    private void onceOut(String oneOut) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String messa = sdf.format(date) + "/ " + oneOut;
        System.out.println("Client " + messa);
        try {
            OutputStream os = this.csocket.getOutputStream();
            DataOutputStream dout = new DataOutputStream(os);
            OutputStreamWriter w = new OutputStreamWriter(dout);
            BufferedWriter bw = new BufferedWriter(w);
            bw.write(messa + "\r\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}