package com.example.hott.socket_client;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.net.Socket;

public class SocketClient extends Thread{
    private String ipAddress;
    private String portId;
    private String id;
    private Socket sock;
    private SendThread st;
    private ReceiveThread rt;
    private Handler sendHandler;
    private Handler mainHandler;

    public SocketClient(String ipAddress, String portId, String id, Handler mHandler){
        this.ipAddress = ipAddress;
        this.portId = portId;
        this.id = id;
        this.mainHandler = mHandler;
    }

    public void run(){
        try{
            sock = new Socket(ipAddress, Integer.parseInt(portId));
            Looper.prepare();

            st = new SendThread(sock);
            rt = new ReceiveThread(mainHandler, sock);

            Looper.loop();
        }catch(Exception e){
            Log.i("SocketClient","ConnectError");
        }finally{
            closeSock();
        }
    }

    public boolean isConnected(){
        return sock.isConnected();
    }

    public void closeSock() {
        try{
            sock.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public void sendMsg(String str){
        Message msg = new Message();
        msg.what = 2;
        msg.obj = str;
        sendHandler.sendMessage(msg);
    }
}
