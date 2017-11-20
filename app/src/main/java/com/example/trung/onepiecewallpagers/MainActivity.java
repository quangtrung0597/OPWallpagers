package com.example.trung.onepiecewallpagers;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import adapter.AdapterGridView;
import adapter.AdapterLvFigures;
import asynctask.JsoupAsyn;
import database.DatabaseUltil;
import model.Figure;
import model.ItemGridView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static ArrayList<Figure> arrFi = new ArrayList<>();
    public static ArrayList<ItemGridView> arrItem = new ArrayList<>();

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private DatabaseUltil databaseUltil;
    private GridView gridView;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialog1;
    private ArrayList<ItemGridView> arrAllItem = new ArrayList<>();
    private ArrayList<ItemGridView> arrItem1 = new ArrayList<>();
    private ListView lvFigures;
    private AdapterGridView adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("loading");
        gridView = findViewById(R.id.gvImage);

        if (arrItem.size() <= 15)
        {
            arrItem1.addAll(arrItem);
            adapter = new AdapterGridView(MainActivity.this,R.layout.item_gridview,arrItem1);
            adapter.notifyDataSetChanged();
            gridView.setAdapter(adapter);
        }
        else
        {
            for (int i=0; i<15; i++)
            {
                arrItem1.add(arrItem.get(i));
            }
            adapter = new AdapterGridView(MainActivity.this,R.layout.item_gridview,arrItem1);
            adapter.notifyDataSetChanged();
            gridView.setAdapter(adapter);
        }
        initViews();
    }

    //load lại dât khi chuyển nhân vật
    private void loadData(String nameOfFigure)
    {
        String link = "";
        for (Figure f:arrFi) {
            if (f.getName().equals(nameOfFigure) == true)
            {
                link = f.getUrl();
            }
        }

        JsoupAsyn jsoupAsyn = new JsoupAsyn(handler);
        jsoupAsyn.execute("https://wall.alphacoders.com/"+link);
    }

    // nhận dữ liệu khi chọn nhân vật khác
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == JsoupAsyn.WHAT_DATA)
            {
                arrItem = (ArrayList<ItemGridView>) msg.obj;
                arrItem1.clear();
                if (arrItem.size() <= 15)
                {
                    arrItem1.addAll(arrItem);
                    adapter = new AdapterGridView(MainActivity.this,R.layout.item_gridview,arrItem1);
                    adapter.notifyDataSetChanged();
                    gridView.setAdapter(adapter);
                    progressDialog.dismiss();
                }
                else
                {
                    for (int i=0; i<15; i++)
                    {
                        arrItem1.add(arrItem.get(i));
                    }
                    adapter = new AdapterGridView(MainActivity.this,R.layout.item_gridview,arrItem1);
                    adapter.notifyDataSetChanged();
                    gridView.setAdapter(adapter);
                    progressDialog.dismiss();
                }
            }
        }
    };

    //sử lý với các view trong MainActivity
    private void initViews() {
        lvFigures = findViewById(R.id.lvFigures);
        AdapterLvFigures adapterLvFigures = new AdapterLvFigures(MainActivity.this,android.R.layout.simple_list_item_1,arrFi);
        lvFigures.setAdapter(adapterLvFigures);
        lvFigures.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, arrFi.get(i).getName(), Toast.LENGTH_SHORT).show();
                loadData(arrFi.get(i).getName());
                drawerLayout.closeDrawers();
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("LOADING");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        });
        drawerLayout = findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,0,0);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        gridView.setOnItemClickListener(this);

        // xu ly load more
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if ((lastInScreen == totalItemCount)) {
                    int a= arrItem1.size();
                    int b= arrItem.size();
                    if(b-a <= 15 && b-a != 0 )
                    {
                        progressDialog1.show();
                        for (int i=a; i<=b-1;i++)
                        {
                            arrItem1.add(arrItem.get(i));
                        }
                        CountDownTimer countDownTimer = new CountDownTimer(5000,1000) {
                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                adapter.notifyDataSetChanged();
                                progressDialog1.dismiss();
                            }
                        }.start();

                    }
                    else if (b-a>15)
                    {
                        progressDialog1.show();
                        for (int i=a-1; i<=a+14;i++)
                        {
                            arrItem1.add(arrItem.get(i));
                        }
                        CountDownTimer countDownTimer = new CountDownTimer(1000,1000) {
                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                adapter.notifyDataSetChanged();
                                progressDialog1.dismiss();
                            }
                        }.start();
                    }
                    else if (b-a == 0)
                    {
                    }

                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
        {
            return true;
        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.fav) {
            Intent intent = new Intent(getApplicationContext(),FavActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ItemGridView itemGridView = arrItem.get(i);
        String imageUrl = itemGridView.getImgUrl();
        Intent intent = new Intent(this,ViewImage.class);
        intent.putExtra("imgUrl",imageUrl);
        intent.putExtra("imgSmall",itemGridView.getSmallImg());
        startActivity(intent);
    }
}
