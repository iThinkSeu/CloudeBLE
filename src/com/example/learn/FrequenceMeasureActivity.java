package com.example.learn;

import java.util.Date;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FrequenceMeasureActivity extends Activity {
	
	//ÇúÏß
    /** The main dataset that includes all the series that go into a chart. */
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    private XYMultipleSeriesDataset mDataset_Freq = new XYMultipleSeriesDataset();
    /** The main renderer that includes all the renderers customizing a chart. */
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    private XYMultipleSeriesRenderer mRenderer_Freq = new XYMultipleSeriesRenderer();
    /** The most recently added series. */
    private TimeSeries mCurrentSeries;
    private TimeSeries mCurrentSeries_Freq;
    /** The most recently created renderer, customizing the current series. */
    private XYSeriesRenderer mCurrentRenderer;
    private XYSeriesRenderer mCurrentRenderer_Freq;
    /** The chart view that displays the data. */
    private GraphicalView mChartView;
    private GraphicalView mChartView_Freq;
    
    private static final long HOUR = 3600 * 1000;

    private static final long DAY = HOUR * 24;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frequency_domain);
        Log.d("ithinker", "Frequence");
        //»­ÇúÏß
  		// set some properties on the main renderer
  		mRenderer.setYLabels(15);
  	    mRenderer.setApplyBackgroundColor(true);
  	    mRenderer.setBackgroundColor(Color.rgb(0x37, 0x3d, 0x49));
  	    //mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
  	    //mRenderer.setBackgroundColor(Color.rgb(0xF2, 0xF2, 0xF2));
  	    mRenderer.setAxisTitleTextSize(16);
  	    mRenderer.setChartTitleTextSize(20);
  	    mRenderer.setLabelsTextSize(15);
  	    mRenderer.setLegendTextSize(15);
  	    mRenderer.setMarginsColor(Color.rgb(0x8c, 0x8c, 0x8c));
  	    //mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
  	    //mRenderer.setMarginsColor(Color.rgb(0xea, 0xea, 0xea));
  	    //mRenderer.setZoomButtonsVisible(true);
  	    mRenderer.setPointSize(5);

  	    mRenderer.setShowLegend(true);
  	    mRenderer.setChartTitle("ÊµÊ±ÇúÏß");  
  	    mRenderer.setXTitle("ºá×ø±ê");  
  	    mRenderer.setYTitle("×Ý×ø±ê");  
  	    mRenderer.setXLabels(10);
  	    mRenderer.setXLabelsColor(Color.WHITE);
  	    mRenderer.setYLabels(12);
  	    mRenderer.setYLabelsColor(0, Color.WHITE);
  	    mRenderer.setShowGrid(true);
  	    //mRenderer.setAxesColor(Color.CYAN); £¬×ø±êÖáÑÕÉ«
  	    
  	    
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
  	    renderer.setColor(Color.CYAN);
  	    renderer.setFillPoints(true);
  	    renderer.setDisplayChartValues(true);
  	    renderer.setDisplayChartValuesDistance(10);
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
  	    
  	    if (mChartView == null) {
  	        LinearLayout layout = (LinearLayout) findViewById(R.id.time_chart);
  	        //mChartView = ChartFactory.getLineChartView(this, mDataset, mRenderer);
  	        mChartView = ChartFactory.getTimeChartView(this, mDataset, mRenderer, "M/d HH:mm a");
  	        // enable the chart click events
  	        mRenderer.setClickEnabled(true);
  	        mRenderer.setSelectableBuffer(10);
  	        mChartView.setOnClickListener(new View.OnClickListener() {
	          public void onClick(View v) {
	            // handle the click event on the chart
	            SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
	          }
	        });
  	        layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
  	            LayoutParams.FILL_PARENT));
  	      } else {
  	       
  	        mChartView.repaint();
  	      }
  	      mChartView.repaint();
  	      
  	    /**ÆµÓò·ÖÎö***/
		//»­ÇúÏß
		// set some properties on the main renderer
		mRenderer_Freq.setYLabels(15);
		mRenderer_Freq.setApplyBackgroundColor(true);
		mRenderer_Freq.setBackgroundColor(Color.rgb(0x37, 0x3d, 0x49));
		//mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
		//mRenderer.setBackgroundColor(Color.rgb(0xF2, 0xF2, 0xF2));
		mRenderer_Freq.setAxisTitleTextSize(16);
		mRenderer_Freq.setChartTitleTextSize(20);
		mRenderer_Freq.setLabelsTextSize(15);
		mRenderer_Freq.setLegendTextSize(15);
		mRenderer_Freq.setMarginsColor(Color.rgb(0x8c, 0x8c, 0x8c));
		//mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
		//mRenderer.setMarginsColor(Color.rgb(0xea, 0xea, 0xea));
		//mRenderer.setZoomButtonsVisible(true);
		mRenderer_Freq.setPointSize(5);
		
		mRenderer_Freq.setShowLegend(true);
		mRenderer_Freq.setChartTitle("ÆµÓòÇúÏß");  
		mRenderer_Freq.setXTitle("ºá×ø±ê");  
		mRenderer_Freq.setYTitle("×Ý×ø±ê");  
		mRenderer_Freq.setXLabels(10);
		mRenderer_Freq.setXLabelsColor(Color.WHITE);
		mRenderer_Freq.setYLabels(12);
		mRenderer_Freq.setYLabelsColor(0, Color.WHITE);
		mRenderer_Freq.setShowGrid(true);
		//mRenderer.setAxesColor(Color.CYAN); £¬×ø±êÖáÑÕÉ«
		
		
		String seriesTitle_Freq = "Series " + (mDataset.getSeriesCount() + 1);
		// create a new series of data
		TimeSeries series_Freq = new TimeSeries(seriesTitle_Freq);
		
		mDataset_Freq.addSeries(series_Freq);
		mCurrentSeries_Freq = series_Freq;
		// create a new renderer for the new series
		XYSeriesRenderer renderer_Freq = new XYSeriesRenderer();
		mRenderer_Freq.addSeriesRenderer(renderer_Freq);
		// set some renderer properties
		renderer_Freq.setPointStyle(PointStyle.CIRCLE);
		renderer_Freq.setColor(Color.CYAN);
		renderer_Freq.setFillPoints(true);
		renderer_Freq.setDisplayChartValues(true);
		renderer_Freq.setDisplayChartValuesDistance(10);
		mCurrentRenderer_Freq = renderer;
		
		
		now = Math.round(new Date().getTime());
		Date[] dates_Freq = new Date[10];
		for(int j=0;j<10;j++)
		{
			dates_Freq[j] = new Date(now - (10 - j) * HOUR);
		}
		
		mCurrentSeries_Freq.add(dates_Freq[0], 1.5);
		mCurrentSeries_Freq.add(dates_Freq[1], 2.0);
		mCurrentSeries_Freq.add(dates_Freq[2], 6.0);
		mCurrentSeries_Freq.add(dates_Freq[3], 7.0);
		mCurrentSeries_Freq.add(dates_Freq[4], 5.5);
		mCurrentSeries_Freq.add(dates_Freq[5], 0.5);
		mCurrentSeries_Freq.add(dates_Freq[6], 0.0);
		mCurrentSeries_Freq.add(dates_Freq[7], 0.0);
		mCurrentSeries_Freq.add(dates_Freq[8], 0.0);
		mCurrentSeries_Freq.add(dates_Freq[9], 0.0);
	   if (mChartView_Freq == null) {
  	        LinearLayout layout = (LinearLayout) findViewById(R.id.frequence_chart);
  	        //mChartView = ChartFactory.getLineChartView(this, mDataset, mRenderer);
  	        mChartView_Freq = ChartFactory.getTimeChartView(this, mDataset_Freq, mRenderer_Freq, "M/d HH:mm a");
  	        // enable the chart click events
  	        mRenderer_Freq.setClickEnabled(true);
  	        mRenderer_Freq.setSelectableBuffer(10);
  	        mChartView_Freq.setOnClickListener(new View.OnClickListener() {
	          public void onClick(View v) {
	            // handle the click event on the chart
	            SeriesSelection seriesSelection = mChartView_Freq.getCurrentSeriesAndPoint();
	          }
	        });
  	        layout.addView(mChartView_Freq, new LayoutParams(LayoutParams.FILL_PARENT,
  	            LayoutParams.FILL_PARENT));
  	      } else {
  	       
  	    	mChartView_Freq.repaint();
  	      }  
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.measuremenu, menu);
		return true;
		}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) {
			case R.id.measure_item1:
				Toast.makeText(this, "You clicked measure item 1", Toast.LENGTH_SHORT).show();
				break;
			case R.id.measure_item2:
				Toast.makeText(this, "You clicked measure item 2", Toast.LENGTH_SHORT).show();
				break;
			default:
			}
		return true;
	}

}
