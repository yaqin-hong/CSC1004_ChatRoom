package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class BufferedReader1 {
    public static void main(String[] args) throws Exception{
        InputStreamReader keyr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(keyr);
        System.out.println("Enter your name");
        String name = br.readLine();
        System.out.println("Welcome " + name);

        while (!name.equals("stop")) {
            System.out.println("Enter data:");
            name = br.readLine();
            System.out.println("data is:" + name);
        }
        br.close();
        keyr.close();
    }
}
