package sample;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientConnectionHandler {
    public static String DIR = "Server";
    private Socket socket = null;
    private BufferedReader bufferedReader = null;
    private DataOutputStream dataOutputStream = null;

    public ClientConnectionHandler(Socket socket) {
        this.socket = socket;

        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException var3) {
            System.err.println("Server Error While Processing New Socket\r\n");
            var3.printStackTrace();
        }

    }
}
