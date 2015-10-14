package com.example.and0701.surfaceviewsample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by 0701AND on 2015/10/13.
 */
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    public final static String TAG = "MySurfaceView";
    Thread thread = null;
    SurfaceHolder holder = null;
    int viewWidth;
    int viewHeight;
    float stepSize;
    Bitmap bmp;

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
        viewWidth = width;
        viewHeight = height;
        stepSize = viewWidth / 50;
        bmp = Bitmap.createScaledBitmap(bmp, viewWidth / 8, viewWidth / 8, true);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread = null;
    }

    public void run() {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(viewWidth / 20);
        paint.setAntiAlias(true);
        Canvas canvas = null;
        while (thread != null) {
            canvas = holder.lockCanvas();
            if (canvas == null) break;
            canvas.drawColor(Color.WHITE);

//			if(targetX-currentX>0){
//				currentX+=stepSize;
//			}else {
//				currentX-=stepSize;
//			}
//
//			if(targetY-currentY>0){
//				currentY+=stepSize;
//			}else {
//				currentY-=stepSize;
//			}


            //目的地までの距離が一歩分以下ならば到着させる
            float distance = getDistance(currentX, currentY, targetX, targetY);
            if (distance < stepSize) {
                currentX = targetX;
                currentY = targetY;
            } else {
                //斜め移動
                double angle = getAngle(currentX, currentY, targetX, targetY);
                float x = -(float) (stepSize * Math.cos(angle * Math.PI / 180.0));
                float y = -(float) (stepSize * Math.sin(angle * Math.PI / 180.0));
                currentX += x;
                currentY += y;
            }

            canvas.drawBitmap(bmp, currentX, currentY, paint);
            holder.unlockCanvasAndPost(canvas);

            try {
                Thread.sleep(1000 / 8);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    float currentX;
    float currentY;
    float targetX;
    float targetY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        targetX = event.getX();
        targetY = event.getY();
        return true;
    }

    private double getAngle(float x, float y, float x2, float y2) {
        double result = 0.0d;
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
}
