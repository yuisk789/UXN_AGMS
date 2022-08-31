package kr.co.uxn.agms.android.activity.signin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.android.material.button.MaterialButton;

import kr.co.uxn.agms.android.CommonConstant;
import kr.co.uxn.agms.android.R;
import kr.co.uxn.agms.android.activity.device.DeviceConnectActivity;
import kr.co.uxn.agms.android.activity.signup.SignupActivity;
import kr.co.uxn.agms.android.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    MaterialButton loginActiveButton;
    MaterialButton loginInactiveButton;
    MaterialButton signinActiveButton;
    MaterialButton signinInactiveButton;
    LoginState mState = LoginState.LOGIN;
    private SiginUpInformation mInformation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_parent);
        loginActiveButton = findViewById(R.id.button_to_login);
        signinInactiveButton = findViewById(R.id.button_to_siginin);
        signinInactiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setClickable(false);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setClickable(true);
                    }
                },200);

                changeToEula();
            }
        });

        loginInactiveButton = findViewById(R.id.button_will_login);
        loginInactiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setClickable(false);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setClickable(true);
                    }
                },200);
                confirmToLogin();
            }
        });
        signinActiveButton = findViewById(R.id.button_will_signin);

        changeToLogin();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putBoolean(CommonConstant.PREF_SKIP_INTRO, true).apply();
    }

    private long Timeback;
    @Override
    public void onBackPressed() {
        if(mState == LoginState.LOGIN){
            if (System.currentTimeMillis() - Timeback > 1000) {
                Timeback = System.currentTimeMillis();
                Toast.makeText(this, R.string.back_press_to_exit, Toast.LENGTH_SHORT).show();
                return;
            }
            finish();
        } else {
            confirmToLogin();
        }

    }
    private void confirmToLogin(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.alert_title)
                .setMessage(R.string.alert_message_exit_sign_up)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        changeToLogin();
                    }
                }).setNegativeButton(android.R.string.cancel,null)
                .show();
    }
    public SiginUpInformation getSignUpInfo(){
        return mInformation;
    }

    public void changeToLogin(){
        mState = LoginState.LOGIN;
        mInformation = null;
        loginActiveButton.setVisibility(View.VISIBLE);
        signinInactiveButton.setVisibility(View.VISIBLE);

        loginInactiveButton.setVisibility(View.GONE);
        signinActiveButton.setVisibility(View.GONE);
        LoginFragment fragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.anchor,fragment).commit();
    }
    public void changeToEula(){
        mState = LoginState.EULA;
        mInformation=  new SiginUpInformation();
        loginActiveButton.setVisibility(View.GONE);
        signinInactiveButton.setVisibility(View.GONE);

        loginInactiveButton.setVisibility(View.VISIBLE);
        signinActiveButton.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.anchor,new EulaFragment()).commit();
    }

    public void changeToTerms(){
        mState = LoginState.TERM;
        loginActiveButton.setVisibility(View.GONE);
        signinInactiveButton.setVisibility(View.GONE);
        loginInactiveButton.setVisibility(View.VISIBLE);
        signinActiveButton.setVisibility(View.VISIBLE);

        getSupportFragmentManager().beginTransaction().replace(R.id.anchor,new TermFragment()).commit();
    }

    public void changeToInputBaseInfo(){
        mState = LoginState.INPUT_BASE_INFO;
        loginActiveButton.setVisibility(View.GONE);
        signinInactiveButton.setVisibility(View.GONE);
        loginInactiveButton.setVisibility(View.VISIBLE);
        signinActiveButton.setVisibility(View.VISIBLE);

        getSupportFragmentManager().beginTransaction().replace(R.id.anchor,new BaseInfoInputFragment()).commit();
    }

    public void changeToCreateAccount(){
        mState = LoginState.CREATE_ACCOUNT;
        loginActiveButton.setVisibility(View.GONE);
        signinInactiveButton.setVisibility(View.GONE);
        loginInactiveButton.setVisibility(View.VISIBLE);
        signinActiveButton.setVisibility(View.VISIBLE);

        getSupportFragmentManager().beginTransaction().replace(R.id.anchor,new CreateAccountFragment()).commit();
    }
    public void changeToAccountCreated(){
        mState = LoginState.ACCOUNT_CREATE;
        loginActiveButton.setVisibility(View.GONE);
        signinInactiveButton.setVisibility(View.GONE);
        loginInactiveButton.setVisibility(View.VISIBLE);
        signinActiveButton.setVisibility(View.VISIBLE);

        getSupportFragmentManager().beginTransaction().replace(R.id.anchor,new AccountCreatedFragment()).commit();
    }

    public void changeToSetting(){
        mState = LoginState.SETTING;
        loginActiveButton.setVisibility(View.GONE);
        signinInactiveButton.setVisibility(View.GONE);
        loginInactiveButton.setVisibility(View.VISIBLE);
        signinActiveButton.setVisibility(View.VISIBLE);

        getSupportFragmentManager().beginTransaction().replace(R.id.anchor,new SettingFragment()).commit();
    }


}
