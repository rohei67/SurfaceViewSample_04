package com.example.and0701.surfaceviewsample;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Random;

public class Droid implements Runnable {
	private int _width, _height;
	private int _viewWidth, _viewHeight;
	private float _currentX, _currentY;
	private float _targetX, _targetY;
	private float _accel = 0.0f;
	private float _distance = 0;

	private RectF _rect = new RectF();
	private ArrayList<Bitmap> _bitmaps;
	private int _animCount = 0, _animFrame = 0;
	Thread _thread = null;
	private boolean _isAuto;


	public void setIsAuto(boolean isAuto) {
		this._isAuto = isAuto;
	}

	public Droid(int size, float initX, float initY) {
		setSize(size, size);
		setCurrentPosition(initX, initY);
		setTargetPosition(initX, initY);
		startThread();
	}

	public void startThread() {
		_thread = new Thread(this);
		_thread.start();
	}

	@Override
	public void run() {
		while (_thread != null) {
			move();
			setAnimationFrame();

			try {
				Thread.sleep(1000 / 60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void finish() {
		_thread = null;
	}

	public void setViewSize(int viewWidth, int viewHeight) {
		this._viewWidth = viewWidth;
		this._viewHeight = viewHeight;
	}

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
		return (Math.atan2(yDistance, xDistance) * 180 / Math.PI ) + 180;
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
		_distance = getDistance(_currentX, _currentY, _targetX, _targetY);
	}

	public void move() {
		float currentDistance = getDistance(_currentX, _currentY, _targetX, _targetY);

		if (currentDistance < 10) {
			_accel = 0.0f;
			setCurrentPosition(_targetX, _targetY);

			if (_isAuto) {
				Random rand = new Random();
				setTargetPosition(rand.nextInt(_viewWidth - _width), rand.nextInt(_viewHeight - _height));
			}
		} else {
			// targetに向かって移動
			if (currentDistance >= _distance / 2.0f)
				_accel++;
			else {
				_accel--;
				_accel = (_accel < 0)? 1.0f: _accel;
			}
			double angle = getAngle(_currentX, _currentY, _targetX, _targetY);
			_currentX += -(float) Math.cos(angle * Math.PI / 180.0) * _accel;
			_currentY += -(float) Math.sin(angle * Math.PI / 180.0) * _accel;
		}
		_rect.set(_currentX, _currentY, _currentX + _width, _currentY + _height);
	}

	public void setAnimationFrame() {
		if (_animCount++ > 20) {
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
