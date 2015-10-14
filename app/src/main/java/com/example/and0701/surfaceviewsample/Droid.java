package com.example.and0701.surfaceviewsample;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Droid {
	private int _width, _height;
	private float _currentX, _currentY;
	private float _targetX, _targetY;
	private Bitmap _bitmap;
	private float _stepSize;

	private double getAngle(float x, float y, float x2, float y2) {
		double result;
		float xDistance = x2 - x;
		float yDistance = y2 - y;
		result = Math.atan2((double) yDistance, (double) xDistance) * 180 / Math.PI;
		result += 180;
		return result;
	}

	private float getDistance(float x, float y, float x2, float y2) {
		double distance = Math.sqrt((x2 - x) * (x2 - x) + (y2 - y) * (y2 - y));
		return (float) distance;
	}

	public void setCurrentPosition(float x, float y) {
		this._currentX = x;
		this._currentY = y;
	}

	public void setTargetPosition(float x, float y) {
		this._targetX = x;
		this._targetY = y;
	}

	public void setSize(int w, int h) {
		this._width = w;
		this._height = h;
	}

	public void setStepSize(float stepSize) {
		this._stepSize = stepSize;
	}

	public Droid() {
	}

	public void move() {
		float distance = getDistance(_currentX, _currentY, _targetX, _targetY);
		if (distance < _stepSize) {
			_currentX = _targetX;
			_currentY = _targetY;
		} else {
			//斜め移動
			double angle = getAngle(_currentX, _currentY, _targetX, _targetY);
			float x = -(float) (_stepSize * Math.cos(angle * Math.PI / 180.0));
			float y = -(float) (_stepSize * Math.sin(angle * Math.PI / 180.0));
			_currentX += x;
			_currentY += y;
		}
	}

	public void draw(Canvas canvas, Paint paint) {
		canvas.drawBitmap(_bitmap, _currentX, _currentY, paint);

	}

	public void setBitmap(Bitmap bitmap) {
		this._bitmap = bitmap;
	}
}
