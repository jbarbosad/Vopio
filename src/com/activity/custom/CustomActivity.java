package com.activity.custom;

import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.activity.utils.TouchEffect;

public class CustomActivity extends FragmentActivity implements OnClickListener
{

	public static final TouchEffect TOUCH = new TouchEffect();

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
			finish();
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onClick(View v)
	{

	}

	public View setTouchNClick(int id)
	{

		View v = setClick(id);
		v.setOnTouchListener(TOUCH);
		return v;
	}

	public View setClick(int id)
	{

		View v = findViewById(id);
		v.setOnClickListener(this);
		return v;
	}
}
