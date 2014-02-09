package info.ss12.battleship;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class MoveBoard extends Activity {


	private Animation mInFromRight;
	private Animation mOutToLeft;
	private Animation mInFromLeft;
	private Animation mOutToRight;
	//private ViewFlipper mViewFlipper;
	private int counter = 0;
	private static int count = 0;
	long pattern[] = {0,10,50,10,50};

	GestureDetector gestureDetector;
	GestureDetector flickers;
	BubbleSurfaceView bubble ;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bubble = new BubbleSurfaceView(this);
		setContentView(bubble);

		gestureDetector = new GestureDetector(getBaseContext(), new GestureListener());
		//mViewFlipper.setDisplayedChild(0);
		initAnimations();
		counter++;
		System.out.println("Counter:" + counter);
		new SendData().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.move_board, menu);
		return true;
	}

	private void initAnimations() {
	/*	mInFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
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
		mOutToLeft.setInterpolator(accelerateInterpolator);*/

		final GestureDetector gestureDetector;
		gestureDetector = new GestureDetector(new MyGestureDetector());

		bubble.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
					boolean allFalse = true;
					int xNum = 0;
					int yNum = 0;
					for(int i = 0; i < 6; i++){
						for(int j = 0; j < 6; j++){
							// Check if on a spot
							if(event.getX()>i*180+85-50 && event.getX()<i*180+85+50 && event.getY()>j*180+605-50 && event.getY()<j*180+605+50){
								allFalse = false;
								xNum = i;
								yNum = j;
							}
						}
					}
					if(!allFalse){
						vib.cancel();
						//$$$$$$$$$$$$$$$$$$$ DO PHP CALL TO FIND HIT STATUS OF X,Y $$$$$$$$$$$$$$$$$$$
						if(true){	// untouched
							vib.vibrate(pattern, 1);
						}
						else if(true){	// hit
							vib.vibrate(1000000000);
						}
						else{	// miss
							vib.cancel();
						}
					}
					else{
						vib.cancel();
					}
				}
				if((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE){
					boolean allFalse = true;
					int xNum = 0;
					int yNum = 0;
					for(int i = 0; i < 6; i++){
						for(int j = 0; j < 6; j++){
							// Check if on a spot
							if(event.getX()>i*180+85-50 && event.getX()<i*180+85+50 && event.getY()>j*180+605-50 && event.getY()<j*180+605+50){
								allFalse = false;
								xNum = i;
								yNum = j;
							}
						}
					}
					if(!allFalse){
						vib.cancel();
						//$$$$$$$$$$$$$$$$$$$ DO PHP CALL TO FIND HIT STATUS OF X,Y $$$$$$$$$$$$$$$$$$$
						if(true){	// untouched
							vib.vibrate(pattern, 1);
						}
						else if(true){	// hit
							vib.vibrate(1000000000);
						}
						else{	// miss
							vib.cancel();
						}
					}
					else{
						vib.cancel();
					}
				}
				else if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
					vib.cancel();
				}
				if (gestureDetector.onTouchEvent(event)) {
				} else {
				}
				return true;
			}
		});
	}

	private class MyGestureDetector extends SimpleOnGestureListener {

		private static final int SWIPE_MIN_DISTANCE = 120;
		private static final int SWIPE_MAX_OFF_PATH = 250;
		private static final int SWIPE_THRESHOLD_VELOCITY = 200;

		@Override
		public boolean onDown(MotionEvent e) {
			/*Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			//if(e.getX() == 100 && e.getY() == 100){
			System.out.println("Hi");
			//v.vibrate(500);
			}
				if(e.getX() == 100 && e.getY() == 200){
					v.vibrate(400);
				}
				if(e.getX() == 100 && e.getY() == 300){
					v.vibrate(300);
				}
				if(e.getX() == 100 && e.getY() == 400){
					v.vibrate(200);
				}
				if(e.getX() == 100 && e.getY() == 500){
					v.vibrate(100);
				}*/
			//onShowPress(e);
			return true;
		}

		/*@Override
		public void onShowPress(MotionEvent e){
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			boolean allFalse = true;
			for(int i = 0; i < 6; i++){
				for(int j = 0; j < 6; j++){
					// Check if on a spot
					if(e.getX()>i*180+85-50 && e.getX()<i*180+85+50 && e.getY()>j*180+605-50 && e.getY()<j*180+605+50){
						allFalse = false;
					}
				}
			}
			if(!allFalse){
				v.vibrate(0);
			}
		}*/

		
		
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
				count++;
				System.out.println(" in onFling() :: ");
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH){
					if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
							&& Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
						// determine grid position that e1 is in
						
						int minX, maxX, minY, maxY;
						for(int i = 0; i < 6; i++){
							for(int j = 0; j < 6; j++){
								minX = (i*180)+85 - 70;
								maxX = (i*180)+85 + 70;
								minY = (j*180)+605 - 70;
								maxY = (j*180)+605 + 70;
								if(e1.getX() >= minX && e1.getX() <= maxX && e1.getY() >= minY && e1.getY() <= maxY){
									//shot = true
									// $$$$$$$$$$$$$$$$$$$ DO PHP CALL TO SEND COORDINATES I,J $$$$$$$$$$$$$$$$$$$
									System.out.println("X is: " + i + " Y is: " + j);
									System.out.println("Shots fired!");
								}
							}
						}
						
						Intent myIntent = new Intent(MoveBoard.this, MoveBoard.class);
				    	//myIntent.putExtra("key", value); //Optional parameters  SEND INFO HERE
						MoveBoard.this.startActivity(myIntent);
					}
				}
				/*if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					mViewFlipper.setInAnimation(mInFromRight);
					mViewFlipper.setOutAnimation(mOutToLeft);
					mViewFlipper.showNext();
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					mViewFlipper.setInAnimation(mInFromLeft);
					mViewFlipper.setOutAnimation(mOutToRight);
					mViewFlipper.showPrevious();
				}*/

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
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			//if(e.getX() == 100 && e.getY() == 100){
			System.out.println("Hi");
			//v.vibrate(500);
			/*}
				if(e.getX() == 100 && e.getY() == 200){
					v.vibrate(400);
				}
				if(e.getX() == 100 && e.getY() == 300){
					v.vibrate(300);
				}
				if(e.getX() == 100 && e.getY() == 400){
					v.vibrate(200);
				}
				if(e.getX() == 100 && e.getY() == 500){
					v.vibrate(100);
				}*/
			return true;
		}
		// event when double tap occurs
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			float x = e.getX();
			float y = e.getY();

			return true;
		}
		@Override
		public void onLongPress(MotionEvent e) {
		}
	}


	public class BubbleSurfaceView extends SurfaceView  
	implements SurfaceHolder.Callback {
		private SurfaceHolder sh;
		private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		public BubbleSurfaceView(Context context) {
			super(context);
			sh = getHolder();
			sh.addCallback(this);
			paint.setColor(Color.BLUE);
			paint.setStyle(Style.FILL);
		}

		// Draw circles for locations on grid
		public void surfaceCreated(SurfaceHolder holder) {
			Canvas canvas = sh.lockCanvas();
			canvas.drawColor(Color.BLACK);
			for(int i = 0; i < 6; i++){
				for(int j = 0; j < 6; j++){
					//$$$$$$$$$$$$$$$$$$$ DO PHP CALL TO FIND HIT STATUS OF X,Y $$$$$$$$$$$$$$$$$$$
					if(true){	// untouched
						paint.setColor(Color.BLUE);
					}
					else if(true){	// hit
						paint.setColor(Color.RED);
					}
					else{	// miss
						paint.setColor(Color.BLACK);
					}
					canvas.drawCircle(i*180+85, j*180+605, 50, paint);
				}
			}
			sh.unlockCanvasAndPost(canvas);
		}
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
		}
		public void surfaceDestroyed(SurfaceHolder holder) {
		}
	}
}
