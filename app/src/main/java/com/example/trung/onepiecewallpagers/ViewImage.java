package com.example.trung.onepiecewallpagers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import database.DatabaseUltil;
import model.ItemGridView;

/**
 * Created by Admin on 11/16/2017.
 */

public class ViewImage extends AppCompatActivity {

    private ImageView imageView;
    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton btnDown;
    private FloatingActionButton btnFav;
    private FloatingActionButton btnShare;
    private FloatingActionButton btnSet;
    private Bitmap bm;

    private Dialog dialogDown;
    private Toolbar toolbar;
    private String imageUrl;
    private String imageSmall;

    public static final String[] PERMISSION_LIST = {
            android.Manifest.permission.INTERNET,
            Manifest.permission.SET_WALLPAPER,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_WIFI_STATE,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.ACCESS_FINE_LOCATION
    } ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_image_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            for (String permission: PERMISSION_LIST){
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(PERMISSION_LIST,0);
                    return;
                }
            }
        }

        initsView();


    }

    private void initsView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initDialogDown();

        floatingActionMenu = findViewById(R.id.btnMenu);
        btnDown = findViewById(R.id.btnDownload);
        btnFav = findViewById(R.id.btnAddFav);
        //btnSet = findViewById(R.id.btnSetWall);
        btnShare = findViewById(R.id.btnShare);

        final Intent intent = getIntent();
        imageUrl = intent.getStringExtra("imgUrl");
        imageSmall = intent.getStringExtra("imgSmall");

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDown.show();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ViewImage.this,ShareFb.class);
                intent1.putExtra("imgS",imageSmall);
                intent1.putExtra("img",imageUrl);
                startActivity(intent1);
            }
        });

        /*btnSet.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {

                final ProgressDialog dialog = new ProgressDialog(ViewImage.this);
                Glide.with(getApplicationContext())
                        .load(imageUrl)
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {


                            @Override
                            public void onLoadStarted(Drawable placeholder) {
                                super.onLoadStarted(placeholder);
                                //Toast.makeText(getApplicationContext(), "Processing...", Toast.LENGTH_SHORT).show();
                                dialog.setMessage("Processing...");
                                dialog.show();
                            }

                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {

                                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                                try {
                                    wallpaperManager.setBitmap(resource);

                                    //wallpaperManager.suggestDesiredDimensions(width, height);
                                    Toast.makeText(getApplicationContext(), "Wallpaper Set", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        }); */

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemGridView itemGridView = new ItemGridView(imageUrl,imageSmall);
                DatabaseUltil databaseUltil = new DatabaseUltil(ViewImage.this);
                databaseUltil.insert(itemGridView);

            }
        });


        imageView = findViewById(R.id.imFull);
        Toast.makeText(this, imageUrl, Toast.LENGTH_SHORT).show();
        Glide.with(this).load(imageSmall)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result: grantResults){
            if (result == PackageManager.PERMISSION_DENIED){
                finish();
                return;
            }
        }
        initsView();
    }

    // down
    class DownLoadFile extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... strings) {

            String link = strings[0];
            String[] str1 = link.split("thumb-");
            String fileName = str1[1];
            try {
                URL url = new URL(link);
                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();
                String pathParent = Environment.getExternalStorageDirectory().getPath()+"/WallPagersOP";
                File f = new File(pathParent);
                if (!f.exists())
                {
                    f.mkdir();
                }
                String path = Environment.getExternalStorageDirectory().getPath()+"/WallPagersOP/"+fileName;
                File file = new File(path);
                file.getParentFile().mkdirs();
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] b = new byte[1024];
                int count = inputStream.read(b);
                while (count != -1){
                    fileOutputStream.write(b,0,count);
                    count = inputStream.read(b);
                }
                fileOutputStream.close();
                inputStream.close();
                return file.getPath();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(ViewImage.this, "success", Toast.LENGTH_SHORT).show();
        }
    }

    // dialog cho người dùng chọn hình thức down
    private void initDialogDown() {
        dialogDown = new Dialog(this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialogDown .setContentView(R.layout.dialog_choose_kind_download);
        Button btnFullHD = dialogDown.findViewById(R.id.btnFullHD);
        Button btnHD = dialogDown.findViewById(R.id.btnHD);
        btnFullHD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ViewImage.this, "full hd", Toast.LENGTH_SHORT).show();
                DownLoadFile downLoadFile = new DownLoadFile();
                downLoadFile.execute(imageUrl);
                dialogDown.dismiss();
            }
        });

        btnHD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ViewImage.this, "hd", Toast.LENGTH_SHORT).show();
                DownLoadFile downLoadFile = new DownLoadFile();
                downLoadFile.execute(imageSmall);
                dialogDown.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
