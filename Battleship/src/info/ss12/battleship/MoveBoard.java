package info.ss12.battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.widget.Toast;

public class MoveBoard extends Activity implements TextToSpeech.OnInitListener{


	private Animation mInFromRight;
	private Animation mOutToLeft;
	private Animation mInFromLeft;
	private Animation mOutToRight;
	//private ViewFlipper mViewFlipper;
	private int counter = 0;
	private static int count = 0;
	long pattern[] = {0,10,50,10,50};
	int board[][] = new int[6][6];

	GestureDetector gestureDetector;
	GestureDetector flickers;
	BubbleSurfaceView bubble ;

	Bundle extras;
	TextToSpeech ttobj;
	private static final int MY_DATA_CHECK_CODE = 1234;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bubble = new BubbleSurfaceView(this);
		setContentView(bubble);

		gestureDetector = new GestureDetector(getBaseContext(), new GestureListener());
		//mViewFlipper.setDisplayedChild(0);
		initAnimations();
		extras = getIntent().getExtras();
		if(extras == null){
			counter = 0;
		}
		else{
			counter = getIntent().getIntExtra("cool", -1);
		}
		ttobj = new TextToSpeech(this, this);

		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
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
						if(checkValid(xNum, yNum)){	// untouched
							vib.vibrate(pattern, 1);
						}
						else if(checkHit(xNum,yNum)){	// hit
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
						if(checkValid(xNum,yNum)){	// untouched
							vib.vibrate(pattern, 1);
						}
						else if(checkHit(xNum,yNum)){	// hit
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
								
								if(true){
									ttobj.speak("Direct hit!", TextToSpeech.QUEUE_FLUSH, null);
								}
								else{
									ttobj.speak("Miss!", TextToSpeech.QUEUE_FLUSH, null);
								}
							}
						}
					}

					if(true){	// end game

					}
					else{
						Intent myIntent = new Intent(MoveBoard.this, MoveBoard.class);
						//myIntent.putExtra("cool", counter);
						MoveBoard.this.startActivity(myIntent);
					}
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
					if(checkValid(i, j)){	// untouched
						paint.setColor(Color.BLUE);
					}
					else if(checkHit(i,j)){	// hit
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

	// Set board to all available
	public void clearBoard(){
		if(board == null){
			return;
		}
		
		for(int i = 0; i < 6; i++){
			for(int j = 0; j < 6; j++){
				board[i][j] = 0;
			}
		}
	}

	public boolean checkValid(int x, int y){
		return(board[x][y] == 0);
	}
	
	public boolean checkHit(int x, int y){
		return(board[x][y] == 1);
	}
	
	public boolean checkMiss(int x, int y){
		return(board[x][y] == 2);
	}

	@Override
	public void onInit(int status) {
		// Read in values from backend
		String x = "";
		int a = 0, b = 0, c=0, d=0;
		if(extras == null && counter == 0){
			ttobj.speak("Player " + x + "'s turn to shoot. Successful hits for: Player 1: " + a + " for Player 2: " + b + ". Remaining ships for: Player 1: " + c + " for Player 2: " + d + "", TextToSpeech.QUEUE_FLUSH, null);
			counter++;
		}

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == MY_DATA_CHECK_CODE)
		{
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
			{
				// success, create the TTS instance
				ttobj = new TextToSpeech(this, this);
			}
			else
			{
				// missing data, install it
				Intent installIntent = new Intent();
				installIntent.setAction(
						TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}

	@Override
	public void onDestroy()
	{
		if (ttobj != null)
		{
			ttobj.stop();
			ttobj.shutdown();
		}
		super.onDestroy();
	}









	private class SendData extends AsyncTask<String, Integer, Void> {

		protected void onProgressUpdate() {
			//called when the background task makes any progress
		}

		protected void onPreExecute() {
			//called before doInBackground() is started
		}
		protected void onPostExecute() {
		}

		@Override
		protected Void doInBackground(String... params) {
			JSONObject json = new JSONObject();
			Gson gson = new Gson();
			try{
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://ec2-54-213-43-47.us-west-2.compute.amazonaws.com");
				// JSON data:
				//RandomBoardGenerator((short)0);
				//String jsonstr = gson.toJson(Ships);
				//System.out.println(jsonstr);
				int limit = 3;
				int cmd = 0;

				String jcmd = gson.toJson(cmd);
				String jlim = gson.toJson(limit);
				//jsonstr+=jlim;
				//jsonstr+=jcmd;
				System.out.println();
				//json.put("limit",3); // best of 3
				json.put("cmd",0); // 0 = start game
				//json.put("dataa",jsonstr);
				JSONArray postjson = new JSONArray();
				postjson.put(json);
				// Post the data:
				//httppost.setHeader("json",jsonstr);
				httppost.getParams().setParameter("jsonpost",postjson);
				// Execute HTTP Post Request
				HttpResponse response = null;
				try{
					response = httpclient.execute(httppost);
				}catch(Exception e){
					System.out.println(e);
				}
				// for reading response (json):
				if(response != null)
				{
					InputStream is = response.getEntity().getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));
					StringBuilder sb = new StringBuilder();
					String line = null;
					try {
						while ((line = reader.readLine()) != null) {
							sb.append(line + "\n");
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					//display in log
					Log.i("RESPONSE", sb.toString());
					if(sb.toString().contains("yay")){
						//do something with the string
					}else{
						Toast.makeText(getApplicationContext(), "The server had trouble with your request. Try again later", Toast.LENGTH_LONG).show();
					}
				}
			} catch (UnsupportedEncodingException uee) {
				System.out.println("unsupported encoding error");
				uee.printStackTrace();
			} catch (ClientProtocolException cpe) {
				System.out.println("client protocol error");
				cpe.printStackTrace();
			} catch (IOException ioe) {
				System.out.println("io error");
				ioe.printStackTrace();
			} catch (JSONException je) {
				System.out.println("json error");
				je.printStackTrace();
			}
			return null;
		}
	}
}
