package sample;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public Client() {
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        Socket s = new Socket("127.0.0.1", 1342);
        Scanner sc1 = new Scanner(s.getInputStream());
        System.out.println("Enter any number");
        int number = sc.nextInt();
        PrintStream p = new PrintStream(s.getOutputStream());
        p.println(number);
        int temp = sc1.nextInt();
        System.out.println(temp);
    }
}