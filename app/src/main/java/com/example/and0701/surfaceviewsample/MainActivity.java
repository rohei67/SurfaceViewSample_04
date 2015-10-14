package com.example.and0701.surfaceviewsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
	MySurfaceView mySurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mySurfaceView = (MySurfaceView)findViewById(R.id.mySurfaceView);
	}

	public void buttonClicked(View v) {
		mySurfaceView.changeColor();
	}

}
