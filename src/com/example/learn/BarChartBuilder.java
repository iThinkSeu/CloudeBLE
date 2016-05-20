package com.example.learn;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

public class BarChartBuilder extends Activity {

	private GraphicalView mChartView;
	
	//饼状图
	private CategorySeries mSeries = new CategorySeries("");
	  /** The main renderer for the main dataset. */
	private DefaultRenderer mRenderer = new DefaultRenderer();
	private GraphicalView mChartView_pie;
	/** Colors to be used for the pie slices. */
	private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN };

	protected void setChartSettings(XYMultipleSeriesRenderer renderer,
			String title, String xTitle, String yTitle, double xMin,
			double xMax, double yMin, double yMax, int axesColor,
			int labelsColor) {
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
	}

	protected XYMultipleSeriesRenderer buildBarRenderer(int[] colors) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(colors[i]);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

	protected XYMultipleSeriesDataset buildBarDataset(String[] titles,
			List<double[]> values) {// 柱形图的数据源和饼图差不多，也是由一些键值对组成
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			CategorySeries series = new CategorySeries(titles[i]);
			double[] v = values.get(i);
			int seriesLength = v.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(v[k]);
			}
			dataset.addSeries(series.toXYSeries());
		}
		return dataset;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pie_chart);
		LinearLayout mLinear = (LinearLayout) findViewById(R.id.chart);
		mLinear.setBackgroundColor(Color.rgb(0x37, 0x3d, 0x49));

		String[] titles = new String[] { "最近一周饮水量" };
		List<double[]> values = new ArrayList<double[]>();
		// values.add(new double[] { 1423, 1230, 1424, 1524, 1590, 1920, 1620
		// });
		double[] history = new double[7];
		for (int i = 0; i < 7; i++) {
			history[i] = (double)100*(i+10);
					
		}
		values.add(history);
		int[] colors = new int[] { Color.WHITE };
		XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);// 柱形图颜色设置
		setChartSettings(renderer, "最近一周饮水量", "时间", "饮水量/mL", 0, 8, 0, 2000,
				Color.GRAY, Color.LTGRAY);// 设置柱形图标题，横轴（X轴）、纵轴（Y轴）、最小的伸所刻度、最大的伸所刻度
		renderer.getSeriesRendererAt(0).setDisplayChartValues(true);// 在第0条柱形图上显示数据
		renderer.setXLabels(12);
		renderer.setYLabels(10);
		renderer.setXLabelsAlign(Align.LEFT);// 数据从左到右显示
		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setPanEnabled(true, false);
		renderer.setZoomEnabled(true);
		renderer.setZoomButtonsVisible(true);// 显示放大缩小功能按钮
		renderer.setZoomRate(1.1f);
		renderer.setBarSpacing(0.5f);// 柱形图间隔
		renderer.setChartValuesTextSize(20);
		renderer.setBackgroundColor(Color.rgb(0x37, 0x3d, 0x49));
		renderer.setMarginsColor(Color.rgb(0x37, 0x3d, 0x49));

		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		Date startDate = new Date();
		Calendar cd = Calendar.getInstance();
		cd.setTime(startDate);
		String dateStr = sdf.format(cd.getTime());
		renderer.addXTextLabel(1, dateStr);
		cd.add(Calendar.DATE, -1);
		for (int i = 7; i > 0; i--) {
			dateStr = sdf.format(cd.getTime());
			renderer.addXTextLabel(i, dateStr);
			cd.add(Calendar.DATE, -1);
			// System.out.println("add" + dateStr);
		}
		// renderer.addXTextLabel(2, "07-24");
		//
		// renderer.addXTextLabel(3, "07-25");
		//
		// renderer.addXTextLabel(4, "07-26");
		//
		// renderer.addXTextLabel(5, "07-27");
		//
		// renderer.addXTextLabel(6, "07-28");
		//
		// renderer.addXTextLabel(7, "07-29");

		if (mChartView == null) {// 构建柱形图
			mChartView = ChartFactory.getBarChartView(getApplicationContext(),
					buildBarDataset(titles, values), renderer, Type.DEFAULT);
			renderer.setClickEnabled(true);
			// renderer.set
			mLinear.addView(mChartView, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		} else
		mChartView.repaint();
		
		
		//饼状图
	    mRenderer.setZoomButtonsVisible(true);
	    mRenderer.setStartAngle(180);
	    mRenderer.setDisplayValues(true);

		
	    int piedata[] = new int[]{1,2,3,5};
	    for(int i=0;i<piedata.length;i++)
	    {
	    	mSeries.add("Series " + (mSeries.getItemCount() + 1), i);
	    	SimpleSeriesRenderer renderer_pie = new SimpleSeriesRenderer();
	        renderer_pie.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
	        mRenderer.addSeriesRenderer(renderer_pie);
	        //mChartView_pie.repaint();
	    }
	    
	    if (mChartView_pie == null) {
		      LinearLayout layout = (LinearLayout) findViewById(R.id.chart_pie);
		      mChartView_pie = ChartFactory.getPieChartView(this, mSeries, mRenderer);
		      mRenderer.setClickEnabled(true);
		      mChartView_pie.setOnClickListener(new View.OnClickListener() {
		        @Override
		        public void onClick(View v) {
		          SeriesSelection seriesSelection = mChartView_pie.getCurrentSeriesAndPoint();
		          if (seriesSelection == null) {
		            Toast.makeText(BarChartBuilder.this, "No chart element selected", Toast.LENGTH_SHORT)
		                .show();
		          } else {
		            for (int i = 0; i < mSeries.getItemCount(); i++) {
		              mRenderer.getSeriesRendererAt(i).setHighlighted(i == seriesSelection.getPointIndex());
		            }
		            mChartView_pie.repaint();
		            Toast.makeText(
		                BarChartBuilder.this,
		                "Chart data point index " + seriesSelection.getPointIndex() + " selected"
		                    + " point value=" + seriesSelection.getValue(), Toast.LENGTH_SHORT).show();
		          }
		        }
		      });
		      layout.addView(mChartView_pie, new LayoutParams(LayoutParams.FILL_PARENT,
		          LayoutParams.FILL_PARENT));
		    } else {
		    	mChartView_pie.repaint();
		    }
		    
	    	//mChartView_pie.repaint();
		
	}
}