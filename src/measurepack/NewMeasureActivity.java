package measurepack;

import iThinkerChartFactory.CreateLineChart;
import iThinkerChartFactory.circleFactory;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.SeriesSelection;

import com.example.learn.R;
import com.example.learn.XYChartBuilder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
	
	
	 @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aty_measure);
		LinearLayout mLinear = (LinearLayout) findViewById(R.id.MeasureLayout);
		mLinear.setBackgroundResource(R.drawable.cup5);
   
		LinearLayout meaLinear = (LinearLayout) findViewById(R.id.chart);
	    
		CreateLineChart myLineChart = new CreateLineChart();
		GraphicalView mChartView = myLineChart.oneLineChart(this);
		meaLinear.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		mChartView.repaint();
		
		ImageView iv = (ImageView) findViewById(R.id.measureVolumn);
		circleFactory myCircle = new circleFactory();
		myCircle.DrawVolumn(iv, (float) 200.02);
		
		//网格按键背景初始化
	    actime_gridbg = (RelativeLayout) findViewById(R.id.actime_relativelayout);
	    dctime_gridbg = (RelativeLayout) findViewById(R.id.dctime_relativelayout);
	    accurrent_gridbg = (RelativeLayout) findViewById(R.id.accurrent_relativelayout);
	    dccurrent_gridbg = (RelativeLayout) findViewById(R.id.dccurrent_relativelayout);
	    acvoltage_gridbg = (RelativeLayout) findViewById(R.id.acvoltage_relativelayout);
	    dcvoltage_gridbg = (RelativeLayout) findViewById(R.id.dcvoltage_relativelayout);
	    
	    //按键绑定事件   
	    actime_button = (Button)findViewById(R.id.actime_button);
	    actime_button.setOnClickListener(new OnClickListener(){
	    	@Override
	    	public void onClick(View v)
	    	{
	    		gridbutton_select = 0;
	    		Log.d("ithinker", "button test"+gridbutton_select);
	    		Toast.makeText(NewMeasureActivity.this, "You clicked Button actime",
	    				Toast.LENGTH_SHORT).show();
	    		set_selectbutton_bg(0);
	    	}
	    	
	    });
	    dctime_button = (Button)findViewById(R.id.dctime_button);
	    dctime_button.setOnClickListener(new OnClickListener(){
	    	@Override
	    	public void onClick(View v)
	    	{
	    		gridbutton_select = 1;
	    		Log.d("ithinker", "button test"+gridbutton_select);
	    		Toast.makeText(NewMeasureActivity.this, "You clicked Button dctime",
	    				Toast.LENGTH_SHORT).show();
	    		set_selectbutton_bg(1);
	    	}
	    	
	    });
	    
	    accurrent_button = (Button) findViewById(R.id.accurrent_button);
	    
	    
	    
	}
	 
	  private void set_selectbutton_bg(int select_int)
	  {
		  switch(select_int){
		  	case 0:
		  		//actime_select
	    		Log.d("ithinker", "button resume"+gridbutton_select);
	    	    actime_gridbg.setBackgroundResource(R.drawable.grid_bgselect);
	    	    actime_gridbg.getBackground().setAlpha(60);
	    	    dctime_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    //actime_gridbg.getBackground().setAlpha(60);
	    	    break;
		  	case 1:
		  		//dctime_select
	    		Log.d("ithinker", "1button resume"+gridbutton_select);
	    	    dctime_gridbg.setBackgroundResource(R.drawable.grid_bgselect);
	    	    dctime_gridbg.getBackground().setAlpha(60);
	    	    actime_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    break;
		  	case 2:
		  		//accurrent_select
	    		Log.d("ithinker", "accurrent");
	    		
	    	    dctime_gridbg.setBackgroundResource(R.drawable.grid_bgselect);
	    	    dctime_gridbg.getBackground().setAlpha(60);
	    	    actime_gridbg.setBackgroundResource(R.drawable.grid_bg);
	    	    break;
		  }
	  } 

	 
}
