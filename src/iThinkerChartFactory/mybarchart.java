package iThinkerChartFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class mybarchart {
	
	//3个功能函数
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

	
	public  GraphicalView createOneBarchart(Context context)
	{
		GraphicalView mChartView = null;

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
				Color.WHITE, Color.WHITE);// 设置柱形图标题，横轴（X轴）、纵轴（Y轴）、最小的伸所刻度、最大的伸所刻度
		renderer.getSeriesRendererAt(0).setDisplayChartValues(true);// 在第0条柱形图上显示数据
		renderer.setXLabels(12);
		renderer.setYLabels(10);
		renderer.setXLabelsAlign(Align.LEFT);// 数据从左到右显示
		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setPanEnabled(true);
		renderer.setZoomEnabled(true);
		renderer.setZoomButtonsVisible(true);// 显示放大缩小功能按钮
		renderer.setZoomRate(1.2f);
		renderer.setBarSpacing(2.5f);// 柱形图间隔
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
		
		renderer.setClickEnabled(true);
		mChartView = ChartFactory.getBarChartView(context,buildBarDataset(titles, values), renderer, Type.DEFAULT);
	
		return mChartView;
	}
	
	
	//双bar
	 private static final int SERIES_NR = 2;
	 
    
	 public XYMultipleSeriesRenderer getBarDemoRenderer() {
		    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		    renderer.setAxisTitleTextSize(26);
		    renderer.setChartTitleTextSize(30);
		    renderer.setLabelsTextSize(25);
		    renderer.setLegendTextSize(25);
			renderer.setXLabelsAlign(Align.LEFT);// 数据从左到右显示
			renderer.setYLabelsAlign(Align.LEFT);
			renderer.setXLabelsColor(Color.BLACK);
	        renderer.setYLabelsColor(0, Color.BLACK);
			renderer.setAxesColor(Color.BLACK);
			renderer.setLabelsColor(Color.BLACK);
			renderer.setXAxisMin(0.5);
			renderer.setXAxisMax(4.5);
			renderer.setYAxisMin(0.5);
			//renderer.setXAxisMax(6.5);
			renderer.addXTextLabel(0.75, "直流电压");//在指定坐标处显示文字
			renderer.addXTextLabel(1.75, "交流电压");//在指定坐标处显示文字
			renderer.addXTextLabel(2.75, "直流电流");//在指定坐标处显示文字
			renderer.addXTextLabel(3.75, "交流电流");//在指定坐标处显示文字
			//renderer.addXTextLabel(4.75, "直流时间");//在指定坐标处显示文字
			//renderer.addXTextLabel(5.75, "交流时间");//在指定坐标处显示文字
			renderer.setXLabels(0);//设置x轴上的下标数量
			renderer.setLabelsTextSize(25);//设置坐标轴上数字的大小
			renderer.setFitLegend(true);
			
			renderer.setPanEnabled(true);
			renderer.setZoomEnabled(true);
			//renderer.setZoomButtonsVisible(true);// 显示放大缩小功能按钮
			renderer.setZoomRate(1.0f);
			renderer.setBarSpacing(0.5f);// 柱形图间隔
			renderer.setBackgroundColor(Color.rgb(0xff, 0xff, 0xff));
			renderer.setMarginsColor(Color.rgb(0xff, 0xff, 0xff));
	
		    renderer.setMargins(new int[] {20, 30, 15, 0});
		    SimpleSeriesRenderer r = new SimpleSeriesRenderer();
		    r.setColor(Color.rgb(43, 201, 219));
		    renderer.addSeriesRenderer(r);
		    r = new SimpleSeriesRenderer();
		    r.setColor( Color.rgb(227, 127, 120));
		    renderer.addSeriesRenderer(r);
			renderer.getSeriesRendererAt(0).setDisplayChartValues(true);// 在第0条柱形图上显示数据
			renderer.getSeriesRendererAt(1).setDisplayChartValues(true);// 在第0条柱形图上显示数据

		    return renderer;
		  }

	 private XYMultipleSeriesDataset getBarDemoDataset(float[][] y) {
		    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		    final int nr = 10;
		    Random r = new Random();
		    if(y.length<=0) return null;
		    for (int i = 0; i < y.length; i++) {
		      CategorySeries series = new CategorySeries(i%2==0?"合格":"不合格");
		      for (int k = 0; k < y[0].length; k++) {
		        series.add("文字",y[i][k]);
		      }
		      dataset.addSeries(series.toXYSeries());
		    }
		    return dataset;
		  }
	 
	  private void setChartSettings(XYMultipleSeriesRenderer renderer) {
		    renderer.setChartTitle("Chart demo");
		    renderer.setXTitle("x values");
		    renderer.setYTitle("y values");
		    //renderer.setXAxisMin(0.5);
		    //renderer.setXAxisMax(10.5);
		    //renderer.setYAxisMin(0);
		    //renderer.setYAxisMax(210);
		  }

	
	public  GraphicalView createTwoBarchart(Context context,float[][] y)
	{
		GraphicalView mChartView = null;
		XYMultipleSeriesRenderer renderer = getBarDemoRenderer();
		setChartSettings(renderer);
		//renderer.setClickEnabled(false);
		//renderer.setClickEnabled(true);
		mChartView = ChartFactory.getBarChartView(context,getBarDemoDataset(y), renderer, Type.DEFAULT);
	
		return mChartView;
	}
	
}
