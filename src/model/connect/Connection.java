package model.connect;

import model.util.Constants;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Alex on 21.04.2017.
 */
public class Connection {

    Socket socket;
    InetAddress address;
    int port;
    PrintWriter out;
    BufferedReader in;
    private static Connection instance;

    public static synchronized Connection getInstance(){
        if (instance==null){
            instance = new Connection();
        }
        return instance;
    }

    private boolean isConnected = false;
    private Connection(){
        try {
            socket = new Socket(Constants.serverAddress,Constants.serverPort);
            System.out.println("Connection success!");
            isConnected = true;
            socket.setSoTimeout(30000);
            address = InetAddress.getLocalHost();
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(){
        try {
            if (isConnected){
                socket.close();
                in.close();
                out.flush();
                out.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String line){
        System.out.println(line);
        out.println(line);
        out.flush();
    }

    public String receive(){
        String result = null;
        try {
            result = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }


}
