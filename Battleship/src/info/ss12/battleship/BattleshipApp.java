package info.ss12.battleship;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class BattleshipApp extends Activity {

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
    	Intent myIntent = new Intent(BattleshipApp.this, ScrollPage.class);
    	//myIntent.putExtra("key", value); //Optional parameters 
    	BattleshipApp.this.startActivity(myIntent);
    }
}
