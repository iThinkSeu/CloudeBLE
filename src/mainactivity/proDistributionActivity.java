package mainactivity;

import iThinkerChartFactory.CreateLineChart;
import iThinkerChartFactory.muchYaxisLineFactory;
import iThinkerChartFactory.mybarchart;

import org.achartengine.GraphicalView;

import com.example.learn.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class proDistributionActivity extends Activity{

	private GraphicalView mChartView_VDC,mChartView_VAC,mChartView_IDC,mChartView_IAC;
	private CreateLineChart myLineChart_VDC,myLineChart_VAC,myLineChart_IDC,myLineChart_IAC;

	private muchYaxisLineFactory muchYLineFactory = new muchYaxisLineFactory();
	
	private TextView titleview;
	private LinearLayout mLinear_VDC,mLinear_VAC,mLinear_IDC,mLinear_IAC;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_newprodistribution);
        bindview();
      	titleview.setText("¸ÅÂÊ·Ö²¼ DISTRIB");
 
    	myLineChart_VDC = new CreateLineChart();
		myLineChart_VDC.setAxesBlack();
		mChartView_VDC = myLineChart_VDC.oneLineChart_black(this);
		myLineChart_VDC.clearSeriesData();
		for(double x=-6;x<6;x+=0.2)
		{
			double y;
			y=1/(Math.sqrt(2*Math.PI))*Math.exp(-1*x*x/2);
			myLineChart_VDC.addXYData(x,y);
		}
		mLinear_VDC.addView(mChartView_VDC, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mChartView_VDC.repaint();
		
    	myLineChart_VAC = new CreateLineChart();
		myLineChart_VAC.setAxesBlack();
		mChartView_VAC = myLineChart_VAC.oneLineChart_black(this);
		
		myLineChart_VAC.clearSeriesData();
		for(double x=-6;x<6;x+=0.2)
		{
			double y;
			y=1/(Math.sqrt(2*Math.PI))*Math.exp(-1*x*x/2);
			myLineChart_VAC.addXYData(x,y);
		}
		mLinear_VAC.addView(mChartView_VAC, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mChartView_VAC.repaint();
		
		
    	myLineChart_IDC = new CreateLineChart();
		myLineChart_IDC.setAxesBlack();
		mChartView_IDC = myLineChart_IDC.oneLineChart_black(this);
		
		myLineChart_IDC.clearSeriesData();
		for(double x=-6;x<6;x+=0.2)
		{
			double y;
			y=1/(Math.sqrt(2*Math.PI))*Math.exp(-1*x*x/2);
			myLineChart_IDC.addXYData(x,y);
		}
		
		mLinear_IDC.addView(mChartView_IDC, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mChartView_IDC.repaint();
		
    	myLineChart_IAC = new CreateLineChart();
		myLineChart_IAC.setAxesBlack();
		mChartView_IAC = myLineChart_IAC.oneLineChart_black(this);
		
		myLineChart_IAC.clearSeriesData();
		for(double x=-6;x<6;x+=0.2)
		{
			double y;
			y=1/(Math.sqrt(2*Math.PI))*Math.exp(-1*x*x/2);
			myLineChart_IAC.addXYData(x,y);
		}
		
		mLinear_IAC.addView(mChartView_IAC, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mChartView_IAC.repaint();
		/*
		myLineChart.addXYData(0,0);
		myLineChart.addXYData((float)0.2,8);
		myLineChart.addXYData((float)0.3,10);
		myLineChart.addXYData((float)0.4,2);
		myLineChart.addXYData((float)0.5,0);
		*/
		/*
		mLinear.removeAllViews();
		//mLinear.clearAnimation();
		mChartView = muchYLineFactory.muchYLineGet(this);
		mLinear.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mChartView.repaint();
		*/
		
	 }	
	private void bindview()
	{
		titleview = (TextView) findViewById(R.id.title);
     	mLinear_VDC = (LinearLayout) findViewById(R.id.chart_VDC);
     	mLinear_VAC = (LinearLayout) findViewById(R.id.chart_VAC);
     	mLinear_IDC = (LinearLayout) findViewById(R.id.chart_IDC);
     	mLinear_IAC = (LinearLayout) findViewById(R.id.chart_IAC);
	}
   
}
