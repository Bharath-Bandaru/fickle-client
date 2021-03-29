package info.fickle.fickleclient;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private int[] mTabsIcons = {
            R.drawable.ic_recents_selector,
            R.drawable.ic_home_selector,
            R.drawable.ic_place_selector};

    String networkSSID = "192.168.43.1";
    Thread socketServerThread;
    ServerSocket serverSocket;
    String message = "";
    WifiManager wifiManager;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout = (CoordinatorLayout)  findViewById(R.id.cl);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        if(!wifi.isWifiEnabled()){
            turnOn();
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setPositiveButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "Turn on your Wifi", Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.YELLOW).setDuration(100000)
                                    .setAction("Turn On", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            turnOn();
                                            Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "You are Awesome!", Snackbar.LENGTH_SHORT);
                                            snackbar1.show();
                                        }
                                    });
                            snackbar.show();
                            dialog.cancel();
                        }
                    });

            builder1.setNegativeButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
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
                            socketServerThread = new Thread(new MainActivity.SocketServerThread());
                            socketServerThread.start();

                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = builder1.create();
            alertDialog.setTitle("Get Offers");
            alertDialog.setMessage("Connect to store!");
            alertDialog.setIcon(R.drawable.logo_140);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

        }
        else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setPositiveButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            turnoff();
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "Turn on your Wifi", Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.YELLOW).setDuration(100000)
                                    .setAction("Turn On", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            turnOn();
                                            Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "You are Awesome!", Snackbar.LENGTH_SHORT);
                                            snackbar1.show();
                                        }
                                    });
                            snackbar.show();
                            dialog.cancel();
                        }
                    });

            builder1.setNegativeButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
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
                            socketServerThread = new Thread(new MainActivity.SocketServerThread());
                            socketServerThread.start();

                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = builder1.create();
            alertDialog.setTitle("Get Offers");
            alertDialog.setMessage("Connect to store!");
            alertDialog.setIcon(R.drawable.logo_140);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

        }
        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.view_pager);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        if (viewPager != null) {
            viewPager.setPagingEnabled(false);
            viewPager.setAdapter(pagerAdapter);
        }

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(viewPager);

            for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = mTabLayout.getTabAt(i);
                if (tab != null)
                    tab.setCustomView(pagerAdapter.getTabView(i));
            }

            mTabLayout.getTabAt(0).getCustomView().setSelected(true);
        }
        mTabLayout.getTabAt(1).select();

    }

    public void turnoff(){
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(false);
    }
    public void turnOn(){
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
        socketServerThread = new Thread(new MainActivity.SocketServerThread());
        socketServerThread.start();

    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public final int PAGE_COUNT = 3;

        private final String[] mTabsTitle = {"Recents", "Favorites", "Nearby"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public View getTabView(int position) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_tab, null);
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(mTabsTitle[position]);
            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            icon.setImageResource(mTabsIcons[position]);
            return view;
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    return LoginFragment.newInstance(1);

                case 1:
                    return HomeFragment.newInstance(2);

                case 2:
                    return PageFragment.newInstance(3);

            }
            return null;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabsTitle[position];
        }
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
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"I'm waiting here: "
                              + serverSocket.getLocalPort(), Toast.LENGTH_SHORT).show();
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
                    message +=messageFromClient;


                    MainActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            HomeFragment.offers.add(new MainModel(message));
                            HomeFragment.setAdap();
                            Toast.makeText(MainActivity.this,message, Toast.LENGTH_SHORT).show();
                        }
                    });

                    String msgReply = "Hello from Android, you are #" + count;
                    dataOutputStream.writeUTF(msgReply);

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                final String errMsg = e.toString();
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,errMsg, Toast.LENGTH_SHORT).show();
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

}
