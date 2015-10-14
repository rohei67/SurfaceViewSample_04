package com.example.and0701.surfaceviewsample;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

public class Droid {
	private float _currentX, _currentY;
	private float _targetX, _targetY;
	private float _velocity;

	private ArrayList<Bitmap> _bitmaps;

	private int _animCount = 0;
	private int _animFrame = 0;

	public void setBitmaps(ArrayList<Bitmap> bitmaps) {
		this._bitmaps = bitmaps;
	}

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

	public void setVelocity(float v) {
		this._velocity = v;
	}

	public Droid() {
	}

	public void move() {
		float distance = getDistance(_currentX, _currentY, _targetX, _targetY);
		if (distance < _velocity) {
			setCurrentPosition(_targetX, _targetY);
		} else {
			// 斜め移動
			double angle = getAngle(_currentX, _currentY, _targetX, _targetY);
			float x = -(float) (_velocity * Math.cos(angle * Math.PI / 180.0));
			float y = -(float) (_velocity * Math.sin(angle * Math.PI / 180.0));
			_currentX += x;
			_currentY += y;
		}

		if(_animCount++ > 10) {
			_animCount = 0;
			_animFrame ^= 1;
		}
	}

	public void draw(Canvas canvas, Paint paint) {
		canvas.drawBitmap(_bitmaps.get(_animFrame), _currentX, _currentY, paint);
	}
}
