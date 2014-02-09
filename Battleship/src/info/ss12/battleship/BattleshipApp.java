package info.ss12.battleship;

import info.ss12.battleship.ScrollPage.MyGestureDetector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.speech.tts.TextToSpeech;
import android.view.SurfaceView;

import com.google.gson.Gson;

public class BattleshipApp extends Activity implements TextToSpeech.OnInitListener{

	boolean p1;
	boolean p1done;
	Ship[] Ships = new Ship[5];
	public static int counter;
	String key;
	Bundle extras;
	TextToSpeech ttobj;
	private static final int MY_DATA_CHECK_CODE = 1234;
	//private ViewFlipper mViewFlipper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		extras = getIntent().getExtras();
		if(extras == null){
			counter = 0;
		}
		else{
			counter = getIntent().getIntExtra("cool", -1);
		}
		ttobj = new TextToSpeech(this, this);

		initAnimations();
		Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void clicked(View view) {
		counter++;
		p1done = false;
		p1 = false;
		System.out.println("Counter is now: " + counter);
		//ttobj.speak("New Game Selected", TextToSpeech.QUEUE_FLUSH, null);
<<<<<<< HEAD
		Intent myIntent = new Intent(BattleshipApp.this, MoveBoard.class);
		//myIntent.putExtra("cool", counter); //Optional parameters
		//new SendData().execute(); // done
		BattleshipApp.this.startActivity(myIntent);
=======
		//Intent myIntent = new Intent(BattleshipApp.this, MoveBoard.class);
		//myIntent.putExtra("cool", counter); //Optional parameters
		new SendData().execute(); // done
		p1 = true;
		new SendData().execute(); // done

		//BattleshipApp.this.startActivity(myIntent);
>>>>>>> a204462d8053fd543bc3ae362634e684b5f7f487
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
				
				RandomBoardGenerator((short)0);
				String jsonstr = gson.toJson(Ships);
				//System.out.println(jsonstr);
				int limit = 1;
				int cmd = 0;
				int team;
				if(!p1)
				team = 1;
				else team = 2;
				JSONArray nh = new JSONArray();
				nh.put(3);//1
				nh.put(2);//2
				nh.put(2);//3
				nh.put(1);//4
				nh.put(1);//5
				JSONArray shipID = new JSONArray();
				shipID.put(1);
				shipID.put(2);
				shipID.put(3);
				shipID.put(4);
				shipID.put(5);
				
				JSONArray X = new JSONArray();
				JSONArray Y = new JSONArray();
				JSONArray hitboxSID = new JSONArray();
				hitboxSID.put(1);
				hitboxSID.put(1);
				hitboxSID.put(1);
				X.put(0);
				Y.put(0);
				X.put(0);
				Y.put(1);
				X.put(0);
				Y.put(2);//end of 1st ship
				hitboxSID.put(2);
				hitboxSID.put(2);
				X.put(3);
				Y.put(3);
				X.put(3);
				Y.put(2);// end of 2nd ship
				hitboxSID.put(3);
				hitboxSID.put(3);
				X.put(2);
				Y.put(1);
				X.put(2);
				Y.put(2);//end of 3rd ship
				hitboxSID.put(4);
				X.put(5);
				Y.put(5);//end of 4th ship
				hitboxSID.put(5);
				X.put(1);
				Y.put(4);//end of 5th ship
				
				json.put("cmd",cmd); // 1 = sign in with user and pw
				json.put("limit", limit);
				json.put("num_hitbox", nh);
				json.put("shipID", shipID);
				json.put("team", team);
				json.put("shipx",X);
				json.put("shipy",Y);
				json.put("hbsid",hitboxSID);
				JSONArray postjson = new JSONArray();
				postjson.put(json);
				// Post the data:
				httppost.setHeader("json",json.toString());
				httppost.getParams().setParameter("jsonpost",postjson);
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
							sb.append(line + "\n ");
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
					int gameID = Integer.parseInt(sb.toString().charAt(5)+"");
					if(sb.toString().contains("added ships")){
						//do something with the string
						Intent intent = new Intent(BattleshipApp.this, MoveBoard.class);
						intent.putExtra("cool", counter); //Optional parameters
						intent.putExtra("GameID", gameID);
						startActivity(intent); //switch activities
					}else{
						System.out.println("Not moving forward to MoveBoard");
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

	public void RandomBoardGenerator(short team){ // team being 0 or 1
		//6x6 board with ships of lengths 3, 2, 2, 1, 1.
		Ships[0] = new Ship(team, 3, 0);
		Ships[1] = new Ship(team, 2, 1);
		Ships[2] = new Ship(team, 2, 2);
		Ships[3] = new Ship(team, 1, 3);
		Ships[4] = new Ship(team, 1, 4);

		Ships[0].hitboxes[0] = new Hitbox((short)0, (short)0, team, 0);
		Ships[0].hitboxes[1] = new Hitbox((short)0, (short)1, team, 0);
		Ships[0].hitboxes[2] = new Hitbox((short)0, (short)2, team, 0);

		Ships[1].hitboxes[0] = new Hitbox((short)3, (short)3, team, 1);
		Ships[1].hitboxes[1] = new Hitbox((short)3, (short)2, team, 1);

		Ships[2].hitboxes[0] = new Hitbox((short)2, (short)1, team, 2);
		Ships[2].hitboxes[1] = new Hitbox((short)2, (short)2, team, 2);

		Ships[3].hitboxes[0] = new Hitbox((short)5, (short)5, team, 3);

		Ships[4].hitboxes[0] = new Hitbox((short)1, (short)4, team, 4);
	}

	public class Ship{
		int ship_ID;
		short team;
		boolean sunk;
		Hitbox[] hitboxes;
		int num_hitbox;
		int limit; // hack
		int cmd; // hack
		Ship(short a, int b, int c){
			limit = 1;
			cmd = 0;
			team = a;
			num_hitbox = b;
			hitboxes = new Hitbox[b]; // uninitialized array of hitboxes.
			ship_ID = c;
		}
	}

	public class Hitbox{
		short x;
		short y;
		short team;
		int ship_ID;
		Hitbox(short a, short b, short c, int d){
			x = a;
			y = b;
			team = c;
			ship_ID = d;
		}
	}

	// THIS IS CALLED WHEN THE NEW GAME SCREEN SHOWS
	@Override
	public void onInit(int arg0) {
		if(extras == null && counter == 0){
			ttobj.speak("Welcome to Battleship!  Swipe across the top of the screen to hear instructions.  TTTap the center of the screen to start a new game.", TextToSpeech.QUEUE_FLUSH, null);
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
	
	private void initAnimations() {

		final GestureDetector gestureDetector;
		gestureDetector = new GestureDetector(new MyGestureDetector());

		SurfaceView x = new SurfaceView(getBaseContext());
		((View) x).setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return false;
				} else {
					return true;
				}
			}
		});
	}

	boolean touchStarted = false;
	int x, y;
	public class MyGestureDetector extends SimpleOnGestureListener {

		private static final int SWIPE_MIN_DISTANCE = 120;
		private static final int SWIPE_MAX_OFF_PATH = 250;
		private static final int SWIPE_THRESHOLD_VELOCITY = 200;
		
		public boolean onTouch(View v, MotionEvent event) {
		    int action = event.getAction();
		    if (action == MotionEvent.ACTION_DOWN) {
		        touchStarted = true;
		    }
		    else if (action == MotionEvent.ACTION_MOVE) {
		        // movement: cancel the touch press
		        touchStarted = false;

		        x = (int) event.getX();
		        y = (int) event.getY();

		    }
		    else if (action == MotionEvent.ACTION_UP) {
		        if (touchStarted) {
		            // touch press complete, show toast
		            Toast.makeText(v.getContext(), "Coords: " + x + ", " + y, 1000).show();
		        }
		    }

		    return true;
		}
		

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				ttobj.speak("Swiped.", TextToSpeech.QUEUE_FLUSH, null);

			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				ttobj.speak("Swiped.", TextToSpeech.QUEUE_FLUSH, null);

			}
			
			return super.onFling(e1, e2, velocityX, velocityY);
		}
	}
}
