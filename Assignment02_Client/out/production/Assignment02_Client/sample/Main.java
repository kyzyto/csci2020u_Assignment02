package sample;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {
    private BorderPane layout;
    ListView<String> sharedFolderLocal = new ListView();
    ListView<String> sharedFolderServer = new ListView();
    String clientpath = "//home//adunife//Assignmen02_Client//client//";
    String serverpath = "//home//adunife//Desktop//Assignment02_Server//server";

    public Main() {
    }

    public void start(Stage primaryStage) throws IOException {
        Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("sample.fxml"));
        primaryStage.setTitle("File Sharer v1.0");
        primaryStage.setScene(new Scene(root, 500.0D, 580.0D));
        this.layout = new BorderPane();
        Scene scene = new Scene(this.layout, 500.0D, 580.0D);
        GridPane editArea = new GridPane();
        editArea.setPadding(new Insets(0.0D, 0.0D, 0.0D, 0.0D));
        editArea.setVgap(0.0D);
        editArea.setHgap(0.0D);
        Button download = new Button("Download");
        editArea.add(download, 0, 0);
        download.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                ObservableList<String> fileNamesS = FXCollections.observableArrayList();
                String selectedItem = (String)Main.this.sharedFolderServer.getSelectionModel().getSelectedItem();
                fileNamesS.add(selectedItem);
                Main.this.sharedFolderLocal.getItems().addAll(fileNamesS);

                try {
                    Socket sock = new Socket("127.0.0.1", 8080);
                    byte[] mybytearray = new byte[1024];
                    InputStream is = sock.getInputStream();
                    PrintStream out = new PrintStream(sock.getOutputStream());
                    out.println("download");
                    out.println(selectedItem);
                    FileOutputStream fos = new FileOutputStream(Main.this.clientpath + selectedItem);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int bytesRead = is.read(mybytearray, 0, mybytearray.length);
                    bos.write(mybytearray, 0, bytesRead);
                    bos.close();
                    sock.close();
                } catch (Exception var12) {
                    System.out.println("DIDNT WORK");
                }

            }
        });
        Button upload = new Button("Upload");
        editArea.add(upload, 1, 0);
        upload.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                ObservableList<String> fileNamesS = FXCollections.observableArrayList();
                String selectedItem = (String)Main.this.sharedFolderLocal.getSelectionModel().getSelectedItem();
                fileNamesS.add(selectedItem);
                Main.this.sharedFolderServer.getItems().addAll(fileNamesS);

                try {
                    Socket sock = new Socket("127.0.0.1", 8080);
                    PrintStream out = new PrintStream(sock.getOutputStream());
                    out.println("upload");
                    out.println(selectedItem);
                    ServerSocket m_ServerSocket = new ServerSocket(8080);
                    Socket clientSocket = m_ServerSocket.accept();
                    File myFile = new File(Main.this.clientpath + selectedItem);

                    while(true) {
                        byte[] mybytearray = new byte[(int)myFile.length()];
                        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
                        bis.read(mybytearray, 0, mybytearray.length);
                        OutputStream os = clientSocket.getOutputStream();
                        os.write(mybytearray, 0, mybytearray.length);
                        os.flush();
                    }
                } catch (Exception var12) {
                    System.out.println("DIDNT WORK");
                }
            }
        });
        SplitPane splitPane = new SplitPane();
        splitPane.setPrefWidth(scene.getWidth());
        splitPane.setPrefHeight(scene.getHeight());
        this.listFileslocal(this.clientpath);
        this.listFilesserver(this.serverpath);
        splitPane.getItems().addAll(new Node[]{this.sharedFolderLocal, this.sharedFolderServer});
        splitPane.setDividerPosition(2, 0.5D);
        this.layout.setTop(editArea);
        this.layout.setBottom(splitPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void listFileslocal(String directoryName) {
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        File[] var4 = fList;
        int var5 = fList.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            File file = var4[var6];
            if (file.isFile()) {
                this.sharedFolderLocal.getItems().add(file.getName());
            }
        }

    }

    public void listFilesserver(String directoryName) {
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        File[] var4 = fList;
        int var5 = fList.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            File file = var4[var6];
            if (file.isFile()) {
                this.sharedFolderServer.getItems().add(file.getName());
            }
        }

    }

    public void download(String n) throws IOException {
        Socket sock = new Socket("127.0.0.1", 8080);
        byte[] mybytearray = new byte[1024];
        InputStream is = sock.getInputStream();
        String name = this.clientpath + n;
        FileOutputStream fos = new FileOutputStream(name);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        int bytesRead = is.read(mybytearray, 0, mybytearray.length);
        bos.write(mybytearray, 0, bytesRead);
        bos.close();
        sock.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
