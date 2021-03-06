package com.example.learn;

import mainactivity.newMainActivity;
import measurepack.NewMeasureActivity;
import statisticscharline.testTableViewActivity;
import statisticscharline.twobarchart;

import com.example.learn.DeviceScanActivity;
import com.example.learn.MainActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	private ImageView imageviewTop;
	private RelativeLayout searchLayout, measureLayout, chartLayout,settingLayout;
	public static int BLEstate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        System.out.println("input!!!");
        imageviewTop = (ImageView) findViewById(R.id.mainImageView);
		imageviewTop.setBackgroundResource(R.drawable.voltagebai);
		
		searchLayout = (RelativeLayout) findViewById(R.id.SearchLayout);
		// imageview1 = (ImageView) findViewById(R.id.home_img_looks);
		searchLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("SearchLayout click");
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, DeviceScanActivity.class);
				startActivity(intent);
			}
		});
		
		measureLayout = (RelativeLayout) findViewById(R.id.MeasureLayout);
		measureLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("MeasureActivity click");
				Log.d("ithinker", "test log.d");
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, MeasureActivity.class);
				startActivity(intent);
			}
		});
		chartLayout = (RelativeLayout) findViewById(R.id.ChartLayout);
		chartLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("ithinker", "intent to two bar chart");
				/*
				Intent intent = new Intent();
				intent = (new SensorValuesChart()).execute(getBaseContext());
				startActivity(intent);
				*/
				Intent intent = new Intent();
				//intent.setClass(MainActivity.this, twobarchart.class);
				intent.setClass(MainActivity.this, newMainActivity.class);
				startActivity(intent);
			}
		});
		
		settingLayout = (RelativeLayout) findViewById(R.id.settingLayout);
		settingLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				// intent = (new SensorValuesChart()).execute(getBaseContext());
				intent.setClass(MainActivity.this, NewMeasureActivity.class);
				startActivity(intent);
			}
		});
    }
  
}
