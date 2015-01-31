package com.activity.ui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.activity.AudioManagement;
import com.activity.R;

public class NewActivity extends Fragment
{
	public Button btnPlay;
	public Button btnSave;
	
	private Thread play = null;
	private Thread save = null;
	
	private AudioManagement audioManagement;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{	
		final View v = inflater.inflate(R.layout.new_activity, null);

		setButtonHandlers(v);
	    enableButtons(true,v);
	    
		return v;
	}
	
	private void setButtonHandlers(View v) {
	    ((Button) v.findViewById(R.id.btnPlay)).setOnClickListener(btnClick);
	    ((Button) v.findViewById(R.id.btnSave)).setOnClickListener(btnClick);
	}

	private void enableButton(View v,int id, boolean isEnable) {
	    ((Button) v.findViewById(id)).setEnabled(isEnable);
	}

	private void enableButtons(boolean isRecording, View v) {
	    enableButton(v,R.id.btnPlay, isRecording);
	    enableButton(v,R.id.btnSave, isRecording);
	}
	
	private View.OnClickListener btnClick = new View.OnClickListener() {
	    public void onClick(View v) {

	    	audioManagement = new AudioManagement();
	    	
	        switch (v.getId()) {
		        case R.id.btnPlay: {
		            play = new Thread(new Runnable() {
		            	public void run() {
		            		audioManagement.play();
		    			} 
		    		});
		    		play.start();
		            break;
		        }   
		        case R.id.btnSave: {
	               save = new Thread(new Runnable() {
		    			public void run() {
		    				File file =  new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Vopio/"+"reverseme.pcm");
		    				save(file);
		    			} 
	               });
	               save.start();
	               break;
		        }
	        }
	    }
	};
	
	public void save(File file)
	{
		File newFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Vopio/"+"reverseme1.pcm");
		
		if (newFile.exists())
			newFile.delete();

		// Create the new file.
		try {
			newFile.createNewFile();
		} catch (IOException e) {
		throw new IllegalStateException("Failed to create " + newFile.toString());
		}
		
		try {
			// Create a DataOuputStream to write the audio data into the saved file.
			OutputStream os = new FileOutputStream(newFile);
			BufferedOutputStream bos = new BufferedOutputStream(os);
			DataOutputStream dos = new DataOutputStream(bos);
			
			// Create a DataInputStream to read the audio data back from the saved file.
			InputStream is = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			DataInputStream dis = new DataInputStream(bis);

			int bufferSize = (int)(file.length());
			short[] buffer = new short[bufferSize];
			
			int i = 0;
			while(i < 108544)
			{
				buffer[i] = dis.readShort();
				dos.writeShort(buffer[i]);
				i++;
			}
			
			dis.close();
			dos.close();

		} catch (Throwable t) {
			Log.e("Saving audio","Saving Failed");
		}
		
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		this.getActivity().getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
