package info.ss12.battleship;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SelectionActivity extends FragmentActivity implements GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener, OnClickListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	GestureDetector gest;
	private static final String DEBUG_TAG = "Gestures";
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selection);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		gestureDetector = new GestureDetector(this, new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		};

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.selection, menu);
		return true;
	}

	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					Toast.makeText(SelectionActivity.this, "Left Swipe", Toast.LENGTH_SHORT).show();
				}  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					Toast.makeText(SelectionActivity.this, "Right Swipe", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}

		@Override
		public boolean onDoubleTap(MotionEvent event) {
			Intent myIntent = new Intent(SelectionActivity.this, BattleshipApp.class);
			//myIntent.putExtra("key", value); //Optional parameters 
			SelectionActivity.this.startActivity(myIntent);
			return true;
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent event) {
			Intent myIntent = new Intent(SelectionActivity.this, BattleshipApp.class);
			//myIntent.putExtra("key", value); //Optional parameters 
			SelectionActivity.this.startActivity(myIntent);
			return true;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			Intent myIntent = new Intent(SelectionActivity.this, BattleshipApp.class);
			//myIntent.putExtra("key", value); //Optional parameters 
			SelectionActivity.this.startActivity(myIntent);
			return true;
		}

		public void setOnClickListener(){
			Intent myIntent = new Intent(SelectionActivity.this, BattleshipApp.class);
			//myIntent.putExtra("key", value); //Optional parameters 
			SelectionActivity.this.startActivity(myIntent);
		}

		public void setOnTouchListener(GestureDetector g){
			Intent myIntent = new Intent(SelectionActivity.this, BattleshipApp.class);
			//myIntent.putExtra("key", value); //Optional parameters 
			SelectionActivity.this.startActivity(myIntent);
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			//this.setOnClickListener(SelectionActivity.this); 
			//this.setOnTouchListener(gestureListener);
		}

		@Override
		public Fragment getItem(int position) {
			/*// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DetailFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			//args.putChar(key, value);
			Button button = new Button(getBaseContext());

			fragment.setArguments(args);
			return fragment;*/

			Fragment fragment = null;
			switch(position){
			case 0:
				fragment = new Fragment1();
				break;
			case 1:
				fragment = new Fragment2();
				break;
			case 2:
				fragment = new Fragment3();
				break;
			default:
				break;
			}

			//set args if necessary (which it isn't?)
			Bundle args = new Bundle();
			fragment.setArguments(args);

			//return fragment
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 6 total pages.
			return 6;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_A);
			case 1:
				return getString(R.string.title_B);
			case 2:
				return getString(R.string.title_C);
			case 3:
				return getString(R.string.title_D);
			case 4:
				return getString(R.string.title_E);
			case 5:
				return getString(R.string.title_F);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_selection_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

	@Override 
	public boolean onTouchEvent(MotionEvent event){ 
		this.gest.onTouchEvent(event);
		// Be sure to call the superclass implementation
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent event) { 
		Intent myIntent = new Intent(SelectionActivity.this, BattleshipApp.class);
		//myIntent.putExtra("key", value); //Optional parameters 
		SelectionActivity.this.startActivity(myIntent);
		return true;
	}

	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2, 
			float velocityX, float velocityY) {
		Intent myIntent = new Intent(SelectionActivity.this, BattleshipApp.class);
		//myIntent.putExtra("key", value); //Optional parameters 
		SelectionActivity.this.startActivity(myIntent);
		return true;
	}

	@Override
	public void onLongPress(MotionEvent event) {
		Intent myIntent = new Intent(SelectionActivity.this, BattleshipApp.class);
		//myIntent.putExtra("key", value); //Optional parameters 
		SelectionActivity.this.startActivity(myIntent);
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		Intent myIntent = new Intent(SelectionActivity.this, BattleshipApp.class);
		//myIntent.putExtra("key", value); //Optional parameters 
		SelectionActivity.this.startActivity(myIntent);
		return true;
	}

	@Override
	public void onShowPress(MotionEvent event) {
		Intent myIntent = new Intent(SelectionActivity.this, BattleshipApp.class);
		//myIntent.putExtra("key", value); //Optional parameters 
		SelectionActivity.this.startActivity(myIntent);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent event) {
		Intent myIntent = new Intent(SelectionActivity.this, BattleshipApp.class);
		//myIntent.putExtra("key", value); //Optional parameters 
		SelectionActivity.this.startActivity(myIntent);
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent event) {
		Intent myIntent = new Intent(SelectionActivity.this, BattleshipApp.class);
		//myIntent.putExtra("key", value); //Optional parameters 
		SelectionActivity.this.startActivity(myIntent);
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent event) {
		Intent myIntent = new Intent(SelectionActivity.this, BattleshipApp.class);
		//myIntent.putExtra("key", value); //Optional parameters 
		SelectionActivity.this.startActivity(myIntent);
		return true;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent event) {
		Intent myIntent = new Intent(SelectionActivity.this, BattleshipApp.class);
		//myIntent.putExtra("key", value); //Optional parameters 
		SelectionActivity.this.startActivity(myIntent);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent myIntent = new Intent(SelectionActivity.this, BattleshipApp.class);
		//myIntent.putExtra("key", value); //Optional parameters 
		SelectionActivity.this.startActivity(myIntent);
	}










	public class DetailFragment extends Fragment 
	implements OnClickListener 
	{
		Button btn;
		DetailFragment(){

			//Toast.makeText(SelectionActivity.this, this.btn.getText(), Toast.LENGTH_SHORT).show();

		}	
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View view = inflater.inflate(
					R.layout.activity_selection, container, false);

			btn = (Button) view.findViewById(R.id.button2);
			btn.setOnClickListener(this);       

			return view;
		}

		@Override
		public void onClick(View v) {
			Toast.makeText(this.getActivity(), 
					"Button is clicked!", Toast.LENGTH_LONG).show();

		}

	}

	public class Fragment1 extends Fragment{
		public void onCreate(View v) {
			Toast.makeText(this.getActivity(), 
					"Button is clicked!", Toast.LENGTH_LONG).show();

		}
	}

	public class Fragment2 extends Fragment{
		public void onCreate(View v) {
			Toast.makeText(this.getActivity(), 
					"Button is clicked!", Toast.LENGTH_LONG).show();

		}
	}
	public class Fragment3 extends Fragment{
		public void onCreate(View v) {
			Toast.makeText(this.getActivity(), 
					"Button is clicked!", Toast.LENGTH_LONG).show();

		}
	}
	
	public void jumpToPage(View view){
		Intent myIntent = new Intent(SelectionActivity.this, BattleshipApp.class);
    	//myIntent.putExtra("key", value); //Optional parameters 
		SelectionActivity.this.startActivity(myIntent);
	}
}

