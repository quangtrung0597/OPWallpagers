package com.example.trung.onepiecewallpagers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;

import asynctask.JsoupAsyn;
import asynctask.JsoupAsynFigures;
import model.Figure;
import model.ItemGridView;

public class SplashScreen extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        ConnectivityManager conManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo _wifi = conManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo _3g = conManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(!_wifi.isAvailable()&&!_3g.isAvailable())
        {
            showNetworkSettingsAlert();
            return;
        }

        JsoupAsynFigures jsoupAsynFigure = new JsoupAsynFigures(handler);
        jsoupAsynFigure.execute("https://wall.alphacoders.com/tags.php?tid=3649");

        JsoupAsyn jsoupAsyn = new JsoupAsyn(handler1);
        jsoupAsyn.execute("https://wall.alphacoders.com/tags.php?tid=36509");

    }

    //check internet
    public void showNetworkSettingsAlert() {
             AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashScreen.this);
             alertDialog.setTitle("Note");
             alertDialog.setMessage("Internet is unavailable");
             alertDialog.setPositiveButton("Setting",
                         new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                             Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                SplashScreen.this.startActivity(intent);
                finish();
                           }
         });
             alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {
                             dialog.cancel();
                          }
         });
             alertDialog.show();
           }

           // nhận dữ liệu danh sách các nhân vật
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == JsoupAsynFigures.WHAT_DATA_ONE)
            {
                MainActivity.arrFi.addAll((Collection<? extends Figure>) msg.obj);
            }
        }
    };

    // nhận dữ liệu một nhân vật
    private Handler handler1 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == JsoupAsyn.WHAT_DATA)
            {
                MainActivity.arrItem = (ArrayList<ItemGridView>) msg.obj;
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };
}
