package mainactivity;

import statisticscharline.twobarchart;
import util.StrUtils;
import measurepack.NewMeasureActivity;

import com.example.learn.DeviceScanActivity;
import com.example.learn.MainActivity;
import com.example.learn.R;
import com.example.learn.testMeasureActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class newMainActivity extends Activity{
	private ImageView yunLogin;
	private ImageView sortStatistics;
	private ImageView measureLine;
	private ImageView paraCorrection;
	private ImageView systemSetting;
	
	private TextView BLEsearch;
	private TextView BLEinfo;
	
	public static String BLEstate = "δ����";
	 @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setContentView(R.layout.aty_main);
		setContentView(R.layout.aty_main);
		//RelativeLayout mLinear = (RelativeLayout) findViewById(R.id.MainLayout);
		LinearLayout mLinear = (LinearLayout) findViewById(R.id.MainLayout);
		mLinear.setBackgroundResource(R.drawable.cup5);
		bindview();
		String token = StrUtils.token(newMainActivity.this);
		String username = "";
		username = StrUtils.username(newMainActivity.this);
		BLEsearch.setText(BLEstate);
		//String BlEState;
	    if (username.equals("")) {
	    	BLEinfo.setText("δ��¼,"+BLEstate+"ithinker");
	    }else{
		    BLEinfo.setText("�ѵ�¼"+username+","+BLEstate+"ithinker");
	    }
	    
	 }
	 
	 private void bindview()
	 {
			
			BLEsearch = (TextView)findViewById(R.id.BLEsearch);
			BLEinfo = (TextView)findViewById(R.id.BLEinfo);
			yunLogin = (ImageView)findViewById(R.id.yunduandenglu);
			sortStatistics = (ImageView)findViewById(R.id.fenxuantongji);
			measureLine = (ImageView)findViewById(R.id.celiangxianshi);
			paraCorrection = (ImageView)findViewById(R.id.canshujiaozheng);
			systemSetting = (ImageView)findViewById(R.id.xitongshezhi);
			BLEsearch.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println("SearchLayout click");
					Intent intent = new Intent();
					intent.setClass(newMainActivity.this, DeviceScanActivity.class);
					startActivity(intent);
				}
			});
			
			yunLogin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println("SearchLayout click");
					Intent intent = new Intent();
					intent.setClass(newMainActivity.this, LoginActivity.class);
					startActivity(intent);
				}
			});
			
			sortStatistics.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println("SearchLayout click");
					Intent intent = new Intent();
					intent.setClass(newMainActivity.this, SelectStatisticActivity.class);
					startActivity(intent);
				}
			});
			
			measureLine.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println("SearchLayout click");
					Intent intent = new Intent();
					intent.setClass(newMainActivity.this, NewMeasureActivity.class);
					startActivity(intent);
				}
			});
			
			systemSetting.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println("systemSetting click");
					Intent intent = new Intent();
					intent.setClass(newMainActivity.this, systemSettingActivity.class);
					startActivity(intent);
				}
			});
			
			paraCorrection.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println("paraCorrection click");
					Intent intent = new Intent();
					intent.setClass(newMainActivity.this, paraCorrectionActivity.class);
					startActivity(intent);
				}
			});
	 }
	

}
