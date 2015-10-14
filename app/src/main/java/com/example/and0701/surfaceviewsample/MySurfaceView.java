package com.example.and0701.surfaceviewsample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
	public static final int NUM_DROID = 3;

	private Thread thread = null;
	private SurfaceHolder holder = null;
	private ArrayList<Droid> droids;

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
		ArrayList<Bitmap> bitmaps = fetchBitmaps(width / 8);

		droids = new ArrayList<>();
		for (int i = 0; i < NUM_DROID; i++)
			droids.add(createDroid(width / 50, bitmaps, 0, i * (width / 8)));

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

	private Droid createDroid(int velocity, ArrayList<Bitmap> bitmaps, float initX, float initY) {
		Droid droid = new Droid();
		droid.setCurrentPosition(initX, initY);
		droid.setTargetPosition(initX, initY);
		droid.setVelocity(velocity);
		droid.setBitmaps(bitmaps);
		return droid;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		thread = null;
	}

	public void run() {
		Paint paint = new Paint();

		while (thread != null) {
			Canvas canvas = holder.lockCanvas();
			if (canvas == null) break;

			canvas.drawColor(Color.WHITE);
			for (Droid droid : droids) {
				droid.move();
				droid.draw(canvas, paint);
			}
			holder.unlockCanvasAndPost(canvas);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		for (Droid droid : droids)
			droid.setTargetPosition(event.getX(), event.getY());
		return true;
	}
}
