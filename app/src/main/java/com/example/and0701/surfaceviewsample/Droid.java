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
	private float _velocity, _velocityX, _velocityY;

	private RectF _rect = new RectF();
	private ArrayList<Bitmap> _bitmaps;
	private int _animCount = 0, _animFrame = 0;
	Thread _thread = null;
	private boolean _isAuto;


	public void setIsAuto(boolean isAuto) {
		this._isAuto = isAuto;
	}

	public Droid(int size, float initX, float initY) {
		_velocityX = 0;
		_velocityY = 0;

		setSize(size, size);
		setCurrentPosition(initX, initY);
		setTargetPosition(initX, initY);

		generateVelocity();
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

	public void generateVelocity() {
		Random rand = new Random();
		this._velocity = rand.nextFloat() * 10 + 5;
	}

	public void move() {
		float distance = getDistance(_currentX, _currentY, _targetX, _targetY);
		if (distance < _velocity) {
			if (_isAuto) {
				Random rand = new Random();
				setTargetPosition(rand.nextInt(_viewWidth - _width), rand.nextInt(_viewHeight - _height));
			} else {
				setCurrentPosition(_targetX, _targetY);
			}
		} else {
			// 斜め移動
			double angle = getAngle(_currentX, _currentY, _targetX, _targetY);

//			_velocityX += -(float) Math.cos(angle * Math.PI / 180.0);
//			_velocityY += -(float) Math.sin(angle * Math.PI / 180.0);
			_velocityX = -(float) Math.cos(angle * Math.PI / 180.0) * _velocity;
			_velocityY = -(float) Math.sin(angle * Math.PI / 180.0) * _velocity;
//			_velocity = (_velocityX > _velocityY) ? _velocityX : _velocityY;
			_currentX += _velocityX;
			_currentY += _velocityY;
/*
			if (_currentX < 0 || _currentX + _width > _viewWidth) {
				_currentX -= _velocityX;
				_velocityX = 0;
			}
			if (_currentY < 0 || _currentY + _height > _viewHeight) {
				_currentY -= _velocityY;
				_velocityY = 0;
			}
*/		}
		_rect.set(_currentX, _currentY, _currentX + _width, _currentY + _height);
	}

	public void setAnimationFrame() {
		if (_animCount++ > _velocity*2) {
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
