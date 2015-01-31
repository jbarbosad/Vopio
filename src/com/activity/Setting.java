package com.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activity.custom.CustomActivity;
import com.activity.ui.NewActivity;
import com.activity.ui.Words;
import com.activity.ui.Snippet;

public class Setting extends CustomActivity
{

	/** Check if the app is running. */
	private boolean isRunning;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		setupActionBar();
		setDummyContents();
	}

	protected void setupActionBar()
	{
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("Settings");
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setLogo(R.drawable.icon);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		isRunning = false;
	}


	private void setDummyContents()
	{
		isRunning = true;
		//setTouchNClick(R.id.btnDemo);

		new Thread(new Runnable() {
			@Override
			public void run()
			{
				//final ProgressBar pBar = (ProgressBar) findViewById(R.id.progressBar1);
				//final TextView lbl1 = (TextView) findViewById(R.id.lblProgress1);
				//final TextView lbl2 = (TextView) findViewById(R.id.lblProgress2);
				while (isRunning)
				{
					try
					{
						Thread.sleep(1000);
					} catch (Exception e)
					{
						e.printStackTrace();
					}
					runOnUiThread(new Runnable() {
						@Override
						public void run()
						{
							//int p = pBar.getProgress() + 5;
							//if (p > 100)
							//	p = 0;
							//pBar.setProgress(p);
							//lbl1.setText(p + "MB/100MB");
							//lbl2.setText(p + "%");
						}
					});
				}
			}
		}).start();
	}

}
