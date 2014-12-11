package com.example.audiorecorder;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder.AudioSource;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	
	private final int BUFFER_SIZE = 64000;
	
	AudioTrack audioTrack = null;
	AudioRecord audioRecorder = null;
	
	Thread recordingThread = null;
	
	byte[] globalBuffer = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		
		TextView displayText = (TextView) findViewById(R.id.screenText);
		
		//display "connecting... while connecting
		
		//display "sending" while sending
		
		//display "receiving" while receiving
		
		ImageButton recordButton = (ImageButton) findViewById(R.id.btn_start_audio_record);
		recordButton.setOnTouchListener(new View.OnTouchListener() {
			//geht noch nicht...
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
					switch(event.getAction()){
						case MotionEvent.ACTION_DOWN:
							recordingThread = new Thread(new Runnable(){

								@Override
								public void run() {
									globalBuffer = startRecording();							
								}
								
							});
							recordingThread.start();

						case MotionEvent.ACTION_UP:
							stopRecording();
					}
				return false;
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
    	
    	return buffer;
    }
    
	public void stopRecording() {

		recordingThread.stop();
		
    	audioRecorder.stop();
    	audioRecorder.release();
    	audioRecorder = null;
	}
    	
}
