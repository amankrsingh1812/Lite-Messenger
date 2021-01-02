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

    GlobalVariables globalVariables;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         globalVariables = (GlobalVariables)this.getApplication();
//        setContentView(R.layout.activity_splash);
         sharedPref = getSharedPreferences("apps", Context.MODE_PRIVATE);
        String clientId = sharedPref.getString("clientId","");
        String clientName = sharedPref.getString("clientName","");
        String token = sharedPref.getString("token", "NULL");
//        Log.d("k", "onCreate: "+clientId);

        // This is the first Activity, so initializations will happen here
        initializations();

        while(!SendRequestTask.isReady()){
            Log.d("waclonedebug", "Creating Socket...");
        }
        Intent intent;
        if(clientId == "") {
            Toast.makeText(getApplicationContext(),"First Time",Toast.LENGTH_SHORT).show();
            intent = new Intent(this, RegisterActivity.class);
        }
        else{
            SendRequestTask.setToken(token);
            globalVariables.clientId=clientId;


            globalVariables.sendMessageService.submit(new SendRequestTask(Request.RequestType.Auth,  globalVariables.serverId, ""));
            Log.d("waclonedebug", "auth sent");
            intent = new Intent(this,MainActivity.class);
            intent.putExtra("clientId",clientId);
            intent.putExtra("clientName",clientName);
        }
        startActivity(intent);
        finish();

    }

    private void initializations(){
        Log.d("waclonedebug", "In initializations");
        globalVariables.sendMessageService = Executors.newSingleThreadExecutor();
        globalVariables.processResponseService = Executors.newSingleThreadExecutor();
        globalVariables.sharedPref = sharedPref;

        ReceivingThread receivingThread = new ReceivingThread();
        receivingThread.start();



    }

}