package com.example.hott.socket_client;

import android.os.Handler;
import android.os.Message;

import java.io.DataInputStream;
import java.net.Socket;

public class ReceiveThread extends Thread {
    private Socket socket;
    DataInputStream dis;
    Handler mHandler;

    public ReceiveThread(Handler mHandler, Socket socck){
        this.socket = socket;
        this.mHandler = mHandler;
        try{
            dis = new DataInputStream(socket.getInputStream());
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void run(){
        Message msg;
        try{
            while(true){
                int read = dis.read();
                if(read != -1){
                    msg = new Message();
                    msg.what = 1;
                    msg.obj = dis.readUTF();
                    mHandler.sendMessage(msg);
                }
            }
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }

}
