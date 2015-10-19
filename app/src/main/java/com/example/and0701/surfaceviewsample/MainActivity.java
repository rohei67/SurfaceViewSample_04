package com.example.and0701.surfaceviewsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
	TextView _tv;
	MySurfaceView _mySurfaceView;
	boolean _isAuto = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		_tv = (TextView)findViewById(R.id.textdroid);
		_mySurfaceView = (MySurfaceView) findViewById(R.id.mySurfaceView);
		_mySurfaceView.setOnDroidChangeListener(new MySurfaceView.OnDroidChangeListener() {
			@Override
			public void onChange(View v, int num) {
				_tv.setText(num+"番目のドロイドが選択されています");
			}
		});
	}

	public void buttonClicked(View v) {
		_isAuto = (!_isAuto);
		_mySurfaceView.setAutoMove(_isAuto);
		_mySurfaceView.changeColor();
	}
}
