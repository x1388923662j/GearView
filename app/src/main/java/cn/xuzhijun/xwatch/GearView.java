package cn.xuzhijun.xwatch;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import cn.xuzhijun.xwatch.util.DisplayUtil;

public class GearView extends View {
	
	private Path  path;
	private Paint paintFill;
	private Paint paintStroke;
	
	private int width, height, r, lon;
	
	//	private Canvas gearCanvas;
	
	private Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
	private Bitmap gear;
	
	private int     gearHeight     = DisplayUtil.dp2px(15);
	private double  transferRadio  = 0;
	private int     gearToothCount = 53;
	private boolean directionRight = true;
	private double  perDegree      = 30;
	private int     gearColor      = Color.BLACK;
	private float   startDegree    = 0;
	private float   strokeWidth    = 1;
	private int     strokeColor    = Color.WHITE;
	private boolean showSideHole   = true;
	private boolean showCenterHole = true;
	
	private long startTime;
	
	public GearView(Context context) {
		super(context);
		init(null);
	}
	
	public GearView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}
	
	public GearView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs);
	}
	
	private void init(@Nullable AttributeSet attrs) {
		path = new Path();
		paintFill = new Paint();
		
		if (attrs != null) {
			TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.GearView);
			gearColor = typedArray.getColor(R.styleable.GearView_gearColor, Color.BLACK);
			gearHeight = typedArray.getDimensionPixelSize(R.styleable.GearView_gearLength, DisplayUtil.dp2px(15));
			gearToothCount = typedArray.getInt(R.styleable.GearView_gearToothCount, 112);
			int direction = typedArray.getInt(R.styleable.GearView_gearDirection, 0);
			perDegree = typedArray.getFloat(R.styleable.GearView_gearDegreePerSecond, 30);
			startDegree = typedArray.getFloat(R.styleable.GearView_gearStartDegree, 0);
			strokeWidth = typedArray.getFloat(R.styleable.GearView_gearStrokeWidth, 1);
			strokeColor = typedArray.getColor(R.styleable.GearView_gearStrokeColor, Color.WHITE);
			showSideHole = typedArray.getBoolean(R.styleable.GearView_gearShowSideHole, true);
			showCenterHole = typedArray.getBoolean(R.styleable.GearView_gearShowCenterHole, true);
			if (direction == 1) {
				directionRight = false;
			}
			typedArray.recycle();
		}
		
		paintFill.setColor(Color.BLACK);
		paintFill.setStrokeWidth(1);
		paintFill.setStyle(Paint.Style.FILL);
		paintFill.setAntiAlias(true);
		
		paintStroke = new Paint();
		paintStroke.setColor(strokeColor);
		paintStroke.setStrokeWidth(strokeWidth);
		paintStroke.setStyle(Paint.Style.STROKE);
		paintStroke.setAntiAlias(true);
		
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		width = getWidth();
		height = getHeight();
		lon = r = Math.min(width, height) / 2;
		r = r - gearHeight / 2;
		//		preDrawGear();
	}
	
	/*private void preDrawGear() {
		gear = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		paintFill.setXfermode(null);
		
		gearCanvas = new Canvas(gear);
		gearCanvas.translate(width / 2, height / 2);
		
		path.reset();
		int    count    = gearToothCount * 4;
		double perRadio = Math.PI / count * 2;
		
		PointF p0 = getRadioPointPI(perRadio, r);
		path.moveTo(p0.x, p0.y);
		
		for (int i = 2; i <= count; i += 2) {
			double radio1      = perRadio * i;
			double radio2      = perRadio * (i + 1);
			PointF pController = getRadioPointPI(radio1, r + ((i % 4 == 0) ? -gearHeight : gearHeight));
			PointF p2          = getRadioPointPI(radio2, r);
			path.quadTo(pController.x, pController.y, p2.x, p2.y);
		}
		
		paintFill.setColor(gearColor);
		gearCanvas.drawPath(path, paintFill);
		gearCanvas.drawPath(path, paintStroke);
		paintFill.setColor(Color.BLACK);
		if (showSideHole) {
			int c = 5;
			for (int i = 0; i < c; i++) {
				PointF p = getRadioPoint(45.0 + (i * 360.0 / c), r / 2.0);
				paintFill.setXfermode(xfermode);
				gearCanvas.drawCircle(p.x, p.y, r / c, paintFill);
				paintFill.setXfermode(null);
				gearCanvas.drawCircle(p.x, p.y, r / c, paintStroke);
			}
		}
		
		if (showCenterHole) {
			paintFill.setXfermode(xfermode);
			gearCanvas.drawCircle(0, 0, (float) (gearHeight / 2.0), paintFill);
			paintFill.setXfermode(null);
			gearCanvas.drawCircle(0, 0, (float) (gearHeight / 2.0), paintStroke);
			paintFill.setColor(gearColor);
		}
	}*/
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		double radio = 0;
		if (startTime != 0) {
			radio = (System.currentTimeMillis() - startTime) / 1000.0 * ((directionRight) ? perDegree : -perDegree);
		}
		startTime = System.currentTimeMillis();
		transferRadio = transferRadio + radio;
		//		canvas.rotate((float) transferRadio + startDegree, width / 2, height / 2);
		//		paintFill.setColor(gearColor);
		//		canvas.drawBitmap(gear, 0, 0, null);
		//		invalidate();
		paintFill.setColor(Color.BLACK);
		canvas.saveLayer(0, 0, width, height, paintFill);
		
		paintFill.setXfermode(null);
		
		//		canvas = new Canvas(gear);
		canvas.translate(width / 2, height / 2);
		canvas.rotate((float) transferRadio + startDegree, 0, 0);
		
		path.reset();
		int    count    = gearToothCount * 4;
		double perRadio = Math.PI / count * 2;
		
		PointF p0 = getRadioPointPI(perRadio, r);
		path.moveTo(p0.x, p0.y);
		
		for (int i = 2; i <= count; i += 2) {
			double radio1      = perRadio * i;
			double radio2      = perRadio * (i + 1);
			PointF pController = getRadioPointPI(radio1, r + ((i % 4 == 0) ? -gearHeight : gearHeight));
			PointF p2          = getRadioPointPI(radio2, r);
			path.quadTo(pController.x, pController.y, p2.x, p2.y);
		}
		
		paintFill.setColor(gearColor);
		canvas.drawPath(path, paintFill);
		canvas.drawPath(path, paintStroke);
		paintFill.setColor(Color.BLACK);
		if (showSideHole) {
			int c = 5;
			for (int i = 0; i < c; i++) {
				PointF p = getRadioPoint(45.0 + (i * 360.0 / c), r / 2.0);
				paintFill.setXfermode(xfermode);
				canvas.drawCircle(p.x, p.y, r / c, paintFill);
				paintFill.setXfermode(null);
				canvas.drawCircle(p.x, p.y, r / c, paintStroke);
			}
		}
		
		if (showCenterHole) {
			paintFill.setXfermode(xfermode);
			canvas.drawCircle(0, 0, (float) (gearHeight / 2.0), paintFill);
			paintFill.setXfermode(null);
			canvas.drawCircle(0, 0, (float) (gearHeight / 2.0), paintStroke);
			paintFill.setColor(gearColor);
		}
		canvas.restore();
		invalidate();
		
	}
	
	private static PointF getRadioPointPI(double radioOdPI, float r) {
		PointF point = new PointF();
		double x, y;
		x = r * Math.cos(radioOdPI);
		y = r * Math.sin(radioOdPI);
		point.set(((float) x), ((float) y));
		return point;
	}
	
	private static PointF getRadioPoint(double radio, double r) {
		PointF point = new PointF();
		double x, y;
		x = r * Math.cos(radio / 180 * Math.PI);
		y = r * Math.sin(radio / 180 * Math.PI);
		point.set(((float) x), ((float) y));
		return point;
	}
	
	public static void main(String[] args) {
		System.out.println(Math.cos(0));
	}
}
