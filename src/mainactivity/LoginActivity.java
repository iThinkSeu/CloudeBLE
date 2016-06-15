package mainactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learn.DeviceScanActivity;
import com.example.learn.R;

public class LoginActivity extends Activity{
	
    private final static String TAG = "AtyLogin";
    public static final String INTENT_CLEAR = "intent_clear";

    EditText mEtUser, mEtPassword;
    Button mBtnLogin;
    TextView mTvRegister;

    //LoadingView mLoadingView;
	 @Override
     public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			//setContentView(R.layout.aty_main);
			setContentView(R.layout.aty_login);
			bindView();
	 }
	 
    private void bindView(){
        mEtUser = (EditText) findViewById(R.id.login_edit_text_user);
        mEtPassword = (EditText) findViewById(R.id.login_edit_text_password);
        mBtnLogin = (Button) findViewById(R.id.login_button_login);
        mTvRegister = (TextView) findViewById(R.id.login_text_view_register);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //login();
             Toast.makeText(LoginActivity.this, "You clicked login", Toast.LENGTH_SHORT).show();
        	 Intent i = new Intent(LoginActivity.this,atyRegisterActivity.class);
             startActivity(i);
            }
        });
        
        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Toast.makeText(LoginActivity.this, "You clicked register", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, atyRegisterActivity.class);
				startActivity(intent);
            }
        });
    }


}
