package com.example.trung.onepiecewallpagers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import adapter.AdapterGridView;
import database.DatabaseUltil;
import model.ItemGridView;

public class FavActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DatabaseUltil databaseUltil;
    private ArrayList<ItemGridView> arrItem = new ArrayList<>();
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        databaseUltil = new DatabaseUltil(this);
        arrItem = databaseUltil.getData();
        initViews();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gridView = findViewById(R.id.gvImageFav);
        AdapterGridView adapterGridView = new AdapterGridView(this,R.layout.item_gridview,arrItem);
        gridView.setAdapter(adapterGridView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ItemGridView itemGridView = arrItem.get(i);
                String imageUrl = itemGridView.getImgUrl();
                Intent intent = new Intent(FavActivity.this,ViewImage.class);
                intent.putExtra("imgUrl",imageUrl);
                intent.putExtra("imgSmall",itemGridView.getSmallImg());
                startActivity(intent);
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
