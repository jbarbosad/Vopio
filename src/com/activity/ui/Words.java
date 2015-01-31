package com.activity.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.activity.R;

public class Words extends Fragment implements OnClickListener
{
	private View tab;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.words, null);

		setupView(v);
		return v;
	}
	
	private void setupView(View v)
	{
		ListView list = (ListView) v.findViewById(R.id.list);
		list.setAdapter(new RouteAdapter());

		tab = v.findViewById(R.id.tabNear);
		tab.setOnClickListener(this);
		v.findViewById(R.id.tabFav).setOnClickListener(this);
	}

	public class RouteAdapter extends BaseAdapter
	{
		private TextView textView2;
		
		public void updater (final ArrayList<String> result)
		{
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
						textView2.setText(result.get(0));
					}
				}
			}).start();
		}
		 
		
		
		@Override
		public int getCount()
		{
			return 1;
		}

		@Override
		public Object getItem(int arg0)
		{
			return null;
		}


		@Override
		public long getItemId(int arg0)
		{
			return arg0;
		}


		@Override
		public View getView(int arg0, View v, ViewGroup arg2)
		{
			if (v == null)
				v = getLayoutInflater(null).inflate(R.layout.words_item, null);
				textView2 = (TextView) v.findViewById(R.id.textView2);
			return v;
		}

	}

	@Override
	public void onClick(View v)
	{
		tab.setEnabled(true);
		tab = v;
		tab.setEnabled(false);
	}
}
