package mainactivity;

import iThinkerChartFactory.createTableFactory;
import iThinkerChartFactory.mybarchart;
import iThinkerChartFactory.pieChartFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.achartengine.GraphicalView;

import com.example.learn.BarChartBuilder;
import com.example.learn.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SelectReportActivity  extends Activity{
	private List<Map<String, Object>> listTitle;
	private List<Map<String, Object>> list;
	private Button pie_button;
	private Button bar_button;
	
	private TextView titleview;
	private ListView lv;
	private ListView lvTitle;
	
	private GraphicalView mChartView_VDC;
	private GraphicalView mChartView_VAC;
	private GraphicalView mChartView_IDC;
	private GraphicalView mChartView_IAC;
	private GraphicalView mChartView_BAR;
	
	private LinearLayout mLinear_VDC;
	private LinearLayout mLinear_VAC;
	private LinearLayout mLinear_IDC;
	private LinearLayout mLinear_IAC;
	private LinearLayout mLinear_BAR;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_listselectreport);
        bindview();
    	titleview.setText("分选报表 REPORT");
      
        
        createTableFactory myTable = new createTableFactory();
		list = myTable.getTableList();
		listTitle = myTable.getTableList();
		SimpleAdapter adapter = new SimpleAdapter(this,list, R.layout.list_item_select_report, new String[] {"measure_model", "nopass","pass"}, new int[] { R.id.txmeasure_model,R.id.txnopass,R.id.txpass});
		SimpleAdapter adapterTitle = new SimpleAdapter(this,listTitle, R.layout.list_item_select_report, new String[] {"measure_model", "nopass","pass"}, new int[] { R.id.txmeasure_model,R.id.txnopass,R.id.txpass});

		lv.setAdapter(adapter);
		lvTitle.setAdapter(adapterTitle);
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> mapTitle = new HashMap<String, Object>();
		mapTitle = new HashMap<String, Object>();
	    mapTitle.put("measure_model","测量模式");
	    mapTitle.put("nopass","不合格数");
	    mapTitle.put("pass","合格数");
		listTitle.add(mapTitle);
		for(int i=1;i<5;i++)
		{
			map = new HashMap<String, Object>();
			map.put("measure_model","直流电压");
			map.put("nopass","20");
			map.put("pass","31");
		    list.add(map);
		}
		
		pieChartFactory ipiechart = new pieChartFactory();
		
		int data[] = {20,60};
		ipiechart.set_pietitle("直流电压");
		mChartView_VDC = ipiechart.create_pie_chart(this,data);
		mLinear_VDC.addView(mChartView_VDC, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mChartView_VDC.repaint();
		                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
		data[0] = 3;
		data[1] = 7;
		ipiechart.set_pietitle("交流电压");
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
		
		
		mLinear_BAR = (LinearLayout) findViewById(R.id.bar_chart);
		//mLinear.setBackgroundColor(Color.rgb(0x37, 0x3d, 0x49));
		
		mybarchart ibarchar = new mybarchart();
		float [][]y = {
						{100,200,700,300},
						{200,400,500,400}};
		mChartView_BAR = ibarchar.createTwoBarchart(this,y);
		mLinear_BAR.addView(mChartView_BAR, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mChartView_BAR.repaint();
		//adapter.notifyDataSetChanged();
		/*
		pie_button = (Button)findViewById(R.id.pie_select_report);
		pie_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("BarChartBuilder click");
				Intent intent = new Intent();
				intent.setClass(SelectReportActivity.this, piechartActivity.class);
				startActivity(intent);
			}                                                                                                                                                                                     
		});
		
		bar_button = (Button)findViewById(R.id.bar_select_report);
		bar_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("BarChartBuilder click");
				Intent intent = new Intent();
				intent.setClass(SelectReportActivity.this, barchartActivity.class);
				startActivity(intent);
			}
		});
		*/
    }	
	
	private void bindview()
	{
		titleview = (TextView) findViewById(R.id.title);
		lv = (ListView) findViewById(R.id.drawListView);
	    lvTitle = (ListView) findViewById(R.id.ListViewTitle);
	    
		mLinear_VDC = (LinearLayout) findViewById(R.id.chart_VDC);
		mLinear_VAC = (LinearLayout) findViewById(R.id.chart_VAC);
		mLinear_IDC = (LinearLayout) findViewById(R.id.chart_IDC);
		mLinear_IAC = (LinearLayout) findViewById(R.id.chart_IAC);
	}
	
}
