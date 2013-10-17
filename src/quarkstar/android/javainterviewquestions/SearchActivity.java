package quarkstar.android.javainterviewquestions;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import model.DBConnect;
import model.Query;
import model.QueryDataSource;
import quarkstar.fima.cardsui.views.CardUI;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class SearchActivity extends Activity
{
	private DrawerLayout mDrawerLayout;
	private String[] mPlanetTitles;
	static boolean isDbChanged = false;
	private ActionBarDrawerToggle mDrawerToggle;

    String searchQuery = "";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		mPlanetTitles = getResources().getStringArray(R.array.category_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.search_layout);
	    searchQuery = getIntent().getStringExtra("searchQuery");
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		if (savedInstanceState == null)
		{
			selectItem(0);
		}
	
	}
	public boolean onOptionsItemSelected(MenuItem item)
	{
		this.finish();
	    return true;
	}
	
	private void selectItem(int position)
	{
		Fragment fragment = new CardListSearchFragment(this, position, searchQuery);
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.search_frame, fragment).commit();

		setTitle(mPlanetTitles[position]);
	}

	@Override
	public void setTitle(CharSequence title)
	{
		getActionBar().setTitle("Search Results");
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
	}
}


class CardListSearchFragment extends Fragment
{
	QueryDataSource dataSource = null;
	Context context = null;
	int position=0;
	boolean flagQueryAvailability = false;
	
	CardUI mCardView = null;
	View rootView = null;
	String searchQuery = "";
	
	public CardListSearchFragment(Context context, int position, String searchQuery)
	{
		this.context = context;
		this.position = position;
		this.searchQuery = searchQuery;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.activity_card_list, container, false);
		
		mCardView = (CardUI) rootView.findViewById(R.id.cardsview);
		mCardView.setSwipeable(false);

		return showCards();
	}

	@Override
	public void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		
		String[] subCatArr = getResources().getStringArray(R.array.category_array);
		
		if ((MainActivity.isDbChanged) && (subCatArr[position].equalsIgnoreCase("favourite")))
		{
			showCards();
		}
		
		MainActivity.isDbChanged = false;
	}
	
	public View showCards()
	{
		String question = "";
		String answer = "";
		
		MyCard card = null;
		
		mCardView.clearCards();

		dataSource = new QueryDataSource(context);
		dataSource.open();
		
		dataSource.justChangedToYes();
		
		MainActivity.mQueryMap = dataSource.getSelectedData(DBConnect.COLUMN_QUESTION+" like'%"+searchQuery+"%' or " + DBConnect.COLUMN_ANSWER+" like'%"+searchQuery+"%'");
		
		dataSource.close();
		
		Iterator<Entry<String, Query>> itr = MainActivity.mQueryMap.entrySet().iterator();

		while(itr.hasNext())			
		{
			flagQueryAvailability = true;
			Map.Entry<String, Query> pairs = (Map.Entry<String, Query>) itr.next();

			final long qId = pairs.getValue().getId();
			question = (String)pairs.getValue().getQuestion();
			answer = (String)pairs.getValue().getAnswer();

			card = new MyCard(question, answer.length() > 200 ? answer.substring(0, 200)+" ......." : answer);
			card.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(getActivity(), ScreenSlideActivity.class);
					intent.putExtra("question_id", qId);
					startActivity(intent);
				}
			});
			mCardView.addCard(card);
		}

		if(!flagQueryAvailability)
			mCardView.addCard(new MyCard("No records found!","Questions in this category will be available in next update."));

		mCardView.refresh();
		
		question = null;
		answer = null;

		return rootView;
	}
}

