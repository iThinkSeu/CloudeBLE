package mainactivity;

import com.example.learn.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class welcomeActivity extends Activity{
	
	private final int SPLASH_DISPLAY_LENGHT = 1500; // —”≥Ÿ¡˘√Î

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setContentView(R.layout);
		setContentView(R.layout.aty_welcome);

		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent mainIntent = new Intent(welcomeActivity.this,
						newMainActivity.class);
				welcomeActivity.this.startActivity(mainIntent);
				welcomeActivity.this.finish();
			}

		}, SPLASH_DISPLAY_LENGHT);

	}

}
