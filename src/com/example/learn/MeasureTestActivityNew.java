package com.example.learn;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.IBinder;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * For a given BLE device, this Activity provides the user interface to connect,
 * display data, and display GATT services and characteristics supported by the
 * device. The Activity communicates with {@code BluetoothLeService}, which in
 * turn interacts with the Bluetooth LE API.
 */
public class MeasureTestActivityNew extends Activity {
	private final static String TAG = MeasureTestActivityNew.class
			.getSimpleName();

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

	private Paint mPaint;
	private Paint paint;
	private List<Map<String, Object>> data = null;
	private List<Map<String, Object>> list;
	private TextView mTxtRecev;
	private EditText mEditSend;
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
				Toast.makeText(getBaseContext(), "蓝牙已经连接", Toast.LENGTH_SHORT)
						.show();
				TextView tv = (TextView) findViewById(R.id.testConnect);
				tv.setText("连接");
				// updateConnectionState(R.string.connected);
				// invalidateOptionsMenu();
				System.out.println("连接");
			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
					.equals(action)) {
				mConnected = false;
				// updateConnectionState(R.string.disconnected);
				// invalidateOptionsMenu();
				TextView tv = (TextView) findViewById(R.id.testConnect);
				tv.setText("断开");
				Toast.makeText(getBaseContext(), "蓝牙失去连接", Toast.LENGTH_SHORT)
						.show();
				// clearUI();
				System.out.println("断开");
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

	private static String hexString = "0123456789ABCDEF";

	public static String decode(String bytes) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(
				bytes.length() / 2);
		// 将每2位16进制整数组装成一个字节
		for (int i = 0; i < bytes.length(); i += 2)
			baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
					.indexOf(bytes.charAt(i + 1))));
		return new String(baos.toByteArray());
	}

	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0) {
			System.out.println("长度不是偶数");
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

	private int temp1 = 0;
	private int v1 = 0;
	private Intent gattServiceIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.measuretest);
		final Intent intent = getIntent();
		mTxtRecev = (TextView) findViewById(R.id.editRecev);
		mTxtRecev.setMovementMethod(ScrollingMovementMethod.getInstance());
		mTxtRecev.setScrollbarFadingEnabled(false);
		mEditSend = (EditText) findViewById(R.id.editSend);
		Button btnClear = (Button) findViewById(R.id.btnClear);
		
		//开启蓝牙服务
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		startService(gattServiceIntent);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
		
		
		btnClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mTxtRecev.setText("");
			}

		});
		Button btnSend = (Button) findViewById(R.id.btnTestSend);
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("send test");
				String str = mEditSend.getText().toString();
				// et.clearComposingText();
				if (mNotifyCharacteristic != null) {
					mNotifyCharacteristic.setValue((byte[]) str.getBytes());
					mBluetoothLeService
							.writeCharacteristic(mNotifyCharacteristic);
				} else {
					System.out.println("mNotifyCharacteristic is null");
				}
			}
		});

		gattServiceIntent = new Intent(this, BluetoothLeService.class);
		// stopService(gattServiceIntent);
		// startService(gattServiceIntent);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

		mEditSend.clearFocus();
		mTxtRecev.clearFocus();
		// mTxtRecev.setFocusable(false);
		mTxtRecev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mTxtRecev.clearFocus();
			}
		});
		btnSend.setFocusable(true);
		btnSend.requestFocus();
		btnSend.setFocusableInTouchMode(true);
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

	private int recSize = 0;

	private String recData = "";
	private int dataType = 0;// 0 温度；1体积
	private int dataNum = 0;
	private String dataSave = "";

	private void displayData(String data) {

		// dataSave += data;
		mTxtRecev.append(data);
		mTxtRecev.setMovementMethod(ScrollingMovementMethod.getInstance());
		// mTxtRecev.setSelection(mTxtRecev.getText().length(), mTxtRecev
		// .getText().length());
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
		String unknownServiceString = getResources().getString(
				R.string.unknown_service);
		String unknownCharaString = getResources().getString(
				R.string.unknown_characteristic);
		ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
		ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();
		mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

		// Loops through available GATT Services.
		for (BluetoothGattService gattService : gattServices) {
			HashMap<String, String> currentServiceData = new HashMap<String, String>();
			uuid = gattService.getUuid().toString();
			System.out.println("uuid 1:" + uuid);
			currentServiceData.put(LIST_NAME,
					SampleGattAttributes.lookup(uuid, unknownServiceString));
			currentServiceData.put(LIST_UUID, uuid);
			gattServiceData.add(currentServiceData);

			ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();
			List<BluetoothGattCharacteristic> gattCharacteristics = gattService
					.getCharacteristics();
			ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();
			System.out.println("gattCharacteristics size:"
					+ gattCharacteristics.size());
			// Loops through available Characteristics.
			for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
				charas.add(gattCharacteristic);
				HashMap<String, String> currentCharaData = new HashMap<String, String>();
				uuid = gattCharacteristic.getUuid().toString();
				System.out.println("uuid" + uuid);
				currentCharaData.put(LIST_NAME,
						SampleGattAttributes.lookup(uuid, unknownCharaString));
				currentCharaData.put(LIST_UUID, uuid);
				gattCharacteristicGroupData.add(currentCharaData);
				if (gattCharacteristic.getUuid().toString()
						.equals(DEFAULT_UUID)) {
					int charaProp = gattCharacteristic.getProperties();
					if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
						System.out
								.println("entern setCharacteristicNotification");
						mNotifyCharacteristic = gattCharacteristic;
						mBluetoothLeService.setCharacteristicNotification(
								gattCharacteristic, true);
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

