package com.example.wa_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private Button registerButton;
    private EditText clientIde;
    private EditText clientNamee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        clientIde = findViewById(R.id.clientIdInput);
        clientNamee = findViewById(R.id.clientNameInput);
        registerButton = findViewById(R.id.register);
        Intent intent = new Intent(this,MainActivity.class);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getSharedPreferences("apps",Context.MODE_PRIVATE);
                Log.d("waclonedebug", "First");
                SharedPreferences.Editor editor = sharedPref.edit();
                Log.d("waclonedebug", "Second");
                String clientId = clientIde.getText().toString();
                String clientName = clientNamee.getText().toString();

//                editor.putString("clientId",clientId);
//                editor.putString("clientName",clientName);
//                editor.commit();
                Log.d("waclonedebug", "Other 2 committed");
                // Send SignUp Request
                GlobalVariables.clientId = clientId;
                if(GlobalVariables.sendMessageService == null) Log.d("waclonedebug", "Problem");
                if(GlobalVariables.processResponseService == null) Log.d("waclonedebug", "Problem pms");
                GlobalVariables.sendMessageService.submit(new SendRequestTask(Request.RequestType.SignUp,  GlobalVariables.serverId, ""));
                Log.d("waclonedebug", "Submitted");
                intent.putExtra("clientId",clientId);
                intent.putExtra("clientName",clientName);
                startActivity(intent);
                finish();
            }
        });

    }
}