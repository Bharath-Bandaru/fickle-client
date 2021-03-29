package info.fickle.fickleclient;

/**
 * Created by prithvi on 06/11/16.
 */

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class ClientClass extends AppCompatActivity {


    TextView textResponse;
    String networkSSID = "192.168.43.1";
    Thread socketServerThread;
    ServerSocket serverSocket;
    String message = "";
    WifiManager wifiManager;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler= new Handler();


        WifiConfiguration wifiConfiguration = new
                WifiConfiguration();
        wifiConfiguration.SSID = String.format("\"%s\"",
                networkSSID);
        wifiConfiguration.preSharedKey = String.format("\"%s\"", "fickle12345");

        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        int netId = wifiManager.addNetwork(wifiConfiguration);

        if (wifiManager.isWifiEnabled()) { //---wifi is turned on---
            //---disconnect it first---
            wifiManager.disconnect();
        } else { //---wifi is turned off---
            //---turn on wifi---
            wifiManager.setWifiEnabled(true);
        }
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
        socketServerThread = new Thread(new ClientClass.SocketServerThread());
        socketServerThread.start();

    }



    private class SocketServerThread extends Thread {

        static final int SocketServerPORT = 1705;
        int count = 0;

        @Override
        public void run() {
            Socket socket = null;
            DataInputStream dataInputStream = null;
            DataOutputStream dataOutputStream = null;

            try {
                serverSocket = new ServerSocket(SocketServerPORT);
                serverSocket.setReuseAddress(true);
                serverSocket.bind(new InetSocketAddress(SocketServerPORT));
                ClientClass.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        textResponse.setText("I'm waiting here: "
                                + serverSocket.getLocalPort());
                    }
                });

                while (true) {
                    socket = serverSocket.accept();
                    dataInputStream = new DataInputStream(
                            socket.getInputStream());
                    dataOutputStream = new DataOutputStream(
                            socket.getOutputStream());

                    String messageFromClient = "";

                    //If no message sent from client, this code will block the program
                    messageFromClient = dataInputStream.readUTF();

                    count++;
                    message +=  messageFromClient;


                    ClientClass.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(ClientClass.this,message, Toast.LENGTH_SHORT).show();
                            //textResponse.setText(message);
                        }
                    });

                    String msgReply = "Hello from Android, you are #" + count;
                    dataOutputStream.writeUTF(msgReply);

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                final String errMsg = e.toString();
                ClientClass.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        textResponse.setText(errMsg);
                    }
                });

            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //handler.removeCallbacks(socketServerThread);
    }
}
