package statisticscharline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iThinkerChartFactory.createTableFactory;

import com.example.learn.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class testTableViewActivity extends Activity{

	private List<Map<String, Object>> list;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_test_view_one);
        ListView lv = (ListView) findViewById(R.id.drawListView);
        
        createTableFactory myTable = new createTableFactory();
		list = myTable.getTableList();
		SimpleAdapter adapter = new SimpleAdapter(this,
				list, R.layout.list_item, new String[] {
						"id", "type","value","select"}, new int[] { R.id.txid,
						R.id.txtype,R.id.txvalue,R.id.txselect});
		lv.setAdapter(adapter);
		Map<String, Object> map = new HashMap<String, Object>();
		for(int i=1;i<60;i++)
		{
			map = new HashMap<String, Object>();
			map.put("id","+"+i);
			map.put("type","IDC");
			map.put("value","1.50KV");
			map.put("select","ºÏ¸ñ");
		    list.add(map);
		}
		//adapter.notifyDataSetChanged();
    }	
}
