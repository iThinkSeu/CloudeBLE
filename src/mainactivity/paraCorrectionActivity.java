package mainactivity;

import iThinkerChartFactory.createTableFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.learn.BluetoothLeService;
import com.example.learn.R;
import com.example.learn.SampleGattAttributes;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learn.BluetoothLeService;


public class paraCorrectionActivity extends Activity{

	private List<Map<String, Object>> listTitle;
	private List<Map<String, Object>> list;

	private RelativeLayout mAdd;
	private TextView mSave;/** Button for adding entered data to table */
	private EditText mX;/** Edit text field for entering the X value of the data to be added. */
	private EditText mY;/** Edit text field for entering the Y value of the data to be added. */ 
	private Map<String, Object> map = new HashMap<String, Object>();
	private int txid;
	
	
	private TextView titleview;
	private LinearLayout select_view_linearlayout;
	private ImageView select_index_image;
	private boolean isSelect = false;
	private TextView setting_correction_enter;
	private SimpleAdapter adapter;
	
	//蓝牙
	private boolean mConnected = false;
	private BluetoothLeService mBluetoothLeService;
	private BluetoothGattCharacteristic mNotifyCharacteristic = null;
	//private ExpandableListView mGattServicesList;
	private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
	private final String DEFAULT_UUID = SampleGattAttributes.DEFULT_UUID;
	private final String WRITE_UUID = SampleGattAttributes.WRITE_UUID;
	byte[] WriteBytes = new byte[20];
	//

	
	private final ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName,
				IBinder service) {
			mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
					.getService();
			if (!mBluetoothLeService.initialize()) {
				//Log.e(TAG, "Unable to initialize Bluetooth");
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
		
		
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_newparacorrection);
        bindview();
       	titleview.setText("校正报告 ADJUST");
       	
        ListView lv = (ListView) findViewById(R.id.drawListView);
        ListView lvTitle = (ListView) findViewById(R.id.ListViewTitle);
        
        //添加
        mX = (EditText) findViewById(R.id.xValue);
        mY = (EditText) findViewById(R.id.yValue);
        mSave = (TextView) findViewById(R.id.save);
        
        createTableFactory myTable = new createTableFactory();
		list = myTable.getTableList();
		listTitle = myTable.getTableList();
		adapter = new SimpleAdapter(this,list, R.layout.list_item_paracorrection, new String[] {"id", "type","real_value","measure_value"}, new int[] { R.id.txid,R.id.txtype,R.id.txreal_value,R.id.txmeasure_value});
		SimpleAdapter adapterTitle = new SimpleAdapter(this,listTitle, R.layout.list_item_paracorrection, new String[] {"id","type","real_value","measure_value"}, new int[] { R.id.txid,R.id.txtype,R.id.txreal_value,R.id.txmeasure_value});

		lv.setAdapter(adapter);
		lvTitle.setAdapter(adapterTitle);
	
		Map<String, Object> mapTitle = new HashMap<String, Object>();
		mapTitle = new HashMap<String, Object>();
	    mapTitle.put("id","序号");
	    mapTitle.put("type", "类型");
	    mapTitle.put("real_value","实际值");
	    mapTitle.put("measure_value","测量值");
		listTitle.add(mapTitle);
		
		//开启蓝牙服务
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		startService(gattServiceIntent);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

		
		txid = 0;
		/*
		for(int i=1;i<6;i++)
		{
			txid++;
			map = new HashMap<String, Object>();
			map.put("id",""+(list.size()+1));
			map.put("type", "VDC");
			map.put("real_value","1.51");
			map.put("measure_value","1.50");
		    list.add(map);
		}
		*/
		txid++;
		map = new HashMap<String, Object>();
		map.put("id",""+(list.size()+1));
		map.put("type", "VDC");
		map.put("real_value","1.500");
		map.put("measure_value","1.510");
	    list.add(map);
	    
		txid++;
		map = new HashMap<String, Object>();
		map.put("id",""+(list.size()+1));
		map.put("type", "VDC");
		map.put("real_value","3.500");
		map.put("measure_value","3.514");
	    list.add(map);
	    
		txid++;
		map = new HashMap<String, Object>();
		map.put("id",""+(list.size()+1));
		map.put("type", "VDC");
		map.put("real_value","5.500");
		map.put("measure_value","5.510");
	    list.add(map);
	    
		txid++;
		map = new HashMap<String, Object>();
		map.put("id",""+(list.size()+1));
		map.put("type", "VDC");
		map.put("real_value","7.500");
		map.put("measure_value","7.514");
	    list.add(map);
	    
		txid++;
		map = new HashMap<String, Object>();
		map.put("id",""+(list.size()+1));
		map.put("type", "VDC");
		map.put("real_value","10.000");
		map.put("measure_value","10.010");
	    list.add(map);
		
		txid++;
		map = new HashMap<String, Object>();
		map.put("id",""+(list.size()+1));
		map.put("type", "VDC");
		map.put("real_value","12.000");
		map.put("measure_value","12.013");
	    list.add(map);
		
		//adapter.notifyDataSetChanged();
		
		mSave.setOnClickListener(new View.OnClickListener() {
		      public void onClick(View v) {
		        
		    	RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		        RadioButton radioButton = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());//获取被选中的单选框。
		        double x = 0;
		        double y = 0;
		        try {
		          x = Double.parseDouble(mX.getText().toString());
		        } catch (NumberFormatException e) {
		          mX.requestFocus();
		          return;
		        }
		        try {
		          y = Double.parseDouble(mY.getText().toString());
		        } catch (NumberFormatException e) {
		          mY.requestFocus();
		          return;
		        }
		        // add a new data point to the current series
		        map = new HashMap<String, Object>();
		        txid++;
				map.put("id",""+(list.size()+1));
				map.put("type", radioButton.getText());
				map.put("real_value",x+"");
				map.put("measure_value",y+"");
			    list.add(map);
			    adapter.notifyDataSetChanged(); 
        		Toast.makeText(paraCorrectionActivity.this, "添加成功", Toast.LENGTH_SHORT).show();

		        mX.setText("");
		        mY.setText("");
		        mX.requestFocus();
		        // repaint the chart such as the newly added point to be visible
		      }
		    });
		
		 lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){
			 
			 @Override  
			 public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
			 {
			       menu.add(0, 0, 0, "删除"); 
                   menu.add(0, 1, 0, "修改"); 
                   menu.add(0, 2, 0, "取消"); 
			 }
		 });
		 

    }
	
	

    @Override  
    public boolean onContextItemSelected(MenuItem item) {  
    	final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item 
                 .getMenuInfo(); 
        //setTitle("点击了长按菜单里面的第"+item.getItemId()+"个项目"); 
        switch(item.getItemId())
        {
        	case 0: 
        		Toast.makeText(paraCorrectionActivity.this, "已删除position"+(info.position+1), Toast.LENGTH_SHORT).show();
        		list.remove(info.position);  
        		for(int i=0;i<list.size();i++)
        		{
        			 ((Map)list.get(i)).put("id","+"+(i+1));//修改值
        	    
        		}	 
        	    adapter.notifyDataSetChanged(); 
        	    break;
        	case 1:	
        		//
        		
        		  AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        		  final EditText feedEditText = new EditText(this);
        		  feedEditText.setText(list.get(info.position).get("measure_value").toString());
	    		  dialog.setTitle("校正值");
	              dialog.setView(feedEditText);
	              dialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){
	                  @Override
	                  public void onClick(DialogInterface dialog,int which){
	                      if(!feedEditText.getText().toString().isEmpty()){
	                    	 list.get(info.position).put("measure_value",feedEditText.getText().toString());
	                    	 System.out.println(info.position);
	                    	 adapter.notifyDataSetChanged(); 
	                      }
	                      else{
	                          Toast.makeText(paraCorrectionActivity.this, "请输入校正值", Toast.LENGTH_SHORT).show();
	                      }
	                  }
	              });
	              dialog.setNegativeButton("取消", null);
	              dialog.show();
	              
        		break;       		
        	case 2:
          		Toast.makeText(paraCorrectionActivity.this, item.getItemId()+"取消 You clicked positon"+info.position, Toast.LENGTH_SHORT).show();
        	    break;
        	default:
          		Toast.makeText(paraCorrectionActivity.this, item.getItemId()+"default You clicked positon"+info.position, Toast.LENGTH_SHORT).show();
          		break;
        }
        //Toast.makeText(SelectStatisticActivity.this, item.getItemId()+"You clicked positon"+info.position, Toast.LENGTH_SHORT).show();
        //list.remove(info.position);  
        //adapter.notifyDataSetChanged();  
        return super.onContextItemSelected(item);  
    }  
	
	
	private void bindview()
	{
		titleview = (TextView) findViewById(R.id.title);
		mAdd = (RelativeLayout) findViewById(R.id.add);
	 	select_view_linearlayout = (LinearLayout) findViewById(R.id.select_view_linearlayout);
	 	select_index_image = (ImageView) findViewById(R.id.select_index_image);
	 	setting_correction_enter = (TextView)findViewById(R.id.setting_enter);
	 	
		mAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isSelect = !isSelect;
				Log.d("ithinker", ""+isSelect);
				if(isSelect==true)
				{
					select_index_image.setImageResource(R.drawable.home_index_arrow_down);
					select_view_linearlayout.setVisibility(View.VISIBLE);
				}else
				{
					select_index_image.setImageResource(R.drawable.home_index_arrow);
					select_view_linearlayout.setVisibility(View.GONE);
				}
			}
		});
		
		setting_correction_enter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String str = "<CR#1#1#12.02#12.00>";
				System.out.println("test send fix data");
				Senddata("start send fix data");
				for(int i=0;i<list.size();i++)
        		{
					str = "<C#1#"+list.get(i).get("id")+"#"+list.get(i).get("real_value")+"#"+list.get(i).get("measure_value")+">";
					
					Senddata(str);
					delay(100);
        			//Log.d("ithinker","list result"+(String) list.get(i).get("id"));//修改值
        	    
        		}	 
        		Toast.makeText(paraCorrectionActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
        		Senddata("finish");
			}
		});
	}
	
	private void delay(int ms)
	{
		int k;
		for(int i=0;i<ms;i++)
		{
			for(int j=0;j<1000;j++)
			{
				k=1;
			}
		}
	}
	
	//蓝牙BLE
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
				//clearUI();
			} else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
					.equals(action)) {
				// Show all the supported services and characteristics on the
				// user interface.
				displayGattServices(mBluetoothLeService.getSupportedGattServices());
			} else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
				//displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
				BLEdReceive(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
			}
		}
	};

	private void BLEdReceive(String response)
	{
		Toast.makeText(paraCorrectionActivity.this, "receive"+response, Toast.LENGTH_SHORT).show();

	}
	
	private void Senddata(String str)
	{
		  if (mNotifyCharacteristic != null) {
				mNotifyCharacteristic.setValue((byte[]) str.getBytes());
				mBluetoothLeService
						.writeCharacteristic(mNotifyCharacteristic);
			} else {
				System.out.println("mNotifyCharacteristic is null");
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
