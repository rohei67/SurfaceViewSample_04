package com.example.and0701.surfaceviewsample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
	private Thread thread = null;
	private SurfaceHolder holder = null;
	private Bitmap bmp;
	private Droid droid;

	public MySurfaceView(Context context) {
		super(context);
		getHolder().addCallback(this);
		bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		this.holder = holder;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		bmp = Bitmap.createScaledBitmap(bmp, width / 8, width / 8, true);

		droid = new Droid();
		droid.setCurrentPosition(0, 0);
		droid.setTargetPosition(0, 0);
		droid.setSize(width / 8, width / 8);
		droid.setBitmap(bmp);
		droid.setStepSize(width / 50);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		thread = null;
	}

	public void run() {
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
		Canvas canvas;
		while (thread != null) {
			canvas = holder.lockCanvas();
			if (canvas == null) break;

			canvas.drawColor(Color.WHITE);
			droid.move();
			droid.draw(canvas, paint);
			holder.unlockCanvasAndPost(canvas);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		droid.setTargetPosition(event.getX(), event.getY());
		return true;
	}
}
