package com.example.wa_client;

import android.app.Application;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;

public class GlobalVariables extends Application {
    public static ExecutorService sendMessageService, processResponseService;
    public static MainActivity mainActivity;
    public final static String serverId = "SERVER";
    public static String clientId;
    public static SharedPreferences sharedPref;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String test;
    private static HashMap<String, String> newChatReqId = new HashMap<>();

    public static void addNewChatToMap(String requestId, String newUserId){
        newChatReqId.put(requestId, newUserId);
    }
    public static String getNewChatFromMap(String requestId){
        return newChatReqId.get(requestId);
    }
    public static String removeNewChatFromMap(String requestId){
        String chatId = newChatReqId.get(requestId);
        newChatReqId.remove(requestId);
        return chatId;
    }

}
