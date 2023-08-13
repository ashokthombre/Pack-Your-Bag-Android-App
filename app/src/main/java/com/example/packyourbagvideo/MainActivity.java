package com.example.packyourbagvideo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;


import com.example.packyourbagvideo.Adapter.Adapter;
import com.example.packyourbagvideo.Data.AppData;
import com.example.packyourbagvideo.Database.RoomDB;
import com.example.packyourbagvideo.constants.MConstants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<String> titles;
    List<Integer>images;
    Adapter adapter;

  RoomDB database;
    private static final int TIME_INTERVAL=2000;
    private long mBackPressed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyclerView);
        getSupportActionBar().hide();

        addAddTitles();
        addAllImages();
        persistAppData();
        database=RoomDB.getInstance(this);
        System.out.println("---------------------->"+database.mainDao().getALlSelected(false).get(0).getItemname());

        adapter=new Adapter(this,titles,images,MainActivity.this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);


        Intent intent=getIntent();
        if (intent.getBooleanExtra("Exit",false))
        {
            finish();
            System.exit(1);
        }


    }

    public  void addAddTitles()
    {
        titles=new ArrayList<>();
        titles.add(MConstants.BASIC_NEEDS_CAMEL_CASE);
        titles.add(MConstants.CLOTHING_CAMEL_CASE);
        titles.add(MConstants.PERSONAL_CARE_CAMEL_CASE);
        titles.add(MConstants.BABY_NEEDS_CAMEL_CASE);
        titles.add(MConstants.HEALTH_CAMEL_CASE);
        titles.add(MConstants.TECHNOLOGY_CAMEL_CASE);
        titles.add(MConstants.FOOD_CAMEL_CASE);
        titles.add(MConstants.BEACH_SUPPLIES_CAMEL_CASE);
        titles.add(MConstants.CAR_SUPPLIES_CAMEL_CASE);
        titles.add(MConstants.NEEDS_CAMEL_CASE);
        titles.add(MConstants.MY_LIST_CAMEL_CASE);
        titles.add(MConstants.MY_SELECTIONS_CAMEL_CASE);
    }
    public  void addAllImages()
    {
        images=new ArrayList<>();
        images.add(R.drawable.p1);
        images.add(R.drawable.p2);
        images.add(R.drawable.p3);
        images.add(R.drawable.p4);
        images.add(R.drawable.p5);
        images.add(R.drawable.p6);
        images.add(R.drawable.p7);
        images.add(R.drawable.p8);
        images.add(R.drawable.p9);
        images.add(R.drawable.p10);
        images.add(R.drawable.p11);
        images.add(R.drawable.p12);
    }

    @Override
    public void onBackPressed() {

        if (mBackPressed+TIME_INTERVAL>System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else
        {
            Toast.makeText(this, "Tap back button in order to exit", Toast.LENGTH_SHORT).show();
        }
        mBackPressed=System.currentTimeMillis();

    }
    private void persistAppData()
    {
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor=prefs.edit();

        database= RoomDB.getInstance(this);
        AppData appData=new AppData(database);
        int last=prefs.getInt(AppData.LAST_VERSION,0);

        if (!prefs.getBoolean(MConstants.FIRST_TIME_CAMEL_CASE,false))
        {
            appData.persistAllData();
            editor.putBoolean(MConstants.FIRST_TIME_CAMEL_CASE,true);
            editor.commit();
        } else if (last<AppData.NEW_VERSION) {
            database.mainDao().deleteAllSystemItems(MConstants.SYSTEM_SMALL);
            appData.persistAllData();
            editor.putInt(AppData.LAST_VERSION,AppData.NEW_VERSION);
            editor.commit();
        }
    }
}