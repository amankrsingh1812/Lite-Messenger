package com.example.wa_client;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

public class ProcessResponseTask implements Runnable {

    private Request request;
//    private static String signUpRId;
//    private static String authRId;
    private GlobalVariables globalVariables;
    private static boolean authenticated = false;
    private Context context;


    public ProcessResponseTask(Request request,GlobalVariables globalVariables) {
        this.request = request;
        this.globalVariables = globalVariables;
    }

    public void run() {
        Request.RequestType action = request.getAction();
        if(action == Request.RequestType.SignUpSuccessful){
            String token = request.getToken();

            // Store token on disk
            SharedPreferences.Editor editor = globalVariables.sharedPref.edit();
            editor.putString("token",token);
            editor.commit();

            SendRequestTask.setToken(token);

            authenticated=true;
        }
        else if(action == Request.RequestType.AuthSuccessful){
            authenticated=true;
        }
//        else if(!authenticated) {
//            Log.d("waclonedebug", request.toString());
//            Log.e("waclonedebug", "Auth not done. Can not do anything else");
//            return;
//        }
        else if(action == Request.RequestType.NewChatPositive){
            Log.v("waclonedebug", "NewChat with " + request.getData());
            String newChatReqId = request.getData();
            String newChatId = globalVariables.getNewChatFromMap(newChatReqId);
//            globalVariables.mainActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
                    globalVariables.mainActivity.addNewContact(globalVariables.mainActivity.getTempContact(newChatReqId));
//                }
//            });
        }
        else if(action == Request.RequestType.UserNotFound){
            String newChatReqId = request.getData();
            String newChatId = globalVariables.removeNewChatFromMap(newChatReqId);
            globalVariables.mainActivity.removeTempContact(newChatId);
        }
        else if(action == Request.RequestType.Message){
            String senderId = request.getSenderId();
            Log.d("waclonedebug", "sid "+senderId);

            Contact contact;
            if(globalVariables.mainActivity.clientIdToContacts.get(senderId) == null) {
                contact = new Contact(senderId,senderId);
//                globalVariables.mainActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
                globalVariables.mainActivity.addNewContact(contact);
//                    }
//                });
                Log.d("waclonedebug", contact.getClientName());
            }
            else {
//                Log.d("waclonedebug", "existing client"+MainActivity.clientIdToContacts.get(senderId));
                contact = globalVariables.mainActivity.contacts.get(globalVariables.mainActivity.clientIdToContacts.get(senderId));
                Log.d("waclonedebug", "existing client found");
            }
            Message message = new Message(request.getData(), request.getTimeStamp(), senderId, contact.getClientName());
            Log.d("waclonedebug", message.getData());
            globalVariables.mainActivity.addNewChatMessage(message, senderId);
        }
        
    }

}
