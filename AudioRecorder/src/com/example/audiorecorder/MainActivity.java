package com.example.audiorecorder;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder.AudioSource;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {
	
	private final int BUFFER_SIZE = 64000;
	
	AudioTrack audioTrack = null;
	AudioRecord audioRecorder = null;
	
	byte[] globalBuffer = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button recordButton = (Button) findViewById(R.id.btn_start_audio_record);
		recordButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				globalBuffer = startRecording();
			}
		});
		
		Button playButton = (Button) findViewById(R.id.btn_start_audio_play);
		playButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startPlaying(globalBuffer);
			}
		});
	}
	    
    private void startPlaying(byte[] buffer){
    	if(buffer == null){
    		return;
    	}
    	audioTrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL, 8000, AudioFormat.CHANNEL_OUT_MONO,
    			AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE, AudioTrack.MODE_STREAM);

    	audioTrack.play();
    	audioTrack.write(buffer, 0, buffer.length);
    }
    
    private void stopPlaying(){
    	audioTrack.stop();
    	audioTrack.release();
    	Log.e("AAAA", "released audiotrack");
    	audioTrack = null;
    }
    
    
    private byte[] startRecording(){
    	
    	byte[] buffer = new byte[BUFFER_SIZE];
    	
    	audioRecorder = new AudioRecord(AudioSource.MIC, 8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);
    	audioRecorder.startRecording();
    	
    	//fixed size for now...
    	int N=0;
    	while(N < BUFFER_SIZE){
    		Log.e("AAAA", "N = "+N);
    		N += audioRecorder.read(buffer,N, 128);
    	}
    	
    	audioRecorder.stop();
    	audioRecorder.release();
    	audioRecorder = null;
    	
    	return buffer;
    }
    	
}
