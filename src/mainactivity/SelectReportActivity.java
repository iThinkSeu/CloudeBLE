package mainactivity;

import iThinkerChartFactory.createTableFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.learn.BarChartBuilder;
import com.example.learn.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SelectReportActivity  extends Activity{
	private List<Map<String, Object>> listTitle;
	private List<Map<String, Object>> list;
	private Button pie_button;
	private Button bar_button;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_selectreport);
        ListView lv = (ListView) findViewById(R.id.drawListView);
        ListView lvTitle = (ListView) findViewById(R.id.ListViewTitle);
        
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
		for(int i=1;i<6;i++)
		{
			map = new HashMap<String, Object>();
			map.put("measure_model","直流电压");
			map.put("nopass","20");
			map.put("pass","31");
		    list.add(map);
		}
		
		//adapter.notifyDataSetChanged();
		
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

    }	
	
}
