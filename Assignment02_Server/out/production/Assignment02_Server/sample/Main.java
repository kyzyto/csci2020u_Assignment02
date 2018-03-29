package sample;

import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public Main() {
    }

    public static void main(String[] args) throws Exception {
        ServerSocket m_ServerSocket = new ServerSocket(8080);
        int var2 = 0;

        while(true) {
            Socket clientSocket = m_ServerSocket.accept();
            ClientServiceThread cliThread = new ClientServiceThread(clientSocket, var2++);
            cliThread.start();
        }
    }
}
