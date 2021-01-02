package com.example.wa_client;

import android.util.Log;

public class SendRequestTask implements Runnable {

//    private Request.RequestType requestType;
//    private String senderId;
//    private String data;

    private Request request;
    private static SendRequest sendRequest;
    private static String token;

    public SendRequestTask(Request.RequestType requestType, String receiverId, String data) {
        this.request = new Request(requestType, GlobalVariables.clientId, receiverId, data, token);
    }

    public static void setToken(String token_m){
        token = token_m;
    }

    public static void setSendRequest(SendRequest sendRequest_m){
        sendRequest = sendRequest_m;
    }

    public static boolean isReady()
    {
        return (sendRequest!=null);
    }
    private void storeAndCloseConnection() {
        Log.d("waclonedebug", "Server is Offline, save to disk");
    }

    public void run() {
//        while(!SendRequestTask.isReady()){
//            ;
//        }
        Request.RequestType reqType = request.getAction();
        if(reqType == Request.RequestType.NewChat){
            GlobalVariables.addNewChatToMap(request.getRequestId(), request.getReceiverId());
        }
        if(!sendRequest.sendRequestSafe(request)){
            storeAndCloseConnection();
        }
    }
}
