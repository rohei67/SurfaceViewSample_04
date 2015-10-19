package com.example.and0701.surfaceviewsample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
	public static final int NUM_DROID = 10;

	// Thread Object
	private Thread thread = null;
	private SurfaceHolder holder = null;

	// Game Object
	private ArrayList<Droid> droids;
	private Droid selectedDroid = null;
	private SoundControl soundControl;
	private Background background;
	private ColorFilter colorFilter = null;

	// Called in MainActivity
	public void changeColor() {
		Random rand = new Random();
		colorFilter = new LightingColorFilter(Color.rgb(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)), 1);
	}

	public MySurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCallback();
	}

	public MySurfaceView(Context context) {
		super(context);
		setCallback();
	}

	private void setCallback() {
		getHolder().addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		droids = new ArrayList<>();
		this.holder = holder;
	}

	// Game Object Initialize
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		initialize(width, height);

		thread = new Thread(this);
		thread.start();
	}

	private void initialize(int width, int height) {
		// Bitmap
		ArrayList<Bitmap> bitmaps = fetchBitmaps(width / NUM_DROID);

		// Droids
		createDroids(width, height, bitmaps);

		// Background
		Bitmap bitmapBg = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.bg_jellyfish);
		background = new Background(bitmapBg, width, height);

		// Sound & Music
		soundControl = new SoundControl(getContext());
	}

	private void createDroids(int width, int height, ArrayList<Bitmap> bitmaps) {
		for (int i = 0; i < NUM_DROID; i++) {
			Droid droid = new Droid(width / NUM_DROID, 0, i * (width / NUM_DROID));
			droid.setBitmaps(bitmaps);
			droid.setViewSize(width, height);
			droids.add(droid);
		}
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

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		thread = null;
		soundControl.release();
		for (Droid droid : droids)
			droid.finish();
		droids = null;
		selectedDroid = null;

	}

	// Main Routine
	public void run() {
		soundControl.playMusic();
		Paint paint = createPaint(Color.RED, Paint.Style.STROKE);
		Paint collisionPaint = createPaint(Color.BLUE, Paint.Style.FILL);

		while (thread != null) {
			Canvas canvas = holder.lockCanvas();
			if (canvas == null) break;
			canvas.drawColor(Color.WHITE);

			background.scroll();
			background.draw(canvas);

			setColorFilter(paint);
//			moveDroids();
			drawCollisionRect(collisionPaint, canvas);
			drawDroids(canvas, paint);

			holder.unlockCanvasAndPost(canvas);
		}
	}

	private void setColorFilter(Paint paint) {
		if (colorFilter != null)
			paint.setColorFilter(colorFilter);
	}

	@NonNull
	private Paint createPaint(int color, Paint.Style style) {
		Paint paint = new Paint();
		paint.setStrokeWidth(5);
		paint.setColor(color);
		paint.setStyle(style);
		return paint;
	}

	private void drawCollisionRect(Paint paint, Canvas canvas) {
		for (Droid droid1 : droids) {
			for (Droid droid2 : droids) {
				if (droid1 == droid2) continue;
				RectF r1 = droid1.getRect();
				RectF r2 = droid2.getRect();
				if (RectF.intersects(r1, r2)) {
					canvas.drawRect(r1, paint);
				}
			}
		}
	}

	private void drawDroids(Canvas canvas, Paint paint) {
		for (Droid droid : droids) {
			if (droid == selectedDroid) {
				canvas.drawRect(selectedDroid.getRect(), paint);
			}
			droid.draw(canvas, paint);
			droid.setAnimationFrame();
		}
	}

	private void moveDroids() {
		for (Droid droid : droids) {
			droid.move();
		}
	}

	//------------- Touch Event
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int i = 1;
		for (Droid droid : droids) {
			if (checkDroidTapped(event, droid)) {
				_listener.onChange(this, i);
				return true;
			}
			i++;
		}
		setNewTargetPosition(event);
		return true;
	}

	private boolean checkDroidTapped(MotionEvent event, Droid droid) {
		if (droid.getRect().contains(event.getX(), event.getY())) {
			if (selectedDroid != droid)
				soundControl.playSound();
			selectedDroid = droid;
			return true;
		}
		return false;
	}

	private void setNewTargetPosition(MotionEvent event) {
		if (selectedDroid != null) {
			float newX = event.getX() - selectedDroid.getWidth() / 2;
			float newY = event.getY() - selectedDroid.getHeight() / 2;
			selectedDroid.setTargetPosition(newX, newY);
		}
	}

	public void setAutoMove(boolean isAuto) {
		for (Droid droid : droids) {
			droid.setIsAuto(isAuto);
		}
	}

	OnDroidChangeListener _listener;

	// Called from Activity
	public void setOnDroidChangeListener(OnDroidChangeListener onDroidChangeListener) {
		_listener = onDroidChangeListener;
	}

	interface OnDroidChangeListener {
		void onChange(View v, int num);
	}
}
