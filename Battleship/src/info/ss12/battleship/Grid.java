package info.ss12.battleship;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Grid extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_grid);
		Bitmap b = Bitmap.createBitmap(1080,1920, Config.RGB_565);
		setContentView(new BubbleSurfaceView(this));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.grid, menu);
		return true;
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
