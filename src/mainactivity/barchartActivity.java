package mainactivity;

import iThinkerChartFactory.mybarchart;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.example.learn.R;

public class barchartActivity extends Activity{
	
	private GraphicalView mChartView;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.pie_chart);
        
		LinearLayout mLinear = (LinearLayout) findViewById(R.id.chart);
		//mLinear.setBackgroundColor(Color.rgb(0x37, 0x3d, 0x49));
		
		mybarchart ibarchar = new mybarchart();
		float [][]y = {
						{100,200,700,300,200,300},
						{200,400,500,400,300,500}};
		mChartView = ibarchar.createTwoBarchart(this,y);
		mLinear.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mChartView.repaint();
	 }	
}
