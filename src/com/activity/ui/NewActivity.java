package com.activity.ui;

import java.util.ArrayList;
import java.util.Locale;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.R;

public class NewActivity extends Fragment
{
	/**
	 * This is the code for recording continually using the Google speech-to-text library
	 * */
	private TextView txtSpeechInput;
	private ImageButton btnSpeak;
	private final int REQ_CODE_SPEECH_INPUT = 100;
	public boolean validation = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.new_activity, null);
		
		txtSpeechInput = (TextView) v.findViewById(R.id.txtSpeechInput);
		btnSpeak = (ImageButton) v.findViewById(R.id.btnSpeak);
		loop();

		btnSpeak.setOnClickListener(new View.OnClickListener() {
			
			NewActivity.promptSpeechInput continuo = new promptSpeechInput();

			@Override
			public void onClick(View v) {
				new Thread(continuo).run();
			}
		});
		 
		
		//setupView(v);
		return v;
	}
	
	public void loop()
	{
		NewActivity.promptSpeechInput continuo = new promptSpeechInput();
		new Thread(continuo).run();
	}
	
	
	
	/**
	 * Showing google speech input dialog
	 * */
	private class promptSpeechInput extends Thread {
		
		@Override
		public void run() {
			
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				getString(R.string.speech_prompt));
			try {
				startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
			} catch (ActivityNotFoundException a) {
				Toast.makeText(getParentFragment().getActivity().getBaseContext(),
						getString(R.string.speech_not_supported),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * Receiving speech input
	 * */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case REQ_CODE_SPEECH_INPUT: {
			if (resultCode == -1 && null != data) {

				final ArrayList<String> result = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				
				//Words palavras = new Words();
				//Words.RouteAdapter novo =  palavras.new RouteAdapter();
				
				new Thread(new Runnable() {
	    			@Override
	    			public void run()
	    			{
	    				try
	    				{
	    					Thread.sleep(1);

	    				} catch (Exception e)
	    				{
	    					e.printStackTrace();
	    				} finally
	    				{
	    					getActivity().runOnUiThread(new Runnable() {
	    						@Override
	    						public void run()
	    						{
	    							txtSpeechInput.setText(result.get(0));
	    						}
	    					});
	    				}
	    			}
	    		}).start();
				
				new Thread(new Runnable() {
	  				@Override
	  				public void run() {
	  					try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
	  					NewActivity.this.loop(); 
	  				}
	  			}).start();
			}
			break;
		}

		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getActivity().getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
		
}
