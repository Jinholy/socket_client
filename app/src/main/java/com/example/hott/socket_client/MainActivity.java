package com.example.hott.socket_client;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public EditText ipEditText;
    public EditText portEditText;
    public EditText idEditText;
    public EditText chatEditText;
    public TextView chatTextView;
    public Button   connectBtn, disconnectBtn, sendBtn;
    public String chatString = "\n";
    public BtnOnClickListener btnListener = new BtnOnClickListener();
    public String portNumber, ipAddress;
    public String userId;
    public SocketClient sc;
    public Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ipEditText   = findViewById(R.id.ipEditText);
        portEditText = findViewById(R.id.portEditText);
        idEditText   = findViewById(R.id.idEditText);
        chatEditText = findViewById(R.id.chatEditText);
        chatTextView = findViewById(R.id.chatTextView);
        connectBtn   = findViewById(R.id.connectButton);
        disconnectBtn = findViewById(R.id.disconnectButton);
        sendBtn       = findViewById(R.id.sendButton);


        portNumber = ""; ipAddress = "";
        userId = "";
        sendBtn.setOnClickListener(btnListener);
        disconnectBtn.setOnClickListener(btnListener);
        connectBtn.setOnClickListener(btnListener);

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                switch(msg.what){
                    case 0:
                        chatString += ("\n[" + ipAddress + ":" + portNumber + "]" + userId + ":  " + msg.arg1);
                        chatTextView.setText(chatString);
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class BtnOnClickListener implements Button.OnClickListener{

        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.connectButton:
                    portNumber = portEditText.getText().toString();
                    userId = idEditText.getText().toString();
                    ipAddress = ipEditText.getText().toString();
                    sc = new SocketClient(ipAddress, portNumber, userId, mHandler);
                    sc.setDaemon(true);
                    sc.start();
                    boolean connected = sc.isConnected();

                    if(connected){
                        Toast.makeText(getApplicationContext(),"Client has been connected!", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Connection Failure!", Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.disconnectButton:
                    sc.closeSock();
                    Toast.makeText(getApplicationContext(),"Disconnected", Toast.LENGTH_LONG).show();
                    break;
                case R.id.sendButton:
                    String str = chatEditText.getText().toString();
                    chatString += ("\n[" + ipAddress + ":" + portNumber + "]" + userId + ":  " + str);
                    chatTextView.setText(chatString);
                    sc.sendMsg(str);
                    chatEditText.setText("");
                    break;
            }
        }
    }


    @Override
    protected void onDestroy() {
        sc.closeSock();
        super.onDestroy();
    }
}
