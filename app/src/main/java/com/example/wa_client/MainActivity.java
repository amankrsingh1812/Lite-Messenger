package com.example.wa_client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static ContactAdapter adapter;
    public static ArrayList<Contact> contacts;
    public static HashMap<String, Integer> clientIdToContacts;
    public static HashMap<String, Contact> tempClientIdToContacts;
    public static HashMap<String, ArrayList<Message>> clientIdToMessages;
    public static HashMap<String, MessageListAdapter> clientIdToMessageListAdapter;
    public static String currentClientId;
    public static String currentClientName;
    private static RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentClientId = getIntent().getStringExtra("clientId");
        currentClientName = getIntent().getStringExtra("clientName");
        contacts = new ArrayList<>();
        clientIdToMessages = new HashMap<>();
        clientIdToContacts = new HashMap<>();
        clientIdToMessageListAdapter = new HashMap<>();
        adapter = new ContactAdapter(contacts);
        addNewContact(new Contact("Test0","123"));
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

    public static void addNewContact(Contact newContact){
//        newContact.setClientName(newContact.getClientName());
        contacts.add(newContact);
        clientIdToContacts.put(newContact.getClientId(),contacts.size()-1);
        ArrayList<Message> messageList = new ArrayList<Message>();
        clientIdToMessages.put(newContact.getClientId(),messageList);
        clientIdToMessageListAdapter.put(newContact.getClientId(),new MessageListAdapter(currentClientId,messageList));
        adapter.notifyItemInserted(contacts.size()-1);
        if(recyclerView != null)
            recyclerView.scrollToPosition(contacts.size()-1);
    }

    public static void addNewChatMessage(Message message, String clientId){
        ArrayList<Message> messageList = clientIdToMessages.get(clientId);
        MessageListAdapter messageListAdapter = clientIdToMessageListAdapter.get(clientId);
        messageList.add(message);
        messageListAdapter.notifyItemInserted(messageList.size()-1);
    }

    public static void sendMessage(String receiverId, String data, RecyclerView recyclerView){
        Message message = new Message(data,System.currentTimeMillis(),currentClientId,currentClientName);
        addNewChatMessage(message,receiverId);
        //Add sending logic
        GlobalVariables.sendMessageService.submit(new SendRequestTask(Request.RequestType.Message, receiverId, data));
    }

    public static void addTempContact(String clientId, String clientName) {
        tempClientIdToContacts.put(clientId, new Contact(clientName, clientId));
    }

    public static void removeTempContact(String clientId) {
        tempClientIdToContacts.remove(clientId);
    }

    public static Contact getTempContact(String clientId) {
        return tempClientIdToContacts.get(clientId);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}