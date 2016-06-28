package mainactivity;

import iThinkerChartFactory.DatePickerFragment;
import iThinkerChartFactory.createTableFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import measurepack.NewMeasureActivity;
import model.Post;

import org.json.JSONArray;
import org.json.JSONObject;

import util.OkHttpUtils;
import util.StrUtils;

import com.example.learn.DeviceScanActivity;
import com.example.learn.MainActivity;
import com.example.learn.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;

import android.view.ContextMenu.ContextMenuInfo; 

import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class SelectStatisticActivity extends Activity{
	
	private static final String TAG = "AtySelectStatistic";

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
	private TextView history_data_enter;
	
	private int start_hour;
	private int start_minute;
	private int end_hour;
	private int end_minute;
	
	
	Map<String, Object> map = new HashMap<String, Object>();
	Map<String, Object> mapTitle = new HashMap<String, Object>();
	SimpleAdapter adapter;
	
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
		adapter = new SimpleAdapter(this,list, R.layout.list_item, new String[] {"id", "type","value","select"}, new int[] { R.id.txid,R.id.txtype,R.id.txvalue,R.id.txselect});
		SimpleAdapter adapterTitle = new SimpleAdapter(this,listTitle, R.layout.list_item, new String[] {"id", "type","value","select"}, new int[] { R.id.txid,R.id.txtype,R.id.txvalue,R.id.txselect});

		lv.setAdapter(adapter);
		lvTitle.setAdapter(adapterTitle);

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
		list.remove(1);
		list.remove(5);
		//list.removeAll(list);
		//修改
		 ((Map)list.get(0)).put("type","ithinker");//修改值
		 adapter.notifyDataSetChanged();//刷新列表
		 
		 lv.setOnItemClickListener(new OnItemClickListener(){
			 
			 @Override
			 public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
			 //Map<String, Object> fruit = list.get(position);
				
			 //Toast.makeText(SelectStatisticActivity.this, "You clicked positon"+position, Toast.LENGTH_SHORT).show();
			 //Toast.makeText(SelectStatisticActivity.this, fruit.get("type"), Toast.LENGTH_SHORT).show();
			 }
		 });
		 
		 lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){
			 
			 @Override  
			 public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
			 {
			       menu.add(0, 0, 0, "删除"); 
                   menu.add(0, 1, 0, "取消"); 
                   //menu.add(0, 2, 0, "对比"); 
			 }
		 });

		//adapter.notifyDataSetChanged();
		
    }	
	
	   //长按菜单响应函数  
    @Override  
    public boolean onContextItemSelected(MenuItem item) {  
    	 AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item 
                 .getMenuInfo(); 
        //setTitle("点击了长按菜单里面的第"+item.getItemId()+"个项目"); 
        switch(item.getItemId())
        {
        	case 0: 
        		Toast.makeText(SelectStatisticActivity.this, item.getItemId()+"delete You clicked positon"+info.position, Toast.LENGTH_SHORT).show();
        		list.remove(info.position);  
        	    adapter.notifyDataSetChanged(); 
        	    break;
        	case 1:
          		Toast.makeText(SelectStatisticActivity.this, item.getItemId()+"取消 You clicked positon"+info.position, Toast.LENGTH_SHORT).show();
        	    break;
        	default:
          		Toast.makeText(SelectStatisticActivity.this, item.getItemId()+"default You clicked positon"+info.position, Toast.LENGTH_SHORT).show();
          		break;
        }
        //Toast.makeText(SelectStatisticActivity.this, item.getItemId()+"You clicked positon"+info.position, Toast.LENGTH_SHORT).show();
        //list.remove(info.position);  
        //adapter.notifyDataSetChanged();  
        return super.onContextItemSelected(item);  
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
	 	
	 	history_data_enter = (TextView) findViewById(R.id.history_data_enter);//确认查询按键
	 	
		probability_distribution = (TextView)findViewById(R.id.probability_distribution);
		select_report = (TextView)findViewById(R.id.select_report);
	 	
		select.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				select_function();
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
				
				   Log.d("ithinker", "start_hour"+start_hour);
            }
        });
        
        tx_enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.setDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    	tx_enddate.setText(String.format("%4d-%02d-%02d", year, monthOfYear + 1, dayOfMonth));
                    }
                });
                //Log.d("ithinker", "startdate");
                datePicker.show(getFragmentManager(), "DatePicker");
            }
        });
        
        tx_endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				new TimePickerDialog(SelectStatisticActivity.this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                     @Override
                     public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    	 tx_endtime.setText(hourOfDay + ":" + minute);
                     }
                 }, end_hour, end_minute, true).show();
            }
        });
        

        history_data_enter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Log.d("ithinker","history enter"+tx_enddate.getText().toString()+tx_endtime.getText().toString());
				String starttime = tx_startdate.getText().toString()+" "+tx_starttime.getText().toString();
				String endtime = tx_enddate.getText().toString()+" "+tx_endtime.getText().toString();
				history_data(starttime,endtime);
				Log.d("ithinker","history starttime"+starttime);
				list.clear();
				//select_function();
				
				
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
	
	private void select_function()
	{
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

	private void history_data(String starttime,String endtime){

        ArrayMap<String,String> param = new ArrayMap<>();
        String token = StrUtils.token(SelectStatisticActivity.this);
        param.put("token", token);
        //param.put("token", "18d54ec8446d03451f5552033c64dbda");
		//param.put("datatype", datatype);
		//param.put("value", value+"");   
        param.put("starttime", starttime);
        param.put("endtime", endtime);
	    OkHttpUtils.post(StrUtils.HISTORY_DATA, param, TAG, new OkHttpUtils.SimpleOkCallBack() {
	        @Override
	        public void onResponse(String s) {
	            //Log.d("ithinker", "post data"+s);
	            JSONObject j = OkHttpUtils.parseJSON(SelectStatisticActivity.this, s);
	            if (j == null) {
	                return;
	            }
	            Log.d("ithinker", "post successful"+s);
	            JSONArray result = j.optJSONArray("result");
	            for(int i = 0; i<result.length(); i++){
	            	addTablePost(Post.fromJSON(result.optJSONObject(i)));
                }
	            
	        }
	    });
	}
	
	private void addTablePost(Post post)
	{
		map = new HashMap<String, Object>();
		map.put("id","+"+post.timestamp);
		map.put("type",post.datatype);
		map.put("value",post.value);
		map.put("select","合格");
	    list.add(map);
	}

}
