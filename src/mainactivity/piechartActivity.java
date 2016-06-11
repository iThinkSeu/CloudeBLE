package mainactivity;

import iThinkerChartFactory.mybarchart;
import iThinkerChartFactory.pieChartFactory;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.example.learn.R;

public class piechartActivity extends Activity{
	
	private GraphicalView mChartView_VDC;
	private GraphicalView mChartView_VAC;
	private GraphicalView mChartView_IDC;
	private GraphicalView mChartView_IAC;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.aty_listpiechart);
        
		LinearLayout mLinear_VDC = (LinearLayout) findViewById(R.id.chart_VDC);
		LinearLayout mLinear_VAC = (LinearLayout) findViewById(R.id.chart_VAC);
		LinearLayout mLinear_IDC = (LinearLayout) findViewById(R.id.chart_IDC);
		LinearLayout mLinear_IAC = (LinearLayout) findViewById(R.id.chart_IAC);
		
		//mLinear_VDC.setBackgroundColor(Color.rgb(0x37, 0x3d, 0x49));
		
		pieChartFactory ipiechart = new pieChartFactory();
	
		int data[] = {20,60};
		mChartView_VDC = ipiechart.create_pie_chart(this,data);
		mLinear_VDC.addView(mChartView_VDC, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mChartView_VDC.repaint();
		
		data[0] = 3;
		data[1] = 7;
		mChartView_VAC = ipiechart.create_pie_chart(this,data);
		mLinear_VAC.addView(mChartView_VAC, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mChartView_VAC.repaint();
		
		data[0] = 9;
		data[1] = 7;
		mChartView_IDC = ipiechart.create_pie_chart(this,data);
		mLinear_IDC.addView(mChartView_IDC, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mChartView_IDC.repaint();
		
		data[0] = 9;
		data[1] = 17;
		mChartView_IAC = ipiechart.create_pie_chart(this,data);
		mLinear_IAC.addView(mChartView_IAC, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mChartView_IAC.repaint();
		
	 }	


}
