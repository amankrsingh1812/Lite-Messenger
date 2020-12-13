package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import static com.mongodb.client.model.Filters.*;

import org.bson.Document;

import server.GlobalVariables.RequestType;

import java.io.IOException;

public class ClientRecievingThread extends Thread {

    private Socket socket;
    private DataInputStream inputStream;
    private String clientId;

    public ClientRecievingThread(Socket inputSocket) {
        socket = inputSocket;
        clientId = "";
    }

    private Boolean isAuth(Request request) {
        if (request.getAction() == RequestType.Auth)
            return true;
        return false;
    }

    private Boolean processRequest(Request request) throws InterruptedException {
        RequestType reqType = request.getAction();
        String receiverId = request.getReceiverId();
        if (clientId.isEmpty())
            clientId = request.getSenderId();
        if (!GlobalVariables.onlineClients.containsKey(clientId)) {
            if (isAuth(request)) {
                System.out.println("Auth");
                DataOutputStream outputStream;
                try {
                    outputStream = new DataOutputStream(socket.getOutputStream());
                    GlobalVariables.onlineClientsAddKey(clientId, new ClientInfo(clientId, inputStream, outputStream));

                    System.out.println("aye");
                    
                    GlobalVariables.databaseLock.acquire();
                    try(MongoClient mongoClient = MongoClients.create(GlobalVariables.connectionString)){
                        System.out.println("Connected to database.");
            
                        GlobalVariables.database = mongoClient.getDatabase("wacloneDB");
                        GlobalVariables.userCollection = GlobalVariables.database.getCollection("users");
                        GlobalVariables.messageCollection = GlobalVariables.database.getCollection("messages");
                        List<Document> messages = GlobalVariables.messageCollection.find(eq("receiverId", clientId)).into(new ArrayList<Document>());
                        GlobalVariables.messageCollection.deleteMany(eq("receiverId", clientId));
                        
                        GlobalVariables.databaseLock.release();
                        //Deliver stored messages to user
                        System.out.println(messages);
                        //Delete these messages from database
                        for(Document message: messages){
                            Request r = new Request(message);
                            GlobalVariables.sendMessage.execute(new SendMessageTask(outputStream, r));
                        }
                    }
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (reqType == RequestType.NewChat) {
            System.out.println("New Chat");
            // Check if the receiver is present in Map
            // if (!GlobalVariables.clientSendBox.containsKey(receiverId)) {
            // // Return error response
            // }
        } else if (reqType == RequestType.Message) {
            System.out.println("Send message");
            if(GlobalVariables.onlineClients.containsKey(receiverId)){
                ClientInfo receiverInfo = GlobalVariables.onlineClients.get(receiverId);
                GlobalVariables.sendMessage.execute(new SendMessageTask(receiverInfo.getOutputStream(), request));
            } else {
                GlobalVariables.databaseLock.acquire();
                try(MongoClient mongoClient = MongoClients.create(GlobalVariables.connectionString)){
                    System.out.println("Connected to database.");
        
                    GlobalVariables.database = mongoClient.getDatabase("wacloneDB");
                    GlobalVariables.userCollection = GlobalVariables.database.getCollection("users");
                    GlobalVariables.messageCollection = GlobalVariables.database.getCollection("messages");
                    GlobalVariables.messageCollection.insertOne(request.toDocument());
                    
                }
                GlobalVariables.databaseLock.release();

            }

        } else {
            System.out.println("FFFFFFFFFFFFFFFFFFFFFF Unknown Command");
            // Unkown command return error response
            return false;
        }
        return true;
    }

    public void run() {
        Gson gson = new Gson();
        try {
            // Get input from socket
            inputStream = new DataInputStream(socket.getInputStream());
            Request request;
            String input;
            while (true) {
                input = inputStream.readUTF();
                // System.out.println(input);
                request = gson.fromJson(input, Request.class);
                System.out.println(request);
                if (!processRequest(request))
                    break;
            }

            inputStream.close();
        } catch (EOFException e) {

            System.out.println("Closing socket from server haha");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            GlobalVariables.onlineClientsRemoveKey(clientId);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;

    }
}
