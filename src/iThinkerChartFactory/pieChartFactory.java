package iThinkerChartFactory;

import java.text.NumberFormat;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.learn.BarChartBuilder;
import com.example.learn.R;

public class pieChartFactory {
	

	private static int[] COLORS = new int[] { Color.rgb(43, 201, 219), Color.rgb(227, 127, 120), Color.MAGENTA, Color.CYAN };
	// 设置图例字体大小
	private int legendTextSize = 30;
	// 设置图例高度
	private int legendHeight = 50;
	// 设置图例的颜色
	private int labelColor = Color.BLACK;
	// 设置饼图标题大小
	private int titleSize = 50;
	
	private String pietitle = "title";
	
	public void set_pietitle(String title)
	{
		pietitle = title;
	}
	
	public GraphicalView create_pie_chart(Context context,int piedata[])
	{
		CategorySeries mSeries = new CategorySeries("");
		  /** The main renderer for the main dataset. */
		DefaultRenderer mRenderer = new DefaultRenderer();
		GraphicalView mChartView_pie;
		/** Colors to be used for the pie slices. */
		
		//饼状图
	    //mRenderer.setZoomButtonsVisible(true);
	    mRenderer.setStartAngle(180);
	    mRenderer.setDisplayValues(true);
	    mRenderer.setFitLegend(true);// 设置是否显示图例
	    mRenderer.setLegendTextSize(legendTextSize);//                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
	    mRenderer.setLegendHeight(legendHeight);
	    mRenderer.setLabelsColor(labelColor);
	    mRenderer.setChartTitle(pietitle);// 设置饼图标题
	    mRenderer.setChartTitleTextSize(titleSize);
		
	    for(int i=0;i<piedata.length;i++)
	    {
	    	mSeries.add(i%2==0?"合格":"不合格", piedata[i]);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
	    	
	    	SimpleSeriesRenderer renderer_pie = new SimpleSeriesRenderer();
	    	renderer_pie.setChartValuesTextSize(100);
	        renderer_pie.setColor(COLORS[i% COLORS.length]);
	        //renderer_pie.setChartValuesFormat(NumberFormat.getPercentInstance());// 设置百分比
	        mRenderer.setInScroll(true);
	        mRenderer.addSeriesRenderer(renderer_pie);
	        //mChartView_pie.repaint();
	    }
	   mChartView_pie = ChartFactory.getPieChartView(context, mSeries, mRenderer);
	   return mChartView_pie;
		 	
	}

}
