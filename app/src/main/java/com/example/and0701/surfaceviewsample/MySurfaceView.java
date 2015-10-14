package com.example.and0701.surfaceviewsample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
	public static final int NUM_DROID = 10;

	private Thread thread = null;
	private SurfaceHolder holder = null;
	private ArrayList<Droid> droids = new ArrayList<>();
	private Droid selectedDroid = null;

	ColorFilter colorFilter = null;

	public void changeColor(){
		Random rand = new Random();
		colorFilter = new LightingColorFilter(Color.rgb(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)), 1);
	}

	public MySurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
	}

	public MySurfaceView(Context context) {
		super(context);
		getHolder().addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		this.holder = holder;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		ArrayList<Bitmap> bitmaps = fetchBitmaps(width / NUM_DROID);

		for (int i = 0; i < NUM_DROID; i++)
			droids.add(createDroid(width, bitmaps, 0, i * (width / NUM_DROID)));

		thread = new Thread(this);
		thread.start();
	}

	@NonNull
	private ArrayList<Bitmap> fetchBitmaps(int size) {
		Bitmap bmp1 = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.droid_leanleft);
		Bitmap bmp2 = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.droid_leanright);
		bmp1 = Bitmap.createScaledBitmap(bmp1, size, size, true);
		bmp2 = Bitmap.createScaledBitmap(bmp2, size, size, true);

		ArrayList<Bitmap> bitmaps = new ArrayList<>();
		bitmaps.add(bmp1);
		bitmaps.add(bmp2);
		return bitmaps;
	}

	private Droid createDroid(int baseSize, ArrayList<Bitmap> bitmaps, float initX, float initY) {
		Droid droid = new Droid();
		droid.setSize(baseSize / NUM_DROID, baseSize / NUM_DROID);
		droid.setCurrentPosition(initX, initY);
		droid.setTargetPosition(initX, initY);
		droid.setVelocity(baseSize / 50);
		droid.setBitmaps(bitmaps);
		return droid;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		thread = null;
	}

	public void run() {
		Paint paint = new Paint();
		paint.setColor(Color.RED);

		while (thread != null) {
			Canvas canvas = holder.lockCanvas();
			if (canvas == null) break;
			canvas.drawColor(Color.WHITE);

			if (colorFilter != null)
				paint.setColorFilter(colorFilter);

			drawDroids(canvas, paint);
			holder.unlockCanvasAndPost(canvas);
		}
	}

	private void drawDroids(Canvas canvas, Paint paint) {
		for (Droid droid : droids) {
			droid.move();
			if (droid == selectedDroid)
				canvas.drawRect(selectedDroid.getRect(), paint);
			droid.draw(canvas, paint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		for (Droid droid : droids) {
			if (droid.getRect().contains(event.getX(), event.getY())) {
				selectedDroid = droid;
				return true;
			}
		}
		setNewPosition(event);
		return true;
	}

	private void setNewPosition(MotionEvent event) {
		if (selectedDroid != null) {
			float newX = event.getX() - selectedDroid.getWidth() / 2;
			float newY = event.getY() - selectedDroid.getHeight() / 2;
			selectedDroid.setTargetPosition(newX, newY);
		}
	}
}
