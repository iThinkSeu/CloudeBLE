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
		
	    public GraphicalView oneLineChart(Context context)
	    {
	    	
				GraphicalView mChartView;
				XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
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
				//mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
				//mRenderer.setMarginsColor(Color.rgb(0xea, 0xea, 0xea));
				//mRenderer.setZoomButtonsVisible(true);
				mRenderer.setPointSize(8);
				
				mRenderer.setShowLegend(true);
				mRenderer.setChartTitle("实时曲线");  
				mRenderer.setXTitle("横坐标");  
				mRenderer.setYTitle("纵坐标");  
				mRenderer.setXLabels(5);//设置x轴显示6个点,根据setChartSettings的最大值和最小值自动计算点的间隔   
				mRenderer.setXLabelsColor(Color.WHITE);
				mRenderer.setYLabels(7);
				mRenderer.setYLabelsColor(0, Color.WHITE);
				mRenderer.setXLabelsAlign(Align.CENTER);
			    mRenderer.setYLabelsAlign(Align.LEFT);
				//mRenderer.setShowGrid(false);
				mRenderer.setShowGridX(true);
				mRenderer.setShowGridY(false);
				//mRenderer.setAxesColor(Color.CYAN); ，坐标轴颜色
		
				
				
				String seriesTitle = "Series " + (mDataset.getSeriesCount() + 1);
				// create a new series of data
				TimeSeries series = new TimeSeries(seriesTitle);
				
				mDataset.addSeries(series);
				mCurrentSeries = series;
				// create a new renderer for the new series
				XYSeriesRenderer renderer = new XYSeriesRenderer();
				mRenderer.addSeriesRenderer(renderer);
				// set some renderer properties
				renderer.setPointStyle(PointStyle.CIRCLE);
				renderer.setColor(Color.WHITE);
				renderer.setFillPoints(true);
				renderer.setDisplayChartValues(true);
				renderer.setDisplayChartValuesDistance(10);
				renderer.setLineWidth(4);
				mCurrentRenderer = renderer;
				
				
				long now = Math.round(new Date().getTime());
				Date[] dates = new Date[4];
				for(int j=0;j<4;j++)
				{
					dates[j] = new Date(now - (4 - j) * HOUR);
				}
				
				
				mCurrentSeries.add(dates[0], 2.0);
				mCurrentSeries.add(dates[1], 6.0);
				mCurrentSeries.add(dates[2], 7.0);
				mCurrentSeries.add(dates[3], 7.05);
				
	 	        mChartView = ChartFactory.getTimeChartView(context, mDataset, mRenderer, "M/d HH:mm a");
	  	        // enable the chart click events
	  	        mRenderer.setClickEnabled(true);
	  	        mRenderer.setSelectableBuffer(10);
				return mChartView;
	    }
		
}