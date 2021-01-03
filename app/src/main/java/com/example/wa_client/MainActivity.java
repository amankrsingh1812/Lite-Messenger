package com.example.wa_client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public ContactAdapter adapter;
    public ArrayList<Contact> contacts;
    public HashMap<String, Integer> clientIdToContacts;
    public HashMap<String, Contact> tempClientIdToContacts;
    public HashMap<String, ArrayList<Message>> clientIdToMessages;
    public HashMap<String, MessageListAdapter> clientIdToMessageListAdapter;
    public String currentClientId;
    public String currentClientName;
    private RecyclerView recyclerView;
    public GlobalVariables globalVariables;
    public boolean isProcessing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        globalVariables = (GlobalVariables)this.getApplication();
        globalVariables.mainActivity = this;
        currentClientId = getIntent().getStringExtra("clientId");
        currentClientName = getIntent().getStringExtra("clientName");
        contacts = new ArrayList<>();
        clientIdToMessages = new HashMap<>();
        tempClientIdToContacts = new HashMap<>();
        clientIdToContacts = new HashMap<>();
        clientIdToMessageListAdapter = new HashMap<>();
        adapter = new ContactAdapter(this,contacts);
//        addNewContact(new Contact("Test0","123"));
        Toast toast = Toast.makeText(getApplicationContext(),"Start",Toast.LENGTH_SHORT);
        toast.show();
    }

    public void setRecyclerviewChatList(RecyclerView recyclerView){
        this.recyclerView = recyclerView;
        recyclerView.setAdapter(adapter);
    }

    public void setRecyclerViewChatFragment(RecyclerView recyclerView, String clientId){
        recyclerView.setAdapter(clientIdToMessageListAdapter.get(clientId));
    }

    public void addNewContact(Contact newContact){
//        newContact.setClientName(newContact.getClientName());
        Log.d("waclonedebug", "addNewContact: start"+newContact.getClientName());
//        contacts.add(newContact);
        clientIdToContacts.put(newContact.getClientId(),contacts.size());
        ArrayList<Message> messageList = new ArrayList<>();
        clientIdToMessages.put(newContact.getClientId(),messageList);
        clientIdToMessageListAdapter.put(newContact.getClientId(),new MessageListAdapter(currentClientId,messageList));
        Log.d("waclonedebug", "addNewContact: mid1"+newContact.getClientName());
        isProcessing = true;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.addContact(newContact);
                if(recyclerView != null)
                    recyclerView.scrollToPosition(contacts.size()-1);
                isProcessing = false;
            }
        });
        while (isProcessing);
        Log.d("waclonedebug", "addNewContact: end"+newContact.getClientName());
    }

    public void addNewChatMessage(Message message, String clientId){
        Log.d("waclonedebug", "addNewChatMessage: start");
        ArrayList<Message> messageList = clientIdToMessages.get(clientId);
        MessageListAdapter messageListAdapter = clientIdToMessageListAdapter.get(clientId);
        int pos = clientIdToContacts.get(clientId);
        Contact contact = contacts.get(pos);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(message);
                contact.incrementUnseenMessages();
                contact.setDisplayMessage(message.getData());
                adapter.notifyItemChanged(pos);
                messageListAdapter.notifyItemInserted(messageList.size()-1);
                messageListAdapter.scrollRecyclerView();
            }
        });

        Log.d("waclonedebug", "addNewChatMessage: end");
    }

//    public void receivedReceiveReceipt(String reqId, String clientId, long receiveTimeStamp){
////        Contact contact = getContact(clientId);
//        ArrayList<Message> messageList = clientIdToMessages.get(clientId);
//        MessageListAdapter messageListAdapter = clientIdToMessageListAdapter.get(clientId);
//        for(int i=messageList.size()-1; i>=0; i--){
//            Message message = messageList.get(i);
//            if(message.getMessageID() == reqId) {
//                final int pos = i;
//                this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        message.setReceiveTimeStamp(receiveTimeStamp);
//                        messageListAdapter.notifyItemChanged(pos);
//                    }
//                });
//                return;
//            }
//        }
//
//    }

    public void sendMessage(String receiverId, String data, RecyclerView recyclerView){
        Log.d("waclonedebug", "sendMessage: "+receiverId);
        Message message = new Message(data,System.currentTimeMillis(),currentClientId,currentClientName);
        addNewChatMessage(message,receiverId);
        //Add sending logic
        globalVariables.sendMessageService.submit(new SendRequestTask(Request.RequestType.Message, receiverId, data, globalVariables));
    }

    public void addTempContact(String clientId, String clientName) {
        tempClientIdToContacts.put(clientId, new Contact(clientName, clientId));
    }

    public void sendNewChatMessage(String newChatClientId){
        globalVariables.sendMessageService.submit(new SendRequestTask(Request.RequestType.NewChat, newChatClientId, "",globalVariables));
    }
    public void removeTempContact(String clientId) {
        tempClientIdToContacts.remove(clientId);
    }

    public Contact getTempContact(String clientId) {
        return tempClientIdToContacts.get(clientId);
    }

    public Contact getContact(String clientId) {
        return contacts.get(clientIdToContacts.get(clientId));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}