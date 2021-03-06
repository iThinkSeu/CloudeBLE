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
import com.example.learn.DeviceControlActivity;
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
import android.app.AlertDialog;

public class NewMeasureActivity  extends Activity{
	
	private int gridbutton_select = 0;
	
	//网格按键  gridbutton
	private Button actime_button;
	private Button dctime_button;
	private Button accurrent_button;
	private Button dccurrent_button;
	private Button acvoltage_button;
	private Button dcvoltage_button;
	private Button save_button;
	//网格按键背景
	private RelativeLayout actime_gridbg;
	private RelativeLayout dctime_gridbg;
	private RelativeLayout accurrent_gridbg;
	private RelativeLayout dccurrent_gridbg;
	private RelativeLayout acvoltage_gridbg;
	private RelativeLayout dcvoltage_gridbg;
	
	//6个参数显示
	TextView error_value,VWRTHD_text,VWRTHD_value,f_value,up_value,down_value,stand_value;
	TextView up_text,down_text,stand_text;
	
	private String main_value="0.0000";
	private String ID = "ABCDEF";
	String datatype = "VAC";
	String separation = "--";
	String up = "--";
	String stand = "12.000";
	String down = "--";
	String Freq = "--";
	String VWRTHD="--";
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
	private int dalayms = 3000;
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
		error_value.setText("0.223kV");
		
		LinearLayout meaLinear = (LinearLayout) findViewById(R.id.chart);
	    
		myLineChart = new CreateLineChart();
		mChartView = myLineChart.oneLineChart_rms(this);
		meaLinear.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		myLineChart.addSeriesData(200);
		mChartView.repaint();
		iv = (ImageView) findViewById(R.id.measureVolumn);
		myCircle.DrawVolumn(iv, "0.0000","VAC");
		
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
		    error_value = (TextView) findViewById(R.id.error_value);
		    VWRTHD_text = (TextView)findViewById(R.id.VWRTHD);
		    VWRTHD_value = (TextView)findViewById(R.id.VWRTHD_value);
		    f_value = (TextView)findViewById(R.id.f_value);
		    up_value = (TextView)findViewById(R.id.up_value);
		    up_text = (TextView)findViewById(R.id.up);
		    down_text = (TextView)findViewById(R.id.down);
		    stand_text = (TextView)findViewById(R.id.stand);
		    down_value = (TextView)findViewById(R.id.down_value);
		    stand_value = (TextView)findViewById(R.id.stand_value);
			//网格按键背景初始化
		    actime_gridbg = (RelativeLayout) findViewById(R.id.actime_relativelayout);
		    dctime_gridbg = (RelativeLayout) findViewById(R.id.dctime_relativelayout);
		    accurrent_gridbg = (RelativeLayout) findViewById(R.id.accurrent_relativelayout);
		    dccurrent_gridbg = (RelativeLayout) findViewById(R.id.dccurrent_relativelayout);
		    acvoltage_gridbg = (RelativeLayout) findViewById(R.id.acvoltage_relativelayout);
		    dcvoltage_gridbg = (RelativeLayout) findViewById(R.id.dcvoltage_relativelayout);	
		    
			//网格按键背景初始化
		    actime_button = (Button) findViewById(R.id.actime_button);
		    dctime_button = (Button) findViewById(R.id.dctime_button);
		    accurrent_button = (Button) findViewById(R.id.accurrent_button);
		    dccurrent_button = (Button) findViewById(R.id.dccurrent_button);
		    acvoltage_button = (Button) findViewById(R.id.acvoltage_button);
		    dcvoltage_button = (Button) findViewById(R.id.dcvoltage_button);	
		    save_button = (Button) findViewById(R.id.save_button);
		    //按键设置
		    moreSetting = (ImageView)findViewById(R.id.aty_info_more);
		    
		    save_button.setOnClickListener(new OnClickListener(){
		    	@Override
		    	public void onClick(View v)
		    	{
					Toast.makeText(NewMeasureActivity.this, main_value,Toast.LENGTH_SHORT).show();
					//private void commitsavedata(String ID,String datatype,String value,String VWRTHD,String separation,String up,String down,String stand,String fre)
					commitsavedata(ID,datatype,main_value,VWRTHD,separation,up,down,stand,Freq);
	        		
		    	}
		    });
		    moreSetting.setOnClickListener(new OnClickListener(){
		    	@Override
		    	public void onClick(View v)
		    	{
		    		final Dialog dialog = new Dialog(NewMeasureActivity.this,R.style.DialogSlideAnim);
	                View content = LayoutInflater.from(NewMeasureActivity.this).inflate(R.layout.aty_info_option,mLinear,false);
	                View.OnClickListener listener = new View.OnClickListener() {
	                    @Override
	                    public void onClick(View v) {
		 		    		handler.removeCallbacks(runnable);
		                    Toast.makeText(NewMeasureActivity.this, "remove", Toast.LENGTH_SHORT).show();

	                        if(v.getId() == R.id.aty_info_option_edit_info){
	                            Intent i = new Intent(NewMeasureActivity.this, MeasureTestActivityNew.class);
	                            startActivity(i);
	                            handler.postDelayed(runnable, dalayms);//每两秒执行一次runnable.   
	                        }else if(v.getId() == R.id.aty_info_option_stand)
	                        {
	        	        		AlertDialog.Builder dialog=new AlertDialog.Builder(NewMeasureActivity.this);
	        	        		final EditText feedEditText = new EditText(NewMeasureActivity.this);
	        	        		feedEditText.setText(stand);
	        		    		dialog.setTitle("标称值");
	        		            dialog.setView(feedEditText);
	        		            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){
	        		                 @Override
	        		                 public void onClick(DialogInterface dialog,int which){
	        		                      if(!feedEditText.getText().toString().isEmpty()){
	        		                    	 feedEditText.getText().toString();
	        		                    	 System.out.println(feedEditText.getText().toString());
	        		                    	 String str1 = "CONF:FILT:CRIT ";
	        		                    	 String str2 = feedEditText.getText().toString()+" "+getDanwei(datatype)+"#";
	        		                    	 Senddata(str1);
	        		                    	 sleepTread(100);
	        		                    	 Senddata(str2);
	        		                         Toast.makeText(NewMeasureActivity.this, "设置标称值为:"+feedEditText.getText().toString(), Toast.LENGTH_SHORT).show();

	        		                      }
	        		                      else{
	        		                          Toast.makeText(NewMeasureActivity.this, "请输入参数", Toast.LENGTH_SHORT).show();
	        		                      }
	        		                     sleepTread(100);
	        		                     timerSenderToBLE();
	        					    	 handler.postDelayed(runnable, dalayms);//每两秒执行一次runnable.   

	        		                  }
	        		             });
	        		            
	        		             dialog.setNegativeButton("取消", new DialogInterface.OnClickListener(){

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										timerSenderToBLE();
										handler.postDelayed(runnable, dalayms);//每两秒执行一次runnable.   
									}
	        		             });
	        		             dialog.show();
	                        }else if(v.getId() == R.id.aty_info_option_uplimit)
	                        {
	        	        		AlertDialog.Builder dialog=new AlertDialog.Builder(NewMeasureActivity.this);
	        	        		final EditText feedEditText = new EditText(NewMeasureActivity.this);
	        	        		feedEditText.setText(up);
	        		    		dialog.setTitle("上限值");
	        		            dialog.setView(feedEditText);
	        		            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){
	        		                 @Override
	        		                 public void onClick(DialogInterface dialog,int which){
	        		                      if(!feedEditText.getText().toString().isEmpty()){
	        		                    	 feedEditText.getText().toString();
	        		                    	 System.out.println(feedEditText.getText().toString());
	        		                    	 String str1 = "CONF:FILT:ULIM ";
	        		                    	 String str2 = feedEditText.getText().toString()+" "+getDanwei(datatype)+"#";
	        		                    	 Senddata(str1);
	        		                    	 sleepTread(100);
	        		                    	 Senddata(str2);
	        		                         Toast.makeText(NewMeasureActivity.this, "上限值设置为:"+feedEditText.getText().toString(), Toast.LENGTH_SHORT).show();

	        		                      }
	        		                      else{
	        		                          Toast.makeText(NewMeasureActivity.this, "请输入参数", Toast.LENGTH_SHORT).show();
	        		                      }
	        		                      sleepTread(100);
	        		                      timerSenderToBLE();
	        		                      handler.postDelayed(runnable, dalayms);//每两秒执行一次runnable. 
	        		                  }
	        		             });
	        		            
	        		             dialog.setNegativeButton("取消", new DialogInterface.OnClickListener(){

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										timerSenderToBLE();
										handler.postDelayed(runnable, dalayms);//每两秒执行一次runnable.   
									}
	        		             });
	        		             dialog.show();
	                        }else if(v.getId() == R.id.aty_info_option_downlimit)
	                        {
	        	        		AlertDialog.Builder dialog=new AlertDialog.Builder(NewMeasureActivity.this);
	        	        		final EditText feedEditText = new EditText(NewMeasureActivity.this);
	        	        		feedEditText.setText(down);
	        		    		dialog.setTitle("下限值");
	        		            dialog.setView(feedEditText);
	        		            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){
	        		                 @Override
	        		                 public void onClick(DialogInterface dialog,int which){
	        		                      if(!feedEditText.getText().toString().isEmpty()){
	        		                    	 feedEditText.getText().toString();
	        		                    	 System.out.println(feedEditText.getText().toString());
	        		                    	 String str1 = "CONF:FILT:LLIM ";
	        		                    	 String str2 = feedEditText.getText().toString()+" "+getDanwei(datatype)+"#";
	        		                    	 Senddata(str1);
	        		                    	 sleepTread(100);
	        		                    	 Senddata(str2);
	        		                         Toast.makeText(NewMeasureActivity.this, "下限值设置为:"+feedEditText.getText().toString(), Toast.LENGTH_SHORT).show();

	        		                      }
	        		                      else{
	        		                          Toast.makeText(NewMeasureActivity.this, "请输入参数", Toast.LENGTH_SHORT).show();
	        		                      }
	        		                      sleepTread(100);
	        		                      timerSenderToBLE();
	        		                      handler.postDelayed(runnable, dalayms);//每两秒执行一次runnable. 
	        		                  }
	        		             });
	        		            
	        		             dialog.setNegativeButton("取消", new DialogInterface.OnClickListener(){

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										timerSenderToBLE();
										handler.postDelayed(runnable, dalayms);//每两秒执行一次runnable.   
									}
	        		             });
	        		             dialog.show();
	                        }else if(v.getId() == R.id.aty_info_option_time)
	                        {
	                        	
	        	        		AlertDialog.Builder dialog=new AlertDialog.Builder(NewMeasureActivity.this);
	        	        		final EditText feedEditText = new EditText(NewMeasureActivity.this);
	        	        		feedEditText.setText(dalayms/1000+"");
	        		    		dialog.setTitle("定时周期(单位s)");
	        		            dialog.setView(feedEditText);
	        		            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){
	        		                 @Override
	        		                 public void onClick(DialogInterface dialog,int which){
	        		                      if(!feedEditText.getText().toString().isEmpty()){
	        		                    	 
	        		                    	  try {
	        		                    		     dalayms = Integer.parseInt(feedEditText.getText().toString())*1000;
	        		                    		} catch (NumberFormatException e) {
	        		                    		    e.printStackTrace();
	        		                    		}
	        		                         Toast.makeText(NewMeasureActivity.this, "定时周期为:"+dalayms/1000+"s", Toast.LENGTH_SHORT).show();

	        		                      }
	        		                      else{
	        		                          Toast.makeText(NewMeasureActivity.this, "请输入参数", Toast.LENGTH_SHORT).show();
	        		                      }
	        		                      timerSenderToBLE();
	        		                      handler.postDelayed(runnable, dalayms);//每两秒执行一次runnable. 
	        		                  }
	        		             });
	        		            
	        		             dialog.setNegativeButton("取消", new DialogInterface.OnClickListener(){

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										timerSenderToBLE();
										handler.postDelayed(runnable, dalayms);//每两秒执行一次runnable.   
									}
	        		             });
	        		             dialog.show();
	        		             
	                        }
		                  
	                        dialog.dismiss();
	                    }
	                };
	                content.findViewById(R.id.aty_info_option_cancel).setOnClickListener(listener);
	                content.findViewById(R.id.aty_info_option_edit_info).setOnClickListener(listener);
	                content.findViewById(R.id.aty_info_option_stand).setOnClickListener(listener);
	                content.findViewById(R.id.aty_info_option_uplimit).setOnClickListener(listener);
	                content.findViewById(R.id.aty_info_option_downlimit).setOnClickListener(listener);
	                content.findViewById(R.id.aty_info_option_time).setOnClickListener(listener);
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
		    dcvoltage_button.setOnClickListener(new OnClickListener(){
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
				    		sleepTread(10);
				    		Senddata("CONF:VOLT DC#");
				    		sleepTread(300);
				    		Senddata("CONF:VOLT DC#");
				    		sleepTread(300);
				    		timerSenderToBLE();
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
		    acvoltage_button.setOnClickListener(new OnClickListener(){
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
				    		sleepTread(300);
				    		Senddata("CONF:VOLT AC#");
				    		sleepTread(300);
				    		timerSenderToBLE();
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
		    dccurrent_button.setOnClickListener(new OnClickListener(){
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
				    		sleepTread(10);
				    		Senddata("CONF:CURR DC#");
				    		sleepTread(300);
				    		Senddata("CONF:CURR DC#");
				    		sleepTread(300);
				    		timerSenderToBLE();
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
		    accurrent_button.setOnClickListener(new OnClickListener(){
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
				    		sleepTread(10);
				    		Senddata("CONF:CURR AC#");
				    		sleepTread(300);
				    		Senddata("CONF:CURR AC#");
				    		sleepTread(300);
				    		timerSenderToBLE();
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
		    dctime_button.setOnClickListener(new OnClickListener(){
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
		 		    		sleepTread(10);
		 		    		Senddata("CONF:TIME DC#");
		 		    		sleepTread(300);
		 		    		Senddata("CONF:TIME DC#");
				    		sleepTread(300);
				    		timerSenderToBLE();
		 		    		handler.postDelayed(runnable, dalayms);//每两秒执行一次runnable.
		                	 	
		                  }
		            });
	                dialog.setNegativeButton("取消", null);
	                dialog.show();
		    	}
		    });
		    
		    //交流时间
		    //actime_button = (Button)findViewById(R.id.actime_button);
		    actime_button.setOnClickListener(new OnClickListener(){
		    	@Override
		    	public void onClick(View v)
		    	{
					//Intent intent = new Intent();
					//intent.setClass(NewMeasureActivity.this, MeasureTestActivityNew.class);
					//startActivity(intent);
		    		//set_selectbutton_bg(6);
		    		AlertDialog.Builder dialog=new AlertDialog.Builder(NewMeasureActivity.this);
		      		dialog.setTitle("确认切换模式吗?");
		            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){
		                 @Override
		                 public void onClick(DialogInterface dialog,int which){
		 		    		//////
		 		    		myLineChart.clearSeriesData();
		 		    		mChartView.repaint();
		 		    		measure_mode = "VAC-T";
		 		    		handler.removeCallbacks(runnable);
		 		    		sleepTread(10);
		 		    		Senddata("CONF:TIME AC#");
		 		    		sleepTread(300);
		 		    		Senddata("CONF:TIME AC#");
				    		sleepTread(300);
				    		timerSenderToBLE();
		 		    		handler.postDelayed(runnable, dalayms);//每两秒执行一次runnable.
		                	 	
		                  }
		            });
	                dialog.setNegativeButton("取消", null);
	                dialog.show();
		    	}
		    	
		    });
		    

	  }
	  
	  
	  private void timerSenderToBLE()
	  {
			String str = "READ?#";
			//switch(measure_mode)
			//{
				//case "VDC":str = "MEASure:VALue?#";break;
				//case "VAC":str = "MEASure:VALue?#";break;
				//case "IDC":str = "MEASure:VALue?#";break;
				//case "IAC":str = "MEASure:VALue?#";break;
				//default:str = "MEASure:VALue?#";break;
			//}
			Senddata(str);
			Log.d("ithinker", "timer function"+str);
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
					myCircle.DrawVolumn(iv, convertData,"VDC");
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
			String data = "0";			
			Log.d("ithinker", "recFrame in enter = "+recFrame+recFrame.contains("\n"));
			//recFrame+="\n";
			
			if(recFrame.contains("\n"))
			{
				Log.d("ithinker", "recFrame = "+recFrame);
				recFrame = recFrame.trim();
				//recFrame = "ID:001_MEA:VAC:2.0020kV_ErrorResult:Low_Up:12.2000_Stand:12.0000_Down:11.8000_Freq:0.0Hz_THD:0.00%";
				String[] sArray = recFrame.split("[: _]");
				recFrame = "";
				Log.d("ithinker", "sAarray.length="+sArray.length);
				String val = "";
				
				if(sArray.length>12)
				{
					ID = sArray[1];
					data = sArray[4];
					datatype = sArray[3];
					separation = sArray[6];
					up = sArray[8];
					stand = sArray[10];
					down = sArray[12];
					Freq = "--";
					VWRTHD="--";
				}
				//String Freq = sArray[14];
				data = data.trim();
				Log.d("ithinker", "!!!!sArray[0]=VDC.data="+data);
				switch(datatype)
				{
					case "VDC":
					{
						Log.d("ithinker", "!!!!sArray[0]=VDC.data="+data);
						val = "";
						for(int i=0;i<data.length();i++)
						{
							//判断是数字
							if(data.charAt(i)=='-'||data.charAt(i)=='.'||(data.charAt(i)-'0'>=0&&data.charAt(i)-'0'<=9))
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
						//特殊参数
						if(sArray.length>14)
							VWRTHD = sArray[14];
						else
							VWRTHD = "--";
					    //刷新界面
						VWRTHD_value.setText(VWRTHD);
						f_value.setText(Freq);
						
						float error_f=(float) (value_VDC-Float.valueOf(stand));
						BigDecimal b_error = new BigDecimal((double)error_f);
						error_f = (float)b_error.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();  
						error_value.setText(error_f+"kV");
						up_value.setText(up);
						down_value.setText(down);
						stand_value.setText(stand);
						
						up_text.setText("上限值");
						down_text.setText("下限值");
						stand_text.setText("标称值");
						myCircle.DrawVolumn(iv, val,"VDC");
						myLineChart.addSeriesData(value_VDC);
			    		mChartView.repaint();
			    		commitdata(ID,"VDC",value_VDC,VWRTHD,separation,up,down,stand,Freq);
			    		break;
					}
					case "VAC":
					{
						val = "";
						for(int i=0;i<data.length();i++)
						{
							//判断是数字
							if(data.charAt(i)=='-'||data.charAt(i)=='.'||(data.charAt(i)-'0'>=0&&data.charAt(i)-'0'<=9))
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
						myCircle.DrawVolumn(iv, val,"VAC");	
						
						float error_f=(float) (value_VAC-Float.valueOf(stand));
						BigDecimal b_error = new BigDecimal((double)error_f);
						error_f = (float)b_error.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();  
						error_value.setText(error_f+"kV");
						//特殊参数
						if(sArray.length>16)
						{
							VWRTHD = sArray[16];
							Freq = sArray[14];
						}
						else
							VWRTHD = "--";
					    //刷新界面
						VWRTHD_text.setText("VWR");
						VWRTHD_value.setText(VWRTHD);
						f_value.setText(Freq);
						
						up_value.setText(up);
						down_value.setText(down);
						stand_value.setText(stand);
						
						up_text.setText("上限值");
						down_text.setText("下限值");
						stand_text.setText("标称值");
						myLineChart.addSeriesData(value_VAC);
			    		mChartView.repaint();
			    		commitdata(ID,"VAC",value_VAC,VWRTHD,separation,up,down,stand,Freq);
			    		break;
					}
					case "IDC":
					{
						val = "";
						for(int i=0;i<data.length();i++)
						{
							//判断是数字
							if(data.charAt(i)=='-'||data.charAt(i)=='.'||(data.charAt(i)-'0'>=0&&data.charAt(i)-'0'<=9))
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
						
						//特殊参数
						VWRTHD = "--";
						Freq="--";
					    //刷新界面
						VWRTHD_value.setText(VWRTHD);
						f_value.setText(Freq);
						
						float error_f=(float) (value_IDC-Float.valueOf(stand));
						BigDecimal b_error = new BigDecimal((double)error_f);
						error_f = (float)b_error.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();  
						error_value.setText(error_f+"mA");
						up_value.setText(up);
						down_value.setText(down);
						stand_value.setText(stand);
						
						up_text.setText("上限值");
						down_text.setText("下限值");
						stand_text.setText("标称值");
						myCircle.DrawVolumn(iv, val,"IDC");
						myLineChart.addSeriesData(value_IDC);
			    		mChartView.repaint();
			    		commitdata(ID,"IDC",value_IDC,VWRTHD,separation,up,down,stand,Freq);
			    	
			    		break;
					}
					case "IAC":
					{
						val = "";
						for(int i=0;i<data.length();i++)
						{
							//判断是数字
							if(data.charAt(i)=='-'||data.charAt(i)=='.'||(data.charAt(i)-'0'>=0&&data.charAt(i)-'0'<=9))
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
						
						//特殊参数
						VWRTHD = "--";
						if(sArray.length>14)
							Freq=sArray[14];;
					    //刷新界面
						VWRTHD_value.setText(VWRTHD);
						f_value.setText(Freq);
						
						float error_f=(float) (value_IAC-Float.valueOf(stand));
						BigDecimal b_error = new BigDecimal((double)error_f);
						error_f = (float)b_error.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();  
						error_value.setText(error_f+"mA");
						up_value.setText(up);
						down_value.setText(down);
						stand_value.setText(stand);
						
						up_text.setText("上限值");
						down_text.setText("下限值");
						stand_text.setText("标称值");
						myCircle.DrawVolumn(iv, val,"IAC");
						myLineChart.addSeriesData(value_IAC);
			    		mChartView.repaint();
			    		commitdata(ID,"IAC",value_IAC,VWRTHD,separation,up,down,stand,Freq);
			    		break;
					}
					case "VDC-T":
					{
						val = "";
						for(int i=0;i<data.length();i++)
						{
							//判断是数字
							if(data.charAt(i)=='-'||data.charAt(i)=='.'||(data.charAt(i)-'0'>=0&&data.charAt(i)-'0'<=9))
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
						
						
						//特殊参数
						VWRTHD = "--";
						Freq="--";
					    //刷新界面
						VWRTHD_value.setText(VWRTHD);
						f_value.setText(Freq);
						
						float error_f=(float) (value_VDC_T-Float.valueOf(stand));
						BigDecimal b_error = new BigDecimal((double)error_f);
						error_f = (float)b_error.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();  
						error_value.setText(error_f+"s");
						up_value.setText(up);
						down_value.setText(down);
						stand_value.setText("--");
						
						up_text.setText("上升时间");
						down_text.setText("下降时间");
						stand_text.setText("--");
						//Log.d("ithinker", "value_VAC"+value_VAC);
						myCircle.DrawVolumn(iv, val,"VDC-T");
						myLineChart.addSeriesData(value_VDC_T);
			    		mChartView.repaint();
			    		commitdata(ID,"VDC-T",value_VDC_T,VWRTHD,separation,up,down,stand,Freq);
			    		break;
					}
					
					case "VAC-T":
					{
						val = "";
						for(int i=0;i<data.length();i++)
						{
							//判断是数字
							if(data.charAt(i)=='-'||data.charAt(i)=='.'||(data.charAt(i)-'0'>=0&&data.charAt(i)-'0'<=9))
							{
								val+=data.charAt(i);
							}else
							{
								break;
							}		
						}
						float value_T = (float) Float.parseFloat(val);						
						BigDecimal b = new BigDecimal((double)value_T);  
						value_T = (float)b.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();  
						
						//特殊参数
						VWRTHD = "--";
						Freq="--";
					    //刷新界面
						VWRTHD_value.setText(VWRTHD);
						f_value.setText(Freq);
						
						float error_f=(float) (value_T-Float.valueOf(stand));
						BigDecimal b_error = new BigDecimal((double)error_f);
						error_f = (float)b_error.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();  
						error_value.setText(error_f+"s");
						up_value.setText(up);
						down_value.setText(down);
						stand_value.setText("--");
						
						up_text.setText("上升时间");
						down_text.setText("下降时间");
						stand_text.setText("--");
						//Log.d("ithinker", "value_VAC"+value_VAC);
						myCircle.DrawVolumn(iv, val,"VAC-T");
						myLineChart.addSeriesData(value_T);
			    		mChartView.repaint();
			    		//commitdata(String ID,String datatype,float value,String VWRTHD,String separation,String up,String down,String stand)
			    		commitdata(ID,"VAC-T",value_T,VWRTHD,separation,up,down,stand,Freq);
			    		break;
					}
				}
				main_value = val;
				
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
							//WriteBytes = new byte[4];
							//WriteBytes[0] = 'O';
							//WriteBytes[1] = 'K';
							//WriteBytes[2] = '#';
							//gattCharacteristic.setValue(WriteBytes);
							//mBluetoothLeService.writeCharacteristic(gattCharacteristic);
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

		private void commitdata(String ID,String datatype,float value,String VWRTHD,String separation,String up,String down,String stand,String fre){
			Log.d("ithinker", "post data in");
	        ArrayMap<String,String> param = new ArrayMap<>();
	        String token = StrUtils.token(NewMeasureActivity.this);
	    	Log.d("ithinker", token);
	        param.put("token", token);
	        //param.put("token", "18d54ec8446d03451f5552033c64dbda");
			param.put("datatype", datatype);
			param.put("value", value+"");
			param.put("VWRTHD", VWRTHD);
			param.put("separation", separation);
			param.put("up", up);
			param.put("down", down);
			param.put("stand", stand);
			param.put("fre", fre);
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
		
		private void commitsavedata(String ID,String datatype,String value,String VWRTHD,String separation,String up,String down,String stand,String fre){
			Log.d("ithinker", "post data in");
	        ArrayMap<String,String> param = new ArrayMap<>();
	        String token = StrUtils.token(NewMeasureActivity.this);
	    	Log.d("ithinker", token);
	        param.put("token", token);
	        //param.put("token", "18d54ec8446d03451f5552033c64dbda");
			param.put("datatype", datatype);
			param.put("value", value+"");
			param.put("VWRTHD", VWRTHD);
			param.put("separation", separation);
			param.put("up", up);
			param.put("down", down);
			param.put("stand", stand);
			param.put("fre", fre);
		    OkHttpUtils.post(StrUtils.POST_SAVE_DATA, param, TAG, new OkHttpUtils.SimpleOkCallBack() {
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
		
		private  String getDanwei(String datatype)
		{
		  String danwei = "";
  		  switch(datatype)
  		  {
  		  	case "VAC":danwei = "kV";break;
  		  	case "VDC":danwei = "kV";break;
  		  	case "IAC":danwei = "mA";break;
  		  	case "IDC":danwei = "mA";break;
  		  	default:danwei = "--";
  		  }
  		  return danwei;
		}
	 
	 
}
