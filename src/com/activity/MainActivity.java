package com.activity;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.activity.custom.CustomActivity;
import com.activity.ui.NewActivity;
import com.activity.ui.Snippet;
import com.activity.ui.Words;


public class MainActivity extends CustomActivity
{

	/** The view pager for swipe views. */
	private ViewPager pager;

	/** The current selected tab. */
	private View currentTab;

	public Timer timer;
	
	public boolean isRecording;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setupActionBar();
		
		initTabs();
		initPager();
		
		timer = new Timer();
		audioSnippet();
	}
	
	public void audioSnippet()
	{
		final MainActivity.recordContinually recordContinually = new recordContinually();			
		
		new Thread(recordContinually).start();
		
		timer.scheduleAtFixedRate(
			new TimerTask() {	
				@Override
				public void run() {
					recordContinually.recording();
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}, 0,10000);
	}
	
	protected void setupActionBar()
	{
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setLogo(R.drawable.icon);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(false);
	}


	private void initTabs()
	{
		findViewById(R.id.tab1).setOnClickListener(this);
		findViewById(R.id.tab2).setOnClickListener(this);
		findViewById(R.id.tab3).setOnClickListener(this);
		setCurrentTab(0);
	}
	

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		
		if (v.getId() == R.id.tab1)
			pager.setCurrentItem(0, true);
		else if (v.getId() == R.id.tab2)
			pager.setCurrentItem(1, true);
		else if (v.getId() == R.id.tab3)
			pager.setCurrentItem(2, true);

	}
	
	private void setCurrentTab(int page)
	{
		if (currentTab != null)
			currentTab.setEnabled(true);
		if (page == 0)
			currentTab = findViewById(R.id.tab1);
		else if (page == 1)
			currentTab = findViewById(R.id.tab2);
		else if (page == 2)
			currentTab = findViewById(R.id.tab3);
		currentTab.setEnabled(false);
		getActionBar().setTitle(((Button)currentTab).getText().toString());
	}


	private void initPager()
	{
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int page)
			{
				setCurrentTab(page);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{
			}

			@Override
			public void onPageScrollStateChanged(int arg0)
			{
			}
		});
		pager.setAdapter(new DummyPageAdapter(getSupportFragmentManager()));
	}
	
	public class recordContinually extends Thread 
	{
		public final int frequency = 11025;
		public final int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
		public final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
		
		// Create a DataOuputStream to write the audio data into the saved file.
		OutputStream os;
		BufferedOutputStream bos;
		DataOutputStream dos;
				

		// Create a new AudioRecord object to record the audio.
		public final int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
		public AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 
		frequency, channelConfiguration, 
		audioEncoding, bufferSize);
		
		final File file;
		
		public short[] buffer;
		
		public recordContinually()
		{
			file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Vopio/"+"reverseme.pcm");

			// Delete any previous recording.
			if (file.exists())
				file.delete();

			// Create the new file.
			try {
				file.createNewFile();
				
				os = new FileOutputStream(file);
				bos = new BufferedOutputStream(os);
				dos = new DataOutputStream(bos);
				
			} catch (IOException e) {
			throw new IllegalStateException("Failed to create " + file.toString());
			}	

		}
		
		//@Override
		public void recording()
		{
			try{
				buffer = new short[bufferSize]; 
				
				audioRecord.startRecording();
				
				while(isRecording){
					int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
					//audioRecord.read(buffer, 0, bufferSize);
					
					for (int i = 0; i < bufferReadResult; i++)
						dos.writeShort(buffer[i]);	
					
					if(isRecording = false)
						break;
				}
			}catch(Throwable t) {
				Log.e("AudioRecord","Recording Failed");
			}
			
			audioRecord.stop();
			//audioRecord.release();
			
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					//isRecording = false;
					MainActivity.this.audioSnippet();				
				}
			}, 0);
		}	
	}


	private class DummyPageAdapter extends FragmentPagerAdapter
	{

		public DummyPageAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public Fragment getItem(int pos)
		{
			if (pos == 0)
				return new NewActivity();
			if (pos == 1)
				return new Words();
			
			return new Snippet();
		}

		@Override
		public int getCount()
		{
			return 4;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId()==R.id.menu_setting)
		{
			startActivity(new Intent(this, Setting.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
