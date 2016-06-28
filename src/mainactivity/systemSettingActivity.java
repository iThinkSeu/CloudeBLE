package mainactivity;

import com.example.learn.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class systemSettingActivity extends Activity{
	private TextView titleview;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_newsystemsetting);
        bindview();
    	titleview.setText("œµÕ≥…Ë÷√ SETUP");
	}
	
	void bindview()
	{
		titleview = (TextView) findViewById(R.id.title);
	}
}
