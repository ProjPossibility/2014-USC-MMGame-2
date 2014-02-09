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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class BattleshipApp extends Activity {

<<<<<<< HEAD
	Ship[] ships = new Ship[5];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void clicked(View view) {
		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setText("hello");
		Intent myIntent = new Intent(BattleshipApp.this, ScrollPage.class);
		//myIntent.putExtra("key", value); //Optional parameters
		//new SendData().execute(); // done
		BattleshipApp.this.startActivity(myIntent);
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
				HttpPost httppost = new HttpPost("54.213.43.47");
				// JSON data:
				RandomBoardGenerator((short)0);
				gson.toJson(ships);
				json.put("limit",3); // best of 3
				json.put("cmd",0); // 0 = start game
				JSONArray postjson = new JSONArray();
				postjson.put(json);
				// Post the data:
				httppost.setHeader("json",json.toString());
				httppost.getParams().setParameter("jsonpost",postjson);
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
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
					if(sb.toString().contains("data accepted")){
						//do something with the string
						Intent intent = new Intent(BattleshipApp.this, ScrollPage.class);
						startActivity(intent); //switch activities
					}else{
						Toast.makeText(getApplicationContext(), 
								"The server had trouble with your request. Try again later", 
								Toast.LENGTH_LONG).show();
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
		ships[0] = new Ship(team, 3);
		ships[1] = new Ship(team, 2);
		ships[2] = new Ship(team, 2);
		ships[3] = new Ship(team, 1);
		ships[4] = new Ship(team, 1);
		ships[0].hitboxes[0] = new Hitbox((short)3, (short)3, team, 0);
	}
	
	public class Ship{
		short team;
		boolean sunk;
		Hitbox[] hitboxes;
		Ship(short a, int b){
			team = a;
			hitboxes = new Hitbox[b]; // uninitialized array of hitboxes.
		}
	}
	
	public class Hitbox{
		short x;
		short y;
		short team;
		int ShipID;
		Hitbox(short a, short b, short c, int d){
			x = a;
			y = b;
			team = c;
			ShipID = d;
		}
	}
=======
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void clicked(View view) {
    	Intent myIntent = new Intent(BattleshipApp.this, Grid.class);
    	//myIntent.putExtra("key", value); //Optional parameters 
    	BattleshipApp.this.startActivity(myIntent);
    }
>>>>>>> 1c20b27dd69b8095ec6183986bf59d454bae5743
}
