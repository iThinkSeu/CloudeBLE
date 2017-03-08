package iThinkerChartFactory;

import java.util.Date;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.example.learn.R;

public class CreateLineChart {
    private static final long HOUR = 3600 * 1000;

    private static final long DAY = HOUR * 24;
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	private TimeSeries series = new TimeSeries("测量值");
	private int AxesColor = Color.WHITE;
	private int xLableColor = Color.WHITE;
	private int yLableColor = Color.WHITE;
	private int LableColor = Color.WHITE;
	private int lineColor = Color.WHITE;
	private int gridColor = Color.WHITE;
	
    public GraphicalView oneLineChart(Context context)
    {
    	
			GraphicalView mChartView;
			XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
			TimeSeries mCurrentSeries;
			XYSeriesRenderer mCurrentRenderer;
			
			  //画曲线
			// set some properties on the main renderer
			mRenderer.setApplyBackgroundColor(true);
			mRenderer.setBackgroundColor(Color.argb(0x00, 0x01, 0x01, 0x01));
			//mRenderer.setBackgroundColor(Color.rgb(0x1B, 0x48, 0x65));
			
			
			
			//mRenderer.setBackgroundColor(Color.rgb(0x44, 0x7E, 0x77));
			mRenderer.setAxisTitleTextSize(16);
			mRenderer.setChartTitleTextSize(20);
			mRenderer.setLabelsTextSize(15);
			mRenderer.setLegendTextSize(15);
			mRenderer.setMarginsColor(Color.argb(0x00, 0x01, 0x01, 0x01));
	
			mRenderer.setMargins(new int[] { 20, 30, 15, 10 });
			//mRenderer.setMarginsColor(Color.rgb(0xea, 0xea, 0xea));
			mRenderer.setZoomButtonsVisible(true);
			mRenderer.setPointSize(4);
			
			mRenderer.setShowLegend(true);
			mRenderer.setChartTitle("实时曲线");  
			mRenderer.setXTitle("横坐标");  
			mRenderer.setYTitle("纵坐标");  
			mRenderer.setXLabels(5);//设置x轴显示6个点,根据setChartSettings的最大值和最小值自动计算点的间隔   
			mRenderer.setXLabelsColor(Color.WHITE);
			mRenderer.setYLabels(8);//设置y轴数据显示多少个点
			mRenderer.setYLabelsColor(0, Color.WHITE);
			mRenderer.setXLabelsAlign(Align.CENTER);
		    mRenderer.setYLabelsAlign(Align.LEFT);
		    //颜色设置
		    mRenderer.setXLabelsColor(xLableColor);
		    mRenderer.setYLabelsColor(0, yLableColor);
		    mRenderer.setAxesColor(AxesColor);
		    mRenderer.setLabelsColor(LableColor);
		    
			//mRenderer.setShowGrid(false);
			mRenderer.setShowGridX(true);
			mRenderer.setShowGridY(false);
			//mRenderer.setGridColor(gridColor);
			//mRenderer.setAxesColor(Color.CYAN); ，坐标轴颜色
	
			
			// create a new series of data
		    mRenderer.setPanEnabled(true);
		    mRenderer.setZoomEnabled(true,true);
		    mRenderer.setZoomButtonsVisible(true);// 显示放大缩小功能按钮
			
			mDataset.addSeries(series);
			//mCurrentSeries = series;
			// create a new renderer for the new series
			XYSeriesRenderer renderer = new XYSeriesRenderer();
			mRenderer.addSeriesRenderer(renderer);
			// set some renderer properties
			renderer.setPointStyle(PointStyle.CIRCLE);
			renderer.setColor(lineColor);
			//renderer.setFillPoints(true);
			
			//renderer.setDisplayChartValues(false); //坐标点显示大小
			//renderer.setDisplayChartValuesDistance(10);
			//renderer.setLineWidth(4);
			mCurrentRenderer = renderer;
			
			
			long now = Math.round(new Date().getTime());
			Date[] dates = new Date[4];
			for(int j=0;j<4;j++)
			{
				dates[j] = new Date(now - (4 - j) * 1000);
			}
			
			series.add(dates[0], 2.0);
			series.add(dates[1], 6.0);
			series.add(dates[2], 7.0);
			series.add(dates[3], 8.05);
			
			
			//mRenderer.setPanEnabled(false);
			//mRenderer.setZoomEnabled(true);
		    mRenderer.setZoomEnabled(true,true);
			mRenderer.setZoomButtonsVisible(true);// 显示放大缩小功能按钮
			mRenderer.setClickEnabled(true);
 	        mChartView = ChartFactory.getTimeChartView(context, mDataset, mRenderer, "HH:mm:ss a");
  	        // enable the chart click events
			return mChartView;
    }
    
    public GraphicalView oneLineChart_black(Context context)
    {
    	
			GraphicalView mChartView;
			XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
			TimeSeries mCurrentSeries;
			XYSeriesRenderer mCurrentRenderer;
			
			  //画曲线
			// set some properties on the main renderer
			mRenderer.setApplyBackgroundColor(true);
			//mRenderer.setBackgroundColor(Color.argb(0x00, 0x01, 0x01, 0x01));
			//mRenderer.setBackgroundColor(Color.rgb(0x1B, 0x48, 0x65));
			
			
			
			//mRenderer.setBackgroundColor(Color.rgb(0x44, 0x7E, 0x77));
			mRenderer.setAxisTitleTextSize(16);
			mRenderer.setChartTitleTextSize(20);
			mRenderer.setLabelsTextSize(15);
			mRenderer.setLegendTextSize(15);
			mRenderer.setMarginsColor(Color.argb(0x00, 0x01, 0x01, 0x01));
			mRenderer.setMargins(new int[] { 20, 30, 15, 10 });
			//mRenderer.setMarginsColor(Color.rgb(0xea, 0xea, 0xea));
			mRenderer.setZoomButtonsVisible(true);
			mRenderer.setPointSize(4);
			
			mRenderer.setShowLegend(true);
			//mRenderer.setChartTitle("实时曲线");  
			mRenderer.setXTitle("横坐标");  
			mRenderer.setYTitle("纵坐标");  
			mRenderer.setXLabels(8);//设置x轴显示6个点,根据setChartSettings的最大值和最小值自动计算点的间隔   
			mRenderer.setXLabelsColor(Color.WHITE);
			mRenderer.setYLabels(9);
			mRenderer.setYLabelsColor(0, Color.WHITE);
			mRenderer.setXLabelsAlign(Align.CENTER);
		    mRenderer.setYLabelsAlign(Align.LEFT);
		    //颜色设置
		    mRenderer.setXLabelsColor(xLableColor);
		    mRenderer.setYLabelsColor(0, yLableColor);
		    mRenderer.setAxesColor(AxesColor);
		    mRenderer.setLabelsColor(LableColor);
		    
			//mRenderer.setShowGrid(false);
			//mRenderer.setShowGridX(true);
			//mRenderer.setShowGridY(true);
			//mRenderer.setGridColor(Color.GRAY);
			//mRenderer.setAxesColor(Color.CYAN); ，坐标轴颜色
	
			
		    mRenderer.setPanEnabled(true);
		    mRenderer.setZoomEnabled(true,true);
		    mRenderer.setZoomButtonsVisible(true);// 显示放大缩小功能按钮
			// create a new series of data

		    series.setTitle("分布曲线");
			mDataset.addSeries(series);
			//mCurrentSeries = series;
			// create a new renderer for the new series
			XYSeriesRenderer renderer = new XYSeriesRenderer();
			mRenderer.addSeriesRenderer(renderer);
			// set some renderer properties
			//renderer.setPointStyle(PointStyle.CIRCLE);
			renderer.setColor(lineColor);
			renderer.setFillPoints(true);
			renderer.setDisplayChartValues(false);
			renderer.setDisplayChartValuesDistance(10);
			//renderer.setLineWidth(4);
			mCurrentRenderer = renderer;
			
		
			
			mRenderer.setZoomButtonsVisible(true);
 	        mChartView = ChartFactory.getLineChartView(context, mDataset, mRenderer);
  	        // enable the chart click events
  	        //mRenderer.setClickEnabled(true);
  	     
  	        //mRenderer.setSelectableBuffer(10);
			return mChartView;
    }
    
    public GraphicalView oneLineChart_rms(Context context)
    {
    	
			GraphicalView mChartView;
			XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
			TimeSeries mCurrentSeries;
			XYSeriesRenderer mCurrentRenderer;
			
			  //画曲线
			// set some properties on the main renderer
			mRenderer.setApplyBackgroundColor(true);
			//mRenderer.setBackgroundColor(Color.argb(0x00, 0x01, 0x01, 0x01));
			//mRenderer.setBackgroundColor(Color.rgb(0x1B, 0x48, 0x65));
			
			//mRenderer.setBackgroundColor(Color.rgb(0x44, 0x7E, 0x77));
			mRenderer.setAxisTitleTextSize(20);
			mRenderer.setChartTitleTextSize(20);
			mRenderer.setLabelsTextSize(25);
			mRenderer.setLegendTextSize(15);
			mRenderer.setMarginsColor(Color.argb(0x00, 0x01, 0x01, 0x01));
			mRenderer.setMargins(new int[] { 20, 30, 15, 10 });
			//mRenderer.setMarginsColor(Color.rgb(0xea, 0xea, 0xea));
			mRenderer.setZoomButtonsVisible(true);
			
			/*设置曲线*/
			mRenderer.setPointSize(4);
			
			
			/*设置坐标轴*/
			mRenderer.setShowLegend(true);
			//mRenderer.setChartTitle("实时曲线");  
			//mRenderer.setXTitle("横坐标");  
			mRenderer.setYTitle("RESULT/KV");  
			mRenderer.setXLabels(6);//设置x轴显示6个点,根据setChartSettings的最大值和最小值自动计算点的间隔   
			mRenderer.setXLabelsColor(Color.WHITE);
			mRenderer.setYLabels(9);
			mRenderer.setYLabelsColor(0, Color.WHITE);
			mRenderer.setXLabelsAlign(Align.CENTER);
		    mRenderer.setYLabelsAlign(Align.LEFT);
		    //颜色设置
		    mRenderer.setXLabelsColor(xLableColor);
		    mRenderer.setYLabelsColor(0, yLableColor);
		    mRenderer.setAxesColor(AxesColor);
		    mRenderer.setLabelsColor(LableColor);
		    
			mRenderer.setShowGrid(false);
			mRenderer.setShowGridX(true);
			//mRenderer.setShowGridY(true);
			//mRenderer.setGridColor(Color.GRAY);
			//mRenderer.setAxesColor(Color.CYAN); ，坐标轴颜色
	
			
		    mRenderer.setPanEnabled(false);
		    mRenderer.setZoomEnabled(false,false);
		    mRenderer.setZoomButtonsVisible(true);// 显示放大缩小功能按钮
			// create a new series of data

			
			mDataset.addSeries(series);
			//mCurrentSeries = series;
			// create a new renderer for the new series
			XYSeriesRenderer renderer = new XYSeriesRenderer();
			mRenderer.addSeriesRenderer(renderer);
			// set some renderer properties
			renderer.setPointStyle(PointStyle.CIRCLE);
			renderer.setColor(lineColor);
			renderer.setFillPoints(true);
			renderer.setDisplayChartValues(false);
			renderer.setDisplayChartValuesDistance(10);
			//renderer.setLineWidth(4);
			mCurrentRenderer = renderer;
			
			
			long now = Math.round(new Date().getTime());
			Date[] dates = new Date[4];
			for(int j=0;j<4;j++)
			{
				dates[j] = new Date(now - (4 - j) * 1000);
			}
			
			
			series.add(dates[0], 2.0);
			series.add(dates[1], 6.0);
			series.add(dates[2], 7.0);
			series.add(dates[3], 18.05);
			
			mRenderer.setZoomButtonsVisible(true);
 	        //mChartView = ChartFactory.getLineChartView(context, mDataset, mRenderer);
			 mChartView = ChartFactory.getTimeChartView(context, mDataset, mRenderer, "HH:mm:ss a");
  	        // enable the chart click events
  	        //mRenderer.setClickEnabled(true);
  	     
  	        //mRenderer.setSelectableBuffer(10);
			return mChartView;
    }
    public void addSeriesData(float y)
    {
    	//long now = Math.round(new Date().getTime());
    	Date curDate = new  Date(System.currentTimeMillis());
    	if(series.getItemCount()>20)
    	{
    		//series.clear();
    		series.remove(0);
    	}
    	series.add(curDate,y);
    	
    }
    public void addXYData(double d, double e)
    //long now = Math.round(new Date().getTime());
    {
    	series.add((double)d,(double)e);
    	
    }
    public void clearSeriesData()
    {
    	series.clear();
    }
    public void setAxesBlack()
    {
    	AxesColor = Color.BLACK;
    	xLableColor = Color.BLACK;
    	yLableColor = Color.BLACK;
    	LableColor = Color.BLACK;
    	lineColor = Color.BLACK;
    }
    
	
}