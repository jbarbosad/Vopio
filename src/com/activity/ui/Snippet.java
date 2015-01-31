package com.activity.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.activity.R;
import com.activity.custom.CustomActivity;

public class Snippet extends Fragment implements OnClickListener
{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.snippets, null);

		setupView(v);
		return v;
	}

	private void setupView(View v)
	{
		View b = v.findViewById(R.id.pause);
		b.setOnClickListener(this);
		b.setOnTouchListener(CustomActivity.TOUCH);

		b = v.findViewById(R.id.finish);
		b.setOnClickListener(this);
		b.setOnTouchListener(CustomActivity.TOUCH);
	}

	@Override
	public void onClick(View v)
	{

	}
}
