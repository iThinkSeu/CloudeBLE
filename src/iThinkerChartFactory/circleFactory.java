package iThinkerChartFactory;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.widget.ImageView;

import com.example.learn.R;

public class circleFactory {
	
	//电压实时测量
	private float currentV;
	private Paint mPaint;
	private Paint paint;
	public void DrawVolumn(ImageView iv,float volumn) {
		currentV = volumn;
		int offset = 120;//50
		int color = 0;
		if (volumn > 600) {
			color = Color.rgb(0x19, 0x19, 0x70);
		} else if (volumn > 400) {
			color = Color.rgb(0x00, 0x00, 0xff);
		} else if (volumn > 30) {
			color = Color.rgb(0x41, 0x69, 0xE1);
		} else if (volumn > 200) {
			color = Color.rgb(0x87, 0xCe, 0xeb);
		} else if (volumn > 100) {
			color = (Color.rgb(0xb0, 0xe0, 0xe6));
		} else if (volumn > 50) {
			color = (Color.rgb(0x22, 0xb9, 0x64));
		} else {
			color = (Color.rgb(0x22, 0xb9, 0x64));
		}
		color = (Color.rgb(0x22, 0xb9, 0x64));
		//ImageView iv = (ImageView) findViewById(R.id.measureVolumn);
		iv.clearAnimation();
		// iv.setBackground(Color.rgb(37, 3d, 49));
		// System.out.println("imageview width" + iv.getWidth());
		mPaint = new Paint();
		Canvas canvas = new Canvas();
		// 创建一张空白图片
		Bitmap baseBitmap = Bitmap.createBitmap(480, 500,
				Bitmap.Config.ARGB_8888);
		// 创建一张画布
		canvas = new Canvas(baseBitmap);
		// 画布背景为灰色
		// canvas.drawColor(Color.WHITE);
		// 创建画笔
		paint = new Paint();
		// 画笔颜色为红色
		paint.setColor(Color.RED);
		// 宽度5个像素
		paint.setStrokeWidth(5);
		// 先将灰色背景画上
		canvas.drawBitmap(baseBitmap, new Matrix(), paint);
		iv.setImageBitmap(baseBitmap);
		int centre = 400 / 2; // 获取圆心的x坐标
		int mCircleWidth = 5;
		int radius = centre - mCircleWidth / 2;// 半径
		paint.setStrokeWidth(mCircleWidth); // 设置圆环的宽度
		paint.setAntiAlias(true); // 消除锯齿
		paint.setStyle(Paint.Style.STROKE); // 设置空心
		RectF oval = new RectF(centre - radius + 50, centre - radius + 60,
				centre + radius + 50, centre + radius + 60); // 用于定义的圆弧的形状和大小的界限
		paint.setColor(Color.LTGRAY); // 设置圆环的颜色
		canvas.drawCircle(centre + 50, centre + 60, radius, paint); // 画出圆环
	    paint.setColor(Color.rgb(0x6d, 0xce, 0x3f)); // 设置圆环的颜色
		paint.setColor(color);
		canvas.drawArc(oval, -90, (int) (volumn * 1.0 / 800 * 360), false,
				paint); // 根据进度画圆弧
		Paint fontPaint = new Paint();
		String familyName = "微软雅黑";
		Typeface font = Typeface.create(familyName, Typeface.BOLD);
		fontPaint.setColor(Color.WHITE);
		fontPaint.setTextSize(52);
		canvas.drawText("直流电压", 150, 200, fontPaint);
		//fontPaint.setColor(Color.rgb(0x07, 0xaF, 0xd9));
		fontPaint.setTypeface(font);
		if (volumn >= 1000) {
			fontPaint.setTextSize(90);
			canvas.drawText(volumn+"", 130, 320, fontPaint);
			fontPaint.setTextSize(30);
			canvas.drawText("KV", 300, 350, fontPaint);
		} else if (volumn < 1000) {
			fontPaint.setTextSize(90);
			canvas.drawText(volumn + "", 130, 300, fontPaint);
			fontPaint.setTextSize(30);
			canvas.drawText("KV", 300, 350, fontPaint);
		} 
		iv.setImageBitmap(baseBitmap);
	}
}
