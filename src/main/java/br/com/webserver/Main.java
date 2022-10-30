package main.java.br.com.webserver;

import main.java.br.com.webserver.service.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("[WebServer] - Iniciando Servidor HttpRequest");
        int port = 5542;

        try{
            ServerSocket serverSocket = new ServerSocket(port);
            while(true){
                Socket socket = serverSocket.accept();
                new Thread(new HttpRequest(socket)).start();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}