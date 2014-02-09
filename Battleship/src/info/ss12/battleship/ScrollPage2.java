package info.ss12.battleship;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class ScrollPage2 extends Activity {


	private Animation mInFromRight;
	private Animation mOutToLeft;
	private Animation mInFromLeft;
	private Animation mOutToRight;
	private ViewFlipper mViewFlipper;
	private int count = 0;

	GestureDetector gestureDetector;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scroll_page2);

		gestureDetector = new GestureDetector(getBaseContext(), new GestureListener());


		mViewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
		mViewFlipper.setDisplayedChild(0);
		initAnimations();

		TextView tv = (TextView) findViewById(R.id.textView2);
		tv.setOnLongClickListener(new View.OnLongClickListener(){
			@Override
			public boolean onLongClick(View v) {
				System.out.println("lolz");
				return true;
			}
		});}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.scroll_page, menu);
			return true;
		}

		private void initAnimations() {
			mInFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
					+1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f);
			mInFromRight.setDuration(100);
			AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
			mInFromRight.setInterpolator(accelerateInterpolator);

			mInFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
					-1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f);
			mInFromLeft.setDuration(100);
			mInFromLeft.setInterpolator(accelerateInterpolator);

			mOutToRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
					0.0f, Animation.RELATIVE_TO_PARENT, +1.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f);
			mOutToRight.setDuration(100);
			mOutToRight.setInterpolator(accelerateInterpolator);

			mOutToLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, -1.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f);
			mOutToLeft.setDuration(100);
			mOutToLeft.setInterpolator(accelerateInterpolator);

			final GestureDetector gestureDetector;
			gestureDetector = new GestureDetector(new MyGestureDetector());

			mViewFlipper.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					if (gestureDetector.onTouchEvent(event)) {
						return false;
					} else {
						return true;
					}
				}
			});
		}

		private class MyGestureDetector extends SimpleOnGestureListener {

			private static final int SWIPE_MIN_DISTANCE = 120;
			private static final int SWIPE_MAX_OFF_PATH = 250;
			private static final int SWIPE_THRESHOLD_VELOCITY = 200;
			
			

			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				count++;
				System.out.println(" in onFling() :: ");
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH){
					if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
							&& Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
						Intent myIntent = new Intent(ScrollPage2.this, BattleshipApp.class);
				    	//myIntent.putExtra("key", value); //Optional parameters  SEND INFO HERE
						ScrollPage2.this.startActivity(myIntent);
					}
				}
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					mViewFlipper.setInAnimation(mInFromRight);
					mViewFlipper.setOutAnimation(mOutToLeft);
					mViewFlipper.showNext();
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					mViewFlipper.setInAnimation(mInFromLeft);
					mViewFlipper.setOutAnimation(mOutToRight);
					mViewFlipper.showPrevious();
				}
				
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		}





		@Override
		public boolean onTouchEvent(MotionEvent e) {
			System.out.println(" in onFling() :: ");
			return gestureDetector.onTouchEvent(e);
		}








		private class GestureListener extends GestureDetector.SimpleOnGestureListener {

			@Override
			public boolean onDown(MotionEvent e) {
				return true;
			}
			// event when double tap occurs
			@Override
			public boolean onDoubleTap(MotionEvent e) {
				float x = e.getX();
				float y = e.getY();

				Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");

				return true;
			}
			@Override
			public void onLongPress(MotionEvent e) {
				System.out.println("Longpress detected");
			}
		}
	}
