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
	
	//3�����ܺ���
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
			List<double[]> values) {// ����ͼ������Դ�ͱ�ͼ��࣬Ҳ����һЩ��ֵ�����
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

		String[] titles = new String[] { "���һ����ˮ��" };
		List<double[]> values = new ArrayList<double[]>();
		// values.add(new double[] { 1423, 1230, 1424, 1524, 1590, 1920, 1620
		// });
		double[] history = new double[7];
		for (int i = 0; i < 7; i++) {
			history[i] = (double)100*(i+10);
					
		}
		values.add(history);
		int[] colors = new int[] { Color.WHITE };
		
		XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);// ����ͼ��ɫ����
		setChartSettings(renderer, "���һ����ˮ��", "ʱ��", "��ˮ��/mL", 0, 8, 0, 2000,
				Color.WHITE, Color.WHITE);// ��������ͼ���⣬���ᣨX�ᣩ�����ᣨY�ᣩ����С�������̶ȡ����������̶�
		renderer.getSeriesRendererAt(0).setDisplayChartValues(true);// �ڵ�0������ͼ����ʾ����
		renderer.setXLabels(12);
		renderer.setYLabels(10);
		renderer.setXLabelsAlign(Align.LEFT);// ���ݴ�������ʾ
		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setPanEnabled(true);
		renderer.setZoomEnabled(true);
		renderer.setZoomButtonsVisible(true);// ��ʾ�Ŵ���С���ܰ�ť
		renderer.setZoomRate(1.2f);
		renderer.setBarSpacing(2.5f);// ����ͼ���
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
	
	
	//˫bar
	 private static final int SERIES_NR = 2;
	 
    
	 public XYMultipleSeriesRenderer getBarDemoRenderer() {
		    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		    renderer.setAxisTitleTextSize(26);
		    renderer.setChartTitleTextSize(30);
		    renderer.setLabelsTextSize(25);
		    renderer.setLegendTextSize(25);
			renderer.setXLabelsAlign(Align.LEFT);// ���ݴ�������ʾ
			renderer.setYLabelsAlign(Align.LEFT);
			renderer.setXLabelsColor(Color.BLACK);
	        renderer.setYLabelsColor(0, Color.BLACK);
			renderer.setAxesColor(Color.BLACK);
			renderer.setLabelsColor(Color.BLACK);
			renderer.setXAxisMin(0.5);
			renderer.setXAxisMax(4.5);
			renderer.setYAxisMin(0.5);
			//renderer.setXAxisMax(6.5);
			renderer.addXTextLabel(0.75, "ֱ����ѹ");//��ָ�����괦��ʾ����
			renderer.addXTextLabel(1.75, "������ѹ");//��ָ�����괦��ʾ����
			renderer.addXTextLabel(2.75, "ֱ������");//��ָ�����괦��ʾ����
			renderer.addXTextLabel(3.75, "��������");//��ָ�����괦��ʾ����
			//renderer.addXTextLabel(4.75, "ֱ��ʱ��");//��ָ�����괦��ʾ����
			//renderer.addXTextLabel(5.75, "����ʱ��");//��ָ�����괦��ʾ����
			renderer.setXLabels(0);//����x���ϵ��±�����
			renderer.setLabelsTextSize(25);//���������������ֵĴ�С
			renderer.setFitLegend(true);
			
			renderer.setPanEnabled(true);
			renderer.setZoomEnabled(true);
			//renderer.setZoomButtonsVisible(true);// ��ʾ�Ŵ���С���ܰ�ť
			renderer.setZoomRate(1.0f);
			renderer.setBarSpacing(0.5f);// ����ͼ���
			renderer.setBackgroundColor(Color.rgb(0xff, 0xff, 0xff));
			renderer.setMarginsColor(Color.rgb(0xff, 0xff, 0xff));
	
		    renderer.setMargins(new int[] {20, 30, 15, 0});
		    SimpleSeriesRenderer r = new SimpleSeriesRenderer();
		    r.setColor(Color.rgb(43, 201, 219));
		    renderer.addSeriesRenderer(r);
		    r = new SimpleSeriesRenderer();
		    r.setColor( Color.rgb(227, 127, 120));
		    renderer.addSeriesRenderer(r);
			renderer.getSeriesRendererAt(0).setDisplayChartValues(true);// �ڵ�0������ͼ����ʾ����
			renderer.getSeriesRendererAt(1).setDisplayChartValues(true);// �ڵ�0������ͼ����ʾ����

		    return renderer;
		  }

	 private XYMultipleSeriesDataset getBarDemoDataset(float[][] y) {
		    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		    final int nr = 10;
		    Random r = new Random();
		    if(y.length<=0) return null;
		    for (int i = 0; i < y.length; i++) {
		      CategorySeries series = new CategorySeries(i%2==0?"�ϸ�":"���ϸ�");
		      for (int k = 0; k < y[0].length; k++) {
		        series.add("����",y[i][k]);
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
