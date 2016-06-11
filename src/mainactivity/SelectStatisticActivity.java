package mainactivity;

import iThinkerChartFactory.createTableFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.learn.DeviceScanActivity;
import com.example.learn.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SelectStatisticActivity extends Activity{

	private List<Map<String, Object>> listTitle;
	private List<Map<String, Object>> list;
	private Button probability_distribution_button;
	private Button select_report_button;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_selectstatistic);
        ListView lv = (ListView) findViewById(R.id.drawListView);
        ListView lvTitle = (ListView) findViewById(R.id.ListViewTitle);
        
        createTableFactory myTable = new createTableFactory();
		list = myTable.getTableList();
		listTitle = myTable.getTableList();
		SimpleAdapter adapter = new SimpleAdapter(this,list, R.layout.list_item, new String[] {"id", "type","value","select"}, new int[] { R.id.txid,R.id.txtype,R.id.txvalue,R.id.txselect});
		SimpleAdapter adapterTitle = new SimpleAdapter(this,listTitle, R.layout.list_item, new String[] {"id", "type","value","select"}, new int[] { R.id.txid,R.id.txtype,R.id.txvalue,R.id.txselect});

		lv.setAdapter(adapter);
		lvTitle.setAdapter(adapterTitle);
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> mapTitle = new HashMap<String, Object>();
		mapTitle = new HashMap<String, Object>();
	    mapTitle.put("id","序号");
	    mapTitle.put("type","模式");
	    mapTitle.put("value","结果");
	    mapTitle.put("select","分选");
		listTitle.add(mapTitle);
		for(int i=1;i<60;i++)
		{
			map = new HashMap<String, Object>();
			map.put("id","+"+i);
			map.put("type","IDC");
			map.put("value","1.50KV");
			map.put("select","合格");
		    list.add(map);
		}
		
		//adapter.notifyDataSetChanged();
		
		probability_distribution_button = (Button)findViewById(R.id.probability_distribution);
		probability_distribution_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("probability_distribution_button click");
				Intent intent = new Intent();
				intent.setClass(SelectStatisticActivity.this, proDistributionActivity.class);
				startActivity(intent);
			}
		});
		select_report_button = (Button)findViewById(R.id.select_report);
		select_report_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("select_report_button click");
				Intent intent = new Intent();
				intent.setClass(SelectStatisticActivity.this, SelectReportActivity.class);
				startActivity(intent);
			}
		});
    }	
}
