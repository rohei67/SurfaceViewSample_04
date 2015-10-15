package com.example.and0701.surfaceviewsample;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background {
	private Bitmap bitmap;
	private int cnt;

	public Background(Bitmap bitmap, int viewWidth, int viewHeight) {
		float scale = (float) viewWidth / (float) bitmap.getWidth();
		this.bitmap = Bitmap.createScaledBitmap(bitmap, viewWidth, (int) (scale * bitmap.getHeight()), true);
	}

	public void scroll() {
		final int SCROLL_SPEED = 30;
		cnt += SCROLL_SPEED;
	}

	public void draw(Canvas canvas) {
		int offset = cnt % bitmap.getHeight();

		canvas.drawBitmap(bitmap, 0, offset - bitmap.getHeight(), null);
		canvas.drawBitmap(bitmap, 0, offset, null);
		canvas.drawBitmap(bitmap, 0, offset + bitmap.getHeight(), null);
	}
}
