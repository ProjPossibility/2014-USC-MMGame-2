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

	Ship[] Ships = new Ship[5];
	
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
		Intent myIntent = new Intent(BattleshipApp.this, MoveBoard.class);
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
				HttpPost httppost = new HttpPost("http://ec2-54-213-43-47.us-west-2.compute.amazonaws.com");
				// JSON data:
				RandomBoardGenerator((short)0);
				String jsonstr = gson.toJson(Ships);
				System.out.println(jsonstr);
				json.put("limit",3); // best of 3
				json.put("cmd",0); // 0 = start game
				json.put("dataa",jsonstr);
				JSONArray postjson = new JSONArray();
				postjson.put(json);
				// Post the data:
				httppost.setHeader("json",jsonstr);
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
						Intent intent = new Intent(BattleshipApp.this, ScrollPage.class);
						startActivity(intent); //switch activities
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
		Ship(short a, int b, int c){
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
}
