package mainactivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.example.learn.R;

public class LoginActivity extends Activity{
	
	 @Override
     public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			//setContentView(R.layout.aty_main);
			setContentView(R.layout.aty_login);
	 }

}
