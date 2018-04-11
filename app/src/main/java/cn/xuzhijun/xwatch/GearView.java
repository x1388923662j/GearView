package cn.xuzhijun.xwatch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class GearView extends View {
	
	private Path  path;
	private Paint paint;
	
	private int width, height, r;
	
	private int gearHeight = 40;
	
	public GearView(Context context) {
		super(context);
		init();
	}
	
	public GearView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public GearView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	
	private void init() {
		path = new Path();
		paint = new Paint();
		
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(1);
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		width = getWidth();
		height = getHeight();
		r = width / 2;
		gearHeight = r / 6;
		r = r - gearHeight;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.translate(width / 2, height / 2);
		canvas.rotate(-90);
		
		//		paint.setColor(Color.YELLOW);
		//		canvas.drawCircle(0, 0, r, paint);
		//		paint.setColor(Color.BLACK);
		
		path.reset();
		int    count    = 96;
		double perRadio = Math.PI / count * 2;
		
		PointF p0 = getRadioPoint(perRadio, r);
		path.moveTo(p0.x, p0.y);
		
		for (int i = 2; i <= count; i += 2) {
			double radio1      = perRadio * i;
			double radio2      = perRadio * (i + 1);
			PointF pController = getRadioPoint(radio1, r + ((i % 4 == 0) ? -gearHeight : gearHeight));
			PointF p2          = getRadioPoint(radio2, r);
			path.quadTo(pController.x, pController.y, p2.x, p2.y);
		}
		canvas.drawPath(path, paint);
	}
	
	private PointF getRadioPoint(double radio, float r) {
		PointF point = new PointF();
		double x, y;
		x = r * Math.cos(radio);
		y = r * Math.sin(radio);
		point.set(((float) x), ((float) y));
		
		Log.d("xxxx", "getRadioPoint: " + point.toString());
		
		return point;
	}
	
	public static void main(String[] args) {
		System.out.println(Math.cos(0));
	}
}
