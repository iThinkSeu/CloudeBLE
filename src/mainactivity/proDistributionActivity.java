package mainactivity;

import iThinkerChartFactory.CreateLineChart;
import iThinkerChartFactory.muchYaxisLineFactory;
import iThinkerChartFactory.mybarchart;

import org.achartengine.GraphicalView;

import com.example.learn.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class proDistributionActivity extends Activity{

private GraphicalView mChartView;
private CreateLineChart myLineChart = new CreateLineChart();
private muchYaxisLineFactory muchYLineFactory = new muchYaxisLineFactory();
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.aty_prodistribution);
        
		LinearLayout mLinear = (LinearLayout) findViewById(R.id.chart);
		//mLinear.setBackgroundColor(Color.rgb(0x37, 0x3d, 0x49));
		
		myLineChart.setAxesBlack();
		mChartView = myLineChart.oneLineChart_rms(this);
		mLinear.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mChartView.repaint();
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
}
