package iThinkerChartFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class createTableFactory {
	public List<Map<String, Object>> buildListForSimpleAdapter() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// ImageView imgA = (ImageView) findViewById(R.id.imgDrawChart);
		Map<String, Object> map = new HashMap<String, Object>();
		// imgA.setScaleType(ImageView.ScaleType.CENTER_CROP);
		// imgA.setImageResource(R.drawable.icon72);
		map.put("chatinfo",
				"                          历史饮水记录                        ");
		map.put("image", null);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("chatinfo", "    时间              日平均饮水量            评分");
		map.put("image", null);
		list.add(map);
		// 向列表容器中添加数据（每列中包括一个头像和聊天信息）
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date();
		String s = sdf.format(d);
		map = new HashMap<String, Object>();
		map.put("chatinfo", s + "          1490mL             ");
		//map.put("image", R.drawable.star5);
		list.add(map);
		Calendar c = Calendar.getInstance();
		long t = c.getTimeInMillis();
		long l = t - 24 * 3600 * 1000;
		d = new Date(l);
		s = sdf.format(d);
		map = new HashMap<String, Object>();
		map.put("chatinfo", s + "          1150mL             ");
		//map.put("image", R.drawable.star3);
		list.add(map);
		t = c.getTimeInMillis();
		l = t - 24 * 3600 * 1000 * 2;
		d = new Date(l);
		s = sdf.format(d);
		map = new HashMap<String, Object>();
		map.put("chatinfo", s + "          1650mL             ");
		//map.put("image", R.drawable.star4);
		list.add(map);
		return list;
	}
	
	public List<Map<String, Object>> getTableList() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// ImageView imgA = (ImageView) findViewById(R.id.imgDrawChart);
		Map<String, Object> map = new HashMap<String, Object>();
		// imgA.setScaleType(ImageView.ScaleType.CENTER_CROP);
		// imgA.setImageResource(R.drawable.icon72);
	    //map.put("id","序号");
		//map.put("type","模式");
		//map.put("value","结果");
		//map.put("select","分选");
		//list.add(map);
		/*
		map = new HashMap<String, Object>();
		map.put("id","1");
		map.put("type","IDC");
		map.put("value","1.50KV");
		map.put("select","合格");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("id","2");
		map.put("type","IDC");
		map.put("value","1.50KV");
		map.put("select","合格");
		list.add(map);
		for(int i=3;i<30;i++)
		{
			map = new HashMap<String, Object>();
			map.put("id","+"+i);
			map.put("type","IDC");
			map.put("value","1.50KV");
			map.put("select","合格");
		    list.add(map);
		}
		*/
		return list;
	}



}
