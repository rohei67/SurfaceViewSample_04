package com.example.and0701.surfaceviewsample;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

/**
 * Created by 0701AND on 2015/10/15.
 */
public class SoundControl {
	private MediaPlayer mediaPlayer;
	private SoundPool soundPool;
	private int id_tapsound;

	public SoundControl(Context context) {
		mediaPlayer = MediaPlayer.create(context, R.raw.music);
		mediaPlayer.setLooping(true);

		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		id_tapsound = soundPool.load(context, R.raw.sound, 1);
	}

	public void playSound(){
		soundPool.play(id_tapsound, 1.0f, 1.0f, 1, 0, 1.0f);
	}

	public void playMusic() {
		mediaPlayer.start();
	}

	public void release() {
		if (soundPool != null)
			soundPool.release();
		if (mediaPlayer != null) {
			mediaPlayer.release();
		}
	}
}
