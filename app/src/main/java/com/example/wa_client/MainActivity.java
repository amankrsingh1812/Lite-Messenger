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
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public ContactAdapter adapter;
    public ArrayList<Contact> contacts;
    public HashMap<String,Integer> clientIdTocontacts;
    public HashMap<String ,ArrayList<Message>> clientIdToMessages;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contacts = new ArrayList<>();
        clientIdToMessages = new HashMap<>();
        clientIdTocontacts = new HashMap<>();
        addNewcontact(new Contact("Test0","123"));
        adapter = new ContactAdapter(contacts);
        Toast toast = Toast.makeText(getApplicationContext(),"bye",Toast.LENGTH_SHORT);
        toast.show();
    }

    public void setRecyclerviewChatList(RecyclerView recyclerView){
        this.recyclerView = recyclerView;
        recyclerView.setAdapter(adapter);
    }

    public void addNewcontact(Contact newContact){
        newContact.setClientName(newContact.getClientName()+contacts.size());
        contacts.add(newContact);
        clientIdTocontacts.put(newContact.getClientId(),contacts.size()-1);
        clientIdToMessages.put(newContact.getClientId(),new ArrayList<Message>());
        adapter.notifyItemInserted(contacts.size()-1);
        recyclerView.scrollToPosition(contacts.size()-1);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}