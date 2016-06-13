package iThinkerChartFactory;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class muchYaxisLineFactory {
	
	private  XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();//数据成员
	private XYSeries series_time = new XYSeries("series for time", 0);
	private XYSeries series_fre = new XYSeries("series for fre", 1);
	private XYSeries series_rms = new XYSeries("series for rms", 2);
	private XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(3);  //renderer定义
	
	//参数集中营
	private float smoothness = 0.3f; //线条参数
    private int[] colors = new int[] { Color.BLUE, Color.YELLOW,Color.RED};
    private PointStyle[] styles = new PointStyle[] { PointStyle.POINT, PointStyle.POINT,PointStyle.POINT};	
    private String title = "双y轴曲线",xTitle = "x轴",yTitle="时间";
    private double xMin,xMax,yMin,yMax;
    private int axesColor= Color.LTGRAY,labelsColor= Color.LTGRAY;
    
    
    public GraphicalView muchYLineGet(Context context)
    {
	    GraphicalView mChartView;
	    mChartView = null;
	    rendererSetting();
        
	    double[]x = {1,2,3,4,5};
	    double[]y1 = {2,3,4,6,7};
	    double[]y2 = {20,30,50,70,30};
	    double[]y3 = {200,400,500,300,200};
	    for(int i=0;i<x.length;i++)
	    {
	    	series_time.add(x[i],y1[i]);
	    	series_fre.add(x[i],y2[i]);
	    	series_rms.add(x[i], y3[i]);
	    }

	    dataset.addSeries(series_time);
	    dataset.addSeries(series_fre);
	    dataset.addSeries(series_rms);
	    mChartView = ChartFactory.getCubeLineChartView(context, dataset, renderer, smoothness);
	  
	    return mChartView;
    }
    
    private void rendererSetting()
    {
	    //renderer  setting
	    renderer.setAxisTitleTextSize(16); //x轴标题字体大小
        renderer.setChartTitleTextSize(20);//整个标题字体大小
        renderer.setLabelsTextSize(15);//
        renderer.setLegendTextSize(15);
        renderer.setPointSize(5f);//点的大小
        renderer.setMargins(new int[] { 20, 30, 15, 20 });
        int length = colors.length;
        for (int i = 0; i < length; i++) {
          XYSeriesRenderer r = new XYSeriesRenderer();
          r.setColor(colors[i]);
          r.setPointStyle(styles[i]);
          r.setLineWidth(3f);
          renderer.addSeriesRenderer(r);
        }
        
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        //renderer.setXAxisMin(xMin);
        //renderer.setXAxisMax(xMax);
        //renderer.setYAxisMin(yMin);
        //renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
        
        renderer.setXLabels(12);
        renderer.setYLabels(10);
        renderer.setShowGrid(true);
        renderer.setXLabelsAlign(Align.RIGHT);
        renderer.setYLabelsAlign(Align.RIGHT);
        renderer.setZoomButtonsVisible(true);
        renderer.setPanLimits(new double[] { -10, 20, -10, 40 });
        renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });
        renderer.setZoomRate(1.05f);
        renderer.setLabelsColor(Color.WHITE);
        renderer.setXLabelsColor(Color.GREEN);
        renderer.setYLabelsColor(0, colors[0]);
        renderer.setYLabelsColor(1, colors[1]);
        renderer.setYLabelsColor(2, colors[2]);
        
        renderer.setYTitle("Hours", 1);
        renderer.setYAxisAlign(Align.RIGHT, 1);
        renderer.setYLabelsAlign(Align.LEFT, 1);
        renderer.setYTitle("rms", 2);
        renderer.setYAxisAlign(Align.RIGHT, 2);
        renderer.setYLabelsAlign(Align.LEFT, 2);
    }
    protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
    	
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setPointSize(5f);
        renderer.setMargins(new int[] { 20, 30, 15, 20 });
        int length = colors.length;
        for (int i = 0; i < length; i++) {
          XYSeriesRenderer r = new XYSeriesRenderer();
          r.setColor(colors[i]);
          r.setPointStyle(styles[i]);
          r.setLineWidth(3f);
          renderer.addSeriesRenderer(r);
        }
      }


}
