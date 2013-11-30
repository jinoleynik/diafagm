package com.example.diaf;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class VentDiafView extends View {

	private float mPercent;
	private int mHeight;
	private int mWidth;
	private int mCenX;
	private int mCenY;
	public Paint paint;
	private ArrayList<SpherePoint> mListPoints;
	public static final int POINTS = 12;

	public VentDiafView(Context context) {
		this(context, null);
	}

	public VentDiafView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setStrokeWidth(15);

	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		mHeight = bottom;
		mWidth = right;
		mCenX = mWidth / 2;
		mCenY = mHeight / 2;
		
		super.onLayout(changed, left, top, right, bottom);
		if (mListPoints == null) {
			int	radius = mCenX>mCenY ?mCenY : mCenX;
			mListPoints = new ArrayList<SpherePoint>();
			for (int i = 0; i < POINTS; i++) {
				mListPoints.add(new SpherePoint(i * 360 / POINTS, radius));
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {

		for (SpherePoint sp : mListPoints) {
			sp.changeRadius(mPercent);
			sp.draw(canvas);
		}
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

		public SpherePoint(int angle, float radius) {
			this.angle = angle;
			this.radius = radius;
			BASE_RADIUS = radius;
			BASE_ANGLE = angle;
		}

		public void changeRadius(float koef) {
			radius = BASE_RADIUS * koef;
			 angle = BASE_ANGLE *(90+koef);
			 if(angle>360){
			 angle -=360;
			 }
		}

		public void draw(Canvas canvas) {
			float xpos = 0;
			float ypos = 0;
			double cos = Math.cos(Math.toRadians(angle));
			double sin = Math.sin(Math.toRadians(angle));			 
			xpos = mCenX + (float) (radius * cos);
			ypos = mCenY + (float) (radius * sin);
			canvas.drawPoint(xpos, ypos, paint);
		}
	}
}
