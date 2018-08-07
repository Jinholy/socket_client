package com.example.hott.socket_client;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.DataOutputStream;
import java.net.Socket;

public class SendThread extends Thread{
    private Socket sc;
    private String msg;
    DataOutputStream opt;
    private Handler scMsgHandler;       //Socket Client로부터 메시지 핸들

    public SendThread(Socket sc){
        this.sc = sc;
        this.msg = "";
        try{
            opt = new DataOutputStream(sc.getOutputStream());
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
    public void run(){
        Looper.prepare();
        scMsgHandler = new Handler(){
            public void handleMessage(Message msg){
                if(msg.what == 2){
                    String var = msg.obj.toString();
                    try{
                        opt.writeChars(var);
                    }catch(Exception e){
                        Log.i("SendThread", "optError");
                    }
                }
            }
        };
        Looper.loop();
    }

}
