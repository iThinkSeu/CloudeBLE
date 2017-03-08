package measurepack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import iThinkerChartFactory.CreateLineChart;
import iThinkerChartFactory.circleFactory;

import mainactivity.newMainActivity;
import mainactivity.paraCorrectionActivity;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.SeriesSelection;
import org.json.JSONObject;

import util.DimensionUtils;
import util.OkHttpUtils;
import util.StrUtils;

import com.example.learn.BluetoothLeService;
import com.example.learn.MeasureActivity;
import com.example.learn.MeasureTestActivityNew;
import com.example.learn.R;
import com.example.learn.SampleGattAttributes;
import com.example.learn.XYChartBuilder;
import com.example.learn.testMeasureActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
	
	//6个参数显示
	TextView error;
	
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
	private BluetoothGattCharacteristic mWriteCharacteristic = null;

	private final String LIST_NAME = "NAME";
	private final String LIST_UUID = "UUID";
	private final String DEFAULT_UUID = SampleGattAttributes.DEFULT_UUID;
	private final String WRITE_UUID = SampleGattAttributes.WRITE_UUID;
	byte[] WriteBytes = new byte[20];
	
	//工具函数,图像
	private circleFactory myCircle = new circleFactory();
	private ImageView iv;
	private CreateLineChart myLineChart = new CreateLineChart();
	private GraphicalView mChartView;
	
	//设置按键
	private ImageView moreSetting;
	LinearLayout mLinear;
	
	//定时器
	private int dalayms = 1000;
    Handler handler=new Handler();  
    //定时任务
    
    Runnable runnable=new Runnable() {  
        @Override  
        public void run() {  
            // TODO Auto-generated method stub  
            //要做的事情  
        	timerSenderToBLE();
            handler.postDelayed(this, dalayms); 
        }  
    };  
    
    //timer定时器
    Timer timer = new Timer();
    //timer.schedule(new MyTask(),1000,2000);
    

    private boolean start_stop_flag = false;
    private String measure_mode = "VDC";
    
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
	  protected void onResume() {
			super.onResume();
			Log.d("ithinker", "Connect request result22");
			System.out.println("test onResume");
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
			handler.removeCallbacks(runnable);   
		}
		
	
	
	 @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aty_measure);
		mLinear = (LinearLayout) findViewById(R.id.MeasureLayout);
		mLinear.setBackgroundResource(R.drawable.cup5);
		bindview();
		error.setText("0.223KV");
		
		LinearLayout meaLinear = (LinearLayout) findViewById(R.id.chart);
	    
		myLineChart = new CreateLineChart();
		mChartView = myLineChart.oneLineChart_rms(this);
		meaLinear.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		myLineChart.addSeriesData(200);
		mChartView.repaint();
		iv = (ImageView) findViewById(R.id.measureVolumn);
		myCircle.DrawVolumn(iv, (float) 12.307,"VAC");
		
		//开启蓝牙服务
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		startService(gattServiceIntent);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
		
	    
	    
	}
	 
	  private void Senddata(String str)
	  {
		  if (mWriteCharacteristic != null) {
				mWriteCharacteristic.setValue((byte[]) str.getBytes());
				mBluetoothLeService
						.writeCharacteristic(mWriteCharacteristic);
			} else {
				System.out.println("mWriteCharacteristic is null");
			}
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
	  
	  private void bindview()
	  {
		    error = (TextView) findViewById(R.id.error_value);
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
		    		AlertDialog.Builder dialog=new AlertDialog.Builder(NewMeasureActivity.this);
		      		dialog.setTitle("确认切换模式吗?");
		            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){
		                 @Override
		                 public void onClick(DialogInterface dialog,int which){
		 		    		//////
		 		    		myLineChart.clearSeriesData();
				    		mChartView.repaint();
				    		
				    		measure_mode = "VDC";
				    		handler.removeCallbacks(runnable);
				    		sleepTread(20);
				    		Senddata("CONF:VOLT DC#");
				    		handler.postDelayed(runnable, dalayms);//每两秒执行一次runnable.    	 	
		                  }
		            });
	                dialog.setNegativeButton("取消", null);
	                dialog.show();
		    		//

		    	}
		    });
		    //交流电压
		    //acvoltage_button = (Button) findViewById(R.id.acvoltage_button);
		    acvoltage_gridbg.setOnClickListener(new OnClickListener(){
		    	@Override
		    	public void onClick(View v)
		    	{
		    		AlertDialog.Builder dialog=new AlertDialog.Builder(NewMeasureActivity.this);
		      		dialog.setTitle("确认切换模式吗?");
		            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){
		                 @Override
		                 public void onClick(DialogInterface dialog,int which){
		 		    		//////
		 		    		myLineChart.clearSeriesData();
				    		mChartView.repaint();
				    		
				    		measure_mode = "VAC";
				    		handler.removeCallbacks(runnable);
				    		sleepTread(20);
				    		Senddata("CONF:VOLT AC#");
				    		sleepTread(20);
				    		Senddata("CONF:VOLT AC#");
				    		handler.postDelayed(runnable, dalayms);//每两秒执行一次runnable.          	 	
		                  }
		            });
	                dialog.setNegativeButton("取消", null);
	                dialog.show();
		    		////
		    	}
		    });
		    //直流电流
		    //dccurrent_button = (Button) findViewById(R.id.dccurrent_button);
		    dccurrent_gridbg.setOnClickListener(new OnClickListener(){
		    	@Override
		    	public void onClick(View v)
		    	{
		    		
		    		AlertDialog.Builder dialog=new AlertDialog.Builder(NewMeasureActivity.this);
		      		dialog.setTitle("确认切换模式吗?");
		            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){
		                 @Override
		                 public void onClick(DialogInterface dialog,int which){
		 		    		//////
		 		    		myLineChart.clearSeriesData();
				    		mChartView.repaint();
				    		measure_mode = "IDC";
				    		handler.removeCallbacks(runnable);
				    		sleepTread(20);
				    		Senddata("CONF:CURR DC#");
				    		sleepTread(20);
				    		Senddata("CONF:CURR DC#");
				    		handler.postDelayed(runnable, dalayms);//每两秒执行一次runnable.            	 	
		                  }
		            });
	                dialog.setNegativeButton("取消", null);
	                dialog.show();
		    		//

		    	}
		    });
		    //交流电流
		    //accurrent_button = (Button)findViewById(R.id.accurrent_button);
		    accurrent_gridbg.setOnClickListener(new OnClickListener(){
		    	@Override
		    	public void onClick(View v)
		    	{
		    		AlertDialog.Builder dialog=new AlertDialog.Builder(NewMeasureActivity.this);
		      		dialog.setTitle("确认切换模式吗?");
		            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){
		                 @Override
		                 public void onClick(DialogInterface dialog,int which){
		 		    		//////
		 		    		myLineChart.clearSeriesData();
				    		mChartView.repaint();
				    		measure_mode = "IAC";
				    		handler.removeCallbacks(runnable);
				    		sleepTread(20);
				    		Senddata("CONF:CURR AC#");
				    		sleepTread(20);
				    		Senddata("CONF:CURR AC#");
				    		handler.postDelayed(runnable, dalayms);//每两秒执行一次runnable.
		                	 	
		                  }
		            });
	                dialog.setNegativeButton("取消", null);
	                dialog.show();
		    	    //

		    	}
		    	
		    });
		    
		    //直流时间
		    //dctime_button = (Button)findViewById(R.id.dctime_button);
		    dctime_gridbg.setOnClickListener(new OnClickListener(){
		    	@Override
		    	public void onClick(View v)
		    	{
		    		AlertDialog.Builder dialog=new AlertDialog.Builder(NewMeasureActivity.this);
		      		dialog.setTitle("确认切换模式吗?");
		            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){
		                 @Override
		                 public void onClick(DialogInterface dialog,int which){
		 		    		//////
		 		    		myLineChart.clearSeriesData();
		 		    		mChartView.repaint();
		 		    		measure_mode = "VDC-T";
		 		    		handler.removeCallbacks(runnable);
		 		    		sleepTread(20);
		 		    		Senddata("CONF:TIME DC#");
		 		    		sleepTread(20);
		 		    		Senddata("CONF:TIME DC#");
		 		    		handler.postDelayed(runnable, dalayms);//每两秒执行一次runnable.
		                	 	
		                  }
		            });
	                dialog.setNegativeButton("取消", null);
	                dialog.show();
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
	  
	  
	  private void timerSenderToBLE()
	  {
			String str = "MEASure:VALue?#";
			switch(measure_mode)
			{
				case "VDC":str = "MEASure:VALue?#";break;
				case "VAC":str = "MEASure:VALue?#";break;
				case "IDC":str = "MEASure:VALue?#";break;
				case "IAC":str = "MEASure:VALue?#";break;
				default:str = "MEASure:VALue?#";break;
			}
			Senddata(str);
			//Log.d("ithinker", "timer function");
	  }
	  
	  private void timer_start_or_stop()
	  {
	  		if(start_stop_flag==true)
		    {
	  			handler.postDelayed(runnable, 2000);//每两秒执行一次runnable. 
					
			}else
			{
	    		handler.removeCallbacks(runnable);   
			}
	  }
	  
	  private void upload_Display_state()
	  {
		  
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
					myCircle.DrawVolumn(iv, (float) Integer.parseInt(convertData),"VDC");
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
			
			Log.d("ithinker", "recFrame in "+recFrame+recFrame.contains("\n"));
			recFrame+="\n";
			
			if(recFrame.contains("\n"))
			{
				Log.d("ithinker", "!!!!");
				recFrame = recFrame.trim();
				String[] sArray = recFrame.split(":");
				if(sArray.length!=2)
				{
					return;
				}
				String val = "";
				String data = sArray[1];
				data = data.trim();
				Log.d("ithinker", "!!!!sArray[0]=VDC.data="+data);
				switch(sArray[0])
				{
					case "VDC":
					{
						Log.d("ithinker", "!!!!sArray[0]=VDC.data="+data);
						val = "";
						for(int i=0;i<data.length();i++)
						{
							//判断是数字
							if(data.charAt(i)=='.'||(data.charAt(i)-'0'>=0&&data.charAt(i)-'0'<=9))
							{
								val+=data.charAt(i);
							}else
							{
								break;
							}
								
						}
						Log.d("ithinker", "val="+val);
						float value_VDC = Float.valueOf(val);
						BigDecimal b = new BigDecimal((double)value_VDC);  
						value_VDC = (float)b.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();  
						myCircle.DrawVolumn(iv, value_VDC,"VDC");
						myLineChart.addSeriesData(value_VDC);
			    		mChartView.repaint();
			    		commitdata("VDC",value_VDC);
			    		break;
					}
					case "VAC":
					{
						val = "";
						for(int i=0;i<data.length();i++)
						{
							//判断是数字
							if(data.charAt(i)=='.'||(data.charAt(i)-'0'>=0&&data.charAt(i)-'0'<=9))
							{
								val+=data.charAt(i);
							}else
							{
								break;
							}
								
						}
						
						float value_VAC = (float) Float.valueOf(val);
						BigDecimal b = new BigDecimal((double)value_VAC);  
						value_VAC = (float)b.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();  
						//Log.d("ithinker", "value_VAC"+value_VAC);
						myCircle.DrawVolumn(iv, value_VAC,"VAC");					
						float error_f=(float) (value_VAC-12.000);
						BigDecimal b_error = new BigDecimal((double)error_f);
						error_f = (float)b_error.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();  
						error.setText(error_f+"KV");
						myLineChart.addSeriesData(value_VAC);
			    		mChartView.repaint();
			    		commitdata("VAC",value_VAC);
			    		break;
					}
					case "IDC":
					{
						val = "";
						for(int i=0;i<data.length();i++)
						{
							//判断是数字
							if(data.charAt(i)=='.'||(data.charAt(i)-'0'>=0&&data.charAt(i)-'0'<=9))
							{
								val+=data.charAt(i);
							}else
							{
								break;
							}							
						}
						float value_IDC = (float) Float.valueOf(val);
						
						BigDecimal b = new BigDecimal((double)value_IDC);  
						value_IDC = (float)b.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();  
						myCircle.DrawVolumn(iv, value_IDC,"IDC");
						myLineChart.addSeriesData(value_IDC);
			    		mChartView.repaint();
			    		commitdata("IDC",value_IDC);
			    	
			    		break;
					}
					case "IAC":
					{
						val = "";
						for(int i=0;i<data.length();i++)
						{
							//判断是数字
							if(data.charAt(i)=='.'||(data.charAt(i)-'0'>=0&&data.charAt(i)-'0'<=9))
							{
								val+=data.charAt(i);
							}else
							{
								break;
							}		
						}
						
						float value_IAC = (float) Float.parseFloat(val);
					
						BigDecimal b = new BigDecimal((double)value_IAC);  
						value_IAC = (float)b.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();  
						//Log.d("ithinker", "value_VAC"+value_VAC);
						myCircle.DrawVolumn(iv, value_IAC,"IAC");
						myLineChart.addSeriesData(value_IAC);
			    		mChartView.repaint();
			    		commitdata("IAC",value_IAC);		
					}
					case "VDC-T":
					{
						val = "";
						for(int i=0;i<data.length();i++)
						{
							//判断是数字
							if(data.charAt(i)=='.'||(data.charAt(i)-'0'>=0&&data.charAt(i)-'0'<=9))
							{
								val+=data.charAt(i);
							}else
							{
								break;
							}		
						}
						float value_VDC_T = (float) Float.parseFloat(val);						
						BigDecimal b = new BigDecimal((double)value_VDC_T);  
						value_VDC_T = (float)b.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();  
						//Log.d("ithinker", "value_VAC"+value_VAC);
						myCircle.DrawVolumn(iv, value_VDC_T,"VDC-T");
						myLineChart.addSeriesData(value_VDC_T);
			    		mChartView.repaint();
			    		commitdata("VDC-T",value_VDC_T);	
					}	
				}
				
				
			}
			
			//Log.d("ithinker", "recTrue"+(recFrame.contains("<")&&recFrame.contains(">")));
			/*
			if(recFrame.contains("<")&&recFrame.contains(">"))
			{
				sub = recFrame.substring(recFrame.indexOf("<") + 1, recFrame.indexOf(">"));
				String[] sArray = sub.split("#");
				
				//Log.d("ithinker", "recFrame NEW"+recFrame);
				//Log.d("ithinker", "sub"+sub);
				//Log.d("ithinker", "s0="+sArray[0]+" s1="+sArray[1]+ " s2="+sArray[2]);
				
				if(sArray[0].equals("data"))
				{
					
					switch(sArray[1])
					{
						case "VDC":		
						{
							float value_VDC = (float) Float.parseFloat(sArray[2]);
							value_VDC = value_VDC/1000;
							BigDecimal b = new BigDecimal((double)value_VDC);  
							value_VDC = (float)b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();  
							myCircle.DrawVolumn(iv, value_VDC,"VDC");
							myLineChart.addSeriesData(value_VDC);
				    		mChartView.repaint();
				    		commitdata("VDC",value_VDC);
				    		Log.d("ithinker", "commit"+value_VDC);
				    		break;
						}
						case "VAC":
						{
							float value_VAC = (float) Float.parseFloat(sArray[2]);
							value_VAC = value_VAC/1000;
							BigDecimal b = new BigDecimal((double)value_VAC);  
							value_VAC = (float)b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();  
							//Log.d("ithinker", "value_VAC"+value_VAC);
							myCircle.DrawVolumn(iv, value_VAC,"VAC");
							myLineChart.addSeriesData(value_VAC);
				    		mChartView.repaint();
				    		commitdata("VAC",value_VAC);					
						}
						case "IDC":		
						{
							float value_IDC = (float) Float.parseFloat(sArray[2]);
							value_IDC = value_IDC/1000;
							BigDecimal b = new BigDecimal((double)value_IDC);  
							value_IDC = (float)b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();  
							myCircle.DrawVolumn(iv, value_IDC,"IDC");
							myLineChart.addSeriesData(value_IDC);
				    		mChartView.repaint();
				    		commitdata("IDC",value_IDC);
				    		Log.d("ithinker", "commit"+value_IDC);
				    		break;
						}
						case "IAC":
						{
							float value_IAC = (float) Float.parseFloat(sArray[2]);
							value_IAC = value_IAC/1000;
							BigDecimal b = new BigDecimal((double)value_IAC);  
							value_IAC = (float)b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();  
							//Log.d("ithinker", "value_VAC"+value_VAC);
							myCircle.DrawVolumn(iv, value_IAC,"IAC");
							myLineChart.addSeriesData(value_IAC);
				    		mChartView.repaint();
				    		commitdata("IAC",value_IAC);					
						}
					}
				}
				
				//清空接收字符串
				recFrame = "";
				//Log.d("ithinker", "clear recFrame");
			}
			*/
			recFrame = "";
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
							System.out.println("addd"+gattCharacteristic.getUuid().toString());
							
						}
					}
					
					if (gattCharacteristic.getUuid().toString()
							.equals(WRITE_UUID)) {
						int charaProp = gattCharacteristic.getProperties();
						if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
							System.out
									.println("entern setCharacteristicWriteUUID");
							mWriteCharacteristic = gattCharacteristic;
							mBluetoothLeService.setCharacteristicNotification(
									gattCharacteristic, true);
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
		
		private  static void sleepTread(int ms)
		{
			try 
			{
		         Thread.currentThread();
				 Thread.sleep(ms);//阻断
			} catch (InterruptedException e) {
		         e.printStackTrace();
			}
		}

		private void commitdata(String datatype,float value){
			Log.d("ithinker", "post data in");
	        ArrayMap<String,String> param = new ArrayMap<>();
	        String token = StrUtils.token(NewMeasureActivity.this);
	    	Log.d("ithinker", token);
	        param.put("token", token);
	        //param.put("token", "18d54ec8446d03451f5552033c64dbda");
			param.put("datatype", datatype);
			param.put("value", value+"");   
		    OkHttpUtils.post(StrUtils.POST_MEASURE_DATA, param, TAG, new OkHttpUtils.SimpleOkCallBack() {
		        @Override
		        public void onResponse(String s) {
		            //Log.d("ithinker", "post data"+s);
		            JSONObject j = OkHttpUtils.parseJSON(NewMeasureActivity.this, s);
		            if (j == null) {
		                return;
		            }
		            Log.d("ithinker", "post successful"+s);
		        }
		    });
		}
		
		private  void dialog()
		{
  		  AlertDialog.Builder dialog=new AlertDialog.Builder(this);
  		  dialog.setTitle("提示切换吗?");
          dialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog,int which){
                   
                }
            });
            dialog.setNegativeButton("取消", null);
            dialog.show();
		}
	 
}
