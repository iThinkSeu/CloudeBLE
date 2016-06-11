package mainactivity;

import iThinkerChartFactory.createTableFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.learn.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class paraCorrectionActivity extends Activity{

	private List<Map<String, Object>> listTitle;
	private List<Map<String, Object>> list;

	private Button mAdd;/** Button for adding entered data to table */
	private EditText mX;/** Edit text field for entering the X value of the data to be added. */
	private EditText mY;/** Edit text field for entering the Y value of the data to be added. */ 
	private Map<String, Object> map = new HashMap<String, Object>();
	private int txid;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_paracorrection);
        ListView lv = (ListView) findViewById(R.id.drawListView);
        ListView lvTitle = (ListView) findViewById(R.id.ListViewTitle);
        
        //添加
        mX = (EditText) findViewById(R.id.xValue);
        mY = (EditText) findViewById(R.id.yValue);
        mAdd = (Button) findViewById(R.id.add);
        
        createTableFactory myTable = new createTableFactory();
		list = myTable.getTableList();
		listTitle = myTable.getTableList();
		SimpleAdapter adapter = new SimpleAdapter(this,list, R.layout.list_item_paracorrection, new String[] {"id", "real_value","measure_value"}, new int[] { R.id.txid,R.id.txreal_value,R.id.txmeasure_value});
		SimpleAdapter adapterTitle = new SimpleAdapter(this,listTitle, R.layout.list_item_paracorrection, new String[] {"id", "real_value","measure_value"}, new int[] { R.id.txid,R.id.txreal_value,R.id.txmeasure_value});

		lv.setAdapter(adapter);
		lvTitle.setAdapter(adapterTitle);
	
		Map<String, Object> mapTitle = new HashMap<String, Object>();
		mapTitle = new HashMap<String, Object>();
	    mapTitle.put("id","序号");
	    mapTitle.put("real_value","实际值");
	    mapTitle.put("measure_value","测量值");
		listTitle.add(mapTitle);
		txid = 0;
		for(int i=1;i<6;i++)
		{
			txid++;
			map = new HashMap<String, Object>();
			map.put("id","+"+txid);
			map.put("real_value","1.51KV");
			map.put("measure_value","1.50KV");
		    list.add(map);
		}
		
		//adapter.notifyDataSetChanged();
		
		 mAdd.setOnClickListener(new View.OnClickListener() {
		      public void onClick(View v) {
		        double x = 0;
		        double y = 0;
		        try {
		          x = Double.parseDouble(mX.getText().toString());
		        } catch (NumberFormatException e) {
		          mX.requestFocus();
		          return;
		        }
		        try {
		          y = Double.parseDouble(mY.getText().toString());
		        } catch (NumberFormatException e) {
		          mY.requestFocus();
		          return;
		        }
		        // add a new data point to the current series
		        map = new HashMap<String, Object>();
		        txid++;
				map.put("id","+"+txid);
				map.put("real_value",x+"KV");
				map.put("measure_value",y+"KV");
			    list.add(map);
			    
		        mX.setText("");
		        mY.setText("");
		        mX.requestFocus();
		        // repaint the chart such as the newly added point to be visible
		      }
		    });
		
    }	
}
