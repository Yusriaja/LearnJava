package quarkstar.android.javainterviewquestions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import model.Query;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

public class ScreenSlideActivity extends FragmentActivity
{
	private ViewPager mPager;
	private int PAGE_COUNT = 1;

	private PagerAdapter mPagerAdapter;
	
	ArrayList<Long> arList_id = new ArrayList<Long>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_slide);

		long questionId = getIntent().getLongExtra("question_id", 0);
		
		mPager = (ViewPager) findViewById(R.id.pager);
		
		PAGE_COUNT = MainActivity.mQueryMap.size();
		
		Iterator<Entry<String, Query>> itr = MainActivity.mQueryMap.entrySet().iterator();
		
		while(itr.hasNext())			
		{
			Map.Entry<String, Query> pairs = (Map.Entry<String, Query>) itr.next();
			
			arList_id.add(pairs.getValue().getId());
		}
		
		mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, arList_id);
		mPager.setAdapter(mPagerAdapter);
		
		mPager.setCurrentItem(arList_id.indexOf(questionId));
	}

	private class ViewPagerAdapter extends FragmentPagerAdapter
	{
		ArrayList<Long> arList_id = new ArrayList<Long>();
		
		public ViewPagerAdapter(FragmentManager fm, Context context, ArrayList<Long> arList_id)
		{
			super(fm);
			this.arList_id = arList_id;
		}

		@Override
		public int getCount()
		{
			return PAGE_COUNT;
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position, Object object)
		{
			super.setPrimaryItem(container, position, object);
		}

		@Override
		public Fragment getItem(int position)
		{
			ScreenSlidePageFragment fragment = new ScreenSlidePageFragment(arList_id.get(position));
			
			return fragment;
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			return "Question # " + (position+1);
		}
	}
}