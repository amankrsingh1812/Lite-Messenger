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
    GlobalVariables globalVariables;
    private Button registerButton;
    private EditText clientIde;
    private EditText clientNamee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        globalVariables = (GlobalVariables)this.getApplication();
//        Log.d("waclonedebug", "gv "+globalVariables.getTest());
        clientIde = findViewById(R.id.clientIdInput);
        clientNamee = findViewById(R.id.clientNameInput);
        registerButton = findViewById(R.id.register);
        Intent intent = new Intent(this,MainActivity.class);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("waclonedebug", "First");
                Log.d("waclonedebug", "Second");
                String clientId = clientIde.getText().toString();
                String clientName = clientNamee.getText().toString();

                SharedPreferences sharedPref = getSharedPreferences("apps",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("clientId",clientId);
                editor.putString("clientName",clientName);
                editor.commit();

                Log.d("waclonedebug", "Other 2 committed");
                // Send SignUp Request
                globalVariables.clientId = clientId;
                if(globalVariables.sendMessageService == null) Log.d("waclonedebug", "Problem");
                if(globalVariables.processResponseService == null) Log.d("waclonedebug", "Problem pms");
                globalVariables.sendMessageService.submit(new SendRequestTask(Request.RequestType.SignUp,  globalVariables.serverId, ""));
                Log.d("waclonedebug", "Submitted");
                intent.putExtra("clientId",clientId);
                intent.putExtra("clientName",clientName);
                startActivity(intent);
                finish();
            }
        });

    }
}