package sample;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

class ClientServiceThread extends Thread {
    Socket clientSocket;
    int clientID = -1;
    boolean running = true;

    ClientServiceThread(Socket s, int i) {
        this.clientSocket = s;
        this.clientID = i;
    }

    public void run() {
        System.out.println("Accepted Client : ID - " + this.clientID + " : Address - " + this.clientSocket.getInetAddress().getHostName());
        String serverpath = "//home//adunife//Desktop//Assignment02_Server//server";

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()));

            while(this.running) {
                String clientCommand = in.readLine();
                System.out.println("Client Says :" + clientCommand);
                if (clientCommand.equalsIgnoreCase("download")) {
                    String filename = in.readLine();
                    File myFile = new File(serverpath + filename);
                    byte[] mybytearray = new byte[(int)myFile.length()];
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
                    bis.read(mybytearray, 0, mybytearray.length);
                    OutputStream os = this.clientSocket.getOutputStream();
                    os.write(mybytearray, 0, mybytearray.length);
                    os.flush();
                }

                if (clientCommand.equalsIgnoreCase("upload")) {
                    byte[] mybytearray = new byte[1024];
                    InputStream is = this.clientSocket.getInputStream();
                    String name = in.readLine();
                    FileOutputStream fos = new FileOutputStream(serverpath + name);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int bytesRead = is.read(mybytearray, 0, mybytearray.length);
                    bos.write(mybytearray, 0, bytesRead);
                    bos.close();
                    this.clientSocket.close();
                } else {
                    System.out.println("didnt work");
                    out.flush();
                    this.clientSocket.close();
                }
            }
        } catch (Exception var11) {
            var11.printStackTrace();
        }

    }
}