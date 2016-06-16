package mainactivity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import util.OkHttpUtils;
import util.StrUtils;
import widgt.APP;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
                login();
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
    
    private void login(){
        String userName = mEtUser.getText().toString();
        String passWord = mEtPassword.getText().toString();
        if(userName.isEmpty()||passWord.isEmpty()){
            Toast.makeText(this,R.string.account_or_password_not_null,Toast.LENGTH_SHORT).show();
            return;
        }
        String passMd5 = StrUtils.md5(passWord);
        Map<String,String> map = new HashMap<>();
        map.put("username", userName);
        map.put("password",passMd5);
//        WindowManager windowManager = getWindowManager();
//        windowManager.addView(mLoadingView,LoadingView.mWindowParams);
        OkHttpUtils.post(StrUtils.LOGIN_URL, map, TAG, new OkHttpUtils.SimpleOkCallBack() {
            @Override
            public void onResponse(String s) {
                Log.d("ithinker", s);
                JSONObject j;
                try {
                    j = new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, R.string.login_fail, Toast.LENGTH_SHORT).show();
                    return;
                }
                String state = j.optString("state", "");
                if (state.equals("successful")) {
                    String token = j.optString("token", "");
                    String id = j.optString("id", "");
                    String username = j.optString("username", "");
                    SharedPreferences sp = LoginActivity.this.getSharedPreferences(StrUtils.SP_USER, MODE_PRIVATE);
                    sp.edit().putString(StrUtils.SP_USER_TOKEN, token).putString(StrUtils.SP_USER_USERNAME, username)
                            .putString(StrUtils.SP_USER_ID, id).apply();
                    Toast.makeText(LoginActivity.this, R.string.login_success+"token"+token, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, newMainActivity.class));
                    finish();
                } else {
                    String reason = j.optString("reason");
                    Toast.makeText(LoginActivity.this, reason, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}
