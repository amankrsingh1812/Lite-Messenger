package com.example.wa_client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public ContactAdapter adapter;
    public ArrayList<Contact> contacts;
    public HashMap<String,Integer> clientIdTocontacts;
    public HashMap<String ,ArrayList<Message>> clientIdToMessages;
    public HashMap<String ,MessageListAdapter> clientIdToMessageListAdapter;
    public String currentClientId;
    public String currentClientName;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentClientId = getIntent().getStringExtra("clientId");
        currentClientName = getIntent().getStringExtra("clientName");
        contacts = new ArrayList<>();
        clientIdToMessages = new HashMap<>();
        clientIdTocontacts = new HashMap<>();
        clientIdToMessageListAdapter = new HashMap<>();
        adapter = new ContactAdapter(contacts);
        addNewcontact(new Contact("Test0","123"));
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

    public void addNewcontact(Contact newContact){
        newContact.setClientName(newContact.getClientName());
        contacts.add(newContact);
        clientIdTocontacts.put(newContact.getClientId(),contacts.size()-1);
        ArrayList<Message> messageList = new ArrayList<Message>();
        clientIdToMessages.put(newContact.getClientId(),messageList);
        clientIdToMessageListAdapter.put(newContact.getClientId(),new MessageListAdapter(currentClientId,messageList));
        adapter.notifyItemInserted(contacts.size()-1);
        if(recyclerView != null)
            recyclerView.scrollToPosition(contacts.size()-1);
    }

    public void addNewChatMessage(Message message, String clientId){
        ArrayList<Message> messageList = clientIdToMessages.get(clientId);
        MessageListAdapter messageListAdapter = clientIdToMessageListAdapter.get(clientId);
        messageList.add(message);
        messageListAdapter.notifyItemInserted(messageList.size()-1);
    }

    public void sendMessage(String receiverId, String data, RecyclerView recyclerView){
        Message message = new Message(data,System.currentTimeMillis(),currentClientId,currentClientName);
        addNewChatMessage(message,receiverId);
        //Add sending logic

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}