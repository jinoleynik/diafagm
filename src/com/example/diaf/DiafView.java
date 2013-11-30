package com.example.diaf;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DiafView extends View {

	private float mPercent = 2;
	private int mHeight;
	private int mWidth;
	private int mCenX;
	private int mCenY;
	private ArrayList<SpherePoint> mListPoints;
	public Paint paintw;
	private Path path;
	private Paint paintp;
	public static final int POINTS = 6;

	public DiafView(Context context) {
		this(context, null);
	}

	@SuppressLint("NewApi")
	public DiafView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paintp = new Paint();
		paintp.setStyle(Style.FILL);
		paintp.setStrokeWidth(2);
		paintp.setColor(Color.BLACK);
		paintw = new Paint();
		paintw.setColor(Color.WHITE);
		paintw.setStrokeWidth(2);
		path = new Path();
		setLayerType(View.LAYER_TYPE_SOFTWARE, null); 
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		mHeight = bottom;
		mWidth = right;
		mCenX = mWidth / 2;
		mCenY = mHeight / 2;

		super.onLayout(changed, left, top, right, bottom);
		if (mListPoints == null) {
			int radius = mCenX > mCenY ? mCenY : mCenX;
			mListPoints = new ArrayList<SpherePoint>();
			for (int i = 0; i < POINTS; i++) {
				mListPoints.add(new SpherePoint(i * 360 / POINTS, radius));
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		path.reset();
		path.moveTo(mWidth, mHeight);
		path.lineTo(mWidth, 0);
		path.lineTo(0, 0);
		path.lineTo(0, mHeight);
		path.lineTo(mWidth, mHeight);
		for (SpherePoint sp : mListPoints) {
			sp.changeRadius(mPercent);
			path.lineTo(sp.xpos, sp.ypos);
		}
		path.lineTo(mListPoints.get(0).xpos, mListPoints.get(0).ypos);
		path.lineTo(mWidth, mHeight);
		canvas.drawPath(path, paintp);
		for (SpherePoint sp : mListPoints) {
			sp.draw(canvas);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		new TimerTread().start();
		return super.onTouchEvent(event);
	}

	public void setValue(int value) {
		mPercent = value / 100f;
		invalidate();
	}

	private class SpherePoint {
		public double angle;
		public float radius;
		private float BASE_RADIUS;
		private double BASE_ANGLE;
		private float startX1;
		private float startY1;
		public float xpos;
		public float ypos;

		public SpherePoint(int angle, float radius) {
			this.angle = angle;
			this.radius = radius;
			BASE_RADIUS = radius;
			BASE_ANGLE = angle;
			startX1 = (float) (mCenX + radius * 2 * Math.cos(Math.toRadians(BASE_ANGLE)));
			startY1 = (float) (mCenY + radius * 2 * Math.sin(Math.toRadians(BASE_ANGLE)));
			 
		}

		public void changeRadius(float koef) {
			radius = BASE_RADIUS * koef;
			angle = BASE_ANGLE + (30 * koef);
			if (angle > 360) {
				angle -= 360;
			}
		}

		public void draw(Canvas canvas) {

			double cos = Math.cos(Math.toRadians(angle));
			double sin = Math.sin(Math.toRadians(angle));
			xpos = mCenX + (float) (radius * cos);
			ypos = mCenY + (float) (radius * sin);
			canvas.drawLine(startX1, startY1, xpos, ypos, paintw);
		}

	}

	private class TimerTread extends Thread {
		private boolean mRun = true;
		private boolean mForw = true;

		@Override
		public void run() {
			setName("timethread");
			while (mRun) {
				if (mForw) {
					mPercent -= 0.05;
				} else {
					mPercent += 0.05;
				}
				if (mPercent < 0) {
					mForw = false;
				}
				if (mPercent > 2) {
					mRun = false;
				}
				try {
					postInvalidate();
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
