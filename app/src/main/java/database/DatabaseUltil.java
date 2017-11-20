package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import model.Figure;
import model.ItemGridView;

/**
 * Created by Admin on 11/16/2017.
 */

public class DatabaseUltil {

    public static final String PATH = Environment.getDataDirectory().getPath()
            + "/data/com.example.trung.onepiecewallpagers/databases/";
    public static final String DB_NAME = "data.sqlite";
    public static final String TABLE_NAME = "favorite";
    public static final String ID = "id";
    public static final String SMALL = "smallImg";
    public static final String URL = "ImageUrl";

    private Context context;
    private SQLiteDatabase database;

    public DatabaseUltil(Context context) {
        this.context = context;
        copyFileToDevice();
    }

    private void copyFileToDevice() {
        File file = new File(PATH + DB_NAME);
        if (!file.exists()) {
            File parent = file.getParentFile();
            parent.mkdirs();
            try {
                InputStream inputStream = context.getAssets().open(DB_NAME);
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] b = new byte[1024];
                int count = inputStream.read(b);
                while (count != -1) {
                    outputStream.write(b, 0, count);
                    count = inputStream.read(b);
                }
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openDatabase() {
        database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
    }

    private void closeDatabase() {
        database.close();
    }

    public ArrayList<ItemGridView> getData() {
        ArrayList<ItemGridView> arrItem = new ArrayList<>();
        openDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();

        int indexId = cursor.getColumnIndex(ID);
        int indexSmall = cursor.getColumnIndex(SMALL);
        int indexUrl = cursor.getColumnIndex(URL);

        while (cursor.isAfterLast() == false) {
            int id = cursor.getInt(indexId);
            String small = cursor.getString(indexSmall);
            String url = cursor.getString(indexUrl);;
            ItemGridView itemGridView = new ItemGridView(small,url);
            arrItem.add(itemGridView);
            cursor.moveToNext();
        }
        closeDatabase();
        return arrItem;
    }

    public long insert(ItemGridView itemGridView) {
        ContentValues values = new ContentValues();
        values.put(SMALL, itemGridView.getSmallImg());
        values.put(URL, itemGridView.getImgUrl());
        openDatabase();
        long id = database.insert(TABLE_NAME, null, values);
        closeDatabase();
        return id;
    }


}
