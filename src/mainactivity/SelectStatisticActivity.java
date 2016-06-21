package mainactivity;

import iThinkerChartFactory.DatePickerFragment;
import iThinkerChartFactory.createTableFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.learn.DeviceScanActivity;
import com.example.learn.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

public class SelectStatisticActivity extends Activity{

	private boolean isSelect = false,VDC_isSelect = false,VAC_isSelect=false,IDC_isSelect=false,IAC_isSelect=false; 
	private TextView titleview;
	private RelativeLayout select,VDC_select_layout,VAC_select_layout,IDC_select_layout,IAC_select_layout;
	private LinearLayout select_view_linearlayout;
	private ImageView select_index_image,VDC_select_image,VAC_select_image,IDC_select_image,IAC_select_image;
	private List<Map<String, Object>> listTitle;
	private List<Map<String, Object>> list;
	private TextView probability_distribution;
	private TextView select_report;
	private TextView tx_startdate,tx_starttime,tx_enddate,tx_endtime;
	
	private int start_hour;
	private int start_minute;
	private int end_hour;
	private int end_minute;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_newhistorydata);
        bindview();
   
    	titleview.setText("历史数据 HISTORY");
         
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
		

		
    }	
	
	private void bindview()
	{
	 	titleview = (TextView)findViewById(R.id.title);
	 	select = (RelativeLayout) findViewById(R.id.select);
	 	VDC_select_layout = (RelativeLayout) findViewById(R.id.VDC_select_layout);
	 	VAC_select_layout = (RelativeLayout) findViewById(R.id.VAC_select_layout);
	 	IDC_select_layout = (RelativeLayout) findViewById(R.id.IDC_select_layout);
	 	IAC_select_layout = (RelativeLayout) findViewById(R.id.IAC_select_layout);
	 	select_view_linearlayout = (LinearLayout) findViewById(R.id.select_view_linearlayout);
	 	select_index_image = (ImageView) findViewById(R.id.select_index_image);
	 	VDC_select_image = (ImageView) findViewById(R.id.VDC_select_image);
	 	VAC_select_image = (ImageView) findViewById(R.id.VAC_select_image);
	 	IDC_select_image = (ImageView) findViewById(R.id.IDC_select_image);
	 	IAC_select_image = (ImageView) findViewById(R.id.IAC_select_image);
		
	 	tx_startdate = (TextView) findViewById(R.id.tx_startdate);
	 	tx_starttime = (TextView) findViewById(R.id.tx_starttime);
	 	tx_enddate = (TextView) findViewById(R.id.tx_enddate);
	 	tx_endtime = (TextView) findViewById(R.id.tx_endtime);
	 	
		probability_distribution = (TextView)findViewById(R.id.probability_distribution);
		select_report = (TextView)findViewById(R.id.select_report);
	 	
		select.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isSelect = !isSelect;
				Log.d("ithinker", ""+isSelect);
				if(isSelect==true)
				{
					select_index_image.setImageResource(R.drawable.home_index_arrow_down);
					select_view_linearlayout.setVisibility(View.VISIBLE);
				}else
				{
					select_index_image.setImageResource(R.drawable.home_index_arrow);
					select_view_linearlayout.setVisibility(View.GONE);
				}
			}
		});
		
		VDC_select_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				VDC_isSelect = !VDC_isSelect;
				Log.d("ithinker", ""+VDC_isSelect);
				if(VDC_isSelect==true)
				{
					VDC_select_image.setImageResource(R.drawable.selectgreen);
				}else
				{
					VDC_select_image.setImageResource(R.drawable.noselect_circle);
				}
			}
		});
		VAC_select_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				VAC_isSelect = !VAC_isSelect;
				//Log.d("ithinker", ""+VDC_isSelect);
				if(VAC_isSelect==true)
				{
					VAC_select_image.setImageResource(R.drawable.selectgreen);
				}else
				{
					VAC_select_image.setImageResource(R.drawable.noselect_circle);
				}
			}
		});
		
		IDC_select_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				IDC_isSelect = !IDC_isSelect;
				//Log.d("ithinker", ""+VDC_isSelect);
				if(IDC_isSelect==true)
				{
					IDC_select_image.setImageResource(R.drawable.selectgreen);
				}else
				{
					IDC_select_image.setImageResource(R.drawable.noselect_circle);
				}
			}
		});
		IAC_select_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				IAC_isSelect = !IAC_isSelect;
				//Log.d("ithinker", ""+VDC_isSelect);
				if(IAC_isSelect==true)
				{
					IAC_select_image.setImageResource(R.drawable.selectgreen);
				}else
				{
					IAC_select_image.setImageResource(R.drawable.noselect_circle);
				}
			}
		});
		
        tx_startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.setDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    	tx_startdate.setText(String.format("%4d-%02d-%02d", year, monthOfYear + 1, dayOfMonth));
                    }
                });
                Log.d("ithinker", "startdate");
                datePicker.show(getFragmentManager(), "DatePicker");
            }
        });
        
        tx_starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				new TimePickerDialog(SelectStatisticActivity.this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                     @Override
                     public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    	 tx_starttime.setText(hourOfDay + ":" + minute);
                     }
                 }, start_hour, start_minute, true).show();
            }
        });
        

		probability_distribution.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("probability_distribution_button click");
				Intent intent = new Intent();
				intent.setClass(SelectStatisticActivity.this, proDistributionActivity.class);
				startActivity(intent);
			}
		});

		select_report.setOnClickListener(new OnClickListener() {

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
