package quarkstar.android.javainterviewquestions;

import quarkstar.android.javainterviewquestions.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import quarkstar.fima.cardsui.objects.Card;

public class MyCard extends Card {

	public MyCard(String title,String desc){
		super(title, desc, 0);
	}

	@Override
	public View getCardContent(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.card_ex, null);

		((TextView) view.findViewById(R.id.title)).setText(title);
		((TextView) view.findViewById(R.id.description)).setText(desc);

		
		return view;
	}

	
	
	
}
