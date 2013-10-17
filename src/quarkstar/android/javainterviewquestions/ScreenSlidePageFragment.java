package quarkstar.android.javainterviewquestions;

import model.Query;
import model.QueryDataSource;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class ScreenSlidePageFragment extends Fragment
{
	private View rootView = null;
	private TextView textViewQues = null;
	private TextView textViewAns = null;
	ImageButton ImageButtonStarred = null;
	private long questionId = 0;
	String isFav = "n";
	String[] array_id = null;
	int ques_pos = 0;
	
	public ScreenSlidePageFragment(long quesId)
	{
		questionId = quesId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.activity_screen_slide_page_fragment, container, false);
		
		textViewQues = (TextView) rootView.findViewById(R.id.text_view_ques);
		textViewAns = (TextView) rootView.findViewById(R.id.text_view_ans);
		ImageButtonStarred = (ImageButton)rootView.findViewById(R.id.image_fav);

		textViewAns.setMovementMethod(new ScrollingMovementMethod());

		Query queryObject = (Query)MainActivity.mQueryMap.get(questionId + "");
		
		textViewQues.setText(queryObject.getQuestion());
		textViewAns.setText(queryObject.getAnswer());
		
		if(queryObject.getStarred().equalsIgnoreCase("yes"))
			ImageButtonStarred.setBackgroundResource(R.drawable.fav_on);
		else
			ImageButtonStarred.setBackgroundResource(R.drawable.fav_off);
		
		ImageButtonStarred.setOnClickListener(new ImageButton.OnClickListener() {

	        public void onClick(View v) {
	        	if ((changeFavStatus(v) > 0) && (!MainActivity.isDbChanged)) 
	        		MainActivity.isDbChanged = true;
	        }
	    });

		return rootView;
	}
	
	public int changeFavStatus(View view)
	{
		String favStatus = "n";
		
		QueryDataSource dataSource  = new QueryDataSource(getActivity());
		
		dataSource.open();

		String isFav = MainActivity.mQueryMap.get(questionId+"").getStarred();
		
		if(isFav.equals("yes"))
		{
			favStatus = "n";
			ImageButtonStarred.setBackgroundResource(R.drawable.fav_off);
		}
		else
		{
			favStatus = "yes";
			ImageButtonStarred.setBackgroundResource(R.drawable.fav_on);
		}
		
		MainActivity.mQueryMap.get(questionId+"").setStarred(favStatus);
		int ret = dataSource.updateFavStatus(questionId, favStatus);
		
		dataSource.close();
		
		return ret;
	}
}