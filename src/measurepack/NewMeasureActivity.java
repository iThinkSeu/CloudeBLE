package measurepack;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import iThinkerChartFactory.CreateLineChart;
import iThinkerChartFactory.circleFactory;

import mainactivity.newMainActivity;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.SeriesSelection;

import util.DimensionUtils;

import com.example.learn.BluetoothLeService;
import com.example.learn.MeasureActivity;
import com.example.learn.MeasureTestActivityNew;
import com.example.learn.R;
import com.example.learn.XYChartBuilder;
import com.example.learn.testMeasureActivity;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class NewMeasureActivity  extends Activity{
	
	private int gridbutton_select = 0;
	
	//网格按键  gridbutton
	private Button actime_button;
	private Button dctime_button;
	private Button accurrent_button;
	private Button dccurrent_button;
	private Button acvoltage_button;
	private Button dcvoltage_button;
	//网格按键背景
	private RelativeLayout actime_gridbg;
	private RelativeLayout dctime_gridbg;
	private RelativeLayout accurrent_gridbg;
	private RelativeLayout dccurrent_gridbg;
	private RelativeLayout acvoltage_gridbg;
	private RelativeLayout dcvoltage_gridbg;
	
	//BLE
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
	byte[] WriteBytes = new byte[20];
	
	//工具函数,图像
	private circleFactory myCircle = new circleFactory();
	private ImageView iv;
	private CreateLineChart myLineChart = new CreateLineChart();
	private GraphicalView mChartView;
	
	//设置按键
	private ImageView moreSetting;
	LinearLayout mLinear;
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
				//displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
				ResponseDisplay(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aty_measure);
		mLinear = (LinearLayout) findViewById(R.id.MeasureLayout);
		mLinear.setBackgroundResource(R.drawable.cup5);
   
		LinearLayout meaLinear = (LinearLayout) findViewById(R.id.chart);
	    
		myLineChart = new CreateLineChart();
		mChartView = myLineChart.oneLineChart_rms(this);
		meaLinear.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		myLineChart.addSeriesData(200);
		mChartView.repaint();
		iv = (ImageView) findViewById(R.id.measureVolumn);
		myCircle.DrawVolumn(iv, (float) 9.82);
		
		//开启蓝牙服务
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		startService(gattServiceIntent);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
		
		//网格按键背景初始化
	    actime_gridbg = (RelativeLayout) findViewById(R.id.actime_relativelayout);
	    dctime_gridbg = (RelativeLayout) findViewById(R.id.dctime_relativelayout);
	    accurrent_gridbg = (RelativeLayout) findViewById(R.id.accurrent_relativelayout);
	    dccurrent_gridbg = (RelativeLayout) findViewById(R.id.dccurrent_relativelayout);
	    acvoltage_gridbg = (RelativeLayout) findViewById(R.id.acvoltage_relativelayout);
	    dcvoltage_gridbg = (RelativeLayout) findViewById(R.id.dcvoltage_relativelayout);
	    
	    //按键设置
	    moreSetting = (ImageView)findViewById(R.id.aty_info_more);
	    
	    moreSetting.setOnClickListener(new OnClickListener(){
	    	@Override
	    	public void onClick(View v)
	    	{
	    		final Dialog dialog = new Dialog(NewMeasureActivity.this,R.style.DialogSlideAnim);
                View content = LayoutInflater.from(NewMeasureActivity.this).inflate(R.layout.aty_info_option,mLinear,false);
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(v.getId() == R.id.aty_info_option_edit_info){
                            Intent i = new Intent(NewMeasureActivity.this, newMainActivity.class);
                            startActivity(i);
                        	
                        }
                        dialog.dismiss();
                    }
                };
                content.findViewById(R.id.aty_info_option_cancel).setOnClickListener(listener);
                content.findViewById(R.id.aty_info_option_edit_info).setOnClickListener(listener);
                dialog.setContentView(content);

                WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.BOTTOM | Gravity.START;
                wmlp.x = 0;   //x position
                wmlp.y = 0;   //y position
                wmlp.width = DimensionUtils.getDisplay(NewMeasureActivity.this).widthPixels;
                dialog.show();
	    	}
	    });
	    //按键绑定事件  
	    //直流电压
	    //dcvoltage_button = (Button) findViewById(R.id.dcvoltage_button);
	    dcvoltage_gridbg.setOnClickListener(new OnClickListener(){
	    	@Override
	    	public void onClick(View v)
	    	{
	    		//myLineChart.addSeriesData(100);
	    		//mChartView.repaint();
	    		//set_selectbutton_bg(1);
	    	}
	    });
	    //交流电压
	    //acvoltage_button = (Button) findViewById(R.id.acvoltage_button);
	    acvoltage_gridbg.setOnClickListener(new OnClickListener(){
	    	@Override
	    	public void onClick(View v)
	    	{
	    		myLineChart.clearSeriesData();
	    		mChartView.repaint();
	    		//set_selectbutton_bg(2);
	    	}
	    });
	    //直流电流
	    //dccurrent_button = (Button) findViewById(R.id.dccurrent_button);
	    dccurrent_gridbg.setOnClickListener(new OnClickListener(){
	    	@Override
	    	public void onClick(View v)
	    	{

	    		//set_selectbutton_bg(3);
	    	}
	    });
	    //交流电流
	    //accurrent_button = (Button)findViewById(R.id.accurrent_button);
	    accurrent_gridbg.setOnClickListener(new OnClickListener(){
	    	@Override
	    	public void onClick(View v)
	    	{
	    	
	    		//set_selectbutton_bg(4);
	    	}
	    	
	    });
	    
	    //直流时间
	    //dctime_button = (Button)findViewById(R.id.dctime_button);
	    dctime_gridbg.setOnClickListener(new OnClickListener(){
	    	@Override
	    	public void onClick(View v)
	    	{
	    	
	    		//set_selectbutton_bg(5);
	    	}
	    	
	    });
	    //交流时间
	    //actime_button = (Button)findViewById(R.id.actime_button);
	    actime_gridbg.setOnClickListener(new OnClickListener(){
	    	@Override
	    	public void onClick(View v)
	    	{
	    	
				Intent intent = new Intent();
				intent.setClass(NewMeasureActivity.this, MeasureTestActivityNew.class);
				startActivity(intent);
				
	    		//set_selectbutton_bg(6);
	    	}
	    	
	    });     
	    
	    
	}
	 
	  private void set_selectbutton_bg(int select_int)
	  {
		  int alpha = 60;
		  switch(select_int){
		  	case 1:
		  		//dcvoltage_select
	    	    dcvoltage_gridbg.setBackgroundResource(R.drawable.grid_bgselect);
	    	    dcvoltage_gridbg.getBackground().setAlpha(alpha);
	    	    //将其他背景改成原来的
	      	    acvoltage_gridbg.setBackgroundResource(R.drawable.grid_bg);
	      	    dccurrent_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    accurrent_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    dctime_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    actime_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    break;

		  	case 2:
		  		//acvoltage
	    	    acvoltage_gridbg.setBackgroundResource(R.drawable.grid_bgselect);
	    	    acvoltage_gridbg.getBackground().setAlpha(alpha);
	    	    //将其他背景改成原来的
	      	    dcvoltage_gridbg.setBackgroundResource(R.drawable.grid_bg);
	      	    dccurrent_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    accurrent_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    dctime_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    actime_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    break;
	    	    
		  	case 3:
		  		//dccurrent
	    	    dccurrent_gridbg.setBackgroundResource(R.drawable.grid_bgselect);
	    	    dccurrent_gridbg.getBackground().setAlpha(alpha);
	    	    //将其他背景改成原来的
	    	    dcvoltage_gridbg.setBackgroundResource(R.drawable.grid_bg);
	      	    acvoltage_gridbg.setBackgroundResource(R.drawable.grid_bg);
	      	    //dccurrent_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    accurrent_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    dctime_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    actime_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    break;
		  	case 4:
		  		//accurrent_select
	    	    accurrent_gridbg.setBackgroundResource(R.drawable.grid_bgselect);
	    	    accurrent_gridbg.getBackground().setAlpha(alpha);
	    	    //将其他背景改成原来的
	    	    dcvoltage_gridbg.setBackgroundResource(R.drawable.grid_bg);
	      	    acvoltage_gridbg.setBackgroundResource(R.drawable.grid_bg);
	      	    dccurrent_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    //accurrent_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    dctime_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    actime_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    break;
		  	case 5:
		  		//dctime
	    	    dctime_gridbg.setBackgroundResource(R.drawable.grid_bgselect);
	    	    dctime_gridbg.getBackground().setAlpha(alpha);
	    	    //将其他背景改成原来的
	    	    dcvoltage_gridbg.setBackgroundResource(R.drawable.grid_bg);
	      	    acvoltage_gridbg.setBackgroundResource(R.drawable.grid_bg);
	      	    dccurrent_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    accurrent_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    //dctime_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    actime_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    break;
		  	case 6:
		  		//actime
	    	    actime_gridbg.setBackgroundResource(R.drawable.grid_bgselect);
	    	    actime_gridbg.getBackground().setAlpha(alpha);
	    	    //将其他背景改成原来的
	    	    dcvoltage_gridbg.setBackgroundResource(R.drawable.grid_bg);
	      	    acvoltage_gridbg.setBackgroundResource(R.drawable.grid_bg);
	      	    dccurrent_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    accurrent_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    dctime_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    //actime_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    break;
		  }
	  } 
	  

	  @Override
	  protected void onResume() {
			super.onResume();
			registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
			if (mBluetoothLeService != null) {
				// final boolean result =
				// mBluetoothLeService.connect(mDeviceAddress);
				Log.d("ithinker", "Connect request result=");
			}
			Log.d("ithinker", "Connect request result22");
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
			Log.d("ithinker","receive raw data " + data);
			Log.d("ithinker","receive raw dataSave " + dataSave);
			
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
				Log.d("ithinker","receive raw dataSave2 " + dataSave);
				//mDataField.setText(data);
				recData = "";
				String convertData = "";
				for (int i = 0; i < dataSave.indexOf("\n"); i++) 
				{
					recData += dataSave.charAt(i);
				}
				Log.d("ithinker","receive raw recData " + recData);
				System.out.println("receive raw recData " + recData);
				if (recData.contains("t") || recData.contains("T")) {// 温度
					for (int i = 0; i < recData.length(); i++) {
						if (recData.charAt(i) >= 48 && recData.charAt(i) <= 57) {
							convertData += dataSave.charAt(i);
						}
					}
					Log.d("ithinker","receive 温度 data" + convertData);
					//mDataField.setText("高压表电流值 "+convertData);
					myCircle.DrawVolumn(iv, (float) Integer.parseInt(convertData));
					myLineChart.addSeriesData((float) Integer.parseInt(convertData));
		    		mChartView.repaint();
				} 
			}	
	      
			/*
			if (data != null) {
				mDataField.setText(data);
			}
			*/
		}
		
		private String recFrame = "";
		
		private void ResponseDisplay(String response)
		{
			recFrame += response;
			String sub;
			
			Log.d("ithinker", "recFrame in "+recFrame);
			Log.d("ithinker", "recTrue"+(recFrame.contains("<")&&recFrame.contains(">")));
			
			if(recFrame.contains("<")&&recFrame.contains(">"))
			{
				sub = recFrame.substring(recFrame.indexOf("<") + 1, recFrame.indexOf(">"));
				String[] sArray = sub.split("#");
				
				Log.d("ithinker", "recFrame NEW"+recFrame);
				Log.d("ithinker", "sub"+sub);
				Log.d("ithinker", "s0="+sArray[0]+" s1="+sArray[1]+ " s2="+sArray[2]);
				
				if(sArray[0].equals("data"))
				{
					if(sArray[1].equals("VDC"))
					{
						Log.d("ithinker", "VDC"+sArray[2]);
						myCircle.DrawVolumn(iv, (float) Float.parseFloat(sArray[2]));
						//myLineChart.addSeriesData((float) Float.parseFloat(sArray[2]));
			    		//mChartView.repaint();
						
						
					}else if(sArray[1].equals("VAC"))
					{
						Log.d("ithinker", "VAC"+sArray[2]);
						myLineChart.addSeriesData((float) Float.parseFloat(sArray[2]));
			    		mChartView.repaint();
					}
					
				}
				//清空接收字符串
				recFrame = "";
				//Log.d("ithinker", "clear recFrame");
			}
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

							mBluetoothLeService.writeCharacteristic(gattCharacteristic);
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
