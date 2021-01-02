package com.example.wa_client;

import android.util.Log;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import com.google.gson.Gson;

public class ReceivingThread extends Thread {

    private Socket socket;

    public ReceivingThread() {
//        this.socket = socket;
    }

    public void run() {
        SendRequest sendRequest = new SendRequest("192.168.0.13", 5000);
        SendRequestTask.setSendRequest(sendRequest);
        socket = sendRequest.getSocket();
        DataInputStream inputStream;
        Log.d("waclonedebug", "socket created");
        try {
            String input;
            Request request;
            Gson gson = new Gson();
            inputStream = new DataInputStream(socket.getInputStream());
            while (true) {
                try {
                    input = inputStream.readUTF();
                    request = gson.fromJson(input, Request.class);
//                    System.out.println(request);
                    GlobalVariables.processResponseService.submit(new ProcessResponseTask(request));
                }
                catch (EOFException e) {
                    System.out.println("Closing socket");
                    break;
                }
                catch (SocketException e){
                    break;
                }
                catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
            try {
                inputStream.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Terminating Receive thread");
    }
}
