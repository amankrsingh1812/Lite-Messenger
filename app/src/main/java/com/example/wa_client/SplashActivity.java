package com.example.wa_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
        SharedPreferences sharedPref = getSharedPreferences("apps", Context.MODE_PRIVATE);

//        SharedPreferences sharedPref = getPreferences(,Context.MODE_PRIVATE);
        String clientId = sharedPref.getString("clientId","");
        String clientName = sharedPref.getString("clientName","");
        String token = sharedPref.getString("token", "NULL");
//        Log.d("k", "onCreate: "+clientId);

        // This is the first Activity, so initializations will happen here
        initializations();

        Intent intent;
        if(clientId == "") {
            Toast.makeText(getApplicationContext(),"First Time",Toast.LENGTH_SHORT).show();
            intent = new Intent(this, RegisterActivity.class);
        }
        else{
            intent = new Intent(this,MainActivity.class);
            intent.putExtra("clientId",clientId);
            intent.putExtra("clientName",clientName);
        }
        startActivity(intent);
        finish();

    }

    private void initializations(){
        Log.d("waclonedebug", "In initializations");
        GlobalVariables.sendMessageService = Executors.newSingleThreadExecutor();
        if(GlobalVariables.sendMessageService == null) Log.d("waclonedebug", "Problem");

        GlobalVariables.processResponseService = Executors.newSingleThreadExecutor();
        SendRequest sendRequest = new SendRequest("127.0.0.1", 5000);
        SendRequestTask.setSendRequest(sendRequest);
        ReceivingThread receivingThread = new ReceivingThread(sendRequest.getSocket());
        receivingThread.start();



    }

}