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
	
	//��ѹʵʱ����
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
		// ����һ�ſհ�ͼƬ
		Bitmap baseBitmap = Bitmap.createBitmap(480, 500,
				Bitmap.Config.ARGB_8888);
		// ����һ�Ż���
		canvas = new Canvas(baseBitmap);
		// ��������Ϊ��ɫ
		// canvas.drawColor(Color.WHITE);
		// ��������
		paint = new Paint();
		// ������ɫΪ��ɫ
		paint.setColor(Color.RED);
		// ���5������
		paint.setStrokeWidth(5);
		// �Ƚ���ɫ��������
		canvas.drawBitmap(baseBitmap, new Matrix(), paint);
		iv.setImageBitmap(baseBitmap);
		int centre = 400 / 2; // ��ȡԲ�ĵ�x����
		int mCircleWidth = 5;
		int radius = centre - mCircleWidth / 2;// �뾶
		paint.setStrokeWidth(mCircleWidth); // ����Բ���Ŀ��
		paint.setAntiAlias(true); // �������
		paint.setStyle(Paint.Style.STROKE); // ���ÿ���
		RectF oval = new RectF(centre - radius + 50, centre - radius + 60,
				centre + radius + 50, centre + radius + 60); // ���ڶ����Բ������״�ʹ�С�Ľ���
		paint.setColor(Color.LTGRAY); // ����Բ������ɫ
		canvas.drawCircle(centre + 50, centre + 60, radius, paint); // ����Բ��
	    paint.setColor(Color.rgb(0x6d, 0xce, 0x3f)); // ����Բ������ɫ
		paint.setColor(color);
		canvas.drawArc(oval, -90, (int) (volumn * 1.0 / 800 * 360), false,
				paint); // ���ݽ��Ȼ�Բ��
		Paint fontPaint = new Paint();
		String familyName = "΢���ź�";
		Typeface font = Typeface.create(familyName, Typeface.BOLD);
		fontPaint.setColor(Color.WHITE);
		fontPaint.setTextSize(52);
		canvas.drawText("ֱ����ѹ", 150, 200, fontPaint);
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
