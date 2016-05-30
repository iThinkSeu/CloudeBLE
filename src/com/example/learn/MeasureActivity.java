/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.learn;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * For a given BLE device, this Activity provides the user interface to connect,
 * display data, and display GATT services and characteristics supported by the
 * device. The Activity communicates with {@code BluetoothLeService}, which in
 * turn interacts with the Bluetooth LE API.
 */
public class MeasureActivity extends Activity {
	private final static String TAG = MeasureActivity.class.getSimpleName();

	public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
	public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

	private TextView mConnectionState;
	private TextView mDataField;
	private String mDeviceName;
	private String mDeviceAddress;
	private ExpandableListView mGattServicesList;
	private BluetoothLeService mBluetoothLeService;
	private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
	private boolean mConnected = false;
	private BluetoothGattCharacteristic mNotifyCharacteristic = null;

	private final String LIST_NAME = "NAME";
	private final String LIST_UUID = "UUID";
	private final String DEFAULT_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb";
	// byte[] WriteBytes = null;
	byte[] WriteBytes = new byte[20];
	// Code to manage Service lifecycle.
	private Button btnSend;
	private Button btnReturn;
	
	//电压实时测量
	private int currentV;
	private Paint mPaint;
	private Paint paint;
	
	//曲线
    /** The main dataset that includes all the series that go into a chart. */
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    /** The main renderer that includes all the renderers customizing a chart. */
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    /** The most recently added series. */
    private TimeSeries mCurrentSeries;
    /** The most recently created renderer, customizing the current series. */
    private XYSeriesRenderer mCurrentRenderer;
    /** The chart view that displays the data. */
    private GraphicalView mChartView;
    
    private static final long HOUR = 3600 * 1000;

    private static final long DAY = HOUR * 24;
    
	private final ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName,
				IBinder service) {
			mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
					.getService();
			if (!mBluetoothLeService.initialize()) {
				Log.e(TAG, "Unable to initialize Bluetooth");
				finish();
			}
			System.out.println("enter onServiceConnected");
			displayGattServices(mBluetoothLeService.getSupportedGattServices());
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mBluetoothLeService = null;
		}
	};

	// Handles various events fired by the Service.
	// ACTION_GATT_CONNECTED: connected to a GATT server.
	// ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
	// ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
	// ACTION_DATA_AVAILABLE: received data from the device. This can be a
	// result of read
	// or notification operations.
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
				mConnected = true;
				// updateConnectionState(R.string.connected);
				// invalidateOptionsMenu();
			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
					.equals(action)) {
				mConnected = false;
				// updateConnectionState(R.string.disconnected);
				// invalidateOptionsMenu();
				clearUI();
			} else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
					.equals(action)) {
				// Show all the supported services and characteristics on the
				// user interface.
				displayGattServices(mBluetoothLeService
						.getSupportedGattServices());
			} else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
				displayData(intent
						.getStringExtra(BluetoothLeService.EXTRA_DATA));
			}
		}
	};

	public static String bin2hex(String bin) {
		char[] digital = "0123456789ABCDEF".toCharArray();
		StringBuffer sb = new StringBuffer("");
		byte[] bs = bin.getBytes();
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(digital[bit]);
			bit = bs[i] & 0x0f;
			sb.append(digital[bit]);
		}
		return sb.toString();
	}

	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0) {
			throw new IllegalArgumentException("长度不是偶数");
		}
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		b = null;
		return b2;
	}

	private void clearUI() {
		mGattServicesList.setAdapter((SimpleExpandableListAdapter) null);
		mDataField.setText(R.string.no_data);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.measuremenu, menu);
		return true;
		}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) {
			case R.id.measure_item1:
				Toast.makeText(this, "You clicked measure item 1", Toast.LENGTH_SHORT).show();
				break;
			case R.id.measure_item2:
				Toast.makeText(this, "You clicked measure item 2", Toast.LENGTH_SHORT).show();
				break;
			default:
			}
		return true;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newmeasurement);
		
		final Intent intent = getIntent();
		// mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
		// mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

		// Sets up UI references.
		// ((TextView)
		// findViewById(R.id.device_address)).setText(mDeviceAddress);
		// mGattServicesList = (ExpandableListView)
		// findViewById(R.id.gatt_services_list);
		// mGattServicesList.setOnChildClickListener(servicesListClickListner);
		System.out.println("Create measure");
		//mDataField = (TextView) findViewById(R.id.lastDrinkWater);
		
		DrawVolumn(10);
		/*
		btnReturn = (Button) findViewById(R.id.MesureReturn);
		
		btnReturn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnSend = (Button) findViewById(R.id.SendTest);
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("send test");
				if (mNotifyCharacteristic != null) {
					WriteBytes[0] = '5';
					WriteBytes[1] = '9';
					mNotifyCharacteristic.setValue(WriteBytes);

					mBluetoothLeService
							.writeCharacteristic(mNotifyCharacteristic);
				} else {
					System.out.println("mNotifyCharacteristic is null");
				}
			}
		});
		*/
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		startService(gattServiceIntent);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
		
		//画曲线
		// set some properties on the main renderer
		mRenderer.setYLabels(15);
	    mRenderer.setApplyBackgroundColor(true);
	    //mRenderer.setBackgroundColor(Color.rgb(0x37, 0x3d, 0x49));
	    //mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
	    mRenderer.setBackgroundColor(Color.rgb(0xF2, 0xF2, 0xF2));
	    mRenderer.setAxisTitleTextSize(16);
	    mRenderer.setChartTitleTextSize(20);
	    mRenderer.setLabelsTextSize(15);
	    mRenderer.setLegendTextSize(15);
	    //mRenderer.setMarginsColor(Color.rgb(0x8c, 0x8c, 0x8c));
		mRenderer.setMarginsColor(Color.argb(0x00, 0x01, 0x01, 0x01));
	    //mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
	    //mRenderer.setMarginsColor(Color.rgb(0xea, 0xea, 0xea));
	    //mRenderer.setZoomButtonsVisible(true);
	    mRenderer.setPointSize(5);

	    mRenderer.setShowLegend(true);
	    mRenderer.setChartTitle("实时曲线");  
	    mRenderer.setXTitle("横坐标");  
	    mRenderer.setYTitle("纵坐标");  
	    mRenderer.setXLabels(10);
	    mRenderer.setXLabelsColor(Color.BLACK);
	    mRenderer.setYLabels(12);
	    mRenderer.setYLabelsColor(0, Color.BLACK);
	    mRenderer.setShowGrid(true);
	    //mRenderer.setAxesColor(Color.CYAN); ，坐标轴颜色
	    
	    
	    String seriesTitle = "Series " + (mDataset.getSeriesCount() + 1);
	    // create a new series of data
	    TimeSeries series = new TimeSeries(seriesTitle);

	    mDataset.addSeries(series);
	    mCurrentSeries = series;
	    // create a new renderer for the new series
	    XYSeriesRenderer renderer = new XYSeriesRenderer();
	    mRenderer.addSeriesRenderer(renderer);
	    // set some renderer properties
	    renderer.setPointStyle(PointStyle.CIRCLE);
	    renderer.setColor(Color.BLACK);
	    renderer.setFillPoints(true);
	    renderer.setDisplayChartValues(true);
	    renderer.setDisplayChartValuesDistance(10);
	    mCurrentRenderer = renderer;
	    
	    
	    long now = Math.round(new Date().getTime());
	    Date[] dates = new Date[4];
	    for(int j=0;j<4;j++)
	    {
	    	dates[j] = new Date(now - (4 - j) * HOUR);
	    }
	    /*
	    
	    mCurrentSeries.add(dates[0], 2.0);
	    mCurrentSeries.add(dates[1], 6.0);
	    mCurrentSeries.add(dates[2], 7.0);
	    mCurrentSeries.add(dates[3], 7.05);
	    
	    */
	    if (mChartView == null) {
	        LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
	        //mChartView = ChartFactory.getLineChartView(this, mDataset, mRenderer);
	        mChartView = ChartFactory.getTimeChartView(this, mDataset, mRenderer, "M/d HH:mm a");
	        // enable the chart click events
	        mRenderer.setClickEnabled(true);
	        mRenderer.setSelectableBuffer(10);
	      
	        mChartView.setOnClickListener(new View.OnClickListener() {
	          public void onClick(View v) {
	            // handle the click event on the chart
	            SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
	            Toast.makeText(MeasureActivity.this, "You clicked measure item 1", Toast.LENGTH_SHORT).show();
	            Intent intent = new Intent();
				// intent = (new SensorValuesChart()).execute(getBaseContext());
				intent.setClass(MeasureActivity.this, FrequenceMeasureActivity.class);
				startActivity(intent);
	          }
	        });
	        
	        layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
	            LayoutParams.FILL_PARENT));
	        layout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(MeasureActivity.this, "You clicked measure item 1", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					// intent = (new SensorValuesChart()).execute(getBaseContext());
					intent.setClass(MeasureActivity.this, FrequenceMeasureActivity.class);
					startActivity(intent);
					
				}
			});
	      } else {
	       
	        mChartView.repaint();
	      }
	  
	    
	      mChartView.repaint();
	    

	}
	


	private void DrawVolumn(int volumn) {
		currentV = volumn;
		int offset = 120;//50
		int color = 0;
		if (volumn > 600) {
			color = Color.rgb(0x19, 0x19, 0x70);
		} else if (volumn > 400) {
			color = Color.rgb(0x00, 0x00, 0xff);
		} else if (volumn > 30) {
			color = Color.rgb(0x41, 0x69, 0xE1);
		} else if (volumn > 200) {
			color = Color.rgb(0x87, 0xCe, 0xeb);
		} else if (volumn > 100) {
			color = (Color.rgb(0xb0, 0xe0, 0xe6));
		} else if (volumn > 50) {
			color = (Color.WHITE);
		} else {
			color = (Color.WHITE);
		}
		ImageView iv = (ImageView) findViewById(R.id.measureVolumn);
		iv.clearAnimation();
		// iv.setBackground(Color.rgb(37, 3d, 49));
		// System.out.println("imageview width" + iv.getWidth());
		mPaint = new Paint();
		Canvas canvas = new Canvas();
		// 创建一张空白图片
		Bitmap baseBitmap = Bitmap.createBitmap(480, 500,
				Bitmap.Config.ARGB_8888);
		// 创建一张画布
		canvas = new Canvas(baseBitmap);
		// 画布背景为灰色
		// canvas.drawColor(Color.WHITE);
		// 创建画笔
		paint = new Paint();
		// 画笔颜色为红色
		paint.setColor(Color.RED);
		// 宽度5个像素
		paint.setStrokeWidth(5);
		// 先将灰色背景画上
		canvas.drawBitmap(baseBitmap, new Matrix(), paint);
		iv.setImageBitmap(baseBitmap);
		int centre = 400 / 2; // 获取圆心的x坐标
		int mCircleWidth = 15;
		int radius = centre - mCircleWidth / 2-30;// 半径
		paint.setStrokeWidth(mCircleWidth); // 设置圆环的宽度
		paint.setAntiAlias(true); // 消除锯齿
		paint.setStyle(Paint.Style.STROKE); // 设置空心
		RectF oval = new RectF(centre - radius + 50, centre - radius + 60,
				centre + radius + 50, centre + radius + 60); // 用于定义的圆弧的形状和大小的界限
		paint.setColor(Color.LTGRAY); // 设置圆环的颜色
		canvas.drawCircle(centre + 50, centre + 60, radius, paint); // 画出圆环
	    paint.setColor(Color.rgb(0x6d, 0xce, 0x3f)); // 设置圆环的颜色
		paint.setColor(color);
		canvas.drawArc(oval, -90, (int) (volumn * 1.0 / 800 * 360), false,
				paint); // 根据进度画圆弧
		Paint fontPaint = new Paint();
		String familyName = "微软雅黑";
		Typeface font = Typeface.create(familyName, Typeface.BOLD);
		fontPaint.setColor(Color.DKGRAY);
		fontPaint.setTextSize(52);
		canvas.drawText("直流电压", 150, 50, fontPaint);
		fontPaint.setColor(Color.rgb(0x07, 0xaF, 0xd9));
		fontPaint.setTypeface(font);
		if (volumn >= 1000) {
			fontPaint.setTextSize(90);
			canvas.drawText(volumn/100 + ".00", 130, 300, fontPaint);
			fontPaint.setTextSize(30);
			canvas.drawText("KV", 300, 350, fontPaint);
		} else if (volumn < 1000) {
			fontPaint.setTextSize(90);
			canvas.drawText(volumn/100 + ".00", 130, 300, fontPaint);
			fontPaint.setTextSize(30);
			canvas.drawText("KV", 300, 350, fontPaint);
		} 
		iv.setImageBitmap(baseBitmap);
	}
	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		if (mBluetoothLeService != null) {
			// final boolean result =
			// mBluetoothLeService.connect(mDeviceAddress);
			// Log.d(TAG, "Connect request result=" + result);
		}
		 Log.d("ithinker","mChartView2");
		 if (mChartView == null) {
			 Log.d("ithinker","mChartView");
		      LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
		      mChartView = ChartFactory.getLineChartView(this, mDataset, mRenderer);
		      // enable the chart click events
		      mRenderer.setClickEnabled(true);
		      mRenderer.setSelectableBuffer(10);
		      mChartView.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		          // handle the click event on the chart
		          SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
		          if (seriesSelection == null) {
		            Toast.makeText(MeasureActivity.this, "No chart element", Toast.LENGTH_SHORT).show();
		          } else {
		            // display information of the clicked point
		            Toast.makeText(
		            		MeasureActivity.this,
		                "Chart element in series index " + seriesSelection.getSeriesIndex()
		                    + " data point index " + seriesSelection.getPointIndex() + " was clicked"
		                    + " closest point value X=" + seriesSelection.getXValue() + ", Y="
		                    + seriesSelection.getValue(), Toast.LENGTH_SHORT).show();
		          }
		        }
		      });
		      layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		 }else{
	          mChartView.repaint();
		 }
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mGattUpdateReceiver);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(mServiceConnection);
		mBluetoothLeService = null;
	}

	/*显示的变量定义*/
	private int recSize = 0;

	private String recData = "";
	private int dataType = 0;// 0 温度；1体积
	private int dataNum = 0;
	private String dataSave = "";
	
	private void displayData(String data) 
	{
		dataSave = "";
		dataSave += data;
		System.out.println("receive raw data " + data);
		System.out.println("receive raw dataSave " + dataSave);
		
		if (dataSave.equals("\0")) {
			if (data.length() == 1)
				System.out.println("remove data 0" + data);
			return;
		}
		
		if (dataSave != null)
		{
			// System.out.println(dataSave);
			dataSave.trim();
			recSize += dataSave.length();
			System.out.println(recSize);
			//mDataField.setText(data);
			recData = "";
			String convertData = "";
			for (int i = 0; i < dataSave.indexOf("\n"); i++) 
			{
				recData += dataSave.charAt(i);
			}
			
			System.out.println("receive raw recData " + recData);
			if (recData.contains("t") || recData.contains("T")) {// 温度
				for (int i = 0; i < recData.length(); i++) {
					if (recData.charAt(i) >= 48 && recData.charAt(i) <= 57) {
						convertData += dataSave.charAt(i);
					}
				}
				Log.d("ithinker","receive 温度 data" + convertData);
				//mDataField.setText("高压表电流值 "+convertData);
				DrawVolumn(Integer.parseInt(convertData));
				
			    long now = Math.round(new Date().getTime()/DAY)*DAY;
			    Log.d("ithinker","time"+now);
			    mCurrentSeries.add(now, (float)Integer.parseInt(convertData));
			    mChartView.repaint();
			} 
		}	
      
		/*
		if (data != null) {
			mDataField.setText(data);
		}
		*/
	}

	// Demonstrates how to iterate through the supported GATT
	// Services/Characteristics.
	// In this sample, we populate the data structure that is bound to the
	// ExpandableListView
	// on the UI.
	private void displayGattServices(List<BluetoothGattService> gattServices) {
		if (gattServices == null) {
			System.out.println("gattServices is null");
			return;
		}
		System.out.println("gattServices size" + gattServices.size());
		String uuid = null;
		ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
		ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();
		mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

		// Loops through available GATT Services.
		for (BluetoothGattService gattService : gattServices) {
			List<BluetoothGattCharacteristic> gattCharacteristics = gattService
					.getCharacteristics();
			ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();

			// Loops through available Characteristics.
			for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
				if (gattCharacteristic.getUuid().toString()
						.equals(DEFAULT_UUID)) {
					int charaProp = gattCharacteristic.getProperties();
					if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
						mNotifyCharacteristic = gattCharacteristic;
						// mBluetoothLeService.setCharacteristicNotification(
						// gattCharacteristic, true);
						System.out.println("addd");
						byte[] value = new byte[20];
						value[0] = (byte) 0x1;
						value[1] = '\0';
						WriteBytes = new byte[4];
						WriteBytes[0] = 'O';
						WriteBytes[1] = 'K';
						WriteBytes[2] = '\0';
						gattCharacteristic.setValue(WriteBytes);

						mBluetoothLeService
								.writeCharacteristic(gattCharacteristic);
					}
				}
			}
		}

	}

	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter
				.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		return intentFilter;
	}
}
