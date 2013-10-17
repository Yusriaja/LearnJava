package quarkstar.android.javainterviewquestions;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import quarkstar.android.javainterviewquestions.R;
import quarkstar.fima.cardsui.views.CardUI;

public class CardList extends Activity
{

	private CardUI mCardView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_list);

		// init CardView
		mCardView = (CardUI) findViewById(R.id.cardsview);
		mCardView.setSwipeable(false);

//		for(int i=1; i<50; i++)
//			mCardView.addCard(new MyCard("What do you know about question no."+i+" "));
//		
	
		mCardView.refresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.card_list, menu);
		return true;
	}
}
