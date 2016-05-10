/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.learn;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer.FillOutsideLine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;

/**
 * Temperature sensor demo chart.
 */
public class SensorValuesChart extends AbstractDemoChart {
	private static final long HOUR = 3600 * 1000;

	private static final long DAY = HOUR * 24;

	private static final int HOURS = 24;

	/**
	 * Returns the chart name.
	 * 
	 * @return the chart name
	 */
	public String getName() {
		return "Sensor data";
	}

	/**
	 * Returns the chart description.
	 * 
	 * @return the chart description
	 */
	public String getDesc() {
		return "The temperature, as read from an outside and an inside sensors";
	}

	/**
	 * Executes the chart demo.
	 * 
	 * @param context
	 *            the context
	 * @return the built intent
	 */
	public Intent execute(Context context) {
		String[] titles = new String[] { "今天饮水量" };
		List<double[]> values = new ArrayList<double[]>();
		List<Date[]> x = new ArrayList<Date[]>();
		Date startDate = new Date();
		Calendar cd = Calendar.getInstance();
		// cd.setf
		cd.setTime(startDate);
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		String dateStr = sdf.format(cd.getTime());
		System.out.println(dateStr);
		// cd.set(cd.YEAR, cd.MONTH, cd.DAY_OF_MONTH, 0, 0);
		Date date = cd.getTime();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		cd.setTime(date);
		dateStr = sdf.format(cd.getTime());
		System.out.println(dateStr);
		for (int i = 0; i < titles.length; i++) {
			Date[] dates = new Date[HOURS];
			for (int j = 0; j < HOURS; j++) {
				dates[j] = cd.getTime();
				cd.add(Calendar.HOUR, 1);
				dateStr = sdf.format(cd.getTime());
				System.out.println(dateStr);
			}
			x.add(dates);
		}

		 values.add(new double[] { 100, 250, 370, 460, 500, 650, 800, 1200,
		 1260, 1350, 1410, 1410, 1410, 1410, 1410, 1410, 1410, 1410,
		 1410, 1410, 1410, 1410, 1410, 1410 });
		/*
		double[] dv = new double[24];
		double temp = 0;
		for (int i = 0; i < 24; i++) {
			temp += 200;
			dv[i] = temp;
		}
		values.add(dv);
		*/
		int[] colors = new int[] { Color.CYAN };
		PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE };
		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
		setChartSettings(renderer, "今日饮水曲线图", "时间", "饮水量/mL",
				x.get(0)[0].getTime(), x.get(0)[HOURS - 1].getTime(), 0, 2000,
				Color.WHITE, Color.WHITE);
		renderer.setXLabels(10);
		renderer.setXLabelsColor(Color.WHITE);
		renderer.setYLabels(12);
		renderer.setYLabelsColor(0, Color.WHITE);
		renderer.setMargins(new int[] { 35, 50, 10, 10 });
		renderer.setShowGrid(true);
		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(Color.rgb(0x37, 0x3d, 0x49));
		renderer.setMarginsColor(Color.rgb(0x37, 0x3d, 0x49));
		renderer.setPointSize(8);
		renderer.setLegendTextSize(20);
		FillOutsideLine fill = new FillOutsideLine(
				FillOutsideLine.Type.BOUNDS_ALL);
		fill.setColor(Color.CYAN);
		// xyRenderer.addFillOutsideLine(fill);
		// fill = new FillOutsideLine(FillOutsideLine.Type.BOUNDS_BELOW);
		// fill.setColor(Color.MAGENTA);
		// xyRenderer.addFillOutsideLine(fill);
		// fill = new FillOutsideLine(FillOutsideLine.Type.BOUNDS_ABOVE);
		// fill.setColor(Color.argb(255, 0, 200, 100));
		// fill.setFillRange(new int[] { 10, 19 });
		XYSeriesRenderer xyRenderer = (XYSeriesRenderer) renderer
				.getSeriesRendererAt(0);
	    xyRenderer.addFillOutsideLine(fill);

		return ChartFactory.getTimeChartIntent(context,
				buildDateDataset(titles, x, values), renderer, "hh:mm a");
	}
}
