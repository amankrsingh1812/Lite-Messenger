package com.example.wa_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
        SharedPreferences sharedPref = getSharedPreferences("apps", Context.MODE_PRIVATE);

//        SharedPreferences sharedPref = getPreferences(,Context.MODE_PRIVATE);
        String clientId = sharedPref.getString("clientId","");
        String clientName = sharedPref.getString("clientName","");
        Log.d("k", "onCreate: "+clientId);
        if(clientId == "") {
            Toast.makeText(getApplicationContext(),"First Time",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("clientId",clientId);
            intent.putExtra("clientName",clientName);
            startActivity(intent);
            finish();
        }

    }
}