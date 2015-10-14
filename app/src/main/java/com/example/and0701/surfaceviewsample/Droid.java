package com.example.and0701.surfaceviewsample;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;

public class Droid {
	private int _width, _height;
	private float _currentX, _currentY;
	private float _targetX, _targetY;
	private float _velocity;

	private RectF _rect = new RectF();
	private ArrayList<Bitmap> _bitmaps;
	private int _animCount = 0, _animFrame = 0;

	public int getWidth() {
		return _width;
	}

	public int getHeight() {
		return _height;
	}

	public void setSize(int w, int h) {
		_width = w;
		_height = h;
	}

	public void setBitmaps(ArrayList<Bitmap> bitmaps) {
		this._bitmaps = bitmaps;
	}

	private double getAngle(float x, float y, float x2, float y2) {
		double xDistance = x2 - x;
		double yDistance = y2 - y;
		double result = Math.atan2(yDistance, xDistance) * 180 / Math.PI;
		return (result + 180);
	}

	private float getDistance(float x, float y, float x2, float y2) {
		return (float) Math.sqrt((x2 - x) * (x2 - x) + (y2 - y) * (y2 - y));
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
		_rect.set(_currentX, _currentY, _currentX+_width, _currentY+_height);

		setAnimationFrame();
	}

	private void setAnimationFrame() {
		if (_animCount++ > 10) {
			_animCount = 0;
			_animFrame ^= 1;
		}
	}

	public void draw(Canvas canvas, Paint paint) {
		canvas.drawBitmap(_bitmaps.get(_animFrame), _currentX, _currentY, paint);
	}

	public RectF getRect() {
		return _rect;
	}
}
