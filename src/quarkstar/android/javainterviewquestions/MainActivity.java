/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package quarkstar.android.javainterviewquestions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import model.DBConnect;
import model.Query;
import model.QueryDataSource;
import quarkstar.fima.cardsui.views.CardUI;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

public class MainActivity extends Activity implements SearchView.OnQueryTextListener
{
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPlanetTitles;
	static boolean isDbChanged = false;
	
	static public HashMap<String, Query> mQueryMap = null;
	
	private SearchView mSearchView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTitle = getTitle();
		mDrawerTitle = "Choose Category";
		mPlanetTitles = getResources().getStringArray(R.array.category_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		
		//mStatusView = (TextView) findViewById(R.id.status_text);
		
		DBConnect dbConnect = new DBConnect(this);
		try 
		{
			dbConnect.createDataBase();
		} 
		catch (IOException ioe) {	throw new Error("Unable to create database");	}


		// set a custom shadow that overlays the main content when the drawer opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mPlanetTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
				mDrawerLayout, /* DrawerLayout object */
				R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
				R.string.drawer_open, /* "open drawer" description for accessibility */
				R.string.drawer_close /* "close drawer" description for accessibility */
				) {
			public void onDrawerClosed(View view)
			{
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView)
			{
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null)
		{
			selectItem(0);
		}
	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		
		MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        setupSearchView(searchItem);		
		
        return true;
	}

	private void setupSearchView(MenuItem searchItem) {
		 
        if (isAlwaysExpanded()) {
            mSearchView.setIconifiedByDefault(false);
        } else {
            searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        }
 
/*        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();
 
            SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
            for (SearchableInfo inf : searchables) {
                if (inf.getSuggestAuthority() != null
                        && inf.getSuggestAuthority().startsWith("applications")) {
                    info = inf;
                }
            }
            mSearchView.setSearchableInfo(info);
        }
*/ 
        mSearchView.setOnQueryTextListener(this);
    }
 
/*    public boolean onQueryTextChange(String newText) {
        mStatusView.setText("Query = " + newText);
        return false;
    }*/
 
    public boolean onQueryTextSubmit(String query) {
//        mStatusView.setText("Query = " + query + " : submitted");
    	
    	Intent intent = new Intent(MainActivity.this, SearchActivity.class);
    	intent.putExtra("searchQuery", query);
    	startActivity(intent);
    	
        return false;
    }
 
  /*  public boolean onClose() {
        mStatusView.setText("Closed!");
        return false;
    }*/
 
    protected boolean isAlwaysExpanded() {
        return false;
    }

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
	ListView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			selectItem(position);
		}
	}

	private void selectItem(int position)
	{
		Fragment fragment = new CardListFragment(this, position);
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

		mDrawerList.setItemChecked(position, true);
		setTitle(mPlanetTitles[position]);

		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title)
	{
		mTitle = title;
		getActionBar().setTitle(mTitle);
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
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onQueryTextChange(String newText)
	{
		// TODO Auto-generated method stub
		return false;
	}

}

class CardListFragment extends Fragment
{
	QueryDataSource dataSource = null;
	Context context = null;
	int position=0;
	boolean flagQueryAvailability = false;
	
	CardUI mCardView = null;
	View rootView = null;
	
	public CardListFragment(Context context, int position)
	{
		this.context = context;
		this.position = position;
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
		
		String[] subcatArray = getResources().getStringArray(R.array.category_array);
		
		if(subcatArray[position].equalsIgnoreCase("favourite"))
			MainActivity.mQueryMap = dataSource.getSelectedData("starred='yes'");
		else
			MainActivity.mQueryMap = dataSource.getSelectedData("subcategory='"+subcatArray[position]+"' ");
		
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
